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
package org.sephire.games.framework4x.core.plugins;

import lombok.Getter;

/**
 * An enumeration of all core resources types, to be loaded automatically
 * when initializing a plugin.
 **/
public enum CoreResourcesTypes {

	/**
	 * Data related to the terrain types
	 **/
	TERRAIN_TYPES("terrains-types.yaml");

	@Getter
	private String fileName;

	CoreResourcesTypes(String fileName) {
		this.fileName = fileName;
	}
}
