package org.sephire.games.framework4x.plugins.standard;

import lombok.Getter;
import org.sephire.games.framework4x.core.model.map.TerrainTypeEnum;

public enum StandardTerrainTypes implements TerrainTypeEnum {
	MOUNTAIN("mountain"),
	HILL("hill"),
	FOREST("forest"),
	PLAIN("plain"),
	OCEAN("ocean"),
	DESERT("desert"),
	GLACIER("glacier");

	@Getter
	private String id;

	StandardTerrainTypes(String id) {
		this.id = id;
	}
}
