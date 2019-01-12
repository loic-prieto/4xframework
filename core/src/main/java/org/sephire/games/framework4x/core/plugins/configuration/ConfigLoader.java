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

import com.yacl4j.core.ConfigurationBuilder;
import com.yacl4j.core.source.optional.ConfigurationSourceNotAvailableException;
import io.vavr.control.Try;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.Predicates.instanceOf;

/**
 * Utility class to return configuration objects (from CFG4J) from
 * yaml files stored in the classpath.
 */
public class ConfigLoader {

	/**
	 * Given a yaml file name and a class to convert to, returns a parsed config class.
	 *
	 * Fields that are not found in the yaml document are set to null.
	 *
	 * May return:
	 *   - {@link ConfigFileNotFoundException}
	 *   - {@link InvalidConfigFileException}
	 * @param classpathFilename
	 * @param configClass
	 * @param <T>
	 * @return
	 */
	public static <T> Try<T> getConfigFor(String classpathFilename, Class<T> configClass) {
		return Try.of(() ->
		  ConfigurationBuilder.newBuilder()
			.source().fromFileOnClasspath(classpathFilename)
			.build(configClass))
		  .mapFailure(
			Case($(instanceOf(ConfigurationSourceNotAvailableException.class)), (e) -> new ConfigFileNotFoundException(classpathFilename)),
			Case($(instanceOf(IllegalStateException.class)), (e) -> new InvalidConfigFileException(classpathFilename, e))
		  );
	}

}
