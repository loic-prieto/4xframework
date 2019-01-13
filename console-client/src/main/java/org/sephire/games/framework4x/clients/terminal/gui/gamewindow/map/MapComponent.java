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
package org.sephire.games.framework4x.clients.terminal.gui.gamewindow.map;

import com.googlecode.lanterna.gui2.AbstractComponent;
import com.googlecode.lanterna.gui2.ComponentRenderer;
import io.vavr.control.Try;
import lombok.Getter;
import org.sephire.games.framework4x.clients.terminal.api.config.ConsoleClientConfigKeyEnum;
import org.sephire.games.framework4x.clients.terminal.api.config.TerrainsMapping;
import org.sephire.games.framework4x.clients.terminal.gui.Basic4XWindow;
import org.sephire.games.framework4x.clients.terminal.gui.gamewindow.CursorMoveEvent;
import org.sephire.games.framework4x.clients.terminal.gui.gamewindow.MapScrollEvent;
import org.sephire.games.framework4x.core.model.game.Game;
import org.sephire.games.framework4x.core.model.map.GameMap;
import org.sephire.games.framework4x.core.model.map.Location;

import static org.sephire.games.framework4x.clients.terminal.utils.Terminal.Position.applyDirection;

/**
 * A component that draws the current zone of the game map,
 * can be embedded in any container.
 */
public class MapComponent extends AbstractComponent<MapComponent> {

	@Getter
	private MapViewport viewport;
	@Getter
	private GameMap map;
	@Getter
	private TerrainsMapping mappings;
	@Getter
	private Location cursorPosition;
	private Basic4XWindow parentContainer;

	private MapComponent(GameMap map,TerrainsMapping mappings,Basic4XWindow parentContainer) {
		this.viewport = new MapViewport();
		this.map = map;
		this.mappings = mappings;
		this.cursorPosition = Location.of(0,0);
		this.parentContainer = parentContainer;

		setupEventHandling();
	}

	private void setupEventHandling(){
		parentContainer.registerEventListener(MapScrollEvent.class,(event)->{
			viewport.moveCamera(event.getDirection(),event.getDistance());
			invalidate();
		});
		parentContainer.registerEventListener(CursorMoveEvent.class,(event)->{
			cursorPosition = applyDirection(cursorPosition,event.getDirection(),event.getDistance());
			invalidate();
		});
	}

	public static Try<MapComponent> of(Game game, Basic4XWindow parent) {
		return Try.of(()->{
			var configuration = game.getConfiguration();
			var mappings = configuration.getConfiguration(ConsoleClientConfigKeyEnum.TERRAIN_CHARACTER_MAPPING,TerrainsMapping.class);
			if(mappings.isFailure() || mappings.get().isEmpty()){
				throw new NoTerrainMappingFoundException();
			}

			return new MapComponent(game.getMap(),mappings.get().get(),parent);
		});

	}

	@Override
	protected ComponentRenderer<MapComponent> createDefaultRenderer() {
		return new MapComponentRenderer();
	}
}
