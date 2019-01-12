/**
 * 4X Framework - Core library - The core library on which to base the game
 * Copyright © 2018 Loïc Prieto Dehennault (loic.sephiroth@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.sephire.games.framework4x.core.model.map;

import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.control.Try;
import lombok.Getter;

import static java.lang.String.format;

/**
 * A map holds information of the different map zones the map universe is composed of.
 */
@Getter
public class GameMap {
	private Map<String, MapZone> zones;
	private MapZone currentZone;

	private GameMap(Map<String, MapZone> zones, MapZone currentZone) {
		this.zones = zones;
		this.currentZone = currentZone;
	}

	public Try<Void> setCurrentZone(String zoneName) {
		return Try.of(() -> {
			this.currentZone = zones.get(zoneName)
			  .getOrElseThrow(() -> new ZoneNotFoundException(zoneName));
			return null;
		});
	}

	public static GameMapBuilderZoneField builder() {
		return new Builder();
	}

	private static class Builder implements GameMapBuilderZoneField, GameMapBuilderBuild {
		private Map<String,MapZone> zones = HashMap.empty();
		private String defaultZone;

		@Override
		public GameMapBuilderZoneField addZone(MapZone zone) {
			this.zones = zones.put(zone.getName(),zone);
			return this;
		}

		@Override
		public GameMapBuilderBuild withDefaultZone(String zoneName) {
			this.defaultZone = zoneName;
			return this;
		}

		@Override
		public Try<GameMap> build() {
			return Try.of(()->{
				if(!zones.containsKey(defaultZone)) {
					throw new IllegalArgumentException(format("The zone %s does not exist",defaultZone));
				}

				return new GameMap(zones,zones.get(defaultZone).get());
			});
		}
	}

	public interface GameMapBuilderZoneField {
		GameMapBuilderZoneField addZone(MapZone zone);
		GameMapBuilderBuild withDefaultZone(String zoneName);
	}

	public interface GameMapBuilderBuild {
		Try<GameMap> build();
	}
}

