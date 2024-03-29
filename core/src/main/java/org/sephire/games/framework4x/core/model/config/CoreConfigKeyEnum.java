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
package org.sephire.games.framework4x.core.model.config;

import org.sephire.games.framework4x.core.model.config.userpreferences.UserPreferences;

public enum CoreConfigKeyEnum implements ConfigKeyEnum {
	/**
	 * This key holds the value for the configuration of cell types.
	 * These are to be defined by at least base plugins.
	 */
	CELL_TYPES,
	/**
	 * Under this key exists a list of game map generators that are available to choose when creating a game.
	 * Map generators may be dynamic, or fixed (as in programmatic or resource-based).
	 */
	MAPS,
	/**
	 * This key is to be used to store the game parameters as defined by the different plugins and
	 * given value by the user when creating a game.
	 */
	GAME_PARAMETERS,
	/**
	 * The key used to store I18N resources loaded from every plugin.
	 * The value is a Map of key-value I18N resources.
	 * By convention, the key should be: packageOfPlugin.restOfTheKey
	 */
	I18N,
	/**
	 * <p>The key under which to store all game commands as provided by plugins</p>
	 * <p>The stored type is {@link org.sephire.games.framework4x.core.model.game.GameCommands}</p>
	 */
	GAME_COMMANDS,
	/**
	 * <p>The list of civilizations available to be selected by the player when creating a game</p>
	 * <p>Map&lt;String,Civilization&gt;</p>
	 */
	CIVILIZATIONS,
	/**
	 * <p>Holds all configuration related to user preferences</p>
	 * <p>The stored type is {@link UserPreferences}</p>
	 */
	USER_PREFERENCES,
	/**
	 * <p>The list of plugins loaded into the current game</p>
	 * <p>The stored type is Set&lt;Plugin&gt;</p>
	 */
	LOADED_PLUGINS,
	/**
	 * Key under which to store all game initialize hook observers to be called when the game is starting.
	 * Plugins define game initialize hook observers.
	 */
	GAME_START_HOOKS;
}
