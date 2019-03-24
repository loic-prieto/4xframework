package org.sephire.games.framework4x.clients.terminal.api.ui.gamewindow;

import io.vavr.control.Option;

/**
 * Convenience functions related to the game window.
 */
public class GameWindowUtils {
	private static Option<GameWindowAPI> currentGameWindow = Option.none();

	/**
	 * <p>When the game window is created, it should register itself using this method
	 * so that plugins can get the current game window if they need to manipulate it.</p>
	 * @param gameWindow
	 */
	public static void setCurrentGameWindow(GameWindowAPI gameWindow) {
		currentGameWindow = Option.of(gameWindow);
	}

	/**
	 * <p>If a plugin needs to get the current game window, it can retrieve it here.</p>
	 * <p>The game window may not have been set when this function is invoked, which is
	 * why it is an optional result.</p>
	 * @return
	 */
	public static Option<GameWindowAPI> getCurrentGameWindow(){
		return currentGameWindow;
	}
}
