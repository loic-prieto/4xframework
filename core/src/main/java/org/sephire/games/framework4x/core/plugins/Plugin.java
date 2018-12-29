package org.sephire.games.framework4x.core.plugins;

import io.vavr.API;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Set;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.Getter;
import org.reflections.Reflections;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.model.config.CoreConfigKeyEnum;
import org.sephire.games.framework4x.core.plugins.configuration.*;
import org.sephire.games.framework4x.core.plugins.map.*;

import javax.naming.OperationNotSupportedException;

import static io.vavr.API.*;
import static io.vavr.Predicates.instanceOf;
import static java.lang.String.format;
import static org.sephire.games.framework4x.core.model.config.CoreConfigKeyEnum.TERRAIN_TYPES;
import static org.sephire.games.framework4x.core.utils.ResourceLoading.packageToFolderPath;

/**
 * Represents a plugin to be loaded when a game starts.
 * <p>
 * A plugin may define model entities as declared in the core framework, if it is a base plugin, or may
 * override and extend the configuration of a base plugin.
 */
public class Plugin {

	@Getter
	private PluginSpec specification;

	private Plugin(PluginSpec spec) {
		this.specification = spec;
	}

	/**
	 * Given a plugin spec, it will try to load it from the classpath.
	 * A plugin has a root package, from which it will scan all automatic
	 * resources and the configuration classes and initialize all those
	 * resources.
	 *
	 * May return the following exceptions as errors:
	 *
	 *
	 * @param pluginSpec
	 * @return
	 */
	public static Try<Plugin> from(PluginSpec pluginSpec,Configuration.Builder configuration) {
		return Try.of(()->{
			// Verify the package exists
			if(!doesPackageExist(pluginSpec)){
				throw new InvalidPluginSpecException(
				  format("The root package %s does not exist",pluginSpec.getRootPackage()),
				  pluginSpec.getPluginName());
			}

			var plugin = new Plugin(pluginSpec);

			var loadingTry = plugin.load(configuration);

			if(loadingTry.isFailure()) {
				throw loadingTry.getCause();
			}

			return plugin;
		});
	}

	private static boolean doesPackageExist(PluginSpec pluginSpec) {
		var packageFolder = pluginSpec.getRootPackage().replaceAll("\\.","/");
		return ClassLoader.getSystemClassLoader().getResource(packageFolder) != null;
	}

	/**
	 * <p>Will invoke the initializing of the plugin resources and configuration with a given
	 * external configuration ready to be filled.</p>
	 * <p>
	 * The result of this process tells if it was successful, so that dependent plugins are not
	 * loaded.
	 * </p>
	 *
	 * @param configuration
	 */
	private Try<Void> load(Configuration.Builder configuration) {

		return Try.of(()->{
			var lifecycleHandler = fetchLifeCycleHandler().getOrElseThrow((t)->t);
			var mapGenerators = fetchMapGenerators().getOrElseThrow((t)->t);

			// This is the fastest way to throw if there is failure, even if we're not interested
			// in the result
			loadTerrainResources(configuration).getOrElseThrow((t)->t);
			loadMapGenerators(mapGenerators,configuration).getOrElseThrow((t)->t);

			// Once everything is done, give a chance to the plugin to add last-minute configuration
			if(lifecycleHandler.isDefined()){
				lifecycleHandler.get().callPluginLoadingHook(configuration).getOrElseThrow((t)->t);
			}

			return null;
		});
	}

	/**
	 * Will load the map generators of this plugin into the configuration.
	 * @param generators
	 * @param configuration
	 * @return
	 */
	private Try<Configuration.Builder> loadMapGenerators(Set<MapGeneratorWrapper> generators,Configuration.Builder configuration) {
		return Try.of(()->{
			var addOperation = configuration.addAllTo(CoreConfigKeyEnum.MAPS,generators);
			if(addOperation.isFailure()) {
				throw new PluginLoadingException(addOperation.getCause());
			}

			return configuration;
		});
	}

	private Try<Configuration.Builder> loadTerrainResources(Configuration.Builder configuration) {
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
		  .map((discardedResult) -> configuration)
		  // The terrain file is not mandatory for a plugin
		  .recover((e) -> Match(e).of(
			Case($(instanceOf(ConfigFileNotFoundException.class)), configuration)
		  ));
	}

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

	private Try<Option<Object>> fetchGameParameterProviders() {
		return Try.failure(new OperationNotSupportedException());
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
	 * When using reflection to search for classes inside a package, the search is done as a regexp. We need to add a point
	 * to the name of the package, so that packages that are named the same as this one but with more characters are not included.
	 * See Reflections.getTypesAnnotatedWith method to see what I mean.
	 *
	 * @param packageName
	 * @return
	 */
	private String normalizePackageNameForReflection(String packageName) {
		return packageName.endsWith(".") ?
			packageName :
			packageName.concat(".");
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
