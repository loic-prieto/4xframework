package org.sephire.games.framework4x.core.model.config;

import io.vavr.collection.*;
import io.vavr.control.Option;
import io.vavr.control.Try;

import java.util.Collection;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.Predicates.instanceOf;

/**
 * <p>This class contains the configMap as loaded by the different plugins.</p>
 * <p>
 * The configMap is read-only once initialized, which means that it is safe
 * to share between threads and classes.
 * </p>
 * <p>
 * To allow for maximum flexibility, a configMap instance is basically an in-memory key/value
 * store with special semantic
 * </p>
 */
public class Configuration {
	private Map<ConfigKeyEnum, Object> configMap;

	private Configuration(Map<ConfigKeyEnum, Object> configMap) {
		this.configMap = configMap;
	}

	/**
	 * Gets a config object and tries to cast it to the given class.
	 *
	 * May return the following error:
	 *  - {@link InvalidConfigurationObjectCast} if the value object is not of the given class
	 * @param key
	 * @param configurationValueClass
	 * @param <T>
	 * @return
	 */
	public <T> Try<Option<T>> getConfiguration(ConfigKeyEnum key, Class<T> configurationValueClass) {
		return Try.of(() ->
		  configMap.get(key)
			.map(configurationValueClass::cast))
		  .mapFailure(
		    Case($(instanceOf(ClassCastException.class)),(e)->new InvalidConfigurationObjectCast(key,configurationValueClass))
		  );
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
		public Builder putConfig(ConfigKeyEnum key, Object configValue) {

			this.configParam = this.configParam.put(key, configValue);
			return this;
		}

		/**
		 * Add a value to a multi-valued configuration key.
		 * To work, the value of the key must be a vavr collection, as in: Set or Seq,
		 * or a Java standard Collection.
		 *
		 * If the key didn't exist previously, will create a vavr list container for the key.
		 *
		 * @param key
		 * @param configValue shouldn't be a container itself. This method is designed to add single-valued values, use
		 *                    addAllTo method if the value is multivalued.
		 * @return
		 */
		public Try<Builder> addConfig(ConfigKeyEnum key, Object configValue) {
			return Try.of(()->{
				var containerSearch = configParam.get(key);
				if(containerSearch.isEmpty()) {
					configParam = configParam.put(key, List.of(configValue));
				} else {
					var container = containerSearch.get();

					if(Seq.class.isAssignableFrom(container.getClass())) {
						configParam = configParam.put(key,Seq.class.cast(container).append(configValue));
					} else if(Set.class.isAssignableFrom(container.getClass())) {
						configParam = configParam.put(key,Set.class.cast(container).add(configValue));
					} else if(Collection.class.isAssignableFrom(container.getClass())) {
						Collection.class.cast(container).add(configValue);
					} else {
						throw new InvalidMultivaluedConfigValueException(key,container.getClass());
					}
				}

				return this;
			});
		}

		/**
		 * Adds all the values of the value sequence (which can only be of type vavr(Seq,Set) or Collection) to the
		 * object stored under the key in the configuration.
		 *
		 * If no value exists yet under that key, the value sequence is stored directly.
		 * If a value exists under that key, that value class must be one of vavr(Seq,Set) or Collection.
		 *
		 * May return the following errors:
		 *  - IllegalArgumentException if either the existing value under the key or the value sequence are not one
		 *    of the accepted collection types ( vavr(Set,Seq) or Collection).
		 *
		 * @param key
		 * @param valueSequence
		 * @return
		 */
		public Try<Builder> addAllTo(ConfigKeyEnum key,Object valueSequence) {
			return Try.of(()->{
				if(!isObjectOneOf(valueSequence,Set.class,Seq.class,Collection.class)) {
					throw new IllegalArgumentException("The value sequence can only be one of vavr(Set, Seq) or Collection");
				}

				var containerSearch = configParam.get(key);
				if(containerSearch.isEmpty()) {
					configParam = configParam.put(key, valueSequence);
				} else {
					var container = containerSearch.get();
					if(!getCollectionTypeFor(container).equals(getCollectionTypeFor(valueSequence))) {
						throw new IllegalArgumentException("The type of the value sequence must be the same as the container stored under the key");
					}

					if(container instanceof Seq) {
						configParam = configParam.put(key,((Seq)container).appendAll((Seq)valueSequence));
					} else if(container instanceof Set) {
						configParam = configParam.put(key,((Set)container).addAll((Set)valueSequence));
					} else if(container instanceof Collection) {
						((Collection) container).addAll((Collection)valueSequence);
					}
				}

				return this;
			});

		}


		public Option<Object> getConfig(ConfigKeyEnum key) {
			return configParam.get(key);
		}

		/**
		 * Gets the value stored under the config key if any.
		 *
		 * May return the following errors:
		 *  - {@link InvalidConfigurationObjectCast} if the key exists but is not castable to the given class
		 * @param key
		 * @param valueClass
		 * @param <T>
		 * @return
		 */
		public <T> Try<Option<T>> getConfig(ConfigKeyEnum key,Class<T> valueClass) {
			return Try.of(()->configParam.get(key).map(valueClass::cast))
			  .mapFailure(
				Case($(instanceOf(ClassCastException.class)),(e)->new InvalidConfigurationObjectCast(key,valueClass))
			  );
		}

		public Configuration build() {
			return new Configuration(configParam);
		}

		/**
		 * Returns whether an object is an instance of one of the given classes
		 * @param instance
		 * @param testedClasses
		 * @return
		 */
		private static boolean isObjectOneOf(Object instance,Class<?> ...testedClasses) {
			return List.of(testedClasses).exists((testedClass)-> testedClass.isAssignableFrom(instance.getClass()));
		}

		/**
		 * Given an object of hopefully container type class, returns which valid container
		 * class it contains.
		 *
		 * May return the following error:
		 *  - {@link IllegalArgumentException} if the instance's class is not one of the expected types
		 * @param instance
		 * @return
		 */
		private static Try<Class<?>> getCollectionTypeFor(Object instance) {
			return Try.of(()->
				List.of(Seq.class,Set.class,Collection.class)
				  .find((clazz)-> clazz.isAssignableFrom(instance.getClass()))
				  .getOrElseThrow(()->new IllegalArgumentException("The value sequence can only be one of vavr(Set, Seq) or Collection")));
		}
	}
}
