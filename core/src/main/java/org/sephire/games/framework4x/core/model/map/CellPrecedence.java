package org.sephire.games.framework4x.core.model.map;

/**
 * <p>When two cells are at the same location, the client needs to know what cell goes under
 * another visually.</p>
 * <p>Although there is no good way to define this in a general-to-be-overriden-by-plugins way, a convention
 * can be the one below. When two cells are at the same location and precedence, the graphical client is
 * free to resolve the conflict as it sees fit.</p>
 **/
public enum CellPrecedence {
	HIGHEST,
	HIGHER,
	NORMAL,
	LOWER,
	LOWEST;
}
