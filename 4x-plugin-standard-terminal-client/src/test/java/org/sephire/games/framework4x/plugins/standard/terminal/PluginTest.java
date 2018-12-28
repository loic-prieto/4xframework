package org.sephire.games.framework4x.plugins.standard.terminal;

import org.sephire.games.framework4x.core.model.map.GameMap;
import org.sephire.games.framework4x.core.model.map.MapZone;
import org.sephire.games.framework4x.core.model.map.Size;
import org.sephire.games.framework4x.plugins.standard.StandardTerrainTypes;

public class PluginTest {

	private static final String STANDARD_PLUGIN_NAME = "org.sephire.games.framework4x.plugins.standard";
	private static final String STANDARD_TERMINAL_EXTENSION_PLUGIN_NAME = "org.sephire.games.framework4x.plugins.standard.terminal";

	/*@Test
	@DisplayName("Should load terrain mappings for the terminal client when loading the plugin")
	public void should_load_terrain_mappings_when_loading_plugin() {

		var gameTry = new Game.Builder()
		  .withPlugins(STANDARD_PLUGIN_NAME, STANDARD_TERMINAL_EXTENSION_PLUGIN_NAME)
		  .withMap(buildMap())
		  .build();

		assertTrue(gameTry.isSuccess());
		var mappings = gameTry.get().getConfiguration().getConfiguration(TERRAIN_CHARACTER_MAPPING);

		assertTrue(mappings.isDefined());
		assertNotNull(((TerrainsMapping) mappings.get())
		  .getMappings()
		  .get(FOREST.getId()));
	}*/

	private static GameMap buildMap(){
		return GameMap.builder()
		  .addZone(MapZone.builder()
		  	.withName("test")
		  	.withDefaultCells(new Size(10,10), StandardTerrainTypes.HILL)
		  	.build().get())
		  .withDefaultZone("test")
		  .build().get();
	}
}
