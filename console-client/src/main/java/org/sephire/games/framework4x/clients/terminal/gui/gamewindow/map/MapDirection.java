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

import com.googlecode.lanterna.input.KeyType;
import io.vavr.control.Option;

import static io.vavr.API.*;

public enum MapDirection {
	UP,
	DOWN,
	LEFT,
	RIGHT;

	/**
	 * Given a keytype from lanterna, returns a Map Direction.
	 * This assumes keys are not re-mappable.
	 * @param keyType
	 * @return
	 */
	public static Option<MapDirection> fromKeyType(KeyType keyType) {
		return Option.of(
		  Match(keyType).of(
			Case($(KeyType.ArrowUp),()->MapDirection.UP),
			Case($(KeyType.ArrowDown),()->MapDirection.DOWN),
			Case($(KeyType.ArrowLeft),()->MapDirection.LEFT),
			Case($(KeyType.ArrowRight),()->MapDirection.RIGHT),
			Case($(),()->null)
		  )
		);
	}
}
