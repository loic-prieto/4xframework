package org.sephire.games.framework4x.core.plugins.map;

import io.vavr.collection.List;
import io.vavr.control.Try;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.model.map.GameMap;

import java.lang.reflect.Method;

import static java.lang.String.format;
import static org.reflections.ReflectionUtils.getAllMethods;
import static org.reflections.ReflectionUtils.withAnnotation;

public class MapProviderWrapper {
	private Object mapProviderInstance;
	private List<Method> mapGenerators;

	private MapProviderWrapper(Object mapProviderInstance, List<Method> mapGenerators) {
		this.mapProviderInstance = mapProviderInstance;
	}

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
	 * Call all map generators of this map provider.
	 * That will update the configuration object with the available maps.
	 *
	 * @param configuration
	 * @return
	 */
	public Try<Void> callMapGenerators(Configuration.Builder configuration) {

		var mapCallsTry = mapGenerators.map(mapGenerator-> Try.of(()->mapGenerator.invoke(mapProviderInstance,configuration)));
		if(Try.sequence(mapCallsTry).isFailure()){
			return Try.failure(new InvalidMapProviderException(mapProviderInstance.getClass(),""))
		}


		return Try.sequence(mapGenerators.map((mapGenerator -> Try.of(() ->
		  mapGenerator.invoke(mapProviderInstance, configuration)))
		  )
		).map((seq) -> null);
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
				  || !method.getParameters()[0].getType().isAssignableFrom(Configuration.Builder.class)
				  || !method.getReturnType().isAssignableFrom(Try.class));

			if (erroneousMethodsExist) {
				throw new InvalidMapProviderException(mapProviderClass, "The signature of the map generators must be: Configuration.Builder -> Try<Void>");
			}

			return List.ofAll(methods);
		});
	}
}
