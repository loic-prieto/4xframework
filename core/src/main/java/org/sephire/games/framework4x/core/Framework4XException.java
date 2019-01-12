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
package org.sephire.games.framework4x.core;

/**
 * Base 4X Framework exception.
 * <p>
 * It is a runtime exception.
 */
public abstract class Framework4XException extends Throwable {

	public Framework4XException() {
	}

	public Framework4XException(String message) {
		super(message);
	}

	public Framework4XException(String message, Throwable cause) {
		super(message, cause);
	}

	public Framework4XException(Throwable cause) {
		super(cause);
	}

}
