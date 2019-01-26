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

import io.vavr.Tuple2;
import io.vavr.collection.Set;
import lombok.Getter;
import org.sephire.games.framework4x.core.Framework4XException;
import org.sephire.games.framework4x.core.utils.FunctionalUtils.Reduce;

import static java.lang.String.format;

/**
 * This exception is thrown when a plugin declared a dependency on another plugin but this other
 * plugin is not on the plugin folder.
 */
public class ParentPluginsNotFoundException extends Framework4XException {
	@Getter
	private Set<Tuple2<String,String>> missingPlugins;

	public ParentPluginsNotFoundException(Set<Tuple2<String,String>> missingPlugins) {
		super(format("The following plugins have their dependencies missing: %s",
		  missingPlugins
			.map((tuple)-> format("%s : %s",tuple._1,tuple._2))
			.reduce(Reduce.strings())
		));
		this.missingPlugins = missingPlugins;
	}
}
