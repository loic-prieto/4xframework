package org.sephire.games.framework4x.core.plugins;

import lombok.Getter;
import org.sephire.games.framework4x.core.Framework4XException;

/**
 * This exception occurs when trying to load the main class from a plugin,
 * but the class is invalid (for example: by not implementing the PluginInitializer
 * interface)
 */
public class InvalidPluginMainClassException extends Framework4XException {
	@Getter
	private String pluginName;
	@Getter
	private Class pluginMainClass;

	public InvalidPluginMainClassException(String pluginName, Class pluginMainClass, Exception cause) {
		super("The class " + pluginMainClass + " is not a valid main class for plugin " + pluginName, cause);
		this.pluginName = pluginName;
		this.pluginMainClass = pluginMainClass;
	}
}
