package org.sephire.games.framework4x.core.plugins.map;

import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.collection.Traversable;
import lombok.Getter;
import org.sephire.games.framework4x.core.Framework4XException;
import org.sephire.games.framework4x.core.utils.FunctionalUtils.Reduce;

import static java.lang.String.format;

/**
 * This exception is thrown when wrapping a map provider for a plugin, but the wrapping fails.
 * This may be caused by a variety of unknown (as of yet) reasons.
 */
public class MapProviderWrappingException extends Framework4XException {
	@Getter
	private Traversable<Throwable> causes;

	public MapProviderWrappingException(Throwable cause) {
		super(format("The wrapping of a map provider failed because of %s",cause.getMessage()));
		this.causes = List.of(cause);
	}

	public MapProviderWrappingException(Traversable<Throwable> causes) {
		super(format("The wrapping of a map provider failed because of %s",causes
		  .map(Throwable::getMessage)
		  .reduce(Reduce.strings())));

		this.causes = causes;
	}
}
