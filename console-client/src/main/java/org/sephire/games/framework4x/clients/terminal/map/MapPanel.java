package org.sephire.games.framework4x.clients.terminal.map;

import com.googlecode.lanterna.gui2.Panel;
import org.sephire.games.framework4x.core.model.map.GameMap;
import org.sephire.games.framework4x.core.model.map.MapZone;
import org.sephire.games.framework4x.core.model.map.Size;

import static io.vavr.collection.List.range;

public class MapPanel extends Panel {
	private GameMap map;
	private char[][] mapVisualization;

	public MapPanel(GameMap map) {
		this.map = map;
		buildMapVisualRepresentation();
	}

	private void buildMapVisualRepresentation() {
		MapZone currentZone = map.getCurrentZone();
		Size zoneSize = currentZone.getSize();
		this.mapVisualization = new char[zoneSize.getWidth()][zoneSize.getHeight()];

		range(0,zoneSize.getWidth()).forEach((x)->{
			range(0,zoneSize.getHeight()).forEach((y)->{
				mapVisualization[x][y] = ' ';
			});
		});
	}
}
