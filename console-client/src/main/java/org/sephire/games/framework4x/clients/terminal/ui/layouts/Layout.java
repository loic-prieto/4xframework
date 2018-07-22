package org.sephire.games.framework4x.clients.terminal.ui.layouts;

import io.vavr.collection.Map;
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
	void addChild(UIElement child, Map<LayoutParameterKey,Object> parameters);

	/**
	 * Update the size and location of all children of the container,
	 * according to the particular layout strategy, and the container
	 * coordinates.
	 */
	void updateChildrenCoordinates();

	/**
	 * A layout cannot live outside a container, but it must be initialized somehow
	 * and by not making mandatory the container in the constructor, we avoid a circular dependency
	 * with the container (since the container itself must have a layout which we want in the constructor).
	 * @param container
	 */
	void setContainer(Container container);
}
