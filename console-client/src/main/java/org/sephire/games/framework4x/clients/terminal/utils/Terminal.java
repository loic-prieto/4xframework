/**
 * 4X Framework - Console client - A terminal-based client for the 4X framework
 * Copyright © 2018 Loïc Prieto Dehennault (loic.sephiroth@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
			return size.withRows(newRows);
		}
	}

}
