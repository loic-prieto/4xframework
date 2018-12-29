package org.sephire.games.framework4x.core.plugins.map;

import io.vavr.control.Try;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.model.map.GameMap;

/**
 * <p>This class wraps a map generator.</p>
 *
 * <p>A map generator is identified by its name and contains a function that builds a map given a configuration object.
 * This function or map generator is declared by a plugin, with help from the annotations @MapProvider and @MapGenerator.</p>
 *
 * <p>See {@link MapProvider} to check the whole explanation on how to use those classes.</p>
 *
 * <p>The MapGeneratorWrapper is the class that the core framework will use when calling the map generators.</p>
 */
@Getter
@EqualsAndHashCode(of = { "name" } )
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
