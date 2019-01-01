package org.sephire.games.framework4x.clients.terminal.gui.selectplugins;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.sephire.games.framework4x.core.plugins.PluginSpec;

/**
 * This event is fired when a plugin inside the plugin list was interacted with,
 * either to select it or deselect it.
 */
@AllArgsConstructor
@Getter
public class PluginInteractedEvent {
	private PluginSpec plugin;
	private boolean isSelected;
}
