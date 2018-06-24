package org.sephire.games.framework4x.clients.terminal.ui.layouts;

import org.junit.jupiter.api.Test;
import org.sephire.games.framework4x.clients.terminal.ui.Coordinates;
import org.sephire.games.framework4x.clients.terminal.ui.Painter;
import org.sephire.games.framework4x.clients.terminal.ui.components.Container;
import org.sephire.games.framework4x.clients.terminal.ui.components.UIElement;
import org.sephire.games.framework4x.clients.terminal.ui.size.QualifiedSizeValue;
import org.sephire.games.framework4x.clients.terminal.ui.size.Size;
import org.sephire.games.framework4x.core.model.map.Location;

import static io.vavr.collection.List.range;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.sephire.games.framework4x.clients.terminal.ui.size.SizeUnit.CHARACTER;

public class FlowLayoutTest {

	@Test
	public void testHorizontalLayoutWithoutPadding() {
		Container container = new TestContainer();
		container.addChildren(range(0, 4).map((index) -> buildUIElement(10, 1)));
		Layout layout = new FlowLayout(container);
		layout.updateChildrenCoordinates();

		UIElement e3 = container.getChildren().get(2);
		assertEquals(23, e3.getCoordinates().getLocation().getX());
		assertEquals(1, e3.getCoordinates().getLocation().getY());
	}

	/**
	 * Convenience method to build ui elements for flow layout testing.
	 * Location is set to 0,0
	 *
	 * @param width
	 * @param height
	 * @return
	 */
	private UIElement buildUIElement(int width, int height) {
		UIElement element = new TestUIElement();
		element.setCoordinates(new Coordinates(
				new Location(0, 0),
				new Size(
						new QualifiedSizeValue(width, CHARACTER),
						new QualifiedSizeValue(10, CHARACTER))
		));

		return element;
	}

	/**
	 * A basic test container that does nothing but contain items.
	 */
	private class TestContainer extends Container {
		@Override
		public void draw(Painter painter) {
			// do nothing
		}
	}

	/**
	 * A basic test ui element that does not even draw itself.
	 */
	private class TestUIElement extends UIElement {
		@Override
		public void draw(Painter painter) {
			//do nothing
		}
	}
}
