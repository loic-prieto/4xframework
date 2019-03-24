package org.sephire.games.framework4x.clients.terminal.gui.gamewindow;

import org.sephire.games.framework4x.clients.terminal.FourXFrameworkClientException;

import static java.lang.String.format;

/**
 * This exception is thrown when a translation is required and is not found, and that constitutes an error.
 */
public class TranslationNotFoundException extends FourXFrameworkClientException {
	public TranslationNotFoundException(String translationKey) {
		super(format("The translation resource %s was not found",translationKey));
	}
}
