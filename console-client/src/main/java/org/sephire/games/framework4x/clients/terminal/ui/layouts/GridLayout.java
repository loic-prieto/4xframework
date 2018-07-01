package org.sephire.games.framework4x.clients.terminal.ui.layouts;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * This is the layout to distribute components along a conceptual 2D grid.
 * This is the most generic grid-based container.
 * <p>
 * To build it, the {@link org.sephire.games.framework4x.clients.terminal.ui.layouts.gridlayout.Builder} class is
 * recommended.
 */
@RequiredArgsConstructor
public class GridLayout implements Layout {

	@NonNull
	private int columns;
	@NonNull
	private int rows;

	@Override
	public void updateChildrenCoordinates() {

	}
}
