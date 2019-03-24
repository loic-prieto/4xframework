package org.sephire.games.framework4x.core.model.game;

import io.vavr.Function1;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * <p>A game command is an action that can occur, manipulating the game or the client in some way.</p>
 * <p>They are designed to be invoked by clients upon activating an input control, be it keys or buttons in the UI, or
 * whatever other mechanism is more appropriate for the game client.</p>
 * <p>A game command has a label and a description so that the user can know what the command will do</p>
 * <p>A game command may return a value when executing</p>
 * <p>A game command must belong to a game command category, there are no free game commands</p>
 */
@AllArgsConstructor
@Getter
@EqualsAndHashCode(of = { "identifier" })
public class GameCommand<RETURN_TYPE> {
	private String identifier;
	private String label;
	private Function1<Game,RETURN_TYPE> commandExecutionMethod;
	private String categoryIdentifier;
}
