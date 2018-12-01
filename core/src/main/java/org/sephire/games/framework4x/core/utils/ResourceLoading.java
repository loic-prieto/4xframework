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
}
