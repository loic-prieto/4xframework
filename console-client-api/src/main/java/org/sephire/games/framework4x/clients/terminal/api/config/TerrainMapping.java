/**
 * 4X Framework - Console client - API - The console client offers an API that plugins that interact with the console can consume. This mainly
        avoid having the client and the related plugin have a cyclic dependency.
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
package org.sephire.games.framework4x.clients.terminal.api.config;

/**
 * Configuration value related to the mapping between a terrain type and its
 * character and color in the terminal client.
 * Used by yacl4j.
 */
public interface TerrainMapping {

	/**
	 * The unicode character to show on the console representing this terrain type.
	 * @return
	 */
	String getCharacter();

	/**
	 * The color to use to paint the character.
	 * @return
	 */
	String getColor();
}
