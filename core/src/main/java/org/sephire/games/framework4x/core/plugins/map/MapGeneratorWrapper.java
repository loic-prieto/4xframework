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
package org.sephire.games.framework4x.core.plugins.map;

import io.vavr.control.Try;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.model.map.GameMap;

/**
 * <p>This class wraps a map generator.</p>
 *
 * <p>A map generator is identified by its name and contains a function that builds a map given a configuration object.
 * This function or map generator is declared by a plugin, with help from the annotations @MapProvider and @MapGenerator.</p>
 *
 * <p>See {@link MapProvider} to check the whole explanation on how to use those classes.</p>
 *
 * <p>The MapGeneratorWrapper is the class that the core framework will use when calling the map generators.</p>
 */
@Getter
@EqualsAndHashCode(of = { "name" } )
public abstract class MapGeneratorWrapper {
	private String name;
	private String displayKey;

	public MapGeneratorWrapper(String name, String displayKey) {
		this.name = name;
		this.displayKey = displayKey;
	}

	/**
	 * <p>Gets the map from this generator, provided a game configuration.</p>
	 *
	 * @param configuration
	 * @return
	 */
	public abstract Try<GameMap> buildMap(Configuration configuration);
}
