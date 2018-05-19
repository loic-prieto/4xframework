package org.sephire.games.framework4x.clients.terminal.utils;

/**
 * Holds a mutable value of type T, to be used inside lambdas.
 * @param <T>
 */
public class MutableValue<T> {
	private T value;

	public MutableValue(T value) {
		this.value = value;
	}

	public T getValue() { return value; }
	public void updateValue(T value) { this.value = value; }
}

