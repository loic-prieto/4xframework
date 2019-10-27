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

import com.googlecode.lanterna.input.KeyStroke;
import io.vavr.Tuple;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.sephire.games.framework4x.clients.terminal.gui.gamewindow.CursorMoveEvent;

/**
 * <p>This event is fired when the game window detects the arrow keys pressing,and will understand that as
 * a scroll event for the map.</p>
 * <p>The ctrl key must be pressed for a map scroll. Not pressing the ctrl key will produce, instead, a cursor
 * movement inside the map. See {@link CursorMoveEvent}</p>
 * <p>When using shift + arrow, the map will scroll 10 times more</p>
 */
@AllArgsConstructor
@Getter
public class MapScrollEvent {
	private MapDirection direction;
	private int distance;

	private static final int DEFAULT_DISTANCE = 1;
	private static final int LONG_DISTANCE = 10;

	/**
	 * Calculates a map scroll event from a pressed key stroke.
	 * @param keyStroke
	 * @return
	 */
	public static Option<MapScrollEvent> fromKeyStroke(KeyStroke keyStroke) {
		return getDirectionFromKey(keyStroke)
		  .map((direction)-> Tuple.of(direction,getDistanceFromKey(keyStroke)))
		  .map((t)->new MapScrollEvent(t._1,t._2));
	}

	private static int getDistanceFromKey(KeyStroke keyStroke) {
		return keyStroke.isShiftDown() ? LONG_DISTANCE : DEFAULT_DISTANCE;
	}

	private static Option<MapDirection> getDirectionFromKey(KeyStroke keyStroke) {
		return keyStroke.isAltDown()? MapDirection.fromKeyStroke(keyStroke) : Option.none();
	}
}
