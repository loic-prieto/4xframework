package org.sephire.games.framework4x.clients.terminal.gui.selectplugins;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.sephire.games.framework4x.core.plugins.PluginSpec;

/**
 * This event is thrown when a plugin is traversed inside the plugin list.
 */
@AllArgsConstructor
@Getter
public class PluginTraversedEvent {
	private PluginSpec selectedPlugin;
}
