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

import lombok.Getter;
import org.sephire.games.framework4x.clients.terminal.FourXFrameworkClientException;

/**
 * When loading a map zone and when its state or behaviour is invalid, this
 * exception is thrown. For example, when not all cells have been filled.
 */
public class InvalidMapZoneException extends FourXFrameworkClientException {
	@Getter
	private String zoneName;

	public InvalidMapZoneException(String zoneName,String reason) {
		super(reason);
		this.zoneName = zoneName;
	}
}
