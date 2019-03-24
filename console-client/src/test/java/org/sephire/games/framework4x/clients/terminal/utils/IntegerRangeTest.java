package org.sephire.games.framework4x.clients.terminal.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IntegerRangeTest {

	private final static IntegerRange containingRange = new IntegerRange(5, 15);
	private final static IntegerRange insideRange = new IntegerRange(6, 8);
	private final static IntegerRange leftRange = new IntegerRange(5, 8);
	private final static IntegerRange rightRange = new IntegerRange(10, 15);
	private final static IntegerRange partiallyContainedInLeft = new IntegerRange(0, 10);
	private final static IntegerRange partiallyContainedInRight = new IntegerRange(12, 20);
	private final static IntegerRange outsideRangeLeft = new IntegerRange(0, 2);
	private final static IntegerRange outsideRangeRight = new IntegerRange(17, 20);
	private final static IntegerRange identicalRange = new IntegerRange(5, 15);

	@Test
	@DisplayName("Should correctly detect the cases where a range is contained within another")
	public void should_detect_contains_cases() {
		assertTrue(containingRange.contains(insideRange));
		assertTrue(containingRange.contains(leftRange));
		assertTrue(containingRange.contains(rightRange));
		assertTrue(containingRange.contains(identicalRange));
		assertFalse(containingRange.contains(partiallyContainedInLeft));
		assertFalse(containingRange.contains(partiallyContainedInRight));
		assertFalse(containingRange.contains(outsideRangeLeft));
		assertFalse(containingRange.contains(outsideRangeRight));
	}

	@Test
	@DisplayName("Should correctly detect the cases where a range is overlapping another")
	public void should_detect_overlap_cases() {
		assertTrue(containingRange.overlaps(partiallyContainedInLeft));
		assertTrue(containingRange.overlaps(partiallyContainedInRight));
		assertTrue(containingRange.overlaps(leftRange));
		assertTrue(containingRange.overlaps(rightRange));
		assertTrue(containingRange.overlaps(insideRange));
		assertTrue(containingRange.overlaps(identicalRange));
		assertFalse(containingRange.overlaps(outsideRangeLeft));
		assertFalse(containingRange.overlaps(outsideRangeRight));
	}
}
