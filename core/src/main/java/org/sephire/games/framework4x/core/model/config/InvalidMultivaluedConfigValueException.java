package org.sephire.games.framework4x.core.model.config;

import org.sephire.games.framework4x.core.Framework4XException;

/**
 * This exception is thrown when trying to add a value to an existing multivalued configuration in a Configuration.Builder,
 * but the container value class is not one of Seq, Set or Collection.
 */
public class InvalidMultivaluedConfigValueException extends Framework4XException {
	public InvalidMultivaluedConfigValueException(ConfigKeyEnum key,Class<?> containerClass) {
		super(new StringBuilder()
		  .append("Tried to add a value to a multivalued config key(").append(key).append("), ")
		  .append("but the type of the config value (").append(containerClass.getName()).append(") ")
		  .append("is not one of the accepted container types (io.vavr.collection.Seq,")
		  .append("io.vavr.collection.Set, java.util.Collection) or you tried to add a collection of values whose ")
		  .append("collection type differs from the type of the collection you provided to the method.")
		  .toString());
	}
}
