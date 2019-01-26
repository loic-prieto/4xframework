/**
 * 4X Framework - Core library - The core library on which to base the game
 * Copyright © 2018 Loïc Prieto Dehennault (loic.sephiroth@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.sephire.games.framework4x.core.plugins.map;

import io.vavr.Function1;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Set;
import io.vavr.control.Try;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.model.map.GameMap;
import org.sephire.games.framework4x.core.plugins.InvalidMethodInvocationException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.Predicates.instanceOf;
import static java.lang.String.format;
import static org.reflections.ReflectionUtils.getAllMethods;
import static org.reflections.ReflectionUtils.withAnnotation;

/**
 * Plugin authors may include map providers in their plugins. As they are free-form built from the annotations
 * @MapProvider and @MapGenerator, the core framework needs an easy way to access those generators at runtime.
 * This is where the map provider wrapper comes in.
 *
 * From a class marked as a MapProvider, it will fetch the map generators, which can be used by the core framework
 * to build maps for games.
 */
public class MapProviderWrapper {
	private Object mapProviderInstance;
	private List<Method> mapGenerators;

	private MapProviderWrapper(Object mapProviderInstance, List<Method> mapGenerators) {
		this.mapProviderInstance = mapProviderInstance;
		this.mapGenerators = mapGenerators;
	}

	/**
	 * Builder for the instance. Takes a @MapGenerator annotated class and transforms it
	 * into a MapProviderWrapper instance.
	 * Checks that the class conforms to the expected contract.
	 *
	 * @param mapProviderClass
	 * @return
	 */
	public static Try<MapProviderWrapper> from(Class<?> mapProviderClass) {
		return Try.of(() -> {
			Object instance = null;
			try {
				instance = mapProviderClass.getConstructor().newInstance();
			} catch (Throwable t) {
				throw new InvalidMapProviderException(mapProviderClass,
				  format("Error instantiating the class: %s", t.getMessage()));
			}

			var mapGenerators = fetchMapGenerators(mapProviderClass);
			if (mapGenerators.isFailure()) {
				throw mapGenerators.getCause();
			}

			return new MapProviderWrapper(instance, mapGenerators.get());
		});
	}

	/**
	 * From this map provider class, get the list of map generators wrapped into MapGeneratorWrapper classes.
	 * The list may be empty.
	 *
	 * I don't know what errors may be returned in the Try as of yet.
	 * @return
	 */
	public Try<Set<MapGeneratorWrapper>> getMapGenerators() {
		return Try.of(()->{
			return mapGenerators.map((method) -> {
				Function1<Configuration, Try<GameMap>> generatorFunction = (configuration) -> Try.of(() -> {
					Try<GameMap> result = (Try<GameMap>) method.invoke(mapProviderInstance,configuration);
					if(result.isFailure()) {
						throw result.getCause();
					}
					return result.get();
				}).mapFailure(
				  // Map standard method calling failures
				  Case($(instanceOf(IllegalAccessException.class)), (e) -> new InvalidMethodInvocationException(method,e)),
				  Case($(instanceOf(InvocationTargetException.class)), (e) -> new InvalidMethodInvocationException(method,e)),
				  Case($(instanceOf(IllegalArgumentException.class)), (e) -> new InvalidMethodInvocationException(method,e))
				);

				var name = method.getAnnotation(MapGenerator.class).name();
				var displayKey = method.getAnnotation(MapGenerator.class).displayKey();

				return new DynamicMapGeneratorWrapper(name,displayKey,generatorFunction);
			}).collect(HashSet.collector());
		});
	}

	private static Try<List<Method>> fetchMapGenerators(Class<?> mapProviderClass) {
		return Try.of(() -> {
			var methods = getAllMethods(mapProviderClass, withAnnotation(MapGenerator.class));

			if (methods.isEmpty()) {
				throw new InvalidMapProviderException(mapProviderClass, "There should be at least one map generator in the class");
			}

			var erroneousMethodsExist = methods.stream()
			  .anyMatch((method) ->
				method.getParameterCount() != 1
				  || !method.getParameters()[0].getType().equals(Configuration.class)
				  || !Try.class.isAssignableFrom(method.getReturnType()));

			if (erroneousMethodsExist) {
				throw new InvalidMapProviderException(mapProviderClass, "The signature of the map generators must be: Configuration -> Try<GameMap>");
			}

			return List.ofAll(methods);
		});
	}
}
