package org.sephire.games.framework4x.core.plugins;

import lombok.Getter;
import org.sephire.games.framework4x.core.Framework4XException;

/**
 * This exception is thrown when a plugin declared a dependency on another plugin but this other
 * plugin is not on the plugin folder.
 */
@Getter
public class ParentPluginNotFoundException extends Framework4XException {
	private String childPlugin;
	private String parentPlugin;

	public ParentPluginNotFoundException(String childPlugin, String parentPlugin,String pluginFolder) {
		super(String.format("Plugin %s declared a dependency on plugin %, but % was not found in the plugin folder %s",
		  childPlugin,parentPlugin,pluginFolder
		));
		this.childPlugin = childPlugin;
		this.parentPlugin = parentPlugin;
	}
}
