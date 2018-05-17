package org.sephire.games.framework4x.clients.terminal.ui;

import io.vavr.control.Option;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.sephire.games.framework4x.clients.terminal.Drawable;
import org.sephire.games.framework4x.core.model.map.Location;

/**
 * Represents a basic UI Element that can be drawn to the screen.
 */
@Getter
@Setter
public abstract class UIElement implements Drawable {
	@NonNull
	private Location location;
	@NonNull
	private Size size;
	private Option<Container> containerParent;
}
