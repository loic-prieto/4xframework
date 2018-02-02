package org.sephire.games.framework4x.clients.terminal.config.tilemapping;

import io.vavr.collection.Map;
import io.vavr.control.Option;
import org.sephire.games.framework4x.core.model.map.items.Terrain;
import org.sephire.games.framework4x.core.model.map.items.TerrainType;

public class TerrainTileMapper implements ItemTileMapper<Terrain> {
	private Map<TerrainType, TileMapping> mappings;

	@Override
	public Option<TileMapping> getMappingFor(Terrain terrain) {
		return mappings.get(terrain.getType());
	}

}
