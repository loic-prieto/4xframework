package org.sephire.games.framework4x.clients.terminal.ui.layouts.gridlayout;

import org.sephire.games.framework4x.clients.terminal.ui.layouts.GridLayout;

public class Builder {
	public static GridLayout ofBase(int columns, int rows) {
		return new GridLayout(columns, rows);
	}
}
