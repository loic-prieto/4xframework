package org.sephire.games.framework4x.core.utils;

import io.vavr.control.Try;
import org.snakeyaml.engine.v1.api.Load;
import org.snakeyaml.engine.v1.api.LoadSettingsBuilder;

import java.io.IOException;
import java.util.Map;

public class ResourceLoading {

	/**
	 * Given a path specified in package syntax, transforms it
	 * to resource folder syntax.
	 *
	 * @param path
	 * @return
	 */
	public static String packageToResourceSyntax(String path) {
		return path.replaceAll("\\.", "/");
	}

	/**
	 * Given an classpath resource parses the yaml content inside it.
	 * <p>
	 * May return the following exception:
	 * - YamlEngineException
	 * - IOException
	 * <p>
	 * Assumes the root of the yaml file is a Map
	 * <p>
	 * Snake YAML uses standard java Map and List, converting to vavr is a pain, so deal with legacy.
	 *
	 * @param fileName
	 * @return
	 */
	public static Try<Map> loadYAMLConfigFile(String fileName) {
		//noinspection unchecked
		return Try.of(() -> {
			var stream = ClassLoader.getSystemClassLoader().getResourceAsStream(fileName);
			if (stream == null) {
				throw new IOException("Could not find yaml file " + fileName + " in the classpath");
			}

			return (java.util.Map) (new Load(new LoadSettingsBuilder().build())).loadFromInputStream(stream);
		});
	}
}
