package org.sephire.games.framework4x.core.plugins;

import lombok.Getter;
import org.sephire.games.framework4x.core.Framework4XException;

/**
 * This exception appears when a plugin spec file is loaded but it has invalid syntax or it does not
 * map correctly to the PluginSpec class.
 */
public class InvalidPluginSpecFileException extends Framework4XException {
	@Getter
	private String pluginName;

	public InvalidPluginSpecFileException(String message, String pluginName) {
		super(message);
		this.pluginName = pluginName;
	}

	public InvalidPluginSpecFileException(String pluginName, Throwable cause) {
		super("The given spec file for the plugin " + pluginName + " is invalid", cause);
		this.pluginName = pluginName;
	}
}
