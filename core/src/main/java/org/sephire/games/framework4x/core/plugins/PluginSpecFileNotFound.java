package org.sephire.games.framework4x.core.plugins;

import lombok.Getter;
import org.sephire.games.framework4x.core.Framework4XException;

/**
 * Each plugin must have a spec file which defines the plugin's metadata.
 * This spec file sits at the root of the plugin's package, named plugin.yml
 *
 * This exception occurs when there is no such file.
 */
public class PluginSpecFileNotFound extends Framework4XException {
	@Getter
	private String pluginName;

	public PluginSpecFileNotFound(String pluginName) {
		super("The plugin.yml spec file could not be found for plugin "+pluginName);
		this.pluginName = pluginName;
	}
}
