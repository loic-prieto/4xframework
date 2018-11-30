package org.sephire.games.framework4x.core.plugins;


import io.vavr.collection.HashMap;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.Value;

import java.util.Map;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.Predicates.instanceOf;

@Value
public class PluginSpec {
	private String pluginName;
	private Boolean isBasePlugin;
	private String mainClass;

	/**
	 * Returns a new plugin spec from a raw result of a YAML deserializing with snake yaml engine,
	 * which is a non parameterized Map of (String,Object)
	 *
	 * @param pluginPackage
	 * @param rawYaml
	 * @return
	 */
	public static Try<PluginSpec> fromYAML(String pluginPackage, Map rawYaml) {
		// noinspection unchecked
		return Try.of(() -> {
			var yaml = HashMap.<String, Object>ofAll(rawYaml);

			var name = (String) yaml.get("name").getOrElseThrow(() -> new InvalidPluginSpecFileException("The name field is mandatory", pluginPackage));
			var clazz = (String) yaml.get("mainClass").getOrElse(name + ".Main");
			var isBase = (boolean) yaml.get("isBasePlugin").getOrElse(false);

			return new PluginSpec(name, isBase, clazz);
		}).mapFailure(
			Case($(instanceOf(ClassCastException.class)), e -> new InvalidPluginSpecFileException(pluginPackage, e))
		);
	}

	public Option<String> getMainClass() {
		return Option.of(mainClass);
	}

	public PluginSpec withMainClass(String mainClass) {
		return new PluginSpec(pluginName, isBasePlugin, mainClass);
	}
}
