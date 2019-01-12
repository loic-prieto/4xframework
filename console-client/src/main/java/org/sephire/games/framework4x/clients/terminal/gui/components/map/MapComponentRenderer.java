/**
 * 4X Framework - Console client - A terminal-based client for the 4X framework
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
package org.sephire.games.framework4x.clients.terminal.gui.components.map;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.ComponentRenderer;
import com.googlecode.lanterna.gui2.TextGUIGraphics;
import org.sephire.games.framework4x.clients.terminal.api.config.TerrainMappingColor;
import org.sephire.games.framework4x.clients.terminal.api.config.TerrainsMapping;
import org.sephire.games.framework4x.core.model.map.GameMap;
import org.sephire.games.framework4x.core.model.map.Location;
import org.sephire.games.framework4x.core.model.map.MapCell;

import static io.vavr.collection.List.range;
import static org.sephire.games.framework4x.clients.terminal.gui.components.map.FakeTerrainType.FAKE;

/**
 * Is in charge of rendering the visible part of a game map.
 */
public class MapComponentRenderer implements ComponentRenderer<MapComponent> {

	private MapViewport viewport;
	private GameMap map;
	private TerrainsMapping mappings;
	private TerminalSize viewportSize;

	public MapComponentRenderer(MapViewport viewport, GameMap map, TerrainsMapping mappings) {
		this.viewport = viewport;
		this.map = map;
		this.mappings = mappings;
	}

	@Override
	public TerminalSize getPreferredSize(MapComponent component) {
		return new TerminalSize(viewport.getSize().getWidth(),viewport.getSize().getHeight());
	}

	@Override
	public void drawComponent(TextGUIGraphics graphics, MapComponent component) {
		if(this.viewportSize == null){
			this.viewportSize = graphics.getSize();
		}

		range(0,viewportSize.getColumns()).forEach((x)->{
			range(0,viewportSize.getRows()).forEach((y)->{
				var terrainType =  map.getCurrentZone().getCells().get(Location.of(x,y))
				  .getOrElse(new MapCell(Location.of(x,y), FAKE))
				  .getTerrainType()
				  .toString().toLowerCase();

				var cellMapping = mappings.getMappings().get(terrainType);
				String cellChar = cellMapping != null ? cellMapping.getCharacter() : " ";
				TextColor cellColor = cellMapping != null ? fromCellMapping(cellMapping.getColor()) : TextColor.ANSI.BLACK;
				graphics.setForegroundColor(cellColor);
				graphics.putString(x,y,cellChar);
			});
		});
	}

	private static TextColor fromCellMapping(TerrainMappingColor mappingColor) {
		return new TextColor.RGB(mappingColor.getRed(),mappingColor.getGreen(),mappingColor.getBlue());
	}
}
