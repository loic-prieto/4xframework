package org.sephire.games.framework4x.core;

import org.junit.jupiter.api.Test;
import org.sephire.games.framework4x.core.plugins.configuration.ConfigLoader;
import org.sephire.games.framework4x.core.plugins.configuration.PluginSpecMapping;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConfigTest {

	@Test
	public void testConfigLoad() {
		var mappingsFileLocation = "org/sephire/games/framework4x/testing/dummyPlugin/plugin.yaml";

		var pluginConfig = ConfigLoader
		  .getConfigFor(mappingsFileLocation,PluginSpecMapping.class);

		assertTrue(pluginConfig.isSuccess());
		assertEquals("org.sephire.games.framework4x.testing.dummyPlugin",pluginConfig.get().getName());
	}
}
