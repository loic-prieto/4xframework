package org.sephire.games.framework4x.core.plugins.map;

import io.vavr.collection.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sephire.games.framework4x.core.Game;

public class PluginMapGenerationTest {
	private static final String DUMMY_PLUGIN_NAME = "org.sephire.games.framework4x.testing.dummyPlugin";
	@Test
	@DisplayName("When loading a plugin, should detect the map provider if any")
	public void should_load_provider_if_exists(){
		new Game.Builder().withPlugins(List.of(DUMMY_PLUGIN_NAME));
	}
}
