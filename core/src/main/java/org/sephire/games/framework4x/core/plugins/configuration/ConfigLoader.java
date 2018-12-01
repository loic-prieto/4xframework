package org.sephire.games.framework4x.core.plugins.configuration;

import com.yacl4j.core.ConfigurationBuilder;
import com.yacl4j.core.source.optional.ConfigurationSourceNotAvailableException;
import io.vavr.control.Try;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.Predicates.instanceOf;

/**
 * Utility class to return configuration objects (from CFG4J) from
 * yaml files stored in the classpath.
 */
public class ConfigLoader {

	/**
	 * Given a yaml file name and a class to convert to, returns a parsed config class.
	 *
	 * Fields that are not found in the yaml document are set to null.
	 *
	 * May return:
	 *   - {@link ConfigFileNotFoundException}
	 *   - {@link InvalidConfigFileException}
	 * @param classpathFilename
	 * @param configClass
	 * @param <T>
	 * @return
	 */
	public static <T> Try<T> getConfigFor(String classpathFilename, Class<T> configClass) {
		return Try.of(() ->
		  ConfigurationBuilder.newBuilder()
			.source().fromFileOnClasspath(classpathFilename)
			.build(configClass))
		  .mapFailure(
			Case($(instanceOf(ConfigurationSourceNotAvailableException.class)), (e) -> new ConfigFileNotFoundException(classpathFilename)),
			Case($(instanceOf(IllegalStateException.class)), (e) -> new InvalidConfigFileException(classpathFilename, e))
		  );
	}

}
