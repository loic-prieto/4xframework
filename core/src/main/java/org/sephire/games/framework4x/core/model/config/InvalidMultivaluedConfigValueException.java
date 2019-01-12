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
package org.sephire.games.framework4x.core.model.config;

import org.sephire.games.framework4x.core.Framework4XException;

/**
 * This exception is thrown when trying to add a value to an existing multivalued configuration in a Configuration.Builder,
 * but the container value class is not one of Seq, Set or Collection.
 */
public class InvalidMultivaluedConfigValueException extends Framework4XException {
	public InvalidMultivaluedConfigValueException(ConfigKeyEnum key,Class<?> containerClass) {
		super(new StringBuilder()
		  .append("Tried to add a value to a multivalued config key(").append(key).append("), ")
		  .append("but the type of the config value (").append(containerClass.getName()).append(") ")
		  .append("is not one of the accepted container types (io.vavr.collection.Seq,")
		  .append("io.vavr.collection.Set, java.util.Collection) or you tried to add a collection of values whose ")
		  .append("collection type differs from the type of the collection you provided to the method.")
		  .toString());
	}
}
