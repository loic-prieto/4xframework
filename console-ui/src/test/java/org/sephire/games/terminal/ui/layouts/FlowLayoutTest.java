package org.sephire.games.terminal.ui.layouts;

import org.junit.jupiter.api.Test;
import org.sephire.games.framework4x.clients.terminal.ui.components.Container;
import org.sephire.games.framework4x.clients.terminal.ui.components.UIElement;
import org.sephire.games.framework4x.clients.terminal.ui.layouts.flowlayout.Direction;

import static io.vavr.collection.List.range;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.sephire.games.terminal.ui.mocks.TestContainer.Builder.container;
import static org.sephire.games.terminal.ui.mocks.TestUIElement.buildUIElement;

public class FlowLayoutTest {

	/**
	 * The characters below are just to have a visual of the results we're expecting
	 * They only make sense for monospaced fonts
	 *
	 * xxxxxxxxxx  xxxxxxxxxx  xxxxxxxxxx  xxxxxxxxxx
	 * 12345678901234567890123456789012345678901234567890
	 * xxxxxxxxxxyyyyyyyyyyccccccccccvvvvvvvvvv
	 *
	 * 1 xxxxxxxxxx
	 * 2
	 * 3
	 * 4 xxxxxxxxxx
	 * 5
	 * 6
	 * 7 xxxxxxxxxx
	 * 8
	 * 9
	 * 0 xxxxxxxxxx
	 */


	@Test
	public void testHorizontalLayoutWithoutPadding() {
		Container container = container().withLayout(new FlowLayout()).build();
		container.addChildren(range(0, 4).map((index) -> buildUIElement(10, 1)));

		UIElement e3 = container.getChildren().get(2);
		assertEquals(21, e3.getCoordinates().getLocation().getX());
		assertEquals(1, e3.getCoordinates().getLocation().getY());
	}

	@Test
	public void testVerticalLayoutWithoutPadding() {
		Container container = container().withLayout(new FlowLayout(Direction.VERTICAL)).build();
		container.addChildren(range(0, 4).map((index) -> buildUIElement(10, 1)));

		UIElement e3 = container.getChildren().get(2);
		assertEquals(1, e3.getCoordinates().getLocation().getX());
		assertEquals(3, e3.getCoordinates().getLocation().getY());
	}

	@Test
	public void testHorizontalLayoutWithPadding() {
		Container container = container().withLayout(new FlowLayout(Direction.HORIZONTAL,2)).build();
		container.addChildren(range(0, 4).map((index) -> buildUIElement(10, 1)));

		UIElement e3 = container.getChildren().get(2);
		assertEquals(25, e3.getCoordinates().getLocation().getX());
		assertEquals(1, e3.getCoordinates().getLocation().getY());
	}

	@Test
	public void testVerticalLayoutWithPadding() {
		Container container = container().withLayout(new FlowLayout(Direction.VERTICAL,2)).build();
		container.addChildren(range(0, 4).map((index) -> buildUIElement(10, 1)));

		UIElement e3 = container.getChildren().get(2);
		assertEquals(1, e3.getCoordinates().getLocation().getX());
		assertEquals(7, e3.getCoordinates().getLocation().getY());
	}

}
