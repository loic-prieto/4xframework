package org.sephire.games.framework4x.clients.terminal.ui.layouts.gridlayout;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.sephire.games.framework4x.clients.terminal.ui.layouts.GridLayout;

public class Builder {
	private int columns;
	private int rows;

	private Builder(int columns,int rows) {
		this.columns = columns;
		this.rows = rows;
	}

	public static BuildStep ofBase(int columns, int rows) {
		Builder params = new Builder(columns,rows);
		return new BuildStep(params);
	}

	@RequiredArgsConstructor
	public static class BuildStep {
		@NonNull
		private Builder builderParams;

		public GridLayout build() {
			return new GridLayout(builderParams.columns,builderParams.rows);
		}

	}
}
