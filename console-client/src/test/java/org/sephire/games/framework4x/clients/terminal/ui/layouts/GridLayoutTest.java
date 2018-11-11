package org.sephire.games.framework4x.clients.terminal.ui.layouts;

import io.vavr.collection.HashMap;
import org.junit.jupiter.api.Test;
import org.sephire.games.framework4x.clients.terminal.ui.components.Container;
import org.sephire.games.framework4x.clients.terminal.ui.layouts.gridlayout.Builder;
import org.sephire.games.framework4x.core.model.map.Location;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.sephire.games.framework4x.clients.terminal.ui.layouts.gridlayout.GridLayoutParameterKeys.CELL;
import static org.sephire.games.framework4x.clients.terminal.ui.mocks.TestContainer.Builder.container;
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
		Container container = container(1,1,28,6)
				.withLayout(Builder.ofBase(4,4).build())
				.build();

		container.addChild(buildUIElement(4, 1, 4, 2),HashMap.of(CELL,new Location(1,1)));
		container.addChild(buildUIElement(1, 2, 9, 2),HashMap.of(CELL,new Location(2,1)));
		container.addChild(buildUIElement(5, 2, 2, 2),HashMap.of(CELL,new Location(1,2)));
		container.addChild(buildUIElement(5, 2, 3, 1),HashMap.of(CELL,new Location(2,2)));

		assertEquals(new Location(4, 1), container.getChildren().get(0).getCoordinates().getLocation());  // A
		assertEquals(new Location(15, 1), container.getChildren().get(1).getCoordinates().getLocation()); // B
		assertEquals(new Location(5, 5), container.getChildren().get(2).getCoordinates().getLocation());  // C
		assertEquals(new Location(20, 5), container.getChildren().get(3).getCoordinates().getLocation()); // D
	}

}
