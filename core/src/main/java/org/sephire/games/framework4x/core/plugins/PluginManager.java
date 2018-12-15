package org.sephire.games.framework4x.core.plugins;

import io.vavr.collection.*;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.utils.FunctionalUtils.Reduce;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.jar.JarFile;

import static io.vavr.API.*;
import static io.vavr.Predicates.instanceOf;
import static io.vavr.collection.Tree.Order.LEVEL_ORDER;

/**
 * This class provides for utility functions that can be used by clients to inspect what plugins are available,
 * and load them.
 */
public class PluginManager {

	public static final String PLUGIN_NAME_MANIFEST_ENTRY_LABEL = "X-4XPlugin-Name";
	public static final String PLUGIN_MAIN_CLASS_MANIFEST_ENTRY_LABEL = "X-4XPlugin-MainClass";
	public static final String PLUGIN_PARENT_ENTRY_LABEL = "X-4XPlugin-ParentPlugin";

	private File pluginFolder;
	private Map<String,PluginSpec> availablePlugins;

	/**
	 * Use fromFolder static method to build
	 * @param pluginFolder
	 */
	private PluginManager(File pluginFolder,List<PluginSpec> availablePlugins) {
		this.pluginFolder = pluginFolder;
		this.availablePlugins = availablePlugins
		  .map((pluginSpec -> Tuple(pluginSpec.getPluginName(),pluginSpec)))
		  .collect(HashMap.collector());
	}

	/**
	 * Return a list of plugins names available to load from the plugin folder.
	 * @return
	 */
	public Set<String> getAvailablePluginsNames(){
		return availablePlugins.keySet();
	}

	public Try<Configuration> loadPlugins(List<String> plugins) {
		return Try.of(()->{
			// First build the dependency graph and load each plugin
			var dependencyGraphTry = buildPluginDependencyGraph();
			if(dependencyGraphTry.isFailure()) {
				throw new PluginLoadingException(dependencyGraphTry.getCause());
			}
			// The graph can be traversed breadth-first to load each plugin in
			// descendant order, so that each child is loaded only after its parents.
			dependencyGraphTry.get().traverse(LEVEL_ORDER).forEach((pluginSpec)->{

			});


			return null;
		});
	}

	private Try<Tree<PluginSpec>> buildPluginDependencyGraph(){
		return null;
	}

	/**
	 * Builder for the PluginManager. Creates a plugin manager for a given plugin folder.
	 *
	 * May return:
	 *  - {@link PluginLoadingException}
	 * @param folderPath
	 * @return
	 */
	public static Try<PluginManager> fromFolder(Path folderPath) {
		return Try.of(()->{
			if(!folderPath.toFile().exists() && !folderPath.toFile().isDirectory()) {
				throw new IllegalArgumentException(String.format("The path %s is not a valid folder",folderPath));
			}

			var folderHasOnlyPlugins = checkFolderHasOnlyPlugins(folderPath.toFile());
			if(folderHasOnlyPlugins.isFailure()){
				throw folderHasOnlyPlugins.getCause();
			}

			if(!folderHasOnlyPlugins.get()) {
				throw new PluginLoadingException(
				  String.format("The plugin folder %s contains jars that are not plugins, this is not valid",folderPath.toAbsolutePath()));
			}

			var availablePluginsTry = buildAvailablePlugins(folderPath.toFile());
			if(availablePluginsTry.isFailure()){
				throw new PluginLoadingException(
				  String.format("While building the list of available plugins, there was an error: %s",availablePluginsTry.getCause()));
			}

			return new PluginManager(folderPath.toFile(),availablePluginsTry.get());
		});
	}



	/**
	 * From the jars in the folder, builds a list of available plugins.
	 * @return
	 */
	private static Try<List<PluginSpec>> buildAvailablePlugins(File pluginFolder) {
		return Try.of(()->{
			var pluginsJars = List.of(pluginFolder.list((dir,name)-> name.endsWith("jar")))
			  .map((jarFileName)->new File(pluginFolder.getAbsolutePath().concat(File.separator).concat(jarFileName)))
			  .map(PluginManager::convertToJarFile);

			if(!pluginsJars.filter(Try::isFailure).isEmpty()) {
				throw new PluginLoadingException(pluginsJars.filter(Try::isFailure).map(Try::getCause));
			}

			return pluginsJars.map(Try::get)
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

				throw new PluginLoadingException(String.format("Jar files in folder %s could not be loaded: %s",folder,cause));
			}

			var someJarIsNotPlugin = jarFiles.map(Try::get)
			  .exists((jarFile -> !PluginManager.has4XPluginInformation(jarFile)));

			return !someJarIsNotPlugin;
		});
	}

	/**
	 * Given a File, converts it into a Jar File.
	 * May return:
	 *  - {@link PluginFileCouldNotBeReadException}
	 */
	private static Try<JarFile> convertToJarFile(File file) {
		return Try.of(()->new JarFile(file))
		  .mapFailure(
		  	Case($(instanceOf(IOException.class)),t -> new PluginFileCouldNotBeReadException(t.getMessage()))
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
			var mainClass = Option.of(manifest.getMainAttributes().getValue(PLUGIN_MAIN_CLASS_MANIFEST_ENTRY_LABEL));

			// If the plugin is a base plugin, this will be empty
			var parentPlugin = Option.of(manifest.getMainAttributes().getValue(PLUGIN_PARENT_ENTRY_LABEL));

			return new PluginSpec(name,mainClass,parentPlugin);
		});
	}


}
