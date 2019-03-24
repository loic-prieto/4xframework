package org.sephire.games.framework4x.clients.terminal.gui.components;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import io.vavr.Tuple;
import io.vavr.Tuple3;
import io.vavr.collection.List;
import io.vavr.control.Try;
import lombok.Getter;
import org.sephire.games.framework4x.clients.terminal.utils.IntegerRange;

import static java.lang.String.format;

/**
 * <p>This is a label that allows for specific characters of a string to have a different style from
 * the main one.</p>
 * <p>Use the builder to create a multi style label.</p>
 */
public class MultiStyleLabel extends Panel {

	@Getter
	private List<Label> labels;

	private MultiStyleLabel(String text, List<Tuple3<IntegerRange, TextColor, TextColor>> styledRanges) {
		this.setLayoutManager(new LinearLayout(Direction.HORIZONTAL).setSpacing(0));

		labels = this.buildLabelsFromRanges(styledRanges, text);
		labels.forEach(this::addComponent);
	}

	private static List<Label> buildLabelsFromRanges(List<Tuple3<IntegerRange, TextColor, TextColor>> styledRanges,String text) {
		return styledRanges
		  .map((tuple) -> {
			  var range = tuple._1;
			  var subText = text.substring(range.getStart(), range.getEnd());
			  var foregroundColor = tuple._2;
			  var backgroundColor = tuple._3;

			  return Tuple.of(subText, foregroundColor, backgroundColor);
		  })
		  .map((tuple) -> new Label(tuple._1).setForegroundColor(tuple._2).setBackgroundColor(tuple._3))
		  .collect(List.collector());
	}

	public static BuilderText builder() {
		return new Builder();
	}


	public interface BuilderStyles {
		Try<BuilderStyles> addStyledRange(IntegerRange range, TextColor foregroundColor, TextColor backgroundColor);

		default Try<BuilderStyles> addStyledRange(IntegerRange range) {
			return addStyledRange(range, null, null);
		}

		Try<MultiStyleLabel> build();
	}

	public interface BuilderText {
		BuilderStyles forText(String text, TextColor defaultForegroundColor, TextColor defaultBackgroundColor);

		default BuilderStyles forText(String text) {
			return forText(text, null, null);
		}
	}

	public static class Builder implements BuilderText, BuilderStyles {
		private List<Tuple3<IntegerRange, TextColor, TextColor>> styleRanges;
		private IntegerRange containingRange;
		private String text;
		private TextColor defaultForegroundColor;
		private TextColor defaultBackgroundColor;
		private boolean hasBorders;

		public Builder() {
			this.styleRanges = List.empty();
		}


		@Override
		public BuilderStyles forText(String text, TextColor defaultForegroundColor, TextColor defaultBackgroundColor) {
			if (text == null || "".equals(text)) {
				throw new IllegalArgumentException("The text cannot be empty");
			}
			this.text = text;
			this.defaultForegroundColor = defaultForegroundColor;
			this.defaultBackgroundColor = defaultBackgroundColor;
			this.containingRange = new IntegerRange(0, text.length());
			this.styleRanges = List.empty();

			return this;
		}

		@Override
		public Try<BuilderStyles> addStyledRange(IntegerRange range, TextColor foregroundColor, TextColor backgroundColor) {

			return Try.of(() -> {
				var finalForegroundColor = foregroundColor == null ? defaultForegroundColor : foregroundColor;
				var finalBackgroundColor = backgroundColor == null ? defaultBackgroundColor: backgroundColor;

				var canAddRange = containingRange.contains(range) && this.styleRanges.find((styledRange)->styledRange._1.overlaps(range)).isEmpty();
				if(!canAddRange) {
					throw new IllegalArgumentException(format("The given range %s is not valid for this label (%s)",range,text));
				}

				styleRanges = styleRanges.append(Tuple.of(range,finalForegroundColor,finalBackgroundColor));

				return this;
			});
		}

		@Override
		public Try<MultiStyleLabel> build() {
			return Try.of(() -> {
				// Build the full style range decomposition
				var fullStyledRanges = containingRange.fillRangeVoids(styleRanges.map(Tuple3::_1)).getOrElseThrow(t->t)
				  .map((range)->{
				  	var matchingStyledRange = styleRanges.find(sr->sr._1.equals(range));
				  	return matchingStyledRange.isEmpty() ?
					  Tuple.of(range,defaultForegroundColor,defaultBackgroundColor) :
					  matchingStyledRange.get();
				  });

				return new MultiStyleLabel(text, fullStyledRanges);
			});
		}
	}
}
