package org.sephire.games.terminal.ui.components.viewport;

/**
 * Represents the visibility of an UI element inside the viewport.
 */
public enum ViewportVisibility {
	/**
	 * An  UI Element is fully visible inside the viewport
	 * when its location and size are contained inside the visible
	 * part of the viewport.
	 */
	VISIBLE,
	/**
	 * An UI Element is not visible when it is completely outside
	 * the visible part of the viewport.
	 */
	NON_VISIBLE,
	/**
	 * An UI Element is partially visible if it is partially contained
	 * inside the viewport (that is, part is not visible, and part is)
	 */
	PARTIALLY_VISIBLE;
}
