package org.sephire.games.framework4x.clients.terminal.gui.startgame;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.sephire.games.framework4x.core.model.civilization.Civilization;

/**
 * This event is fired when a civilization is selected from the dropdown menu in the
 * start game screen.
 */
@Getter
@AllArgsConstructor
public class CivilizationSelectedEvent {
	private Civilization civilization;
}
