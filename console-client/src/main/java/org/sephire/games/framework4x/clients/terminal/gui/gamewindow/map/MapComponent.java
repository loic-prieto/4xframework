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

import com.googlecode.lanterna.gui2.AbstractComponent;
import com.googlecode.lanterna.gui2.ComponentRenderer;
import com.googlecode.lanterna.gui2.TextGUIGraphics;
import io.vavr.control.Option;
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

import static org.sephire.games.framework4x.clients.terminal.utils.Terminal.Dimensions.fromTerminalSize;
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
			var newPosition = applyDirection(cursorPosition,event.getDirection(),event.getDistance());
			var xn = newPosition.getX(); var yn = newPosition.getY();

			if(xn < 0 || xn > viewport.getSize().getWidth()-1) {
				var xDelta = xn < 0 ? xn : (xn - (viewport.getSize().getWidth() - 1));
				parentContainer.fireEvent(mapScrollEventFromDeltas(xDelta,0).get());
				return;
			}

			if(yn < 0 || yn > viewport.getSize().getHeight()-1) {
				var yDelta = yn < 0 ? yn : (yn - (viewport.getSize().getHeight() - 1));
				parentContainer.fireEvent(mapScrollEventFromDeltas(0,yDelta).get());
				return;
			}

			cursorPosition = newPosition;
			invalidate();
		});
	}

	@Override
	protected void onAfterDrawing(TextGUIGraphics graphics) {
		super.onAfterDrawing(graphics);
		this.viewport.setSize(fromTerminalSize(getSize()));
	}

	private static Option<MapScrollEvent> mapScrollEventFromDeltas(int x, int y) {
		Option<MapScrollEvent> result = Option.none();

		if(x != 0) {
			var mapDirection = x > 0 ? MapDirection.RIGHT : MapDirection.LEFT;
			var distance = Math.abs(x);
			result = Option.of(new MapScrollEvent(mapDirection,distance));
		} else if(y != 0) {
			var mapDirection = y > 0 ? MapDirection.DOWN : MapDirection.UP;
			var distance = Math.abs(y);
			result = Option.of(new MapScrollEvent(mapDirection,distance));
		}

		return result;
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
