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

import io.vavr.API;
import io.vavr.Tuple;
import io.vavr.Tuple3;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Set;
import io.vavr.collection.*;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.ResourcesScanner;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.model.config.CoreConfigKeyEnum;
import org.sephire.games.framework4x.core.model.game.Game;
import org.sephire.games.framework4x.core.plugins.commands.GameCommandGenerator;
import org.sephire.games.framework4x.core.plugins.commands.GameCommandGeneratorWrapper;
import org.sephire.games.framework4x.core.plugins.configuration.*;
import org.sephire.games.framework4x.core.plugins.configuration.resources.civilizations.CivilizationsConfigurationLoader;
import org.sephire.games.framework4x.core.plugins.map.MapGeneratorWrapper;
import org.sephire.games.framework4x.core.plugins.map.MapProvider;
import org.sephire.games.framework4x.core.plugins.map.MapProviderWrapper;
import org.sephire.games.framework4x.core.plugins.map.MapProviderWrappingException;
import org.sephire.games.framework4x.core.utils.FunctionalUtils.Functions;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import static io.vavr.API.*;
import static io.vavr.Predicates.instanceOf;
import static java.lang.String.format;
import static org.sephire.games.framework4x.core.model.config.CoreConfigKeyEnum.I18N;
import static org.sephire.games.framework4x.core.model.config.CoreConfigKeyEnum.TERRAIN_TYPES;
import static org.sephire.games.framework4x.core.utils.FunctionalUtils.Collectors.toTry;
import static org.sephire.games.framework4x.core.utils.ResourceLoading.normalizePackageNameForReflection;
import static org.sephire.games.framework4x.core.utils.ResourceLoading.packageToFolderPath;

/**
 * <p>Represents a plugin to be loaded when a game starts.</p>
 * <p>A plugin may define model entities as declared in the core framework, if it is a base plugin, or may
 * override and extend the configuration of a base plugin.</p>
 */
@EqualsAndHashCode(of = {"specification"})
public class Plugin {

	private final static Pattern PROPERTIES_BUNDLE_FORMAT =
	  Pattern.compile("^(?<filename>(?<package>.*)/(?<bundleName>[^_]*))_?(?<locale>.*)?$");

	@Getter
	private PluginSpec specification;
	private Option<PluginLifecycleHandlerWrapper> lifecycleHandler;

	private Plugin(PluginSpec spec) throws Throwable {
		this.specification = spec;
		this.lifecycleHandler = fetchLifeCycleHandler().getOrElseThrow(e->e);
	}

	/**
	 * <p>Given a plugin spec, it will try to load it from the classpath.</p>
	 * <p>A plugin has a root package, from which it will scan all automatic
	 * resources and the configuration classes and initialize all those
	 * resources.</p>
	 *
	 * <p>May return the following exceptions as errors:
	 * <ul>
	 *     <li>InvalidPluginLifecycleHandlerException</li>
	 *     <li>PluginLoadingException</li>
	 * </ul>
	 *  </p>
	 *
	 * @param pluginSpec
	 * @return
	 */
	public static Try<Plugin> from(PluginSpec pluginSpec,Configuration.Builder configuration) {
		return Try.of(()->{
			var plugin = new Plugin(pluginSpec);
			var loadingTry = plugin.load(configuration);
			if(loadingTry.isFailure()) {
				throw loadingTry.getCause();
			}

			return plugin;
		});
	}

	/**
	 * <p>Call the game start hook of the plugin if it exists</p>
	 * @param game
	 * @return
	 */
	public Try<Void> callGameStartHook(Game game) {
		return Try.of(()->{
			lifecycleHandler.map((handler)->handler.callGameLoadingHook(game));
			return null;
		});
	}

	private static void updateConfigWithBundleEntry(Configuration.Builder configuration, Tuple3<Locale, String, String> bundleEntry) {
		var i18nMap = configuration.getConfig(CoreConfigKeyEnum.I18N).map((v) -> (Map<Locale, Map<String, String>>) v)
		  .getOrElse(HashMap.empty());
		var i18nLocaleMap = i18nMap.get(bundleEntry._1).getOrElse(HashMap.empty());
		configuration.putConfig(
		  I18N,
		  i18nMap.put(
			bundleEntry._1,
			i18nLocaleMap.put(
			  bundleEntry._2, bundleEntry._3)));
	}

	private static Option<ResourceBundle> bundleFromFileName(String filename) {
		var matcher = PROPERTIES_BUNDLE_FORMAT.matcher(filename);
		var matched = matcher.find();

		var result = Option.<ResourceBundle>none();

		if(matched) {
			var bundleBaseFilename = matcher.group("filename");
			var locale = matcher.group("locale");

			result = matcher.group("locale") != null ?
			  Option.of(PropertyResourceBundle.getBundle(bundleBaseFilename,Locale.forLanguageTag(locale))) :
			  Option.of(PropertyResourceBundle.getBundle(bundleBaseFilename));
		}

		return result;
	}

