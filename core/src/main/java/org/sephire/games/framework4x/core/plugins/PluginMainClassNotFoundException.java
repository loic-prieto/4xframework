package org.sephire.games.framework4x.core.plugins;

import org.sephire.games.framework4x.core.Framework4XException;

/**
 * This exception occurs when trying to load the main class of a plugin, but
 * the class is not found.
 */
public class PluginMainClassNotFoundException extends Framework4XException {

	private String className;
	private String pluginName;

	public PluginMainClassNotFoundException(String className, String pluginName) {
		super("The class " + className + " does not exist, even though it was defined in plugin " + pluginName);
		this.className = className;
		this.pluginName = pluginName;
	}
}
