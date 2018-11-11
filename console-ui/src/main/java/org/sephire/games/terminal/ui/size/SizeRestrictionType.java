package org.sephire.games.terminal.ui.size;

/**
 * Represents a restriction type for a size value of a container regarding its growth.
 * This is to be used by layouts to know how a size value (height or width) should grow when the
 * container is resized.
 * A SizeRestrictionType is assigned to a region inside a layout and/or to a component inside a region.
 */
public enum SizeRestrictionType {
	/**
	 * At most is a maximum size the component will occupy if given the space.
	 */
	AT_MOST,
	/**
	 * At least means that the component or region should have at least this size. Can conflict with other
	 * regions and their preferred minimum size. The layouts should throw exceptions when conflicting configurations
	 * are encountered.
	 * This means that the component can grow indefinitely but can only shrink to this size.
	 * In combination with the at most restriction, sets a range of valid values while giving flexibility for other
	 * components inside the layout.
	 */
	AT_LEAST,
	/**
	 * A fixed value restriction means that the region/component will always have this size regardless of resizing.
	 * If the value unit is a percentage, it will grow and shrink to reach the relative value of the new size.
	 * May conflict with other components restrictions.
	 */
	FIXED;
}
