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
package org.sephire.games.framework4x.core.plugins;

import lombok.Getter;
import org.sephire.games.framework4x.core.Framework4XException;

/**
 * This exception appears when a plugin spec is used to load a plugin, but the data inside the spec is invalid.
 * For example, the specified root package of the plugin doesn't exist.
 */
public class InvalidPluginSpecException extends Framework4XException {
	@Getter
	private String pluginName;

	public InvalidPluginSpecException(String message, String pluginName) {
		super(message);
		this.pluginName = pluginName;
	}

	public InvalidPluginSpecException(String pluginName, Throwable cause) {
		super("The given spec for the plugin " + pluginName + " is invalid", cause);
		this.pluginName = pluginName;
	}
}
