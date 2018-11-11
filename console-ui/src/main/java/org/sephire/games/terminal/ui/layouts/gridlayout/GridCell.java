package org.sephire.games.terminal.ui.layouts.gridlayout;

import lombok.Value;
import org.sephire.games.framework4x.clients.terminal.ui.size.FixedSize;
import org.sephire.games.framework4x.core.model.map.Location;

/**
 * Represents a cell inside a Grid layout.
 * The cell has a location and a size, which is used to distribute
 * child items inside a container with a grid layout.
 */
@Value
public class GridCell {
	private Location location;
	private FixedSize size;

}
