package org.sephire.games.framework4x.clients.terminal.drawables;

import lombok.Getter;
import org.sephire.games.framework4x.clients.terminal.Drawable;
import org.sephire.games.framework4x.clients.terminal.config.tilemapping.TerrainTileMapper;
import org.sephire.games.framework4x.clients.terminal.config.tilemapping.TileMapping;
import org.sephire.games.framework4x.clients.terminal.ui.Painter;
import org.sephire.games.framework4x.core.model.map.items.Terrain;

public class TerrainTile implements Drawable {
	@Getter
	private Terrain terrain;
	@Getter
	private TileMapping tileMapping;

	public TerrainTile(TerrainTileMapper mappingConfig, Terrain terrain) {
		this.terrain = terrain;
		/*this.tileMapping = mappingConfig.getMappingFor(terrain.getType())
				.getOrElseThrow(ConfigException::new);*/
	}

	@Override
	public void draw(Painter painter) {
		/*
		painter.getViewport().getRelativePositionFor(terrain.getLocation())
				.peek((relativeLocation) -> {
					TextGraphics g = painter.getScreen().newTextGraphics();
					g.setForegroundColor(tileMapping.getColor());
					g.setCharacter(
							relativeLocation.getX(),
							relativeLocation.getY(),
							tileMapping.getCharacter().charValue());
				});*/
	}
}
