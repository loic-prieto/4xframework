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
import com.googlecode.lanterna.input.KeyType;
import io.vavr.control.Option;

import static io.vavr.API.*;

public enum MapDirection {
	NORTH,
	SOUTH,
	WEST,
	EAST,
	UP,
	DOWN;

	/**
	 * Given a keytype from lanterna, returns a Map Direction.
	 * This assumes keys are not re-mappable.
	 * @param keyStroke
	 * @return
	 */
	public static Option<MapDirection> fromKeyStroke(KeyStroke keyStroke) {
		if(keyStroke.getKeyType().equals(KeyType.Character)) {
		 	return Option.of(
			  Match(keyStroke.getCharacter()).of(
				Case($('<'),()->MapDirection.UP),
				Case($('>'),()->MapDirection.DOWN),
				Case($(),()->null)
			  )
			);
		} else {
			return Option.of(
			  Match(keyStroke.getKeyType()).of(
				Case($(KeyType.ArrowUp),()->MapDirection.NORTH),
				Case($(KeyType.ArrowDown),()->MapDirection.SOUTH),
				Case($(KeyType.ArrowLeft),()->MapDirection.WEST),
				Case($(KeyType.ArrowRight),()->MapDirection.EAST),
				Case($(),()->null)
			  )
			);
		}

	}
}
