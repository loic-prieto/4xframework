package org.sephire.games.framework4x.core.plugins;

import lombok.Getter;
import org.sephire.games.framework4x.core.Framework4XException;

import static java.lang.String.format;

/**
 * This exception is thrown when trying to read information from a plugin jar, but
 * it could not be done (for a variety of reasons, mainly related to io).
 */
@Getter
public class PluginFileCouldNotBeReadException extends Framework4XException {

	public PluginFileCouldNotBeReadException(String cause) {
		super(format("Could not read plugin jar file: %s",cause));
	}
}
