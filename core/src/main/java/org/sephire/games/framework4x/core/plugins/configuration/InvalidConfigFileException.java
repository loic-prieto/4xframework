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
package org.sephire.games.framework4x.core.plugins.configuration;

import org.sephire.games.framework4x.core.Framework4XException;

/**
 * This exception is thrown when a config file is being parsed into a config file
 * and the parsing failed.
 */
public class InvalidConfigFileException extends Framework4XException {
	private String configFile;

	public InvalidConfigFileException(String configFile, Throwable reason) {
		super(String.format("The config file %s is syntactically invalid: %s", configFile, reason.getMessage()), reason);
		this.configFile = configFile;
	}
}
