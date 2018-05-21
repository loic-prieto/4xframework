package org.sephire.games.framework4x.clients.terminal.ui.size;

import lombok.Value;

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

	public QualifiedSizeValue withValue(int value) {
		return new QualifiedSizeValue(value,unit);
	}

	public QualifiedSizeValue withUnit(SizeUnit unit) {
		return new QualifiedSizeValue(value,unit);
	}

	public QualifiedSizeValue addValue(int addedValue) {
		return new QualifiedSizeValue(value+addedValue,unit);
	}

	@Override
	public int compareTo(QualifiedSizeValue o) {
		if(o == this) return 0;
		if(this.unit != o.unit) throw new SizeUnitNotComparableException();
		if(this.value == o.value) return 0;

		return this.value > o.value ? 1 : -1;
	}
}
