/**
 * 4X Framework - Console client - A terminal-based client for the 4X framework
 * Copyright © 2018 Loïc Prieto Dehennault (loic.sephiroth@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.sephire.games.framework4x.clients.terminal.utils;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import io.vavr.control.Option;
import org.sephire.games.framework4x.clients.terminal.gui.gamewindow.map.MapDirection;
import org.sephire.games.framework4x.core.model.map.Location;
import org.sephire.games.framework4x.core.model.map.Size;

import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import static io.vavr.API.*;

public class Terminal {

	public static class Plugins {

		/**
		 * Default folder to store plugins in the console client
		 */
		public static Path DEFAULT_PATH = Path.of(".", "plugins");
	}

	public static class Dimensions {

		public static Size fromTerminalSize(TerminalSize size) {
			return new Size(size.getColumns(),size.getRows());
		}

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

	public static class Position {

		/**
		 * Given a fixed location, apply direction and distance to it to obtain a new location.
		 * @param location
		 * @param direction
		 * @param distance
		 * @return
		 */
		public static Location applyDirection(Location location, MapDirection direction,int distance) {

			return Match(direction).of(
			  Case($(MapDirection.UP),()->location.substract(0,distance)),
			  Case($(MapDirection.DOWN),()->location.add(0,distance)),
			  Case($(MapDirection.LEFT),()->location.substract(distance,0)),
			  Case($(MapDirection.RIGHT),()->location.add(distance,0))
			);
		}

		/**
		 * Transform a Location object into a TerminalPosition object
		 * @param location
		 * @return
		 */
		public static TerminalPosition terminalPositionFrom(Location location) {
			return new TerminalPosition(location.getX(),location.getY());
		}
	}

	public static class Translation {

		/**
		 * <p>Gets the translation for a given locale of a string resource identidied by key and parameterized
		 * with the provided values if any</p>
		 * <p>Uses the translation bundles from the terminal client vs the plugin i18n resources <br/>
		 * So this is mainly a utility for the terminal client of the 4XFramework for resources that are not
		 * defined in any plugin belonging to the UI</p>
		 * @param locale
		 * @param labelKey
		 * @param params
		 * @return
		 */
		public static Option<String> getTranslationFor(Locale locale, String labelKey, Object... params) {
			ResourceBundle bundle = PropertyResourceBundle.getBundle("i18n.BasicUI", locale);
			return Option.of(bundle.getString(labelKey))
			  .map(t -> MessageFormat.format(t, params));
		}
	}

}
