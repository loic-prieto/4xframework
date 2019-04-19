/**
 * 4X Framework - Core library - The core library on which to base the game
 * Copyright © 2018 Loïc Prieto Dehennault (loic.sephiroth@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.sephire.games.framework4x.core.plugins;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Set;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.model.config.CoreConfigKeyEnum;
import org.sephire.games.framework4x.core.model.game.Game;
import org.sephire.games.framework4x.core.plugins.configuration.resources.civilizations.UserPreferencesCivilizationLoader;
import org.sephire.games.framework4x.core.utils.FunctionalUtils.Reduce;
import org.sephire.games.framework4x.core.utils.RootlessTree;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.jar.JarFile;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.Predicates.instanceOf;
import static java.lang.String.format;
import static java.util.function.Predicate.not;
import static org.sephire.games.framework4x.core.utils.FunctionalUtils.Collectors.toTry;

/**
 * This class provides for utility functions that can be used by clients to inspect what plugins are available,
 * and load them.
 */
public class PluginManager {

	public static final String PLUGIN_NAME_MANIFEST_ENTRY_LABEL = "X-4XPlugin-Name";
	public static final String PLUGIN_ROOT_PACKAGE_MANIFEST_ENTRY_LABEL = "X-4XPlugin-RootPackage";
	public static final String PLUGIN_PARENT_ENTRY_LABEL = "X-4XPlugin-ParentPlugin";

	private Map<String,PluginSpec> availablePlugins;
	private Map<String,Plugin> plugins;

	/**
	 * Returns a set of plugins loaded inside the plugin manager.
	 *
	 * @return
	 */
	public Set<String> getLoadedPlugins() {
		return this.plugins.keySet();
	}

	/**
	 * <p>Executes the loading method for each plugin, loading every configuration and resource to prepare for a
	 * game creation into the given configuration builder.</p>
	 * <p>Will also load all user-stored configuration afterwards, to overwrite the configuration loaded by the
	 * plugins with what the user has stored in it's home folder</p>
	 * @param plugins
	 * @return
	 */
	public Try<Void> loadPlugins(Set<String> plugins, Configuration.Builder configuration) {
		return Try.of(() -> {
			invokeLoadingOperation(plugins, configuration)
			  .andThen(() -> loadUserStoredConfiguration(configuration))
			  .getOrElseThrow(t -> t);

			return null;
		});
	}

	/**
	 * <p>Fills the configuration object with everything the requested set of plugins have loaded and then
	 * puts the loaded plugins themselves into the configuration.</p>
	 * @param plugins
	 * @return
	 */
	private Try<Void> invokeLoadingOperation(Set<String> plugins, Configuration.Builder configuration) {
		return Try.of(()->{
			// First build the dependency list and load each plugin
			var sortedPluginsOperation = buildPluginToLoadList(plugins);
			if(sortedPluginsOperation.isFailure()) {
				throw sortedPluginsOperation.getCause();
			}
			var pluginLoadingTry = sortedPluginsOperation.get().map((pluginSpec) -> Plugin.from(pluginSpec,configuration) );
			if(Try.sequence(pluginLoadingTry).isFailure()) {
				var exceptions = pluginLoadingTry.filter(Try::isFailure).map(Try::getCause);
				throw new PluginLoadingException("Error while loading plugins",exceptions.toJavaArray(Throwable.class));
			}

			var loadedPlugins = pluginLoadingTry.map(Try::get).collect(HashSet.collector());
			configuration.putConfig(CoreConfigKeyEnum.LOADED_PLUGINS, loadedPlugins);

			return null;
		});
	}

	/**
	 * <p>Overwrite the plugins configuration with user stored config files on their user preferences folder.</p>
	 * @param configuration
	 * @return
	 */
	private Try<Void> loadUserStoredConfiguration(Configuration.Builder configuration) {
		return new UserPreferencesCivilizationLoader().load(configuration);
	}

	/**
	 * Given the set of loaded plugins, call their game start hooks with an initialized game and loaded configuration.
	 * @param game
	 * @return
	 */
	public Try<Void> callGameStartHooks(Game game){
		return Try.of(()->{
			// Build the dependency tree
			var pluginsTree = RootlessTree.fromItemSet(plugins.values().toSet(),
			  (plugin,potentialParent)->plugin.getSpecification().getParentPlugin().get().equals(potentialParent.getSpecification().getPluginName()),
			  (plugin)->plugin.getSpecification().isBasePlugin())
			  .getOrElseThrow((e)->e);

			ExecutorService threadExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
			pluginsTree.getTopologicallyOrderedBranches().forEach((pluginsBranch)->{
				threadExecutor.execute(()->pluginsBranch.forEach((plugin)->plugin.callGameStartHook(game)));
			});
			threadExecutor.shutdown();
			var timedOut = !threadExecutor.awaitTermination(3, TimeUnit.MINUTES);
			if(timedOut) {
				throw new GameHooksExecutionTimedoutException();
			}

			return null;
		});
	}

