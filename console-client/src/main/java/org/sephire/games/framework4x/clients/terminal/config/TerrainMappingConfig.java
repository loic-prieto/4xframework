package org.sephire.games.framework4x.clients.terminal.config;

import io.vavr.collection.Map;
import io.vavr.control.Option;
import org.sephire.games.framework4x.core.model.map.items.TerrainType;

public class TerrainMappingConfig {
    private Map<TerrainType, TileMapping> mappings;

    public TerrainMappingConfig(Map<TerrainType, TileMapping> mappings) {
        this.mappings = mappings;
    }

    public Option<TileMapping> getMappingForTerrainType(TerrainType type) {
        return mappings.get(type);
    }
}
