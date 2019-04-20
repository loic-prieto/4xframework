package org.sephire.games.framework4x.clients.terminal.utils;

import io.vavr.collection.List;
import io.vavr.control.Try;
import lombok.Value;

import java.util.Comparator;

/**
 * A simple semi open range of integers, from initialize to end,
 */
@Value
public class IntegerRange {
	private int start;
	private int end;

	public IntegerRange(int inclusiveStart, int exclusiveEnd) {
		this.start = inclusiveStart;
		this.end = exclusiveEnd;
	}

	/**
	 * Checks whether a given range overlaps with the current one
	 * @param other
	 * @return
	 */
	public boolean overlaps(IntegerRange other) {
		return other.start < this.end && other.end > this.start;
	}

	/**
	 * Checks wether this range contains the given one.
	 * @param other
	 * @return
	 */
	public boolean contains(IntegerRange other) {
		return this.start <= other.start && this.end >= other.end;
	}

	/**
	 * <p>Given a list of ranges, fills the voids between them and this range</p>
	 * <p>
	 *     For example, given the following ranges: [2,5), [6,7), [10,13)<br />
	 *     and given the self range [0,13)<br />
	 *     Returns the following list: [0,2),[2,5),[5,6),[6,7),[7,10),[10,13)
	 * </p>
	 * <p>For this method to work, the given ranges cannot overlap each other and must be contained within this range</p>
	 * @param ranges
	 * @return
	 */
	public Try<List<IntegerRange>> fillRangeVoids(List<IntegerRange> ranges) {
		return Try.of(()->{
			// Basic assumption validation
			var allRangesContained = ranges.forAll(this::contains);
			if (!allRangesContained)
				throw new IllegalArgumentException("The given contained ranges do not fit inside this range");
			var noOverlappingRanges = ranges.forAll((range) -> ranges.remove(range).find(range::overlaps).isEmpty());
			if (!noOverlappingRanges)
				throw new IllegalArgumentException("The contained ranges overlap between themselves");

			if(ranges.isEmpty()) return List.of(this);

			var fullRanges = List.<IntegerRange>empty();

			var orderedRanges = ranges.sortBy(new OrderByXAxisComparator(),r->r);
			if(orderedRanges.size() < 2) {
				fullRanges = fullRanges.append(orderedRanges.get(0));
				return fullRanges;
			}

			boolean startingPositionLowerThanRanges = this.start < ranges.get(0).start;
			if(startingPositionLowerThanRanges) {
				fullRanges = fullRanges.append(new IntegerRange(this.start,orderedRanges.get(0).start));
			}

			for(int i=0;i<orderedRanges.size()-1;i++){
				var range = orderedRanges.get(i);
				fullRanges = fullRanges.append(range);

				var nextRange = orderedRanges.get(i+1);
				var fillingRangeSize = nextRange.start - range.end;
				if(fillingRangeSize > 0){
					fullRanges = fullRanges.append(new IntegerRange(range.end,range.end+fillingRangeSize));
				}
			}
			fullRanges = fullRanges.append(orderedRanges.last());

			var endPositionGreaterThanLastRange = orderedRanges.last().end < this.end;
			if(endPositionGreaterThanLastRange) {
				fullRanges = fullRanges.append(new IntegerRange(orderedRanges.last().end,this.end));
			}

			return fullRanges;
		});
	}


	/**
	 * <p>With this comparator, an integer range is lower than another range if it is positioned before in the x axis.</p>
	 * <p>If the ranges are overlapping, a range is lower than another if it starts and ends before the other</p>
	 * <p>It is non-semantic to compare a range that contains another, since the order does not make sense as
	 * a concept</p>
	 */
	public static class OrderByXAxisComparator implements Comparator<IntegerRange> {
		@Override
		public int compare(IntegerRange first, IntegerRange second) {
			if(first.contains(second) || second.contains(first)) {
				throw new IllegalCompareOperationException(
				  "A range cannot be compared to another it contains when sorting by the position on the X axis");
			}

			var firstStartsFirst = first.start < second.start;
			var firstEndsFirst = first.end < second.end;

			if(firstStartsFirst && firstEndsFirst) return -1;
			else return 1;
		}
	}
}
