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
import io.vavr.collection.Seq;
import io.vavr.control.Try;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

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
			return stringsWithSeparator("\n");
		}

		/**
		 * Reduce a set of strings by concatenating them with the given separator
		 */
		public static Function2<String,String,String> stringsWithSeparator(String separator) {
			return (a,b) -> String.format("%s%s%s",a,separator,b);
		}
	}

	public static class Collectors {

		/**
		 * Given a stream of tries, collects it into a single try, in the same manner that Try.sequence does,
		 * but in a chainable manner.
		 *
		 * @param <T>
		 */
		public static class TryCollector<T> implements Collector<Try<T>, List<Try<T>>,Try<Seq<T>>> {
			@Override
			public Supplier<List<Try<T>>> supplier() {
				return ArrayList::new;
			}

			@Override
			public BiConsumer<List<Try<T>>, Try<T>> accumulator() {
				return List::add;
			}

			@Override
			public BinaryOperator<List<Try<T>>> combiner() {
				return (left,right)-> { left.addAll(right); return left; };
			}

			@Override
			public Function<List<Try<T>>, Try<Seq<T>>> finisher() {
				return Try::sequence;
			}

			@Override
			public Set<Characteristics> characteristics() {
				return EnumSet.of(Characteristics.UNORDERED);
			}
		}

		/**
		 * See {@link TryCollector}
		 * @param <T>
		 * @return
		 */
		public static <T> TryCollector<T> toTry() {
			return new TryCollector<>();
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
