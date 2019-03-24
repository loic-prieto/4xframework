package org.sephire.games.framework4x.core.plugins.commands;

import org.sephire.games.framework4x.core.Framework4XException;

import static java.lang.String.format;

/**
 * This exception is thrown when a class should have a default empty constructor,
 * but it doesn't.
 */
public class NoDefaultConstructorException extends Framework4XException {
	public NoDefaultConstructorException(Class badClass) {
		super(format("The class %s must have a no-parameters constructor",badClass));
	}
}
