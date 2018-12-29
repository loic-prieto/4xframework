package org.sephire.games.framework4x.core.plugins.map;

import io.vavr.Function1;
import io.vavr.control.Try;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.model.map.GameMap;

/**
 * <p>A dynamic map generator wrapper wraps a function that is used to generate a map dynamically at game creation time.</p>
 *
 * <p>This function comes from a method annotated with &#64;MapGenerator inside a class annotated with &#64;MapProvider</p>
 */
public class DynamicMapGeneratorWrapper extends MapGeneratorWrapper {

	private Function1<Configuration, Try<GameMap>> generator;

	public DynamicMapGeneratorWrapper(String name, String displayKey, Function1<Configuration, Try<GameMap>> generator) {
		super(name, displayKey);
		this.generator = generator;
	}

	@Override
	public Try<GameMap> buildMap(Configuration configuration) {
		return generator.apply(configuration);
	}
}
