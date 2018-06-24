package org.sephire.games.framework4x.clients.terminal.ui.layouts;

/**
 * The layout manager for a container class.
 * Takes care of position and sizing ui elements inside
 * a container.
 * <p>
 * This is the base interface all layouts will implement.
 */
public interface Layout {

	/**
	 * Update the size and location of all children of the container,
	 * according to the particular layout strategy, and the container
	 * coordinates.
	 */
	void updateChildrenCoordinates();
}
