package org.sephire.games.framework4x.clients.terminal.gui.gamewindow.topmenu;

import org.sephire.games.framework4x.clients.terminal.FourXFrameworkClientException;

import static java.lang.String.format;

/**
 * <p>This exception happens when building the top menu in the game window, trying to create
 * a shortcut key for an item element but there are no more free characters to build a shortcut
 * from because they have already been taken by other menu items</p>
 * <p>This is a very rare occurrence, and the only way to show an example is in the following case:</br>
 * Imagine five game command menu item which have all the same label: game <br/>
 * The first four items will have the shortcut key g, a, m and e respectively, but the fifth one won't have
 * any free available character to take and this is an error that cannot be recovered from, aside from
 * removing the plugin that causes this situation.
 * </p>
 * <p>Other ways to solve this would be to assign a random character from the alphabet that is not already
 * taken, which would delay this problem, but we will get to it when this becomes a problem</p>
 */
public class NotEnoughCharactersForShortcutKeyException extends FourXFrameworkClientException {
	public NotEnoughCharactersForShortcutKeyException(String menuLabel) {
		super(format("A shortcut key could not be created for label %s",menuLabel));
	}
}
