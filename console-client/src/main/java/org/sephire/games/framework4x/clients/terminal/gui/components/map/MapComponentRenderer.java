package org.sephire.games.framework4x.clients.terminal.gui.components.map;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.ComponentRenderer;
import com.googlecode.lanterna.gui2.TextGUIGraphics;
import io.vavr.collection.List;
import org.sephire.games.framework4x.clients.terminal.api.config.TerrainsMapping;
import org.sephire.games.framework4x.core.model.map.GameMap;
import org.sephire.games.framework4x.core.model.map.Location;
import org.sephire.games.framework4x.core.model.map.MapCell;

import static io.vavr.collection.List.range;
import static org.sephire.games.framework4x.clients.terminal.gui.components.map.FakeTerrainType.*;

/**
 * Is in charge of rendering the visible part of a game map.
 */
public class MapComponentRenderer implements ComponentRenderer<MapComponent> {

	private MapViewport viewport;
	private GameMap map;
	private TerrainsMapping mappings;
	private TerminalSize viewportSize;

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
		if(this.viewportSize == null){
			this.viewportSize = graphics.getSize();
		}

		range(0,viewportSize.getColumns()).forEach((x)->{
			range(0,viewportSize.getRows()).forEach((y)->{
				String cellChar = mappings.getMappings().get(
				  map.getCurrentZone().getCells().get(Location.of(x,y))
					.getOrElse(new MapCell(Location.of(x,y), FAKE))
					.getTerrainType()
				).getCharacter();

				graphics.putString(x,y,cellChar);
			});
		});
	}
}
