package org.sephire.games.framework4x.core.plugins.configuration;

import io.vavr.control.Try;
import org.cfg4j.provider.ConfigurationProvider;

import java.util.NoSuchElementException;

import static io.vavr.API.*;
import static io.vavr.Predicates.instanceOf;

/**
 * This is a wrapper for the cfg4j provider class that transforms the provider
 * to vavr results for handling errors.
 */
public class CoreConfigProvider {
	private ConfigurationProvider configuration;

	public CoreConfigProvider(ConfigurationProvider configuration) {
		this.configuration = configuration;
	}

	/**
	 * For a given binding class and config key, tries to map every
	 * field found in the config value to the binding class fields.
	 * <p>
	 * May return:
	 * - NoSuchElementException if the key does not exist
	 * - IllegalArgumentException if the value class is not correct
	 * - IllegalStateException if the configuration could not be fetched from the file (cfg4j internal error)
	 *
	 * @param key
	 * @param bindingClass
	 * @param <T>
	 * @return
	 */
	public <T> Try<T> bindConfigFor(String key, Class<T> bindingClass) {
		return Try.of(() -> configuration.bind(key, bindingClass));
	}

	/**
	 * For a given config key path (in cfg4j style) in the resource content,
	 * try to retrieve its value.
	 * <p>
	 * May return:
	 * - NoSuchElementException if the key does not exist
	 * - IllegalArgumentException if the value class is not correct
	 * - IllegalStateException if the configuration could not be fetched from the file (cfg4j internal error)
	 *
	 * @param key
	 * @param valueClass
	 * @param <T>
	 * @return
	 */
	public <T> Try<T> getConfigFor(String key, Class<T> valueClass) {
		return Try.of(() -> configuration.getProperty(key, valueClass));
	}

	/**
	 * Tries to get a value from a given key path (in cfg4j style) in the resource content,
	 * or returns the given default value if it could not find it.
	 * <p>
	 * May return:
	 * - IllegalArgumentException if the value class is not correct for the key
	 * - IllegalStateException if the configuration could not be fetched from the source file (cf4j internal error)
	 *
	 * @param key
	 * @param defaultValue
	 * @param <T>
	 * @return
	 */
	public <T> Try<T> getConfigFor(String key, T defaultValue) {
		var valueClass = (Class<T>) defaultValue.getClass();
		return Try.of(() -> configuration.getProperty(key, valueClass))
		  .recover((e) -> Match(e).of(
			Case($(instanceOf(NoSuchElementException.class)), () -> defaultValue))
		  );
	}

	/**
	 * Returns whether a given key exists inside the configuration source file.
	 *
	 * @param key
	 * @return
	 */
	public boolean existsKey(String key) {
		return Try.of(() -> {
			configuration.getProperty(key, Object.class);
			return true;
		}).recover((e) -> Match(e).of(
		  Case($(instanceOf(NoSuchElementException.class)), false)
		)).get();
	}
}
