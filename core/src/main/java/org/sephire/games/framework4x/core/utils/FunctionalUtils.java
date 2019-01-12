/**
 * 4X Framework - Core library - The core library on which to base the game
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
package org.sephire.games.framework4x.core.utils;

import io.vavr.Function2;

/**
 * Set of functions related with functional manipulation of data structures.
 */
public class FunctionalUtils {

	/**
	 * Reduce operation utility functions
	 */
	public static class Reduce {

		/**
		 * Reduce a set of strings by concatenating them with a new line each.
		 */
		public static Function2<String,String,String> strings() {
			return FunctionalUtils.Reduce.stringsWithSeparator("\n");
		}

		/**
		 * Reduce a set of strings by concatenating them with the given separator
		 */
		public static Function2<String,String,String> stringsWithSeparator(String separator) {
			return (a,b) -> String.format("%s%s%s",a,separator,b);
		}
	}
}
