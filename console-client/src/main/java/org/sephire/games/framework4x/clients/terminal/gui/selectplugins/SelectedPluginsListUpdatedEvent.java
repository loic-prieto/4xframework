package org.sephire.games.framework4x.clients.terminal.gui.selectplugins;

import io.vavr.collection.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.sephire.games.framework4x.core.plugins.PluginSpec;

/**
 * This event is fired when the list of selected plugins in the selected plugins windows
 * changes.
 */
@AllArgsConstructor
@Getter
public class SelectedPluginsListUpdatedEvent {
	private Set<PluginSpec> selectedPlugins;
}
