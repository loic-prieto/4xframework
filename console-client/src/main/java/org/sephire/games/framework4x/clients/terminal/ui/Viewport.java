package org.sephire.games.framework4x.clients.terminal.ui;

import io.vavr.control.Option;
import org.sephire.games.framework4x.core.model.map.Location;

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
public class Viewport {
	// The location of the viewport in the screen
    private Location screenLocation;
	private Size size;
	// The offset on the x axis. How many columns the viewport is displaced.
	// Can be thought as the displacement of the camera on the x axis.
	private int xOffset;
	// The offset on the x axis. How many columns the viewport is displaced.
	// Can be thought as the displacement of the camera on the y axis.
    private int yOffset;

	public Viewport(Location screenLocation, Size size) {
        this.screenLocation = screenLocation;
		this.size = size;
    }

	/**
	 * <p>Given the position of an element inside the viewport, return the absolute
	 * position of the element in the screen.</p>
	 * <p>If the element is outside the viewport viewing range, no position is returned.</p>
	 * Example:
	 * <ul>
	 *     <li>Given a city element, on position 132,34 on a viewport of size 80x22, where
	 * 	       no camera displacement has been produced, and where the ui widget this viewport is
	 * 	       representing is located at location 0,1 in the screen, this method will return a None
	 * 	       result since it is outside the visible range of the viewport.
	 * 	   </li>
	 *     <li>Given a city element, on position 10,5 on a viewport of size 80x22, where
	 * 	       no camera displacement has been produced, and where the ui widget this viewport is
	 * 	       representing is located at location 0,1 in the screen, this method will return a 10,6
	 * 	       screen location.
	 * 	   </li>
	 *     <li>Given a city element, on position 120,60 on a viewport of size 80x22, where
	 * 	       the camera has been moved 50 positions in the x axis and 50 positions in the y axis, and where
	 * 	       the ui widget this viewport is representing is located at location 0,1 in the screen,
	 * 	       this method will return a 10,11 screen location.
	 * 	   </li>
	 * </ul>
	 */
	public Option<Location> getScreenPositionFor(Location viewportLocation) {
		Option<Location> screenPosition = Option.none();
		if(isLocationVisible(viewportLocation)) {
            screenPosition = Option.of(
                    viewportLocation
                            .substract(xOffset,yOffset)
                            .add(screenLocation));
        }

		return screenPosition;
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
		        .substract(size.getWidth(), size.getHeight())
                .hasPositiveValue();
	}
}
