package org.sephire.games.framework4x.core.model.game;

import lombok.Getter;
import org.sephire.games.framework4x.core.Framework4XException;

/**
 * This exception is thrown when trying to get a state object and it is not found, and not founding it
 * represents an application error.
 */
public class GameStateNotFoundException extends Framework4XException {
	@Getter
	private GameStateEnumKey stateKey;

	public GameStateNotFoundException(GameStateEnumKey stateKey) {
		super(String.format("The game state with key %s was not found",stateKey));
		this.stateKey = stateKey;
	}
}
