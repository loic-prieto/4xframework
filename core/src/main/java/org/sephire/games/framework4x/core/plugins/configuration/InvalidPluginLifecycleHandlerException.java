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
package org.sephire.games.framework4x.core.plugins.configuration;

import io.vavr.control.Option;
import lombok.Getter;
import org.sephire.games.framework4x.core.Framework4XException;

import static java.lang.String.format;

/**
 * This exception is thrown when loading the plugin lifecycle handler for
 * a plugin, but it is found invalid.
 *
 * There may be many causes for a plugin lifecycle handler to be invalid:
 * - there can only be one per plugin
 * - there can only be one plugin loading hook method in the class
 * - there can only be one game loading hook method in the class
 * - the signature of the hook methods are invalid
 */
public class InvalidPluginLifecycleHandlerException extends Framework4XException {
	@Getter
	private Option<Class<?>> handlerClass;

	public InvalidPluginLifecycleHandlerException(Class<?> handlerClass,String cause) {
		super(format("While loading the plugin lifecycle handler for plugin %s there was an error: %s",handlerClass.getName(),cause));
		this.handlerClass= Option.of(handlerClass);
	}

	public InvalidPluginLifecycleHandlerException(String cause) {
		super(format("General error with the plugin lifecycle handler: %s",cause));
		this.handlerClass = Option.none();
	}
}
