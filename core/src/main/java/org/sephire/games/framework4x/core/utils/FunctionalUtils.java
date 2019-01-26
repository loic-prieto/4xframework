/**
 * 4X Framework - Core library - The core library on which to base the game
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

	/**
	 * Generic functions to use
	 */
	public static class Functions {

		/**
		 * <p>Due to the way the Function vavr API is defined, functions that are parameterized with Void must return null.
		 * This is an inconvenience when you want to avoid using non vavr functions (for example, Consumer from the java
		 * lang api).</p>
		 * <p>This function allows to map to void value, by discarding the object it is being given and returning null</p>
		 * @param item
		 * @param <T>
		 * @return
		 */
		public static <T> Void toVoid(T item) { return null; }
	}
}
