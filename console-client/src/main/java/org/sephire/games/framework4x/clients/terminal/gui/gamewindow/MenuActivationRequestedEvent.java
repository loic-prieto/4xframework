package org.sephire.games.framework4x.clients.terminal.gui.gamewindow;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import io.vavr.control.Option;
import lombok.Getter;

/**
 * This event is fired when a top menu activation is requested from the game window.
 */
public class MenuActivationRequestedEvent {

	@Getter
	private Character character;

	public MenuActivationRequestedEvent(Character character) {
		this.character = character;
	}

	public static Option<MenuActivationRequestedEvent> from(KeyStroke keyStroke) {
		return isPotentialMenuActivation(keyStroke) ?
		  Option.of(new MenuActivationRequestedEvent(keyStroke.getCharacter())) :
		  Option.none();
	}

	/**
	 * A top menu activation is performed by pressing Alt+menu shortcut key
	 * @param keyStroke
	 * @return
	 */
	private static boolean isPotentialMenuActivation(KeyStroke keyStroke) {
		var isCtrlKeyPressed = keyStroke.isCtrlDown();
		var isAlphaKeyPressed = keyStroke.getKeyType() == KeyType.Character;

		return isCtrlKeyPressed && isAlphaKeyPressed;
	}
}
