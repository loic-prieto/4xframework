package org.sephire.games.framework4x.core.plugins;


import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.sephire.games.framework4x.core.plugins.configuration.PluginSpecMapping;

@Getter
@AllArgsConstructor
public class PluginSpec {
	private String pluginName;
	private Boolean isBasePlugin;
	private String mainClass;

	/**
	 * Returns a plugin spec from a configuration spec file parsed by cfg4j.
	 * <p>
	 * May return:
	 * - InvalidPluginSpecFileException when the required fields (name) are not present
	 *
	 * @param pluginName
	 * @param config
	 * @return
	 */
	public static Try<PluginSpec> fromConfiguration(String pluginName, PluginSpecMapping config) {
		return Try.of(() -> {

			if (config.getName() == null) {
				throw new InvalidPluginSpecFileException("The name field is mandatory in the spec file", pluginName);
			}

			var clazz = config.getMainClass() != null ? config.getMainClass() : pluginName.concat(".Main");
			var isBasePlugin = config.getIsBasePlugin() != null ? config.getIsBasePlugin() : false;

			return new PluginSpec(config.getName(), isBasePlugin, clazz);
		});
	}

	public Option<String> getMainClass() {
		return Option.of(mainClass);
	}

	public PluginSpec withMainClass(String mainClass) {
		return new PluginSpec(pluginName, isBasePlugin, mainClass);
	}
}
