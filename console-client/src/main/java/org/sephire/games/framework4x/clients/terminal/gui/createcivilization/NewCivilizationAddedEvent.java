package org.sephire.games.framework4x.clients.terminal.gui.createcivilization;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.sephire.games.framework4x.core.model.civilization.Civilization;

/**
 * This event is thrown when a new civilization has been added from the create civilization window
 */
@Getter
@AllArgsConstructor
public class NewCivilizationAddedEvent {
	private Civilization civilization;
}
