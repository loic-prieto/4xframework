package org.sephire.games.framework4x.core.model.game;

import io.vavr.collection.HashSet;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

		var pluginManagerTry = PluginManager.fromFolder(pluginTempFolder);
		assertTrue(pluginManagerTry.isSuccess());
		var pluginManager = pluginManagerTry.get();
		var pluginLoadingTry = pluginManager.loadPlugins(HashSet.of(PLUGIN1_NAME));
		assertTrue(pluginLoadingTry.isSuccess());

		var gameTry = Game.builder()
		  .withMapGenerator(new DynamicMapGeneratorWrapper(
			"testMapGenerator",
			"testMapGenerator",
			(configuration)-> Try.success(null)))
		  .withPluginManager(pluginManager)
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
