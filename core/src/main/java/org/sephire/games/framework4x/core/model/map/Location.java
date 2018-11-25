package org.sephire.games.framework4x.core.model.map;

import lombok.Value;

/**
 * The location of an item in a 3D space.
 * Along with the (x,y,z) coordinates, this class provides some basic arithmetic to ease usage.
 */
@Value
public class Location {
	private int x;
	private int y;
	private int z;

	/**
	 * Returns a new location which is the result of substracting the given
	 * location coordinates to the current location coordinates.
	 * Does not modify the current location.
	 *
	 * @param location
	 * @return
	 */
	public Location substract(Location location) {
		return new Location(this.x - location.x,this.y - location.y,this.z - location.z);
	}
	public Location substract(int x, int y,int z) {
		return this.substract(new Location(x,y,z));
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
		return new Location(this.x + location.x,this.y + location.y,this.z + location.z);
	}
	public Location add(int x,int y) { return this.add(new Location(x,y,z));}

	/**
	 * Very simple convenience fluent factory method for locations.
	 *
	 * @param x
	 * @param y
	 * @return
	 */
	public static Location of(int x, int y, int z) {
		return new Location(x,y,z);
	}
}
