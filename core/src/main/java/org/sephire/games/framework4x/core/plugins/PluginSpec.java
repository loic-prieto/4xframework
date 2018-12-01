package org.sephire.games.framework4x.core.plugins;


import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.Value;
import org.sephire.games.framework4x.core.plugins.configuration.CoreConfigProvider;

@Value
public class PluginSpec {
	private String pluginName;
	private Boolean isBasePlugin;
	private String mainClass;

	/**
	 * Returns a plugin spec from a configuration spec file parsed by cfg4j.
	 * <p>
	 * May return:
	 * - InvalidPluginSpecFileException when the required fields are not present or are invalid
	 *
	 * @param pluginName
	 * @param config
	 * @return
	 */
	public static Try<PluginSpec> fromConfiguration(String pluginName, CoreConfigProvider config) {
		return Try.of(() -> {
			if (!config.existsKey("plugin.name")) {
				throw new InvalidPluginSpecFileException("The name field is mandatory in the spec file", pluginName);
			}

			var name = config.getConfigFor("plugin.name", String.class).get();
			var clazz = config.getConfigFor("plugin.mainClass", ".Main")
			  .getOrElseThrow(() -> new InvalidPluginSpecFileException("The mainClass attribute is invalid", pluginName));
			var isBasePlugin = config.getConfigFor("plugin.isBasePlugin", Boolean.FALSE)
			  .getOrElseThrow(() -> new InvalidPluginSpecFileException("The isBasePlugin attribute is invalid", pluginName));

			return new PluginSpec(name, isBasePlugin, clazz);
		});
	}

	public Option<String> getMainClass() {
		return Option.of(mainClass);
	}

	public PluginSpec withMainClass(String mainClass) {
		return new PluginSpec(pluginName, isBasePlugin, mainClass);
	}
}
