/**
 * 4X Framework - Core library - The core library on which to base the game
 * Copyright © 2018 Loïc Prieto Dehennault (loic.sephiroth@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.sephire.games.framework4x.core.model.config;

import io.vavr.collection.*;
import io.vavr.control.Option;
import io.vavr.control.Try;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Locale;

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
	 * <p>The I18N data loaded from each plugin is stored in the configuration object under the
	 * CoreConfigKeyEnum.I18N enum value, with type signature Map&lt;Locale,Map&lt;String,String&gt;&gt;</p>
	 *<p>The usual key for a plugin i18n resource is: pluginRootPackage.key
	 * Where pluginRootPackage is the full root package of the plugin and the key is the normal properties key wich
	 * may include any valid character</p>
	 *<p>The I18N resource may be parameterized and this parametrization follows the java MessageFormat standard conventions:
	 *	<pre><code>
	 *	   // the resource bundle key "some.plugin.some.key" has the value "Your civilization is {0}"
	 *	   var civilizationName = "Sumeria";
	 *	   var userLanguage = Locale.ENGLISH;
	 *	   var civilizationLabel = configuration.getTranslationFor(userLanguage,"some.plugin.some.key",civilizationName);
	 *
	 *	   assert civilizationLabel.get().equals("Your civilization is Sumeria")
	 *	</code></pre>
	 * </p>
	 *
	 * @param key
	 * @param params
	 * @return
	 */
	public Option<String> getTranslationFor(Locale locale, String key, Object... params) {
		return configMap.get(CoreConfigKeyEnum.I18N)
		  .map(rawMap -> (Map<Locale, Map<String, String>>) rawMap)
		  .flatMap(i18nMap -> i18nMap.get(locale))
		  .flatMap(localeMap -> localeMap.get(key))
		  .map(value -> MessageFormat.format(value, params));
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