	/**
	 * Given a list of plugins to load, build a list that orders the plugins by load order, meaning
	 * that dependent plugins are loaded before the plugins that need them.
	 *
	 * This method also checks that all needed plugins are loaded. If they aren't, it will try to include
	 * them from the list of available plugins. Failing if dependent plugins are not available.
	 *
	 * May return the following error:
	 *  - {@link ParentPluginsNotFoundException}
	 *
	 * @return
	 */
	private Try<List<PluginSpec>> buildPluginToLoadList(Set<String> pluginsToLoad){
		return Try.of(()->{

			// Check we have all needed dependencies
			var completePluginList = completeNeededPlugins(pluginsToLoad)
			  .getOrElseThrow(e->e);

			// We use a tree to order plugins by dependency parentage
			var pluginsTree = RootlessTree.fromItemSet(completePluginList,
			  (PluginSpec item,PluginSpec potentialParentItem)-> {
				return potentialParentItem.getPluginName().equals(item.getParentPlugin().get());
			  },
			  PluginSpec::isBasePlugin)
			  .getOrElseThrow(e->e);

			return pluginsTree.getItemsOrderedTopologically();
		});
	}

	/**
	 * Given a set of plugins names to check, this function will add all needed dependencies that were not
	 * specified in the set, if they can be found in the list of available plugins.
	 * If they can't be found in the list of available plugins, an error will be returned.
	 *
	 * May return errors:
	 *   - {@link PluginsNotFoundException} if some plugin name from the given set was not found
	 *   - {@link ParentPluginsNotFoundException} if a needed dependency was not found in the list of available plugins
	 * @param requestedPluginsNames
	 * @return
	 */
	private Try<Set<PluginSpec>> completeNeededPlugins(Set<String> requestedPluginsNames) {
		return Try.of(()->{
			var missingPlugins = requestedPluginsNames.filter((plugin)->availablePlugins.get(plugin).isEmpty());
			if(missingPlugins.length() > 0) {
				throw new PluginsNotFoundException(missingPlugins);
			}

			var requestedPlugins = requestedPluginsNames.flatMap(availablePlugins::get);
			var neededDependencies = requestedPlugins
			  .filter(not(PluginSpec::isBasePlugin))
			  .map((pluginSpec -> Tuple.of(pluginSpec,availablePlugins.get(pluginSpec.getParentPlugin().get()))));

			var missingDependencies = neededDependencies
			  .filter((pluginDependencyTuple)-> pluginDependencyTuple._2.isEmpty());
			if(missingDependencies.length() > 0) {
				throw new ParentPluginsNotFoundException(missingDependencies
				  .map((t)->Tuple.of(t._1.getPluginName(),t._1.getParentPlugin().get())));
			}

			return neededDependencies.flatMap(Tuple2::_2)
			  .union(requestedPlugins);
		});
	}

	/**
	 * Checks whether a folder is a valid 4x plugin folder.
	 *
	 * @param folderPath
	 * @return
	 */
	public boolean isPluginFolderValid(Path folderPath) {
		var checks = Try.of(() -> {
			if(!folderPath.toFile().exists() && !folderPath.toFile().isDirectory()) {
				throw new IllegalArgumentException(format("The path %s is not a valid folder",folderPath));
			}

			var folderHasOnlyPlugins = checkFolderHasOnlyPlugins(folderPath.toFile());
			if(folderHasOnlyPlugins.isFailure()){
				throw folderHasOnlyPlugins.getCause();
			}
			if(!folderHasOnlyPlugins.get()) {
				throw new PluginLoadingException(
				  format("The plugin folder %s contains jars that are not plugins, this is not valid",folderPath.toAbsolutePath()));
			}

			return null;
		});

		return checks.isSuccess();
	}



	/**
	 * From the jars in the folder, builds a list of available plugins.
	 * @return
	 */
	public Try<Set<PluginSpec>> getAvailablePlugins(Path pluginFolder) {
		return Try.of(()->{
			var pluginsJars = HashSet.of(pluginFolder.toFile().list((dir, name) -> name.endsWith("jar")))
			  .map((jarFileName) -> pluginFolder.resolve(jarFileName).toFile())
			  .map(PluginManager::convertToJarFile)
			  .collect(toTry()).getOrElseThrow(t -> new PluginLoadingException(t))
			  .collect(HashSet.collector());

			return pluginsJars
			  .map(PluginManager::buildPluginSpecFrom)
			  .map(Try::get);
		});
	}