	private static Set<Tuple3<Locale, String, String>> entriesFromBundle(ResourceBundle bundle) {
		return HashSet.ofAll(bundle.keySet())
		  .map((key) -> Tuple.of(bundle.getLocale(), key, bundle.getString(key)));
	}

	/**
	 * <p>Will invoke the initializing of the plugin resources and configuration with a given
	 * external configuration ready to be filled.</p>
	 * <p>The result of this process tells if it was successful, so that dependent plugins are not
	 * loaded</p>
	 * <p>The configuration object will be updated in place</p>
	 *
	 * @param configuration
	 */
	private Try<Void> load(Configuration.Builder configuration) {

		return loadTerrainResources(configuration)
		  .andThen(() -> loadMapGenerators(configuration))
		  .andThen(() -> loadI18NResources(configuration))
		  .andThen(() -> loadGameCommands(configuration))
		  .andThen(() -> loadCivilizations(configuration))
		  .andThen(() -> callPluginLoadingHooks(configuration));
	}

	/**
	 * Allows a plugin to hook into the plugin loading process, which allows to put some extra configuration
	 * that cannot be generated by the standard resource files or generators.
	 * @param configuration
	 * @return
	 */
	private Try<Void> callPluginLoadingHooks(Configuration.Builder configuration) {
		return Try.of(()->{
			if(lifecycleHandler.isDefined()){
				lifecycleHandler.get().callPluginLoadingHook(configuration).getOrElseThrow((t)->t);
			}
			return null;
		});
	}

	/**
	 * Will load all i18n resources into the configuration object from the plugin's i18n package folder
	 * Resources are defined as standard resource bundle properties files.
	 * The configuration will have the following type inside the I18N key: Map&lt;Locale, Map&lt;String,String&gt;&gt;
	 * @param configuration
	 * @return
	 */
	private Try<Void> loadI18NResources(Configuration.Builder configuration) {
		return Try.of(() -> {
			Reflections reflections = new Reflections(
			  normalizePackageNameForReflection(this.specification.getRootPackage().concat(".i18n")),
			  new ResourcesScanner());

			HashSet.ofAll(reflections.getResources(Pattern.compile(".*\\.properties")))
			  .map((name) -> name.replaceAll("\\.properties", ""))
			  .map(Plugin::bundleFromFileName)
			  .filter(Option::isDefined).map(Option::get)
			  .flatMap(Plugin::entriesFromBundle)
			  .forEach(bundleEntry -> updateConfigWithBundleEntry(configuration, bundleEntry));

			return null;
		});
	}

	/**
	 * Will load the map generators of this plugin into the configuration.
	 * @param configuration
	 * @return
	 */
	private Try<Void> loadMapGenerators(Configuration.Builder configuration) {
		return Try.of(()->{
			var mapGenerators = fetchMapGenerators().getOrElseThrow((t) -> t);
			var addOperation = configuration.addAllTo(CoreConfigKeyEnum.MAPS, mapGenerators);
			if(addOperation.isFailure()) {
				throw new PluginLoadingException(addOperation.getCause());
			}

			return null;
		});
	}

	/**
	 * <p>Loads the civilizations from the civilizations defined in pluginPackage/civilizations.xml optional file of the
	 * plugin.</p>
	 * <p>There is currently no other way to define civilizations, although user defined civilizations should be
	 * retrievable from the user's home directory in a later iteration of this application</p>
	 * @param configuration
	 * @return
	 */
	private Try<Void> loadCivilizations(Configuration.Builder configuration) {
		return Try.of(() -> {
			var civLoader = new CivilizationsConfigurationLoader(getSpecification().getRootPackage());
			civLoader.load(configuration).getOrElseThrow(t -> t);
			return null;
		});
	}

	/**
	 * Loads the terrain resources defined in this plugin, found in the CoreResourcesTypes.TERRAIN_TYPES.getFileName() file
	 * in the class folder of the plugin, into the configuration under the CoreConfigKeyEnum.TERRAIN_TYPES key.
	 *
	 * @param configuration
	 * @return
	 */
	private Try<Void> loadTerrainResources(Configuration.Builder configuration) {
		var terrainTypesFilename = toClasspathFile(CoreResourcesTypes.TERRAIN_TYPES.getFileName());
		return ConfigLoader.getConfigFor(terrainTypesFilename, TerrainsTypesMapping.class)
		  .map((mapping) -> mapping.getTypes().toArray(new String[]{}))
		  .map(API::Set)
		  // Merge with previous terrain config
		  .peek((terrainSet) -> {
			  var newTerrainSet = terrainSet;
			  var existentTerrainConfig = configuration.getConfig(TERRAIN_TYPES);
			  if (existentTerrainConfig.isDefined()) {
				  newTerrainSet = newTerrainSet.union((Set<String>) existentTerrainConfig.get());
			  }
			  configuration.putConfig(TERRAIN_TYPES, newTerrainSet);
		  })
		  .map(Functions::toVoid)
		  // The terrain file is not mandatory for a plugin
		  .recover((e) -> Match(e).of(
			Case($(instanceOf(ConfigFileNotFoundException.class)), (Void) null)
		  ));
	}

