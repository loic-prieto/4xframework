package org.sephire.games.framework4x.clients.terminal.utils;

/**
 * When you need a mutable value just to increment a value over time,
 * it is easier to just use a mutable counter.
 */
public class MutableIntCounter extends MutableValue<Integer> {

	public MutableIntCounter(int value) {
		super(value);
	}

	public MutableIntCounter() {
		this(0);
	}

	public void incrementValue(int step) {
		updateValue(getValue() + step);
	}

	public void incrementValue() {
		updateValue(getValue() + 1);
	}
}
