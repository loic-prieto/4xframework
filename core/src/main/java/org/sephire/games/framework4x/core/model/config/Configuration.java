package org.sephire.games.framework4x.core.model.config;

import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import io.vavr.control.Try;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.Predicates.instanceOf;

/**
 * This class contains the configMap as loaded by the different plugins.
 * <p>
 * The configMap is read-only once initialized, which means that it is safe
 * to share between threads and classes.
 * <p>
 * To allow for maximum flexibility, a configMap instance is basically an in-memory key/value
 * store with special semantic
 */
public class Configuration {
	private Map<ConfigKeyEnum, Object> configMap;

	private Configuration(Map<ConfigKeyEnum, Object> configMap) {
		this.configMap = configMap;
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
		  configMap.get(key)
			.map(configurationValueClass::cast)
			.getOrElseThrow(() -> new ConfigurationKeyNotFound(key)))
		  .mapFailure(
		    Case($(instanceOf(ClassCastException.class)),(e)->new InvalidConfigurationObjectCast(key,configurationValueClass))
		  );

	}

	public Option<Object> getConfiguration(ConfigKeyEnum key) {
		return configMap.get(key);
	}

	/**
	 * Returns a new configuration object that is the result of merging the new configuration with the old.
	 *
	 * The new object keys have precedence over the old keys, which means that new configuration override old configurations.
	 *
	 * @param overridingConfiguration
	 * @return
	 */
	public Configuration mergeWith(Configuration overridingConfiguration) {
		return new Configuration(this.configMap.merge(overridingConfiguration.configMap,(oldConfig,newConfig)->newConfig));
	}

	public static Builder builder() {
		return new Builder();
	}

	/**
	 * To build a configMap object, use this builder.
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
