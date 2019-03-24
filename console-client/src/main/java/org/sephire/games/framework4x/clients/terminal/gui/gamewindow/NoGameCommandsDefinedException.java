package org.sephire.games.framework4x.clients.terminal.gui.gamewindow;

import org.sephire.games.framework4x.clients.terminal.FourXFrameworkClientException;

/**
 * <p>This exception is thrown when trying to load the top menu bar in the game window, and
 * no game commands have been defined in any plugin or in the client itself.</p>
 * <p>It is an error that at least a basic quit command does not exist to let the user exit the application</p>
 */
public class NoGameCommandsDefinedException extends FourXFrameworkClientException {
}
