package org.sephire.games.framework4x.core.model.map;

import lombok.Value;

/**
 * Holds information about a map cell, which is every item slot that exists inside a 3D map.
 */
@Value
public class MapCell {
	private Location location;
	private TerrainTypeEnum terrainType;
}
