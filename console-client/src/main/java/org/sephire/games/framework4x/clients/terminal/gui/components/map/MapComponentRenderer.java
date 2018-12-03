package org.sephire.games.framework4x.clients.terminal.gui.components.map;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.ComponentRenderer;
import com.googlecode.lanterna.gui2.TextGUIGraphics;
import org.sephire.games.framework4x.clients.terminal.config.TerrainsMapping;
import org.sephire.games.framework4x.core.model.map.GameMap;

/**
 * Is in charge of rendering the visible part of a game map.
 */
public class MapComponentRenderer implements ComponentRenderer<MapComponent> {

	private MapViewport viewport;
	private GameMap map;
	private TerrainsMapping mappings;

	public MapComponentRenderer(MapViewport viewport, GameMap map, TerrainsMapping mappings) {
		this.viewport = viewport;
		this.map = map;
		this.mappings = mappings;
	}

	@Override
	public TerminalSize getPreferredSize(MapComponent component) {
		return new TerminalSize(viewport.getSize().getWidth(),viewport.getSize().getHeight());
	}

	@Override
	public void drawComponent(TextGUIGraphics graphics, MapComponent component) {
		graphics.putString(5,5,"LOL");
	}
}
