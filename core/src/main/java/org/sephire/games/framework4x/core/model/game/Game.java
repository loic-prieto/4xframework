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

import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.model.map.GameMap;
import org.sephire.games.framework4x.core.plugins.PluginManager;
import org.sephire.games.framework4x.core.plugins.map.MapGeneratorWrapper;

import static java.lang.String.format;

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
	private PluginManager pluginManager;
	private GameState gameState;

	private Game(GameMap map,PluginManager pluginManager) {
		this.configuration = pluginManager.getLoadedConfiguration().get();
		this.pluginManager = pluginManager;
		this.map = map;
		this.gameState = new GameState();
	}

	public void putState(GameStateEnumKey key,Object value) {
		gameState.put(key,value);
	}

	public <T> Try<Option<T>> getState(GameStateEnumKey key,Class<T> valueType) {
		return gameState.get(key,valueType);
	}

	/**
	 * Execute the plugins code related to the game start hook.
	 * @return
	 */
	private Try<Void> executeGameStartHooks() {
		return pluginManager.callGameStartHooks(this);
	}

	public static BuilderMapGenerator builder() {
		return new Builder();
	}

	public static class Builder implements BuilderBuilder,BuilderMapGenerator,BuilderPluginManager{
		private MapGeneratorWrapper mapGenerator;
		private PluginManager pluginManager;

		@Override
		public BuilderPluginManager withMapGenerator(MapGeneratorWrapper mapGenerator) {
			this.mapGenerator = mapGenerator;
			return this;
		}

		@Override
		public BuilderBuilder withPluginManager(PluginManager pluginManager) {
			this.pluginManager = pluginManager;
			return this;
		}

		@Override
		public Try<Game> build() {
			return Try.of(()->{
				if(mapGenerator == null) {
					throw new IllegalArgumentException("The map generator cannot be null");
				}
				if (pluginManager == null) {
					throw new IllegalArgumentException("The plugin manager cannot be null");
				}
				if(pluginManager.getLoadedConfiguration().isEmpty()) {
					throw new IllegalArgumentException("The configuration must have been loaded");
				}

				var configuration = pluginManager.getLoadedConfiguration().get();
				Try<GameMap> mapTry = mapGenerator.buildMap(configuration);
				if(mapTry.isFailure()) {
					log.error(format("Error while calling generating map: %s",mapTry.getCause().getMessage()));
					throw mapTry.getCause();
				}

				var game = new Game(mapTry.get(),pluginManager);
				var startHooks = game.executeGameStartHooks();
				if(startHooks.isFailure()){
					log.error(format("Error while calling game start hooks: %s",startHooks.getCause().getMessage()));
					throw startHooks.getCause();
				}

				return game;
			});
		}
	}

	public interface BuilderMapGenerator { BuilderPluginManager withMapGenerator(MapGeneratorWrapper mapGenerator);}
	public interface BuilderPluginManager { BuilderBuilder withPluginManager(PluginManager pluginManager);}
	public interface BuilderBuilder { Try<Game> build();}
}
