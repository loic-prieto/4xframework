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
