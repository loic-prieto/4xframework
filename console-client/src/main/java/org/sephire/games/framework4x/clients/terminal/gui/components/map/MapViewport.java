package org.sephire.games.framework4x.clients.terminal.gui.components.map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.sephire.games.framework4x.core.model.map.Location;
import org.sephire.games.framework4x.core.model.map.Size;

/**
 * Represents the camera viewport of the map panel. That is, what zone of the map
 * is visible, so as not to render not visible cells.
 */
@AllArgsConstructor
public class MapViewport {

	@Getter
	private Location cameraOffset;
	@Getter
	private Size size;

	public MapViewport() {
		this.cameraOffset = Location.of(0,0);
		this.size = new Size(0,0);
	}
}
