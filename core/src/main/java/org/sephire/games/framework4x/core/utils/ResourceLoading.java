package org.sephire.games.framework4x.core.utils;

public class ResourceLoading {

	/**
	 * Given a path specified in package syntax, transforms it
	 * to resource folder syntax.
	 *
	 * @param path
	 * @return
	 */
	public static String packageToResourceSyntax(String path) {
		return path.replaceAll("\\.", "/");
	}
}
