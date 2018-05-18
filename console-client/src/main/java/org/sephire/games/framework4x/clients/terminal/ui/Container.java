package org.sephire.games.framework4x.clients.terminal.ui;

import io.vavr.collection.List;
import io.vavr.control.Option;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.sephire.games.framework4x.core.model.map.Location;

/**
 * An UI Element that contains other elements that extend beyond
 * what is visible. Can occupy the whole screen, or part of it.
 * Will only draw the visible content of childrens.
 *
 */
@Getter
@Setter
public abstract class Container extends UIElement {
    /**
     * The viewport of the container.
     */
    @NonNull
    private Viewport viewport;
	/**
	 * The list of element contained in this container.
	 */
	private List<UIElement> children = List.empty();

	/**
	 * The coordinates of a child inside a container are relative
	 * to the container itself. This means that a child with coordinates
	 * (10,5) inside a container in a location (1,2) has true coordinates
	 * of (11,7) relative to the parent of the container.
	 * The container itself may be contained by another container. This means
	 * that for a child to know its true screen location, the chain of
	 * containers will have to resolve the coordinates recursively.
	 * If a child coordinate is outside the viewport of the container, then no
	 * location can be returned.
	 *
	 * @param childLocation
	 * @return
	 */
	public Option<Location> getScreenLocationFor(Location childLocation) {
		Option<Location> screenLocation;
		// First check if the child location is inside the view port
		if (viewport.isLocationVisible(childLocation)) {
			// First calculate the location of the child relative to this container
			Location unpackedLocation = childLocation.add(this.getCoordinates().getLocation());

			// assign the unpacking of the new location relative to the parent of
			// this container
			screenLocation = this.getContainerParent()
					.map(container -> container.getScreenLocationFor(unpackedLocation))
					.getOrElse(() -> Option.of(unpackedLocation));

		} else {
			screenLocation = Option.none();
		}

		return screenLocation;
	}

}
