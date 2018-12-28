package org.sephire.games.framework4x.core.plugins;

import lombok.Getter;
import org.sephire.games.framework4x.core.Framework4XException;

/**
 * This exception is thrown when loading a list of plugins, and dependent plugins are
 * not included in that list.
 */
@Getter
public class PluginDependencyNotIncludedException extends Framework4XException {
	private String dependencyStarvingPlugin;
	private String dependentPlugin;

	public PluginDependencyNotIncludedException(String plugin, String dependentPlugin) {
		this.dependencyStarvingPlugin = plugin;
		this.dependentPlugin = dependentPlugin;
	}
}
