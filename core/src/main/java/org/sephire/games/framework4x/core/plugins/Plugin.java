package org.sephire.games.framework4x.core.plugins;

import io.vavr.API;
import io.vavr.collection.Set;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.Getter;
import org.reflections.Reflections;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.plugins.configuration.*;

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
				throw new InvalidPluginSpecFileException(
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
	 * Will invoke the initializing of the plugin resources and configuration with a given
	 * external configuration ready to be filled.
	 * <p>
	 * The result of this process tells if it was successful, so that dependent plugins are not
	 * loaded.
	 *
	 * @param configuration
	 */
	private Try<Void> load(Configuration.Builder configuration) {

		return Try.of(()->{
			var lifecycleHandler = fetchLifeCycleHandler().getOrElseThrow((t)->t);

			loadTerrainResources(configuration);

			// Once everything is done, give a chance to the plugin to add last-minute configuration
			lifecycleHandler.peek(handler->handler.callPluginLoadingHook(configuration));

			return null;
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
			  configuration.addConfig(TERRAIN_TYPES, newTerrainSet);
		  })
		  .map((discardedResult) -> configuration)
		  // The terrain file is not mandatory for a plugin
		  .recover((e) -> Match(e).of(
			Case($(instanceOf(ConfigFileNotFoundException.class)), configuration)
		  ));
	}

	private Try<Option<PluginLifecycleHandlerWrapper>> fetchLifeCycleHandler() {
		return Try.of(()->{
			// If we don't put an extra point here, the regexp filter to search for packages becomes
			// nameOfPackage.*, which would include nameOfPackageSomething packages at the same level.
			// This way the regexp filter for Reflections ends the name of the package with the dot
			Reflections reflections = new Reflections(this.specification.getRootPackage().concat("."));
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

	private Try<Option<Object>> fetchMapProviders() {
		return Try.failure(new OperationNotSupportedException());
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
