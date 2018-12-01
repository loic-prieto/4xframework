package org.sephire.games.framework4x.core.plugins;

import io.vavr.collection.List;
import lombok.Getter;
import org.sephire.games.framework4x.core.Framework4XException;

public class PluginLoadingException extends Framework4XException {

	@Getter
	private List<Throwable> exceptions;

	public PluginLoadingException(List<Throwable> exceptions) {
		super(exceptions
		  .map(Throwable::getMessage)
		  .reduce((a, b) -> a + "\n" + b));
		this.exceptions = exceptions;
	}

	public PluginLoadingException(Throwable... exceptions) {
		this(List.of(exceptions));
	}

	public PluginLoadingException(String cause, Throwable... exceptions) {
		super(cause);
		this.exceptions = List.of(exceptions);
	}

	public PluginLoadingException(String cause) {
		super(cause);
		this.exceptions = List.empty();
	}
}
