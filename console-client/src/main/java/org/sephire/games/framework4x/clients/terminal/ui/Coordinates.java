package org.sephire.games.framework4x.clients.terminal.ui;

import lombok.NonNull;
import lombok.Value;
import org.sephire.games.framework4x.clients.terminal.ui.size.Size;
import org.sephire.games.framework4x.core.model.map.Location;

/**
 * The coordinates of an item in a 2D space.
 * They are defined by two elements:
 * - location
 * - size
 * The location represents an (x,y) position in a 2D grid,
 * and the size represents a qualified dimension in this 2D grid,
 * such as width and height with different unit types, namely percentage
 * or a fixed block value.
 */
@Value
public class Coordinates {
	@NonNull
	private Location location;
	@NonNull
	private Size size;

	public Coordinates withSize(Size newSize) {
		return new Coordinates(location, newSize);
	}

	public Coordinates withLocation(Location newLocation) {
		return new Coordinates(newLocation, size);
	}
}
