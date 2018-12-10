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
