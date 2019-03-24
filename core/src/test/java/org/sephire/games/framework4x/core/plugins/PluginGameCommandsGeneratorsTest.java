package org.sephire.games.framework4x.core.plugins;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.model.config.CoreConfigKeyEnum;
import org.sephire.games.framework4x.core.model.game.GameCommands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PluginGameCommandsGeneratorsTest {

	private static final String GAME_COMMAND_GENERATOR_PLUGIN = "org.sephire.games.framework4x.testing.testPlugin1";

	@Test
	@DisplayName("Should load successfully game commands when loading a plugin")
	public void should_load_game_commands_when_loading_plugin(){
		var pluginSpec = PluginSpec.builder().withPluginName(GAME_COMMAND_GENERATOR_PLUGIN)
		  .withRootPackage(GAME_COMMAND_GENERATOR_PLUGIN)
		  .build();
		assertTrue(pluginSpec.isSuccess());

		var configurationBuilder = Configuration.builder();

		var pluginLoadingTry = Plugin.from(pluginSpec.get(),configurationBuilder);
		var configuration = configurationBuilder.build();

		assertTrue(pluginLoadingTry.isSuccess());

		var gameCommandsSearch = configuration.getConfiguration(CoreConfigKeyEnum.GAME_COMMANDS, GameCommands.class);
		assertTrue(gameCommandsSearch.isSuccess());
		assertTrue(gameCommandsSearch.get().isDefined());

		var gameCommands = gameCommandsSearch.get().get();
		var expectedRootCategoriesNumber = 3;

		assertEquals(expectedRootCategoriesNumber, gameCommands.getRootCategories().size());

		var gameCategorySearch = gameCommands.getRootCategories().find((category)->category.getIdentifier().equals("game"));
		assertTrue(gameCategorySearch.isDefined());
		var gameSaveCommandSearch = gameCategorySearch.get().getCommands().find((command)->command.getIdentifier().equals("game.save"));
		assertTrue(gameSaveCommandSearch.isDefined());

	}
}
