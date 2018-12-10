package org.sephire.games.framework4x.core.plugins;

import org.sephire.games.framework4x.core.Framework4XException;

/**
 * This exception is thrown when loading information from a plugin jar file, mainly
 * on the manifest, and either the manifest doesn't exist, or it does not contain 4X framework plugin entries.
 */
public class InvalidPluginJarException extends Framework4XException {
	public InvalidPluginJarException(String jarfileName,String cause) {
		super(String.format("The jar file %s does is not a valid plugin jar file: %s",jarfileName,cause));
	}
}
