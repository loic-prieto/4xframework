package org.sephire.games.framework4x.core.model.map.items;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Terrain extends BaseItem {
	@NonNull
	@Getter
	private TerrainType type;

}
