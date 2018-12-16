package org.sephire.games.framework4x.core.plugins.map;

import lombok.Getter;
import org.sephire.games.framework4x.core.Framework4XException;

import static java.lang.String.format;

/**
 * This exception is thrown when loading a map provider, but it is invalid.
 * For example: it may have no map generators, or the signature of the map generator methods is not the
 * expected one.
 */
public class InvalidMapProviderException extends Framework4XException {
	@Getter
	private Class<?> providerClass;

	public InvalidMapProviderException(Class<?> providerClass,String cause) {
		super(format("Invalid map provider for class %s: %s",providerClass.getName(),cause));
		this.providerClass = providerClass;
	}
}
