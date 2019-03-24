package org.sephire.games.framework4x.clients.terminal.api.ui.gamewindow;

import io.vavr.control.Try;

/**
 * Represents the set of functions that can be called on the game window for plugins
 * wishing to manipulate it.
 */
public interface GameWindowAPI {
	/**
	 * Closes the game window and return to the main menu.
	 * @return
	 */
	Try<Void> closeWindow();
}
