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

import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import lombok.Getter;

/**
 * <p>A map map is divided into zones. They are meant to represent maps of the same map universe,
 * meaning a zone can represent another planet, a pocket plane, an underground level,
 * a space orbit around a planet, a galactic map where the scope is now planets instead of cities.</p>
 * <p>Zones have a name, and a set of map cells which holds the map tiles with their 2D position.</p>
 * <p>A given location may have more than one cell representing terrain or features. The graphical client must
 * decide how to show those cells.</p>
 */
public class Zone {
	@Getter
	private String name;
	@Getter
	private Size size;
	private Map<Location,List<Cell>> cells;

	private Zone(String name, Map<Location, List<Cell>> cells) {
		this.name = name;
		this.cells = cells;
		buildSize();
	}

	/**
	 * Get the highest precedence cell at the given location.
	 * @param location
	 * @return
	 */
	public Option<Cell> getCellAt(Location location) {
		return this.cells
		  .get(location)
		  .map(cells->cells.get(0));
	}

	/**
	 * Get the list of cells at the given location, ordered by precedence from highest to lowest.
	 * @param location
	 * @return
	 */
	public Option<List<Cell>> getCellsAt(Location location) {
		return this.cells.get(location);
	}

	/**
	 * Creates a new zone with the new cell added to the list of cells,
	 * @param cell
	 * @return
	 */
	public Zone withCell(Cell cell) {
		var locationList = this.cells.getOrElse(cell.getLocation(),List.empty());
		var newCells = this.cells.put(cell.getLocation(),locationList.append(cell).sorted());

		return new Zone(name,newCells);
	}

	/**
	 * Updates the size of the zone based on the current map cells.
	 *
	 * @return
	 */
	private void buildSize() {
		int maxXPosition = cells.keySet()
		  .map(Location::getX)
		  .reduce((previousX, currentX) -> currentX < previousX ? previousX : currentX);
		int maxYPosition = cells.keySet()
		  .map(Location::getY)
		  .reduce((previousY, currentY) -> currentY < previousY ? previousY : currentY);

		this.size = new Size(maxXPosition, maxYPosition);
	}
}
