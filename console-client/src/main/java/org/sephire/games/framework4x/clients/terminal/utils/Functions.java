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

import io.vavr.Function1;

import java.util.function.Consumer;

public class Functions {

	/**
	 * Wraps a Consumer function into a Function1 lambda that returns null.
	 * This is needed in the case that we want to have the signature of the
	 * methods use vavr function classes with void return function, but still be able
	 * to write functions that return nothing, which otherwise Function classes force you
	 * to do it.
	 *
	 * @param consumer
	 * @param <T>
	 * @return
	 */
	public static <T> Function1<T, Void> wrap(Consumer<T> consumer) {
		Function1<T, Void> wrappedConsumer = (T item) -> {
			consumer.accept(item);
			return null;
		};

		return wrappedConsumer;
	}
}
