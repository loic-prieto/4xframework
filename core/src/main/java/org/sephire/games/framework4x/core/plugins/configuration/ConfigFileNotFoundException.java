package org.sephire.games.framework4x.core.plugins.configuration;

import lombok.Getter;
import org.sephire.games.framework4x.core.Framework4XException;

/**
 * When trying to load a configuration provider from a classpath file,
 * and the file does not exist, this exception will be thrown.
 */
public class ConfigFileNotFoundException extends Framework4XException {
	@Getter
	private String filename;

	public ConfigFileNotFoundException(String filename) {
		super("The classpath file " + filename + " has not been found");
		this.filename = filename;
	}
}
