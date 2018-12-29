package org.sephire.games.framework4x.testing.testPlugin2;

import io.vavr.control.Try;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.model.map.GameMap;
import org.sephire.games.framework4x.core.plugins.map.MapGenerator;
import org.sephire.games.framework4x.core.plugins.map.MapProvider;

@MapProvider
public class Plugin2Maps {

	@MapGenerator(name = "plugin2.map_generator_1",displayKey = "plugin2.map_generators.1.displayName")
	public Try<GameMap> buildRandomMap1(Configuration configuration) {
		return Try.success(null);
	}

	@MapGenerator(name = "plugin2.map_generator_2",displayKey = "plugin2.map_generators.2.displayName")
	public Try<GameMap> buildRandomMap2(Configuration configuration) {
		return Try.success(null);
	}
}

