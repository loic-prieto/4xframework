package org.sephire.games.framework4x.core.model.game;

import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.Getter;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.model.map.GameMap;
import org.sephire.games.framework4x.core.plugins.Plugin;
import org.sephire.games.framework4x.core.plugins.PluginLoadingException;
import org.sephire.games.framework4x.core.plugins.map.MapGeneratorWrapper;

/**
 * This is the main framework 4x component, it hold the data needed to run a game. Mainly the loaded configuration
 * and the game map as of now.
 */
public class Game {

	@Getter
	private GameMap map;
	@Getter
	private Configuration configuration;

	private Game(GameMap map,Configuration configuration) {
		this.configuration = configuration;
		this.map = map;
	}

	public static BuilderConfiguration builder() {
		return new Builder();
	}

	public static class Builder implements BuilderConfiguration,BuilderBuilder,BuilderMapGenerator {
		private MapGeneratorWrapper mapGenerator;
		private Configuration configuration;

		@Override
		public BuilderBuilder withMapGenerator(MapGeneratorWrapper mapGenerator) {
			this.mapGenerator = mapGenerator;
			return this;
		}

		@Override
		public BuilderMapGenerator withConfiguration(Configuration configuration) {
			this.configuration = configuration;
			return this;
		}

		@Override
		public Try<Game> build() {
			return Try.of(()->{
				if(configuration == null) {
					throw new IllegalArgumentException("The configuration cannot be null");
				}
				if(mapGenerator == null) {
					throw new IllegalArgumentException("The map generator cannot be null");
				}

				Try<GameMap> mapTry = mapGenerator.buildMap(configuration);
				if(mapTry.isFailure()) {
					throw mapTry.getCause();
				}

				return new Game(mapTry.get(),configuration);
			});
		}
	}

	public interface BuilderConfiguration { BuilderMapGenerator withConfiguration(Configuration configuration);}
	public interface BuilderMapGenerator { BuilderBuilder withMapGenerator(MapGeneratorWrapper mapGenerator);}
	public interface BuilderBuilder { Try<Game> build();}
}
