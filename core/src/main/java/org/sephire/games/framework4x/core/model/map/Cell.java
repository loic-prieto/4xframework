package org.sephire.games.framework4x.core.model.map;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents a cell inside a map zone.
 */
@Getter
@AllArgsConstructor
public class Cell {
	private Location location;
	private CellPrecedence precedence;
	private CellType type;
}
