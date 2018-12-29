package org.sephire.games.framework4x.core.plugins;

import lombok.Getter;
import org.sephire.games.framework4x.core.Framework4XException;

/**
 * This exception appears when a plugin spec is used to load a plugin, but the data inside the spec is invalid.
 * For example, the specified root package of the plugin doesn't exist.
 */
public class InvalidPluginSpecException extends Framework4XException {
	@Getter
	private String pluginName;

	public InvalidPluginSpecException(String message, String pluginName) {
		super(message);
		this.pluginName = pluginName;
	}

	public InvalidPluginSpecException(String pluginName, Throwable cause) {
		super("The given spec for the plugin " + pluginName + " is invalid", cause);
		this.pluginName = pluginName;
	}
}
