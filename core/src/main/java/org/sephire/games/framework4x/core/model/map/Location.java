/**
 * 4X Framework - Core library - The core library on which to base the game
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
package org.sephire.games.framework4x.core.model.map;

import lombok.Value;

/**
 * The location of an item in a 2D space.
 * Along with the (x,y) coordinates, this class provides some basic arithmetic to ease usage.
 */
@Value
public class Location {
	private int x;
	private int y;

	/**
	 * Very simple convenience fluent factory method for locations.
	 *
	 * @param x
	 * @param y
	 * @return
	 */
	public static Location of(int x, int y) {
		return new Location(x, y);
	}

	/**
	 * Returns a new location which is the result of substracting the given
	 * location coordinates to the current location coordinates.
	 * Does not modify the current location.
	 *
	 * @param location
	 * @return
	 */
	public Location substract(Location location) {
		return new Location(this.x - location.x, this.y - location.y);
	}

	public Location substract(int x, int y) {
		return this.substract(new Location(x, y));
	}

	/**
	 * Returns a new location which is the result of adding the given
	 * location coordinates to the current location coordinates.
	 * Does not modify the current location.
	 *
	 * @param location
	 * @return
	 */
	public Location add(Location location) {
		return new Location(this.x + location.x, this.y + location.y);
	}

	public Location add(int x, int y,int z) {
		return this.add(new Location(x, y));
	}
}
