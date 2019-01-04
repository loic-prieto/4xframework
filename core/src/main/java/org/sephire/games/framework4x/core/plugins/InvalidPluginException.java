package org.sephire.games.framework4x.core.plugins;

import org.sephire.games.framework4x.core.Framework4XException;

/**
 * This exception is thrown when a plugin is invalid for some reason, but the cause
 * is way too specific to create a new exception class for it.
 */
public class InvalidPluginException extends Framework4XException {

	public InvalidPluginException(String cause) {
		super(cause);
	}

	public InvalidPluginException(String message, Throwable cause) {
		super(message, cause);
	}
}
