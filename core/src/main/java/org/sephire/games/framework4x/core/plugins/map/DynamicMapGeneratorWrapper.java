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
package org.sephire.games.framework4x.core.plugins.map;

import io.vavr.Function1;
import io.vavr.control.Try;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.model.map.GameMap;

/**
 * <p>A dynamic map generator wrapper wraps a function that is used to generate a map dynamically at game creation time.</p>
 *
 * <p>This function comes from a method annotated with &#64;MapGenerator inside a class annotated with &#64;MapProvider</p>
 */
public class DynamicMapGeneratorWrapper extends MapGeneratorWrapper {

	private Function1<Configuration, Try<GameMap>> generator;

	public DynamicMapGeneratorWrapper(String name, String displayKey, Function1<Configuration, Try<GameMap>> generator) {
		super(name, displayKey);
		this.generator = generator;
	}

	@Override
	public Try<GameMap> buildMap(Configuration configuration) {
		return generator.apply(configuration);
	}


}
