package org.sephire.games.framework4x.clients.terminal.gui.startgame;

import org.sephire.games.framework4x.clients.terminal.FourXFrameworkClientException;

/**
 * This exception is thrown when for a given selection of plugins, no maps are defined.
 */
public class NoMapFoundsException extends FourXFrameworkClientException {
	public NoMapFoundsException() {
		super("There were no maps found in the configuration for the current selection of loaded plugins");
	}
}
