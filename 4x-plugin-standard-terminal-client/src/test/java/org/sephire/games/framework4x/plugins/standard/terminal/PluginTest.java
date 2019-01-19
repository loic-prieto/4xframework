/**
 * 4X Framework - Standard plugin terminal client adapter - A console client adapter/bridge for the 4X Framework Standard Plugin
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

		var gameTry = new GameTest.Builder()
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
