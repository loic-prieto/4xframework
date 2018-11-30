package org.sephire.games.framework4x.core.model.map;

import io.vavr.collection.Map;
import lombok.Getter;

/**
 * A map map is divided into zones. They are meant to represent maps of the same map universe,
 * meaning a zone can represent another planet, a pocket plane, an underground level,
 * a space orbit around a planet, a galactic map where the scope is now planets instead of cities.
 *
 * Zones have a name, and a set of map cells which holds the map tiles with their 2D position.
 */
@Getter
public class MapZone {
	private String name;
	private Map<Location,MapCell> cells;
	private Size size;

	public MapZone(Map<Location, MapCell> cells) {
		this.cells = cells;
		buildSize();
	}

	/**
	 * Updates the size of the zone based on the current map cells.
	 * @return
	 */
	private void buildSize(){
		int maxXPosition = cells.keySet()
			.map(Location::getX)
			.reduce((previousX,currentX)-> currentX < previousX ? previousX : currentX);
		int maxYPosition = cells.keySet()
			.map(Location::getY)
			.reduce((previousY,currentY)-> currentY < previousY ? previousY : currentY);

		this.size = new Size(maxXPosition,maxYPosition);
	}
}
