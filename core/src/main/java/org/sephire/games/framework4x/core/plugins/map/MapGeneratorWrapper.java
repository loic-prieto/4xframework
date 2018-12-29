package org.sephire.games.framework4x.core.plugins.map;

import io.vavr.control.Try;
import lombok.Getter;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.model.map.GameMap;

/**
 * This class wraps a map generator.
 * As of now, there are two
 */
@Getter
public abstract class MapGeneratorWrapper {
	private String name;
	private String displayKey;

	public MapGeneratorWrapper(String name, String displayKey) {
		this.name = name;
		this.displayKey = displayKey;
	}

	/**
	 * <p>Gets the map from this generator, provided a game configuration.</p>
	 *
	 * @param configuration
	 * @return
	 */
	public abstract Try<GameMap> buildMap(Configuration configuration);
}
