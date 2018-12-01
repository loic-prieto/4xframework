package org.sephire.games.framework4x.core.model.config;

import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import io.vavr.control.Try;

/**
 * This class contains the configuration as loaded by the different plugins.
 * <p>
 * The configuration is read-only once initialized, which means that it is safe
 * to share between threads and classes.
 * <p>
 * To allow for maximum flexibility, a configuration instance is basically an in-memory key/value
 * store with special semantic
 */
public class Configuration {
	private Map<ConfigKeyEnum, Object> configuration;

	private Configuration(Map<ConfigKeyEnum, Object> configuration) {
		this.configuration = configuration;
	}

	/**
	 * Gets a config object and tries to cast it to the given class.
	 *
	 * @param key
	 * @param configurationValueClass
	 * @param <T>
	 * @return
	 */
	public <T> Try<T> getConfiguration(ConfigKeyEnum key, Class<T> configurationValueClass) {
		return Try.of(() ->
		  configuration.get(key)
			.map(configurationValueClass::cast)
			.getOrElseThrow(() -> new ConfigurationKeyNotFound(key)));

	}

	public Option<Object> getConfiguration(ConfigKeyEnum key) {
		return configuration.get(key);
	}

	/**
	 * To build a configuration object, use this builder.
	 */
	public static class Builder {
		private Map<ConfigKeyEnum, Object> configParam;

		public Builder() {
			this.configParam = HashMap.empty();
		}

		/**
		 * Adds a config value for a given key.
		 *
		 * @param key
		 * @param configValue
		 * @return
		 */
		public Builder addConfig(ConfigKeyEnum key, Object configValue) {

			this.configParam = this.configParam.put(key, configValue);
			return this;
		}

		public Option<Object> getConfig(ConfigKeyEnum key) {
			return configParam.get(key);
		}

		public Configuration build() {
			return new Configuration(configParam);
		}
	}
}
