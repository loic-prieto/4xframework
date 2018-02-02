package org.sephire.games.framework4x.clients.terminal.ui;

import io.vavr.control.Option;
import lombok.Value;
import org.sephire.games.framework4x.core.model.map.Location;
import org.sephire.games.framework4x.core.model.map.Range;

/**
 * Represents the viewport of a map.
 * Will be influenced by screen size and overlapping ui elements like
 * a top menu.
 */
@Value
public class Viewport {
    private int xOffset;
    private int yOffset;
    private int width;
    private int height;

    public Range toRange() {
        return new Range(xOffset, yOffset, width, height);
    }

	/**
	 * Given an absolute location on a scene, returns a new location
	 * on the viewport.
	 * For example, given a city on the world map location (145,120),
	 * if the viewport is located at (100,105) and has a size of (80,40),
	 * then the city location inside the viewport is: (45,15)
	 * If the given location is outside the bounds of the viewport it will
	 * return a None value.
	 *
	 * @param location
	 * @return
	 */
	public Option<Location> getRelativePositionFor(Location location) {

		if (!contains(location)) {
			return Option.none();
		}

		return Option.of(new Location(location.getX() - xOffset, location.getY() - yOffset));
	}

	/**
	 * Check whether a given location is contained inside the viewport screen.
	 *
	 * @param location
	 * @return
	 */
	public boolean contains(Location location) {
		return location.getX() < (xOffset + width) &&
				location.getX() >= xOffset &&
				location.getY() < (yOffset + height) &&
				location.getY() >= yOffset;

	}
}
