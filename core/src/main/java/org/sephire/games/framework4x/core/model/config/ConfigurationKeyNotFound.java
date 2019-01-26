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

import lombok.Getter;
import org.sephire.games.framework4x.core.Framework4XException;

@Getter
public class ConfigurationKeyNotFound extends Framework4XException {

	private ConfigKeyEnum notFoundKey;

	public ConfigurationKeyNotFound(ConfigKeyEnum notFoundKey) {
		super("The key '" + notFoundKey + "' doesn't exist in the configuration global object");
		this.notFoundKey = notFoundKey;
	}
}
