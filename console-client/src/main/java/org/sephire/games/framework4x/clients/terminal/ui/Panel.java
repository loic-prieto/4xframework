package org.sephire.games.framework4x.clients.terminal.ui;

import org.sephire.games.framework4x.core.model.map.Location;

/**
 * Represents a panel wich contains child elements.
 * Can contain a border, in which case the viewport will have a reduced
 * size to make place for the border.
 */
public class Panel extends Container {

	private boolean hasBorder;

	public Panel(boolean hasBorder, Location location, Size size, UIElement... childElements) {
		this.hasBorder = true;
		this.setLocation(location);

		if (hasBorder) {
			this.setViewport(new Viewport());
			this.setSize(size);
		} else {
			this.setSize(size);
		}
	}

	@Override
	public void draw(Painter painter) {

	}
}
