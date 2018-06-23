package org.sephire.games.framework4x.clients.terminal.ui.layouts;

import io.vavr.collection.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.sephire.games.framework4x.clients.terminal.ui.Coordinates;
import org.sephire.games.framework4x.clients.terminal.ui.components.Container;
import org.sephire.games.framework4x.clients.terminal.ui.components.UIElement;
import org.sephire.games.framework4x.clients.terminal.ui.layouts.flowlayout.Direction;
import org.sephire.games.framework4x.clients.terminal.utils.MutableIntCounter;
import org.sephire.games.framework4x.clients.terminal.utils.MutableValue;

import static org.sephire.games.framework4x.clients.terminal.ui.layouts.flowlayout.Direction.HORIZONTAL;

/**
 * The flow layout lays the children items of the container one after the other
 * be it, horizontally or vertically.
 * A Flow layout only adjusts coordinate location of the children, not the size.
 * <p>
 * A padding may be specified to put between each element.
 */
@RequiredArgsConstructor
@AllArgsConstructor
public class FlowLayout {
	@NonNull
	@Getter
	private Container container;
	@NonNull
	@Getter
	private Direction direction;
	@Getter
	private int padding = 0;

	/**
	 * Will update the coordinates of the children inside the container.
	 * This modifies the children coordinates.
	 * The flow layout will only adjust location, not size.
	 */
	public void updateChildrenCoordinates() {
		List<UIElement> children = container.getChildren();

		if (direction == HORIZONTAL) {
			MutableIntCounter rightmostPosition = new MutableIntCounter();
			MutableValue<Integer> modifiedPadding = new MutableValue<>(0);

			children.forEach((child) -> {
				Coordinates updatedCoordinates = child.getCoordinates().withLocation(
						child.getCoordinates().getLocation()
								.add(rightmostPosition.getValue() + modifiedPadding.getValue(), 0)
				);
				child.setCoordinates(updatedCoordinates);
				// The first element does not have padding
				modifiedPadding.updateValue(padding);
				rightmostPosition.incrementValue(child.getCoordinates().getSize().getWidth().getValue() + padding);
			});

		} else {
			// On vertical orientation the location is based on the index
			// of the item.
			MutableValue<Integer> modifiedPadding = new MutableValue<>(0);

			children.zipWithIndex().forEach((childWithIndex) -> {
				Coordinates updatedCoordinates = childWithIndex._1.getCoordinates().withLocation(
						childWithIndex._1.getCoordinates().getLocation().add(0, childWithIndex._2 * modifiedPadding.getValue())
				);
				childWithIndex._1.setCoordinates(updatedCoordinates);
				modifiedPadding.updateValue(padding);
			});
		}
	}
}
