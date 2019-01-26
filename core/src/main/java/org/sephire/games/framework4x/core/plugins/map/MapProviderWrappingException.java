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
package org.sephire.games.framework4x.core.plugins.map;

import io.vavr.collection.List;
import io.vavr.collection.Traversable;
import lombok.Getter;
import org.sephire.games.framework4x.core.Framework4XException;
import org.sephire.games.framework4x.core.utils.FunctionalUtils.Reduce;

import static java.lang.String.format;

/**
 * This exception is thrown when wrapping a map provider for a plugin, but the wrapping fails.
 * This may be caused by a variety of unknown (as of yet) reasons.
 */
public class MapProviderWrappingException extends Framework4XException {
	@Getter
	private Traversable<Throwable> causes;

	public MapProviderWrappingException(Throwable cause) {
		super(format("The wrapping of a map provider failed because of %s",cause.getMessage()));
		this.causes = List.of(cause);
	}

	public MapProviderWrappingException(Traversable<Throwable> causes) {
		super(format("The wrapping of a map provider failed because of %s",causes
		  .map(Throwable::getMessage)
		  .reduce(Reduce.strings())));

		this.causes = causes;
	}
}
