package org.sephire.games.framework4x.core.plugins.commands;

import org.sephire.games.framework4x.core.Framework4XException;

import java.lang.reflect.Method;

import static java.lang.String.format;

/**
 * This exception is thrown when loading a game command generator and its signature is invalid.
 */
public class GameCommandGeneratorInvalidSignatureException extends Framework4XException {
	public GameCommandGeneratorInvalidSignatureException(Method gameCommandGenerator) {
		super(format("The class %s declared an invalid signature game command generator for method %s",
		  gameCommandGenerator.getDeclaringClass().getName(),
		  gameCommandGenerator.getName()));
	}
}
