package org.sephire.games.framework4x.core.plugins;

import io.vavr.Function2;
import io.vavr.control.Try;
import lombok.Getter;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.plugins.configuration.ConfigFileNotFoundException;
import org.sephire.games.framework4x.core.plugins.configuration.ConfigLoader;

import java.util.Arrays;
import java.util.List;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.Predicates.instanceOf;
import static io.vavr.control.Try.failure;
import static org.sephire.games.framework4x.core.model.config.CoreConfigKeyEnum.TERRAIN_TYPES;
import static org.sephire.games.framework4x.core.utils.ResourceLoading.packageToResourceSyntax;

/**
 * Represents a plugin to be loaded when a game starts.
 * <p>
 * A plugin may define model entities as declared in the core framework, if it is a base plugin, or may
 * override and extend the configuration of a base plugin.
 */
public class Plugin {
	private static final String DEFAULT_PLUGIN_SPEC_FILE = "plugin.yml";

	@Getter
	private PluginInitializer mainClass;
	@Getter
	private PluginSpec specification;

	private Plugin(PluginSpec spec, PluginInitializer mainClass) {
		this.mainClass = mainClass;
		this.specification = spec;
	}

	/**
	 * Given a plugin name, it will try to load it from the classpath.
	 * A plugin name is the name of its root java package, in which the Main class resides.
	 * This will be changed in the future to be able to specify the main class to load.
	 * <p>
	 * Once loaded, the plugin will be called to perform the changes to the configuration
	 * that it wants to do, with the current configuration loaded.
	 * <p>
	 * May return the following exceptions as errors:
	 * - PluginMainClassNotFoundException
	 * - PluginSpecFileNotFoundException
	 * - InvalidPluginSpecFileException
	 * - InvalidPluginMainClassException
	 *
	 * @param pluginName
	 * @return
	 */
	public static Try<Plugin> of(String pluginName) {
		var specTry = loadPluginSpecification(pluginName);
		if (specTry.isFailure()) {
			return failure(specTry.getCause());
		}
		var spec = specTry.get();

		return specTry
			.flatMap(Plugin::initializePluginMainClass)
			.map(Function2.of(Plugin::new).apply(spec));
	}

	/**
	 * Loads the main class of the plugin. Verifies that it is a valid main class.
	 *
	 * @param spec
	 * @return
	 */
	private static Try<PluginInitializer> initializePluginMainClass(PluginSpec spec) {

		return Try.of(() -> ClassLoader.getSystemClassLoader().loadClass(spec.getMainClass().get()))
			.mapFailure(
				Case($(instanceOf(ClassNotFoundException.class)),
					t -> new PluginMainClassNotFoundException(spec.getMainClass().get(), spec.getPluginName()))
			)
			.map(clazz -> (Class<? extends PluginInitializer>) clazz)
			.flatMap((Class<? extends PluginInitializer> pluginClass) -> Try.of(() -> {
				PluginInitializer pluginMainClass = null;
				try {
					pluginMainClass = pluginClass.getConstructor().newInstance();
				} catch (Exception e) {
					throw new InvalidPluginMainClassException(spec.getPluginName(), pluginClass, e);
				}

				return pluginMainClass;
			}));
	}

	/**
	 * Given a plugin name (which is its package), load the yaml specification.
	 *
	 * @param pluginName
	 * @return
	 */
	private static Try<PluginSpec> loadPluginSpecification(String pluginName) {

		return ConfigLoader.getConfigFor(packageToResourceSyntax(pluginName.concat("." + DEFAULT_PLUGIN_SPEC_FILE)))
			.flatMap(Function2.of(PluginSpec::fromConfiguration).apply(pluginName))
			.mapFailure(
				Case($(instanceOf(ConfigFileNotFoundException.class)), e -> new PluginSpecFileNotFound(pluginName))
			);
	}

	/**
	 * Will invoke the initializing of the plugin resources and configuration with a given
	 * external configuration ready to be filled.
	 *
	 * The result of this process tells if it was successful, so that dependent plugins are not
	 * loaded.
	 *
	 * @param configuration
	 * @return the same configuration builder, so that it can be chained
	 */
	public Try<Configuration.Builder> load(Configuration.Builder configuration) {
		return loadTerrainResources()
			.peek((terrainConfig) -> configuration.addConfig(TERRAIN_TYPES, terrainConfig))
			// Once everything is done, let the plugin load its final configuration programmatically
			.andThenTry(() -> mainClass.pluginLoad(configuration))
			.map((discardedResult) -> configuration);
	}

	private Try<List<String>> loadTerrainResources() {

		return ConfigLoader.getConfigFor(packageToResourceSyntax(toClasspathFile(CoreResourcesTypes.TERRAIN_TYPES.getFileName())))
			.flatMap((config) -> config.getConfigFor("terrains.types", Arrays.<String>asList()));

	}

	/**
	 * Given a filename to be fetched from the plugin classpath, transforms it to a fully
	 * pathed resource.
	 *
	 * @param fileName
	 * @return
	 */
	private String toClasspathFile(String fileName) {
		return this.specification.getPluginName().concat("." + fileName);
	}

}
