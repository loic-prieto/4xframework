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
package org.sephire.games.framework4x.core.model.game;

import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.model.events.DomainEvent;
import org.sephire.games.framework4x.core.model.events.DomainEvents;
import org.sephire.games.framework4x.core.model.map.GameMap;
import org.sephire.games.framework4x.core.plugins.PluginManager;
import org.sephire.games.framework4x.core.plugins.map.MapGeneratorWrapper;

import static java.lang.String.format;
import static org.sephire.games.framework4x.core.model.game.CoreGameStateKeys.CURRENT_TURN;
import static org.sephire.games.framework4x.core.utils.Validations.areArgumentsNotNull;

/**
 * This is the main framework 4x component, it hold the data needed to run a game. Mainly the loaded configuration
 * and the game map as of now.
 */
@Slf4j
public class Game {

	@Getter
	private GameMap map;
	@Getter
	private Configuration configuration;
	private GameState gameState;

	private Game(GameMap map, Configuration configuration) {
		this.configuration = configuration;
		this.map = map;
		this.gameState = new GameState();
	}

	private Try<Void> initializeCoreState() {
		return Try.of(()->{
			gameState.put(CURRENT_TURN,0);

			return null;
		});
	}

	public void putState(GameStateEnumKey key,Object value) {
		gameState.put(key,value);
	}

	public <T> Try<Option<T>> getState(GameStateEnumKey key,Class<T> valueType) {
		return gameState.get(key,valueType);
	}

	public static BuilderMapGenerator builder() {
		return new Builder();
	}

	/**
	 * Initialize a starting game state. This method must be invoked before the game
	 * can initialize so that all required systems of the framework have been notified.
	 * @return
	 */
	public Try<Void> initialize() {
		return initializeCoreState()
		  .andThenTry(()->{
			  DomainEvents.getInstance().fireEvent(new GameStartedEvent(this));
		  });
	}

	public interface BuilderMapGenerator {
		BuilderConfiguration withMapGenerator(MapGeneratorWrapper mapGenerator);
	}

	public interface BuilderConfiguration {
		BuilderBuilder withConfiguration(Configuration configuration);
	}

	public static class Builder implements BuilderBuilder, BuilderMapGenerator,  BuilderConfiguration {
		private MapGeneratorWrapper mapGenerator;
		private Configuration configuration;

		@Override
		public BuilderConfiguration withMapGenerator(MapGeneratorWrapper mapGenerator) {
			this.mapGenerator = mapGenerator;
			return this;
		}

		@Override
		public BuilderBuilder withConfiguration(Configuration configuration) {
			this.configuration = configuration;
			return this;
		}

		@Override
		public Try<Game> build() {
			return Try.of(()->{
				areArgumentsNotNull(mapGenerator, configuration).getOrElseThrow(t -> t);

				Try<GameMap> mapTry = mapGenerator.buildMap(configuration);
				if(mapTry.isFailure()) {
					log.error(format("Error while calling generating map: %s",mapTry.getCause().getMessage()));
					throw mapTry.getCause();
				}

				var game = new Game(mapTry.get(), configuration);


				return game;
			});
		}
	}
	public interface BuilderBuilder { Try<Game> build();}
}
