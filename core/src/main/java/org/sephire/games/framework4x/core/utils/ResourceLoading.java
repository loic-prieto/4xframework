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
package org.sephire.games.framework4x.core.utils;

public class ResourceLoading {

	/**
	 * Given a path specified in package syntax, transforms it
	 * to resource folder syntax.
	 *
	 * @param packagePath
	 * @return
	 */
	public static String packageToFolderPath(String packagePath) {
		return packagePath.replaceAll("\\.", "/");
	}

	/**
	 * When using reflection to search for classes inside a package, the search is done as a regexp. We need to add a point
	 * to the name of the package, so that packages that are named the same as this one but with more characters are not included.
	 * See Reflections.getTypesAnnotatedWith method to see what I mean.
	 *
	 * @param packageName
	 * @return
	 */
	public static String normalizePackageNameForReflection(String packageName) {
		return packageName.endsWith(".") ?
		  packageName :
		  packageName.concat(".");
	}

}
