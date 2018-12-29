package org.sephire.games.framework4x.core.plugins;

import io.vavr.collection.Set;
import lombok.Getter;
import org.sephire.games.framework4x.core.Framework4XException;

import static java.lang.String.format;
import static org.sephire.games.framework4x.core.utils.FunctionalUtils.Reduce.strings;

/**
 * This exception is thrown when loading a plugin, but the plugin is not found in the classpath.
 */
public class PluginsNotFoundException extends Framework4XException {
	@Getter
	private Set<String> pluginsNames;

	public PluginsNotFoundException(Set<String> pluginsNames) {
		super(format("The plugins %s were not found in the plugin folder",pluginsNames.reduce(strings())));
		this.pluginsNames = pluginsNames;
	}
}
