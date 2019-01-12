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

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.sephire.games.framework4x.core.model.map.Location;
import org.sephire.games.framework4x.core.model.map.Size;

/**
 * Represents the camera viewport of the map panel. That is, what zone of the map
 * is visible, so as not to render not visible cells.
 */
@AllArgsConstructor
public class MapViewport {

	@Getter
	private Location cameraOffset;
	@Getter
	private Size size;

	public MapViewport() {
		this.cameraOffset = Location.of(0,0);
		this.size = new Size(0,0);
	}
}
