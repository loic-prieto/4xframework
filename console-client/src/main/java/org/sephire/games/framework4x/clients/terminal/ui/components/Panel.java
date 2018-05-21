package org.sephire.games.framework4x.clients.terminal.ui.components;

import io.vavr.collection.List;
import io.vavr.control.Option;
import org.sephire.games.framework4x.clients.terminal.ui.Coordinates;
import org.sephire.games.framework4x.clients.terminal.ui.Painter;
import org.sephire.games.framework4x.clients.terminal.ui.Viewport;
import org.sephire.games.framework4x.clients.terminal.ui.size.Size;
import org.sephire.games.framework4x.core.model.map.Location;

import static org.sephire.games.framework4x.clients.terminal.ui.components.viewport.ViewportVisibility.NON_VISIBLE;

/**
 * Represents a panel which contains child elements.
 * Can contain a border, in which case the viewport will have a reduced
 * size to make place for the border.
 */
public class Panel extends Container {

	private int borderSize;

	public Panel(Coordinates coordinates, int borderSize, UIElement... childElements) {
		this.setCoordinates(coordinates);
		this.borderSize = borderSize;
		this.setChildren(List.of(childElements));
		Coordinates viewportCoordinates = new Coordinates(new Location(0, 0), coordinates.getSize());
		this.setViewport(new Viewport(viewportCoordinates, this));

		// Modify the size of the component by adding the border size
		if (hasBorder()) {
			Size borderedSize = this.getCoordinates().getSize().addSize(borderSize, borderSize);
			this.setCoordinates(this.getCoordinates().withSize(borderedSize));
		}
	}

	@Override
	public void draw(Painter painter) {
		//First paint the panel itself
		drawBorderBox(painter);

		//Then paint each of the visible children
		this.getChildren()
				.filter(child -> getViewport().coordinatesVisibility(child.getCoordinates()) != NON_VISIBLE)
				.forEach(child -> child.draw(painter));
	}

	@Override
	public Option<Location> getScreenLocationFor(Location childLocation) {
		// In a Panel, the location of a child element is modified by the width and
		// height of the border if present.
		// For example, if a panel with coordinates (1,1) has a one-width border,
		// a child element of 1x1 size located at the (3,4) coordinate within the the panel,
		// will have a screen coordinate of (4,5).
		return super.getScreenLocationFor(childLocation)
				.map(location -> location.add(borderSize, borderSize));
	}

	/**
	 * Draw the border box, using unicode characters from:
	 * https://en.wikipedia.org/wiki/Box-drawing_character
	 * <p>
	 * We're using the "BOX DRAWINGS LIGHT X" characters
	 *
	 * @param painter
	 */
	private void drawBorderBox(Painter painter) {
		if (hasBorder()) {
			int x1, y1, x2, y2;
			Viewport vp = getViewport();
			x1 = getCoordinates().getLocation().getX();
			y1 = getCoordinates().getLocation().getY();
			x2 = x1 + getCoordinates().getSize().getWidth();
			y2 = y1 + getCoordinates().getSize().getHeight();
			painter.drawChar(x1, y1, '┌', vp);
			painter.drawChar(x2, y1, '┐', vp);
			painter.drawChar(x1, y2, '└', vp);
			painter.drawChar(x2, y2, '┘', vp);
			List.ofAll(y1, y2).forEach(row -> {
				List.range(x1, x2).forEach(column -> {
					painter.drawChar(column, row, '─', vp);
				});
			});
			List.ofAll(x1, x2).forEach(column -> {
				List.range(y1, y2).forEach(row -> {
					painter.drawChar(column, row, '│', vp);
				});
			});
		}
	}

	private boolean hasBorder() {
		return borderSize > 0;
	}
}