	/**
	 * <p>Loads all game command generators from the plugin.</p>
	 * <p>May return the following errors:<ul>
	 *     <li>{@link org.sephire.games.framework4x.core.model.game.ParentCategoryDoesntExistException} if a parent
	 *     category used for a category o command doesn't exist previously</li>
	 * </ul></p>
	 * <p>Methods are loaded by name order<br/>
	 * In general, the plugin shouldn't rely on a loading order inside the plugin, that's what the different method
	 * signatures are for (if there is a dependency between categories and plugins, better to return the category with
	 * the command inside, than to rely on method load order.<br/>
	 * But if for some reason the load order is important, then it is useful to know that the methods will be loaded by
	 * method name order.
	 * </p>
	 * @param configuration
	 * @return
	 */
	private Try<Void> loadGameCommands(Configuration.Builder configuration) {
		return Try.of(()->{
			var reflections = new Reflections(normalizePackageNameForReflection(this.specification.getRootPackage()),
			  new MethodAnnotationsScanner());

			List.ofAll(reflections.getMethodsAnnotatedWith(GameCommandGenerator.class))
			  .sortBy(Comparator.comparing(Method::getName),(method)->method)
			  .map(GameCommandGeneratorWrapper::from)
			  .collect(toTry()).getOrElseThrow(t->t)
			  .map(generator->generator.execute(configuration)).collect(toTry()).getOrElseThrow(t->t);

			return null;
		});
	}

	/**
	 * Retrieve the plugin lifecycle hooks if any have been defined.
	 * @return
	 */
	private Try<Option<PluginLifecycleHandlerWrapper>> fetchLifeCycleHandler() {
		return Try.of(()->{
			Reflections reflections = new Reflections(normalizePackageNameForReflection(this.specification.getRootPackage()));
			var lifecycleHandlersClasses = reflections.getTypesAnnotatedWith(PluginLifecycleHandler.class);
			if(lifecycleHandlersClasses.size() > 1) {
				throw new InvalidPluginLifecycleHandlerException(
				  format("Too many plugin lifecycle handlers for plugin %s",specification.getPluginName()));
			}

			Option<PluginLifecycleHandlerWrapper> pluginLifecycleHandler = Option.none();
			if(!lifecycleHandlersClasses.isEmpty()) {
				var pluginLifecycleHandlerTry = PluginLifecycleHandlerWrapper.from(lifecycleHandlersClasses.iterator().next());
				if (pluginLifecycleHandlerTry.isFailure()) {
					throw pluginLifecycleHandlerTry.getCause();
				}

				pluginLifecycleHandler = Option.of(pluginLifecycleHandlerTry.get());
			}

			return pluginLifecycleHandler;
		});
	}

	/**
	 * From the root package of the plugin, will try to fetch all map generators found inside that root package.
	 * An empty set is a valid value.
	 *
	 * May return the following error:
	 *  - {@link MapProviderWrappingException} if there was an error while wrapping a map provider or map generator
	 *  - unknown errors as of yet, to be discovered with testing
	 * @return
	 */
	private Try<Set<MapGeneratorWrapper>> fetchMapGenerators() {

		return Try.of(()->{
			Reflections reflections = new Reflections(normalizePackageNameForReflection(this.specification.getRootPackage()));
			var mapProviders = reflections.getTypesAnnotatedWith(MapProvider.class);

			Set<MapGeneratorWrapper> mapGeneratorWrappers = HashSet.empty();
			if(!mapProviders.isEmpty()) {
				var mapProvidersWrappingOperations = List.ofAll(mapProviders.stream()).map(MapProviderWrapper::from);
				if(Try.sequence(mapProvidersWrappingOperations).isFailure()) {
					var failures = mapProvidersWrappingOperations.filter(Try::isFailure).map(Try::getCause);
					throw new MapProviderWrappingException(failures);
				}

				var mapGenerators = mapProvidersWrappingOperations.map(Try::get)
				  .map(MapProviderWrapper::getMapGenerators)
				  .collect(HashSet.collector());

				if(Try.sequence(mapGenerators).isFailure()) {
					var failures = mapGenerators.filter(Try::isFailure).map(Try::getCause);
					throw new MapProviderWrappingException(failures);
				}

				mapGeneratorWrappers = mapGenerators.flatMap(Try::get);
			}

			return mapGeneratorWrappers;
		});
	}

	/**
	 * Given a filename to be fetched from the plugin classpath, transforms it to a fully
	 * pathed resource.
	 *
	 * @param fileName
	 * @return
	 */
	private String toClasspathFile(String fileName) {
		return packageToFolderPath(this.specification.getRootPackage()).concat("/" + fileName);
	}
}
