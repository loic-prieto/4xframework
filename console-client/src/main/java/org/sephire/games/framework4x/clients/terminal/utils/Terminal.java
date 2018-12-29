package org.sephire.games.framework4x.clients.terminal.utils;

import com.googlecode.lanterna.TerminalSize;
import org.sephire.games.framework4x.core.model.map.Size;

public class Terminal {

	public static class Size {

		/**
		 * Given a terminal size, returns a new terminal size where the columns have been reduced to the
		 * specified percentage of the original.
		 *
		 * @param size
		 * @param percentage
		 * @return
		 */
		public static TerminalSize sizeWithWidthToPercent(TerminalSize size, float percentage) {
			var newColumns = (int) Math.floor(size.getColumns() * percentage);
			return size.withColumns(newColumns);
		}

		/**
		 * Given a terminal size, returns a new terminal size where the rows have been reduced to the
		 * specified percentage of the original.
		 *
		 * @param size
		 * @param percentage
		 * @return
		 */
		public static TerminalSize sizeWithHeightToPercent(TerminalSize size, float percentage) {
			var newRows = (int) Math.floor(size.getRows() * percentage);
			return size.withColumns(newRows);
		}
	}

}
