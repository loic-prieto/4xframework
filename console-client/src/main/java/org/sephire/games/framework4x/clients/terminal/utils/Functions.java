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
