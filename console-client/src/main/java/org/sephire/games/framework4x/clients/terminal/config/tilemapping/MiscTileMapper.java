package org.sephire.games.framework4x.clients.terminal.config.tilemapping;

import io.vavr.control.Option;
import org.sephire.games.framework4x.core.model.map.items.Misc;

public class MiscTileMapper implements ItemTileMapper<Misc> {
	@Override
	public Option<TileMapping> getMappingFor(Misc misc) {
		return null;
	}
}
