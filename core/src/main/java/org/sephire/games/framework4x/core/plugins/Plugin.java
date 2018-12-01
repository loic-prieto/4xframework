package org.sephire.games.framework4x.core.plugins;

import io.vavr.Tuple;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.control.Try;
import lombok.Getter;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.model.config.CoreConfigKeyEnum;
import org.sephire.games.framework4x.core.model.config.InvalidResourceFileException;
import org.snakeyaml.engine.v1.exceptions.YamlEngineException;

import java.io.IOException;

import static io.vavr.API.*;
import static io.vavr.Predicates.instanceOf;
import static org.sephire.games.framework4x.core.utils.ResourceLoading.loadYAMLConfigFile;
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

	private Plugin(PluginInitializer mainClass, PluginSpec spec) {
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
			return Try.failure(specTry.getCause());
		}
		var spec = specTry.get();

		return specTry
		  .flatMap(Plugin::initializePluginMainClass)
		  .map(mainClass -> new Plugin(mainClass, spec));
	}

	/**
	 * Loads the main class of the plugin. Verifies that it is a valid main class.
	 *
	 * @param spec
	 * @return
	 */
	private static Try<PluginInitializer> initializePluginMainClass(PluginSpec spec) {
		//noinspection unchecked // This is needed because of mapFailure
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

		//noinspection unchecked
		return loadYAMLConfigFile(packageToResourceSyntax(pluginName.concat("." + DEFAULT_PLUGIN_SPEC_FILE)))
		  .mapFailure(
			Case($(instanceOf(YamlEngineException.class)), e -> new InvalidPluginSpecFileException(pluginName, e)),
			Case($(instanceOf(IOException.class)), e -> new PluginSpecFileNotFound(pluginName))
		  )
		  .flatMap((data) -> PluginSpec.fromYAML(pluginName, data));
	}

	/**
	 * Will invoke the initializing of the plugin resources and configuration with a given
	 * external configuration ready to be filled.
	 * <p>
	 * The result of this process tells if it was successful, so that dependent plugins are not
	 * loaded.
	 *
	 * @param configuration
	 * @return
	 */
	public Try<Void> load(Configuration.Builder configuration) {
		return loadTerrainResources(configuration)
		  // Once everything is done, let the plugin load its final configuration programmatically
		  .andThenTry(() -> mainClass.pluginLoad(configuration));

		configuration.addConfig(CoreConfigKeyEnum.TERRAIN_TYPES, terrainTypes);
	}

	private Try<Map<String, String>> loadTerrainResources() {
		String fileName = packageToResourceSyntax(toClasspathFile(CoreResourcesTypes.TERRAIN_TYPES.getFileName()));
		//noinspection unchecked
		return
		  loadYAMLConfigFile(fileName)
			.map((rawYamlConfig) ->
			  rawYamlConfig.entrySet().stream().map((rawEntry) -> {
				  var entry = (java.util.Map.Entry) rawEntry;
				  return Tuple.of(entry.getKey(), entry.getValue());
			  }).collect(HashMap.collector());
			)
			.recover(e -> Match(e).of(Case($(instanceOf(IOException.class)), HashMap.empty()))) // All resource files are optional
		  .mapFailure(Case($(instanceOf(YamlEngineException.class)), e -> new InvalidResourceFileException(fileName, e)));
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
