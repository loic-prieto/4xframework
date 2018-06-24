package org.sephire.games.framework4x.clients.terminal.config.tilemapping;

import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import org.sephire.games.framework4x.core.model.map.Item;
import org.sephire.games.framework4x.core.model.map.items.Misc;
import org.sephire.games.framework4x.core.model.map.items.Resource;
import org.sephire.games.framework4x.core.model.map.items.Terrain;

public class TileMapper {
	private Map<Class<? extends Item>, ItemTileMapper<? extends Item>> mappers;

	public TileMapper() {
		mappers = HashMap.of(
				Misc.class, new MiscTileMapper(),
				Resource.class, new ResourceTileMapper(),
				Terrain.class, new TerrainTileMapper()
		);
	}

	public Option<TileMapping> getMappingFor(Item item) {
		/*getMapperFor(item.getClass())
				.map(mapper -> mapper.getMappingFor(item));*/


		return null;
	}

	private Option<ItemTileMapper<? extends Item>> getMapperFor(Class<? extends Item> itemClass) {
		Option<ItemTileMapper<? extends Item>> mapper = Option.none();

		if (itemClass.isAssignableFrom(Misc.class)) {
			mapper = mappers.get(Misc.class);
		} else if (itemClass.isAssignableFrom(Resource.class)) {
			mapper = mappers.get(Resource.class);
		} else if (itemClass.isAssignableFrom(Terrain.class)) {
			mapper = mappers.get(Terrain.class);
		}

		return mapper;
	}
}
