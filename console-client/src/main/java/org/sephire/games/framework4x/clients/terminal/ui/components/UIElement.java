package org.sephire.games.framework4x.clients.terminal.ui.components;

import io.vavr.control.Option;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.sephire.games.framework4x.clients.terminal.Drawable;
import org.sephire.games.framework4x.clients.terminal.ui.Coordinates;
import org.sephire.games.framework4x.clients.terminal.ui.Size;
import org.sephire.games.framework4x.core.model.map.Location;

/**
 * Represents a basic UI Element that can be drawn to the screen.
 */
@Getter
@Setter
public abstract class UIElement implements Drawable {
	@NonNull
	private Coordinates coordinates;
	@NonNull
	private Option<Container> containerParent;

	public UIElement() {
		this(new Coordinates(new Location(0,0),new Size(1,1)),null);
	}

	public UIElement(Coordinates coordinates,Container containerParent) {
		this.coordinates = coordinates;
		this.containerParent = Option.of(containerParent);
	}
}
