package org.sephire.games.framework4x.core.model.config;

import lombok.Getter;
import org.sephire.games.framework4x.core.Framework4XException;

/**
 * This exception appears when there has been an error while parsing a resource file
 * from the classpath.
 */
public class InvalidResourceFileException extends Framework4XException {
	@Getter
	private String filename;

	public InvalidResourceFileException(String filename, Throwable cause) {
		super("There was an error while parsing the file " + filename + ": " + cause.getMessage(), cause);
		this.filename = filename;
	}
}
