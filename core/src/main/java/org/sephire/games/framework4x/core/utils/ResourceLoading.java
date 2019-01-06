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
