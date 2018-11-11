package org.sephire.games.terminal.ui;

import lombok.Getter;
import lombok.Setter;
import org.sephire.games.terminal.ui.components.Container;


/**
 * Represents the viewport of an ui widget that contains other elements inside.
 * The viewport is what is visible on the screen.
 * For example, for a terminal size of 80x24, a menu panel may be of size 10x15, located
 * at position 5,5 on the screen. An element inside that panel will have a position inside the
 * panel that may exceed the size of the container and would not be shown but still has to be tracked
 * to check whether it should be shown.
 * A viewport also acts as a "camera" in the sense that it has a current position which modifies what
 * is visible on the screen.
 *
 * This class helps in translating children coordinates to screen coordinates and to check what is
 * visible in the screen.
 */
@Getter @Setter
public class Viewport {
	// The location and size of the viewport in the container element
	private Coordinates coordinates;
	// The offset on the x axis. How many columns the viewport is displaced.
	// Can be thought as the displacement of the camera on the x axis.
	private int xOffset;
	// The offset on the x axis. How many columns the viewport is displaced.
	// Can be thought as the displacement of the camera on the y axis.
    private int yOffset;
	// The container UI element the viewport is attached to
	private Container containerElement;

	public Viewport(Coordinates coordinates, Container container) {
		this.coordinates = coordinates;
		this.containerElement = container;
		xOffset = 0;
		yOffset = 0;
	}

	/**
	 * Check whether a given location is contained inside the visible part of the
     * viewport screen.
	 *
	 * @param location
	 * @return
	 */
	public boolean isLocationVisible(Location location) {
        return !location.substract(xOffset,yOffset)
		        .substract(coordinates.getSize().getWidth().getValue(), coordinates.getSize().getHeight().getValue())
                .hasPositiveValue();
	}

	/**
	 * For the given coordinates (location+size), check what kind of visibility they
	 * have inside the viewport.
	 *
	 * @param elementCoordinates
	 * @return
	 */
	public ViewportVisibility coordinatesVisibility(Coordinates elementCoordinates) {
		int x = elementCoordinates.getLocation().getX();
		int y = elementCoordinates.getLocation().getY();
		int width = elementCoordinates.getSize().getWidth().getValue();
		int height = elementCoordinates.getSize().getHeight().getValue();

		boolean hasVisibility = false;
		boolean hasInvisibility = false;

		for (int i = y; i < (y + height); i++) {
			for (int j = x; i < (x + width); j++) {
				if (isLocationVisible(new Location(j, i))) {
					hasVisibility = true;
				} else {
					hasInvisibility = true;
				}
			}
		}

		boolean isPartial = hasVisibility && hasInvisibility;
		return isPartial ? ViewportVisibility.PARTIALLY_VISIBLE :
				hasVisibility ? ViewportVisibility.VISIBLE :
						ViewportVisibility.NON_VISIBLE;
	}

}
