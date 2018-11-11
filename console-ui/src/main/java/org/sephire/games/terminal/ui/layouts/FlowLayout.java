package org.sephire.games.terminal.ui.layouts;

import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import org.sephire.games.framework4x.clients.terminal.ui.Coordinates;
import org.sephire.games.framework4x.clients.terminal.ui.components.UIElement;
import org.sephire.games.framework4x.clients.terminal.ui.layouts.flowlayout.Direction;
import org.sephire.games.framework4x.clients.terminal.utils.MutableIntCounter;
import org.sephire.games.framework4x.clients.terminal.utils.MutableValue;
import org.sephire.games.framework4x.core.model.map.Location;

import java.util.UUID;

import static org.sephire.games.framework4x.clients.terminal.ui.layouts.flowlayout.Direction.HORIZONTAL;

/**
 * The flow layout lays the children items of the container one after the other
 * be it, horizontally or vertically.
 * A Flow layout only adjusts coordinate location of the children, not the size.
 * <p>
 * A padding may be specified to put between each element.
 */
public class FlowLayout extends BaseLayout {
	private Direction direction = HORIZONTAL;
	private int padding = 0;

	public FlowLayout() {
		this(HORIZONTAL, 0);
	}

	public FlowLayout(Direction direction) {
		this(direction, 0);
	}

	public FlowLayout(Direction direction, int padding) {
		super(Option.none());
		this.direction = direction;
		this.padding = padding;
	}

	@Override
	public void updateChildrenCoordinates() {
		Map<UUID,Coordinates> updatedChildrenCoordinates = direction == HORIZONTAL ?
			getContainer().getOrElseThrow(LayoutMustHaveContainerException::new).getChildren()
				.map((child) -> {
					return HashMap.empty();
				}) :
			getContainer().getOrElseThrow(LayoutMustHaveContainerException::new).getChildren()
				.map((child) -> {

				});



		if (direction == HORIZONTAL) {
			MutableIntCounter rightmostPosition = new MutableIntCounter(1);
			// The first element does not have padding
			MutableValue<Integer> modifiedPadding = new MutableValue<>(0);

			children.forEach((child) -> {
				Coordinates updatedCoordinates = child.getCoordinates().withLocation(
						new Location(rightmostPosition.getValue(), 1)
				);
				child.setCoordinates(updatedCoordinates);
				modifiedPadding.updateValue(padding);
				rightmostPosition.incrementValue(child.getCoordinates().getSize().getWidth().getValue() + padding);
			});

		} else {
			// On vertical orientation the location is based on the index
			// of the item.
			MutableValue<Integer> modifiedPadding = new MutableValue<>(0);

			children.zipWithIndex().forEach((childWithIndex) -> {
				UIElement child = childWithIndex._1;
				int index = childWithIndex._2;

				Coordinates updatedCoordinates = child.getCoordinates().withLocation(
						new Location(1, index + modifiedPadding.getValue() * index + 1)
				);
				child.setCoordinates(updatedCoordinates);
				modifiedPadding.updateValue(padding);
			});
		}
	}
}
