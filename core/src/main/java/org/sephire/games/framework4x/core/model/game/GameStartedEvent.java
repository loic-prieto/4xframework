package org.sephire.games.framework4x.core.model.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.sephire.games.framework4x.core.model.events.DomainEvent;

/**
 * <p>This event is fired when a game has started, with all the configuration
 * loaded.</p>
 */
@AllArgsConstructor
public class GameStartedEvent extends DomainEvent {
	@Getter
	private Game game;
}
