package org.sephire.games.terminal.ui.size;

import lombok.Value;

import static org.sephire.games.framework4x.clients.terminal.ui.size.SizeUnit.CHARACTER;

/**
 * Represents the tuple numeric value and unit to use in size measurements.
 * A size for a given component has a width and an height and they can be
 * either relative to the container size in percentages, or can have a fixed
 * character-wide value.
 */
@Value
public class QualifiedSizeValue implements Comparable<QualifiedSizeValue> {
	private int value;
	private SizeUnit unit;

	/**
	 * Fixed qualified size value for value 1.
	 * It will often be used for single width or height items, so it makes sense
	 * to avoid verbose constructors for this one value.
	 */
	public static QualifiedSizeValue FIXED_SIZE_ONE = new QualifiedSizeValue(1, CHARACTER);

	public QualifiedSizeValue withValue(int value) {
		return new QualifiedSizeValue(value, unit);
	}

	public QualifiedSizeValue withUnit(SizeUnit unit) {
		return new QualifiedSizeValue(value, unit);
	}

	public QualifiedSizeValue addValue(int addedValue) {
		return new QualifiedSizeValue(value + addedValue, unit);
	}

	@Override
	public int compareTo(QualifiedSizeValue o) {
		if (o == this) return 0;
		if (this.unit != o.unit) throw new SizeUnitNotComparableException();
		if (this.value == o.value) return 0;

		return this.value > o.value ? 1 : -1;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		QualifiedSizeValue that = (QualifiedSizeValue) o;
		if (this.unit != that.unit) throw new SizeUnitNotComparableException();

		return value == that.value;
	}

}
