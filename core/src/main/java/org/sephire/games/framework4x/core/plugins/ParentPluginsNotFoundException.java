package org.sephire.games.framework4x.core.plugins;

import io.vavr.Tuple2;
import io.vavr.collection.Set;
import lombok.Getter;
import org.sephire.games.framework4x.core.Framework4XException;
import org.sephire.games.framework4x.core.utils.FunctionalUtils;
import org.sephire.games.framework4x.core.utils.FunctionalUtils.Reduce;

import static java.lang.String.format;

/**
 * This exception is thrown when a plugin declared a dependency on another plugin but this other
 * plugin is not on the plugin folder.
 */
public class ParentPluginsNotFoundException extends Framework4XException {
	@Getter
	private Set<Tuple2<String,String>> missingPlugins;

	public ParentPluginsNotFoundException(Set<Tuple2<String,String>> missingPlugins) {
		super(format("The following plugins have their dependencies missing: %s",
		  missingPlugins
			.map((tuple)-> format("%s : %s",tuple._1,tuple._2))
			.reduce(Reduce.strings())
		));
		this.missingPlugins = missingPlugins;
	}
}
