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
package org.sephire.games.framework4x.testing.testPlugin2;

import io.vavr.control.Try;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.model.map.GameMap;
import org.sephire.games.framework4x.core.plugins.map.MapGenerator;
import org.sephire.games.framework4x.core.plugins.map.MapProvider;

@MapProvider
public class Plugin2Maps {

	@MapGenerator(name = "plugin2.map_generator_1",displayKey = "plugin2.map_generators.1.displayName")
	public Try<GameMap> buildRandomMap1(Configuration configuration) {
		return Try.success(null);
	}

	@MapGenerator(name = "plugin2.map_generator_2",displayKey = "plugin2.map_generators.2.displayName")
	public Try<GameMap> buildRandomMap2(Configuration configuration) {
		return Try.success(null);
	}
}

