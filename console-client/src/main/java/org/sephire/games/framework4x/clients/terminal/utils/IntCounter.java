package org.sephire.games.framework4x.clients.terminal.utils;

/**
 * When you need a mutable value just to increment a value over time,
 * it is easier to just use a mutable counter.
 */
public class IntCounter extends MutableValue<Integer> {

	public IntCounter(int value) {
		super(value);
	}

	public IntCounter() {
		this(0);
	}

	public void incrementValue(int step) {
		updateValue(getValue() + step);
	}

	public void incrementValue() {
		updateValue(getValue() + 1);
	}
}
