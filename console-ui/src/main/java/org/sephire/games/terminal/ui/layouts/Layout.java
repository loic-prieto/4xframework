package org.sephire.games.terminal.ui.layouts;

import io.vavr.collection.Map;
import io.vavr.control.Option;
import org.sephire.games.framework4x.clients.terminal.ui.Coordinates;
import org.sephire.games.framework4x.clients.terminal.ui.components.Container;
import org.sephire.games.framework4x.clients.terminal.ui.components.UIElement;

/**
 * The layout manager for a container class.
 * Takes care of position and sizing ui elements inside
 * a container.
 *
 * This is the base interface all layouts will implement.
 */
public interface Layout {

	/**
	 * Add a child element to the layout with the given parameters.
	 * @param child
	 * @param parameters
	 */
	void addChild(UIElement child, Map<LayoutParameterKey, Object> parameters);

	/**
	 * A layout cannot live outside a container, but it must be initialized somehow
	 * and by not making mandatory the container in the constructor, we avoid a circular dependency
	 * with the container (since the container itself must have a layout which we want in the constructor).
	 * @param container
	 */
	void setContainer(Container container);

	/**
	 * Given a ui child element inside a container, the layout defines the size and location of the child.
	 * This method of the layout allows to get the calculated coordinates of a child inside a container
	 * as defined by the layout.
	 * A coordinate may be None of the child element doesn't exist inside the layout.
	 *
	 * @param childElement
	 * @return the modified coordinates of the childElement if the child element has been added to the layout.
	 */
	Option<Coordinates> getChildCoordinates(UIElement childElement);

	/**
	 * When a child is added or removed from the layout, potentially every child coordinate needs to be
	 * recalculated. This is the method that does it.
	 */
	void updateChildrenCoordinates();
}
