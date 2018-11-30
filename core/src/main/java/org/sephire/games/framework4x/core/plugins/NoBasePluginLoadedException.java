package org.sephire.games.framework4x.core.plugins;

import org.sephire.games.framework4x.core.Framework4XException;

/**
 * This exception occurs when in the process of creating a game, while loading all plugins,
 * there is no base plugin being loaded.
 */
public class NoBasePluginLoadedException extends Framework4XException {
	public NoBasePluginLoadedException() {
		super("No base plugin has been requested to be loaded. A game needs exactly one base plugin to be loaded");
	}
}
