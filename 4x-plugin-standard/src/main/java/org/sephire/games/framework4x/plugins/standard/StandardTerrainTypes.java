/**
 * 4X Framework - Standard plugin - The standard base plugin for a game with a civ-like approach
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
