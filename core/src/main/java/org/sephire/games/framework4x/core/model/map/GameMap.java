/**
 * 4X Framework - Core library - The core library on which to base the game
 * Copyright © 2018 Loïc Prieto Dehennault (loic.sephiroth@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
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
	private Map<String, Zone> zones;

	private GameMap(Map<String, Zone> zones) {
		this.zones = zones;
	}

	public static GameMapBuilderZoneField builder() {
		return new Builder();
	}

	private static class Builder implements GameMapBuilderZoneField, GameMapBuilderBuild {
		private Map<String, Zone> zones = HashMap.empty();

		@Override
		public GameMapBuilderZoneField addZone(Zone zone) {
			this.zones = zones.put(zone.getName(),zone);
			return this;
		}


		@Override
		public Try<GameMap> build() {
			return Try.of(()->{
				if(zones.isEmpty()) {
					throw new IllegalArgumentException("There must be at least one zone");
				}

				return new GameMap(zones);
			});
		}
	}

	public interface GameMapBuilderZoneField {
		GameMapBuilderZoneField addZone(Zone zone);
	}

	public interface GameMapBuilderBuild {
		Try<GameMap> build();
	}
}

