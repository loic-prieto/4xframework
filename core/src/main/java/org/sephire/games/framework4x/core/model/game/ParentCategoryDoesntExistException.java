package org.sephire.games.framework4x.core.model.game;

import org.sephire.games.framework4x.core.Framework4XException;

import static java.lang.String.format;

/**
 * <p>This exception is thrown when trying to add a game command / game command category before
 * its corresponding category has been created.</p>
 * <p>A game command must belong to a game category.</p>
 */
public class ParentCategoryDoesntExistException extends Framework4XException {

	public ParentCategoryDoesntExistException(GameCommand<?> gameCommand) {
		super(format("Tried to add game command %s but its owner category %s doesn't exist",
		  gameCommand.getLabel(),gameCommand.getCategoryIdentifier()));
	}

	public ParentCategoryDoesntExistException(GameCommandCategory gameCommandCategory) {
		super(format("Tried to add game command %s but its owner category %s doesn't exist",
		  gameCommandCategory.getIdentifier(),gameCommandCategory.getParentCategoryIdentifier().get()));
	}
}