	/**
	 * The PluginManager can only work with folders that only contain plugin jars.
	 *
	 * This is a design decision to enforce that the plugins folders only contain plugins, and dependencies go all into
	 * the classpath or lib folder. This way it becomes easier for everyone to know where should everything goes, and
	 * also to check for the same libraries but with different versions (which would cause classpath conflicts).
	 *
	 * @return
	 */
	private static Try<Boolean> checkFolderHasOnlyPlugins(File folder){
		return Try.of(()->{
			var jarFiles = List.of(folder.list((dir,name)-> name.endsWith("jar")))
			  .map((jarFileName)->new File(folder.getAbsolutePath().concat(File.separator).concat(jarFileName)))
			  .map(PluginManager::convertToJarFile);

			if(!jarFiles.filter(Try::isFailure).isEmpty()) {
				var cause = jarFiles.filter(Try::isFailure)
				  .map(Try::getCause)
				  .map(Throwable::getMessage)
				  .reduce(Reduce.strings());

				throw new PluginLoadingException(format("Jar files in folder %s could not be loaded: %s",folder,cause));
			}

			var someJarIsNotPlugin = jarFiles.map(Try::get)
			  .exists((jarFile -> !PluginManager.has4XPluginInformation(jarFile)));

			return !someJarIsNotPlugin;
		});
	}

	/**
	 * Given a File, converts it into a Jar File.
	 * May return:
	 *  - {@link PluginJarFileCouldNotBeReadException}
	 */
	private static Try<JarFile> convertToJarFile(File file) {
		return Try.of(()->new JarFile(file))
		  .mapFailure(
		  	Case($(instanceOf(IOException.class)),t -> new PluginJarFileCouldNotBeReadException(t.getMessage()))
		  );
	}

	/**
	 * Checks whether a jar file is a 4X plugin.
	 *
	 * @param jarFile
	 * @return
	 */
	private static boolean has4XPluginInformation(JarFile jarFile) {
		boolean is4XPlugin = false;

		try {
			var manifest = jarFile.getManifest();
			if(manifest != null) {
				is4XPlugin = manifest.getMainAttributes().getValue(PLUGIN_NAME_MANIFEST_ENTRY_LABEL) != null;
			}
		}catch(IOException ioe) {}

		return is4XPlugin;
	}

	/**
	 * Builds a plugin spec from a jar file, assuming the jar file is a plugin jar.
	 *
	 * @param jarFile
	 * @return
	 */
	private static Try<PluginSpec> buildPluginSpecFrom(JarFile jarFile) {
		return Try.of(()->{
			var manifest = jarFile.getManifest();
			if(manifest == null) {
				throw new InvalidPluginJarException(jarFile.getName(),"There is no manifest in the jar file");
			}

			var name = manifest.getMainAttributes().getValue(PLUGIN_NAME_MANIFEST_ENTRY_LABEL);
			if(name == null) {
				throw new InvalidPluginJarException(jarFile.getName(),"The manifest does not contain the "+PLUGIN_NAME_MANIFEST_ENTRY_LABEL+" entry");
			}

			// A plugin may not have a main class (which means it only has automatic resources)
			var rootPackage = manifest.getMainAttributes().getValue(PLUGIN_ROOT_PACKAGE_MANIFEST_ENTRY_LABEL);
			if(rootPackage== null) {
				throw new InvalidPluginJarException(jarFile.getName(),"The manifest does not contain the "+PLUGIN_ROOT_PACKAGE_MANIFEST_ENTRY_LABEL+" entry");
			}

			// If the plugin is a base plugin, this will be empty
			var parentPlugin = Option.of(manifest.getMainAttributes().getValue(PLUGIN_PARENT_ENTRY_LABEL));
			if(parentPlugin.isDefined()) {
				if(parentPlugin.get().equals(name)) {
					throw new InvalidPluginJarException(jarFile.getName(),"A plugin cannot be a parent of itself");
				}
			}

			var pluginSpec = PluginSpec.builder()
			  .withRootPackage(rootPackage)
			  .withPluginName(name)
			  .withParent(parentPlugin)
			  .build();
			if(pluginSpec.isFailure()){
				throw pluginSpec.getCause();
			}

			return pluginSpec.get();
		});
	}


}
