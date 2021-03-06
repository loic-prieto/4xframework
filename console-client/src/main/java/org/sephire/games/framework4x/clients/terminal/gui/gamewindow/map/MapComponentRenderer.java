/**
 * 4X Framework - Console client - A terminal-based client for the 4X framework
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
package org.sephire.games.framework4x.clients.terminal.gui.gamewindow.map;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.ComponentRenderer;
import com.googlecode.lanterna.gui2.TextGUIGraphics;
import org.sephire.games.framework4x.clients.terminal.api.config.TerrainMappingColor;
import org.sephire.games.framework4x.core.model.map.Location;

import static io.vavr.collection.List.range;
import static org.sephire.games.framework4x.clients.terminal.utils.Terminal.Position.terminalPositionFrom;

/**
 * Is in charge of rendering the visible part of a game map.
 */
public class MapComponentRenderer implements ComponentRenderer<MapComponent> {

	private TerminalSize viewportSize;

	private static final String CURSOR_CHAR = "\u02DF";

	public MapComponentRenderer() {
	}

	@Override
	public TerminalSize getPreferredSize(MapComponent component) {
		var viewport = component.getViewport();
		return new TerminalSize(viewport.getSize().getWidth(),viewport.getSize().getHeight());
	}

	@Override
	public void drawComponent(TextGUIGraphics graphics, MapComponent component) {
		if(this.viewportSize == null){
			this.viewportSize = component.getSize();
		}

		var viewport = component.getViewport();
		var mappings = component.getMappings();
		var currentZone = component.getCurrentZone();

		// Draw cells
		range(0,viewportSize.getColumns()).forEach((x)->{
			range(0,viewportSize.getRows()).forEach((y)->{
				var cellLocation = Location.of(x,y).add(viewport.getCameraOffset());

				// Fetch the highest precedence cell at this viewport location
				var cell = currentZone.getCellAt(cellLocation);

				// Draw terrain
				//Code here to get the terrain type of the current cell
				var terrainType = (Void)null; // fake code

				var cellMapping = mappings.getMappings().get(terrainType);
				String cellChar = cellMapping != null ? cellMapping.getCharacter() : " ";
				TextColor cellColor = cellMapping != null ? fromCellMapping(cellMapping.getColor()) : TextColor.ANSI.BLACK;
				graphics.setForegroundColor(cellColor);
				graphics.putString(x,y,cellChar);
			});
		});

		// Draw cursor
		graphics.setForegroundColor(TextColor.ANSI.RED);
		graphics.putString(terminalPositionFrom(component.getCursorPosition()), CURSOR_CHAR, SGR.BLINK,SGR.BOLD);
	}

	private static TextColor fromCellMapping(TerrainMappingColor mappingColor) {
		return new TextColor.RGB(mappingColor.getRed(),mappingColor.getGreen(),mappingColor.getBlue());
	}
}
