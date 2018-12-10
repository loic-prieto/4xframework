package org.sephire.games.framework4x.clients.terminal.gui.components.map;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.AbstractComponent;
import com.googlecode.lanterna.gui2.ComponentRenderer;
import org.sephire.games.framework4x.clients.terminal.api.config.TerrainsMapping;
import org.sephire.games.framework4x.core.model.map.GameMap;
import org.sephire.games.framework4x.core.model.map.Location;

import static org.sephire.games.framework4x.clients.terminal.utils.Terminal.sizeFromTerminalSize;

/**
 * A component that draws the current zone of the game map,
 * can be embedded in any container.
 */
public class MapComponent extends AbstractComponent<MapComponent> {

	private MapViewport viewport;
	private GameMap map;
	private TerrainsMapping mappings;

	public MapComponent(GameMap map, TerrainsMapping mappings) {
		this.viewport = new MapViewport();
		this.map = map;
		this.mappings = mappings;
	}

	@Override
	protected ComponentRenderer<MapComponent> createDefaultRenderer() {
		return new MapComponentRenderer(viewport,map,mappings);
	}
}
