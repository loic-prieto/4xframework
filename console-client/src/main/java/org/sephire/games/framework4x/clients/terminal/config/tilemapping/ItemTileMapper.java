package org.sephire.games.framework4x.clients.terminal.config.tilemapping;

import io.vavr.control.Option;
import org.sephire.games.framework4x.core.model.map.Item;

public interface ItemTileMapper<ITEM extends Item> {
	Option<TileMapping> getMappingFor(ITEM item);
}
