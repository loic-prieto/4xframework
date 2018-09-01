package org.sephire.games.framework4x.clients.terminal.ui.components;

import io.vavr.control.Option;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.sephire.games.framework4x.clients.terminal.Drawable;
import org.sephire.games.framework4x.clients.terminal.ui.Coordinates;
import org.sephire.games.framework4x.clients.terminal.ui.size.Size;
import org.sephire.games.framework4x.core.model.map.Location;

import java.util.UUID;

import static org.sephire.games.framework4x.clients.terminal.ui.size.QualifiedSizeValue.FIXED_SIZE_ONE;

/**
 * Represents a basic UI Element that can be drawn to the screen.
 */
@Getter
@Setter
@EqualsAndHashCode(of = "identifier" )
public abstract class UIElement implements Drawable {
	@NonNull
	private Coordinates coordinates;
	@NonNull
	private Option<Container> containerParent;
	@NonNull
	private UUID identifier;

	public UIElement() {
		this(new Coordinates(new Location(0, 0), new Size(FIXED_SIZE_ONE, FIXED_SIZE_ONE)), null);
	}

	public UIElement(Coordinates coordinates) {
		this(coordinates,null);
	}

	public UIElement(Coordinates coordinates,Container containerParent) {
		this.coordinates = coordinates;
		this.containerParent = Option.of(containerParent);
		this.identifier = UUID.randomUUID();
	}
}
