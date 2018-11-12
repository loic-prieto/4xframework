package org.sephire.games.framework4x.clients.terminal.utils;

import io.vavr.Function1;

import java.util.function.Consumer;

public class Functions {

	public static <T> Function1<T,Void> wrap(Consumer<T> consumer) {
		Function1<T,Void> wrappedConsumer = (T item)-> {
			consumer.accept(item);
			return null;
		};

		return wrappedConsumer;
	}
}
