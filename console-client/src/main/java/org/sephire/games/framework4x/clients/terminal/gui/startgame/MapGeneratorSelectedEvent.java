package org.sephire.games.framework4x.clients.terminal.gui.startgame;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.sephire.games.framework4x.core.plugins.map.MapGeneratorWrapper;

/**
 * This event is fired when a map generator has been selected from the select map panel
 */
@Getter
@AllArgsConstructor
public class MapGeneratorSelectedEvent {
	private MapGeneratorWrapper mapGenerator;
}
