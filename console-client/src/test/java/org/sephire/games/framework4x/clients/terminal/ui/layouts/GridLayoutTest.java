package org.sephire.games.framework4x.clients.terminal.ui.layouts;

import org.junit.jupiter.api.Test;
import org.sephire.games.framework4x.clients.terminal.ui.components.Container;
import org.sephire.games.framework4x.clients.terminal.ui.layouts.gridlayout.Builder;
import org.sephire.games.framework4x.clients.terminal.ui.mocks.TestContainer;
import org.sephire.games.framework4x.core.model.map.Location;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.sephire.games.framework4x.clients.terminal.ui.mocks.TestUIElement.buildUIElement;

public class GridLayoutTest {

	/**
	 * We will test the following layout
	 *     12345678901234 56789012345678
	 *    *******************************
	 * 1  *   AAAA       *BBBBBBBBB     *
	 * 2  *   AAAA       *BBBBBBBBB     *
	 * 3  *              *              *
	 *    *******************************
	 * 4  *              *              *
	 * 5  *    CC        *     DDD      *
	 * 6  *    CC        *              *
	 *    *******************************
	 *
	 * The separations do not exist, it is just to visualize them in the test
	 */
	@Test
	public void basicGridLayoutTest() {
		Container container = new TestContainer();
		Layout layout = Builder.ofBase(4, 4);
		container.setLayout(layout);
		container.addChild(buildUIElement(4, 1, 4, 2));
		container.addChild(buildUIElement(1, 2, 9, 2));
		container.addChild(buildUIElement(5, 2, 2, 2));
		container.addChild(buildUIElement(5, 2, 3, 1));

		layout.updateChildrenCoordinates();

		assertEquals(new Location(4, 1), container.getChildren().get(0).getCoordinates().getLocation());
		assertEquals(new Location(15, 1), container.getChildren().get(1).getCoordinates().getLocation());
		assertEquals(new Location(5, 5), container.getChildren().get(2).getCoordinates().getLocation());
		assertEquals(new Location(20, 5), container.getChildren().get(3).getCoordinates().getLocation());
	}

}
