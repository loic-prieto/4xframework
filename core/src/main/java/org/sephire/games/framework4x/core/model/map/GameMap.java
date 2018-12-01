package org.sephire.games.framework4x.core.model.map;

import io.vavr.collection.Map;
import io.vavr.control.Try;
import lombok.Getter;

/**
 * A map holds information of the different map zones the map universe is composed of.
 */
@Getter
public class GameMap {
	private Map<String, MapZone> zones;
	private MapZone currentZone;

	public Try<Void> setCurrentZone(String zoneName) {
		return Try.of(() -> {
			this.currentZone = zones.get(zoneName)
			  .getOrElseThrow(() -> new ZoneNotFoundException(zoneName));
			return null;
		});
	}
}
