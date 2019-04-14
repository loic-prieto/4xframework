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

import io.vavr.collection.HashSet;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.plugins.PluginManager;
import org.sephire.games.framework4x.core.plugins.map.DynamicMapGeneratorWrapper;
import org.sephire.games.framework4x.testing.testPlugin1.TestPlugin1GameStateKeys;

import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.sephire.games.framework4x.core.plugins.PluginCreationUtils.buildPluginJar;

public class GameTest {

	private static final String PLUGIN1_NAME = "org.sephire.games.framework4x.testing.testPlugin1";

	@Test
	@DisplayName("When a game has been started, plugins can write state into the game")
	public void should_load_game_state_when_starting_game() throws IOException {
		var pluginTempFolder = Files.createTempDirectory("PluginManagerTest-");
		buildPluginJar(pluginTempFolder,PLUGIN1_NAME, Option.none());

		var configuration = Configuration.builder();

		var pluginManagerTry = PluginManager.fromFolder(pluginTempFolder);
		assertTrue(pluginManagerTry.isSuccess());
		var pluginManager = pluginManagerTry.get();
		var pluginLoadingTry = pluginManager.loadPlugins(HashSet.of(PLUGIN1_NAME), configuration);
		assertTrue(pluginLoadingTry.isSuccess());

		var gameTry = Game.builder()
		  .withMapGenerator(new DynamicMapGeneratorWrapper(
			"testMapGenerator",
			"testMapGenerator",
			(config) -> Try.success(null)))
		  .withPluginManager(pluginManager)
		  .withConfiguration(configuration.build())
		  .build();

		assertTrue(gameTry.isSuccess());
		var game = gameTry.get();

		var testPlugin1StateTry = game.getState(TestPlugin1GameStateKeys.KEY1,String.class);
		assertTrue(testPlugin1StateTry.isSuccess());
		assertTrue(testPlugin1StateTry.get().isDefined());

		var stateValue = testPlugin1StateTry.get().get();
		assertEquals(String.class,stateValue.getClass());
		assertEquals("test",stateValue);
	}

}
