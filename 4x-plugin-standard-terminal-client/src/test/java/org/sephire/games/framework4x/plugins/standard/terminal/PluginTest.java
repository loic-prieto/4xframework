package org.sephire.games.framework4x.plugins.standard.terminal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sephire.games.framework4x.clients.terminal.config.TerrainsMapping;
import org.sephire.games.framework4x.core.Game;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.sephire.games.framework4x.clients.terminal.config.ConsoleClientConfigKeyEnum.TERRAIN_CHARACTER_MAPPING;
import static org.sephire.games.framework4x.plugins.standard.StandardTerrainTypes.FOREST;

public class PluginTest {

	private static final String STANDARD_PLUGIN_NAME = "org.sephire.games.framework4x.plugins.standard";
	private static final String STANDARD_TERMINAL_EXTENSION_PLUGIN_NAME = "org.sephire.games.framework4x.plugins.standard.terminal";

	@Test
	@DisplayName("Should load terrain mappings for the terminal client when loading the plugin")
	public void should_load_terrain_mappings_when_loading_plugin() {

		var gameTry = new Game.Builder().withPlugins(STANDARD_PLUGIN_NAME, STANDARD_TERMINAL_EXTENSION_PLUGIN_NAME).build();

		assertTrue(gameTry.isSuccess());
		var mappings = gameTry.get().getConfiguration().getConfiguration(TERRAIN_CHARACTER_MAPPING);

		assertTrue(mappings.isDefined());
		assertNotNull(((TerrainsMapping) mappings.get())
		  .mappings()
		  .get(FOREST.getId()));
	}
}
