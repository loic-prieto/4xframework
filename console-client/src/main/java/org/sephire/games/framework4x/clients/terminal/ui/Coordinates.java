package org.sephire.games.framework4x.clients.terminal.ui;

import lombok.NonNull;
import lombok.Value;
import org.sephire.games.framework4x.clients.terminal.ui.size.Size;
import org.sephire.games.framework4x.core.model.map.Location;

@Value
public class Coordinates {
	@NonNull
	private Location location;
	@NonNull
	private Size size;

	public Coordinates withSize(Size newSize) {
		return new Coordinates(location, newSize);
	}

	public Coordinates withLocation(Location newLocation) {
		return new Coordinates(newLocation, size);
	}
}
