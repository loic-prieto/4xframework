package org.sephire.games.framework4x.testing.testPlugin2;

import io.vavr.control.Try;
import org.sephire.games.framework4x.core.model.map.GameMap;
import org.sephire.games.framework4x.core.plugins.map.MapProvider;

@MapProvider
public class Plugin2Maps {

	public Try<GameMap> buildRandomMap() {
		return Try.success(null);
	}
}

