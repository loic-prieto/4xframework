package org.sephire.games.framework4x.core.plugins;

import io.vavr.control.Option;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sephire.games.framework4x.core.model.config.Configuration;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This test relates to the {@link Plugin} class, but the testing behaviour is
 * split between files so as to reduce the size of each plugin test file.
 *
 * Perhaps this is a smell that the plugin class does too much.
 */
public class PluginMapGeneratorsTest {

	private static final String MAP_GENERATOR_PLUGIN_NAME = "org.sephire.games.framework4x.testing.testPlugin2";

	@Test
	@DisplayName("When loading a plugin, maps generators should be loaded and available")
	public void should_create_map_generator_when_loading_plugin() {
		var pluginSpec = new PluginSpec(MAP_GENERATOR_PLUGIN_NAME,MAP_GENERATOR_PLUGIN_NAME, Option.none());
		var configuration = Configuration.builder();

		var pluginLoadingTry = Plugin.from(pluginSpec,configuration);

		assertTrue(pluginLoadingTry.isSuccess());
	}
}
