package org.sephire.games.framework4x.clients.terminal;

/**
 * The base class for all client exceptions for the 4xframework.
 */
public abstract class FourXFrameworkClientException extends RuntimeException {

	public FourXFrameworkClientException() {
	}

	public FourXFrameworkClientException(String s) {
		super(s);
	}
}
