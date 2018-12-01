package org.sephire.games.framework4x.core.plugins.configuration;

import org.sephire.games.framework4x.core.Framework4XException;

/**
 * This exception is thrown when a config file is being parsed into a config file
 * and the parsing failed.
 */
public class InvalidConfigFileException extends Framework4XException {
	private String configFile;

	public InvalidConfigFileException(String configFile, Throwable reason) {
		super(String.format("The config file %s is syntactically invalid: %s", configFile, reason.getMessage()), reason);
		this.configFile = configFile;
	}
}
