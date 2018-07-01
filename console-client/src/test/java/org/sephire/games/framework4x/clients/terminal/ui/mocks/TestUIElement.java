package org.sephire.games.framework4x.clients.terminal.ui.mocks;

import org.sephire.games.framework4x.clients.terminal.ui.Coordinates;
import org.sephire.games.framework4x.clients.terminal.ui.Painter;
import org.sephire.games.framework4x.clients.terminal.ui.components.UIElement;
import org.sephire.games.framework4x.clients.terminal.ui.size.QualifiedSizeValue;
import org.sephire.games.framework4x.clients.terminal.ui.size.Size;
import org.sephire.games.framework4x.core.model.map.Location;

import static org.sephire.games.framework4x.clients.terminal.ui.size.SizeUnit.CHARACTER;

/**
 * A basic test ui element that does not even draw itself.
 */
public class TestUIElement extends UIElement {
	/**
	 * Convenience method to build ui elements for layout testing.
	 * Location is set to 1,1
	 *
	 * @param width
	 * @param height
	 * @return
	 */
	public static UIElement buildUIElement(int width, int height) {
		return buildUIElement(1, 1, width, height);
	}

	/**
	 * Convenience method to build ui elements for layout testing.
	 *
	 * @param width
	 * @param height
	 * @return
	 */
	public static UIElement buildUIElement(int x, int y, int width, int height) {
		UIElement element = new TestUIElement();
		element.setCoordinates(new Coordinates(
				new Location(x, y),
				new Size(
						new QualifiedSizeValue(height, CHARACTER),
						new QualifiedSizeValue(width, CHARACTER))
		));

		return element;
	}

	@Override
	public void draw(Painter painter) {
	}
}
