package org.sephire.games.framework4x.clients.terminal.api.ui.gamewindow;

/**
 * <p>When adding an element to the bottom bar, it must be positioned into one of three slots
 * <ul>
 *     <li>Left</li>
 *     <li>Center</li>
 *     <li>Right</li>
 * </ul>
 * The elements are aligned depending on their slot, and the center position has the remaining space
 * between the center and right slots.
 * </p>
 */
public enum BottomBarPosition {
	Left,
	Center,
	Right;
}
