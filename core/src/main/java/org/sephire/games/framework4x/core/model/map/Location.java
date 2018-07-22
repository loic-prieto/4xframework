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
	 * Returns a new location which is the result of substracting the given
	 * location coordinates to the current location coordinates.
	 * Does not modify the current location.
	 *
	 * @param location
	 * @return
	 */
	public Location substract(Location location) {
		return new Location(this.x - location.x,this.y - location.y);
	}

	public Location substract(int x, int y) {
		return this.substract(new Location(x,y));
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
		return new Location(this.x + location.x,this.y + location.y);
	}

	public Location add(int x,int y) { return this.add(new Location(x,y));}

	/**
	 * Checks whether one of the two coordinates has a positive value.
	 * @return
	 */
	public boolean hasPositiveValue() {
		return (x >= 0 || y >= 0);
	}

	/**
	 * Very simple convenience fluent factory method for locations.
	 *
	 * @param x
	 * @param y
	 * @return
	 */
	public static Location of(int x, int y) {
		return new Location(x,y);
	}
}
