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

import org.sephire.games.framework4x.core.model.config.ConfigKeyEnum;

/**
 * Configuration keys used by the console client.
 */
public enum ConsoleClientConfigKeyEnum implements ConfigKeyEnum {
	/**
	 * <p>Holds a list of elements to be positioned in the bottom bar.</p>
	 * <p>Signature: Map&lt;BottomBarPosition,List&lt;BottomBarElement&gt;&gt;</p>
	 */
	BOTTOM_BAR_ELEMENTS,
	/**
	 * Holds the configuration for the terrain->character mapping for the terminal client.
	 */
	TERRAIN_CHARACTER_MAPPING;
}
