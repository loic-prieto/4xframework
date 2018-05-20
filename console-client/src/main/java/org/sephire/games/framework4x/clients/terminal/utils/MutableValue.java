package org.sephire.games.framework4x.clients.terminal.utils;

/**
 * Holds a mutable value of type T, to be used inside lambdas.
 *
 * Inside a lambda function, or, indeed, any closure in java, outside
 * variables cannot be reassigned. This is a problem for non primitive
 * types, since you will often want to reassign them during a calculation,
 * or if you're using the lambda to append values to an existing immutable
 * data structure or any other similar operation.
 *
 * This problem can be solved by using lambdas and iterations to build the
 * objects themselves fully, but then it involves repeating the iterations
 * many times to build many things at once, and for large iterations, it
 * becomes time consuming.
 *
 * Hence the use of a mutable value object, that can have its value modified
 * without changing the object reference itself.
 *
 * @param <T> The type of value it will hold
 */
public class MutableValue<T> {
	private T value;

	public MutableValue(T value) {
		this.value = value;
	}

	public T getValue() { return value; }

	public void updateValue(T value) { this.value = value; }
}

