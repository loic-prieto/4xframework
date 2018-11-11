package org.sephire.games.terminal.ui.components;

import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.sephire.games.framework4x.clients.terminal.ui.Coordinates;
import org.sephire.games.framework4x.clients.terminal.ui.Viewport;
import org.sephire.games.framework4x.clients.terminal.ui.layouts.Layout;
import org.sephire.games.framework4x.clients.terminal.ui.layouts.LayoutParameterKey;
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

    @NonNull
    private Viewport viewport;
	@NonNull
	private Layout layout;

	public Container(Coordinates coordinates,Container parentContainer) {
		super(coordinates,parentContainer);
	}
	public Container(Coordinates coordinates) {
		super(coordinates);
	}

	public Container() {
		super();
		this.viewport = new Viewport(this.getCoordinates(),this);
	}

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

	public void addChild(UIElement child) {
		addChild(child,HashMap.empty());
	}

	public void addChild(UIElement child, Map<LayoutParameterKey,Object> layoutParameters) {
		children = children.append(child);
		child.setContainerParent(Option.of(this));
		getLayout().addChild(child,layoutParameters);
	}

	/**
	 * Add children with no layout parameters. Only valid for very simple use cases.
	 * It is a minor performance improvement for a high number of children if they do not have layout parameters.
	 * @param children
	 */
	public void addChildren(List<UIElement> children) {
		this.children = this.children.appendAll(children);
		children.forEach((child) -> {
			child.setContainerParent(Option.of(this));
			getLayout().addChild(child,HashMap.empty());
		});
	}

	/**
	 * The container acts as the aggregate root regarding the layout
	 * @param layout
	 */
	public void setLayout(Layout layout) {
		this.layout = layout;
		layout.setContainer(this);
	}

}
