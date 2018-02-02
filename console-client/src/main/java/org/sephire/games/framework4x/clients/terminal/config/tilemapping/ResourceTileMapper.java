package org.sephire.games.framework4x.clients.terminal.config.tilemapping;

import io.vavr.control.Option;
import org.sephire.games.framework4x.core.model.map.items.Resource;

public class ResourceTileMapper implements ItemTileMapper<Resource> {
	@Override
	public Option<TileMapping> getMappingFor(Resource resource) {
		return null;
	}
}
