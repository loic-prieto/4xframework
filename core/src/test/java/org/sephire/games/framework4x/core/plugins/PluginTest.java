package org.sephire.games.framework4x.core.plugins;

import io.vavr.control.Option;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.model.config.CoreConfigKeyEnum;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.sephire.games.framework4x.testing.testPlugin1.TestPlugin1ConfigKeys.TEST_VALUE;

public class PluginTest {

	private static final String TEST1_PLUGIN_NAME = "org.sephire.games.framework4x.testing.testPlugin1";

	@Test
	@DisplayName("Given a valid spec, a plugin should load successfully")
	public void should_load_successfully() {
		var pluginSpec = new PluginSpec(TEST1_PLUGIN_NAME, TEST1_PLUGIN_NAME, Option.none());
		var configuration = Configuration.builder();
		var pluginLoadTry = Plugin.from(pluginSpec, configuration);

		assertTrue(pluginLoadTry.isSuccess());
	}

	@Test
	@DisplayName("Given a valid spec, a plugin should put its post load configuration correctly")
	public void should_load_configuration_successfully() {
		var pluginSpec = new PluginSpec(TEST1_PLUGIN_NAME, TEST1_PLUGIN_NAME, Option.none());
		var configuration = Configuration.builder();
		var pluginLoadTry = Plugin.from(pluginSpec, configuration);

		assertTrue(pluginLoadTry.isSuccess());
		assertTrue(configuration.getConfig(TEST_VALUE).isDefined());
		assertEquals("someValue", configuration.getConfig(TEST_VALUE).get());
	}

	@Test
	@DisplayName("Should load terrain types successfully when loading a plugin with terrain data")
	public void should_load_terrain_types_successfully() {
		var pluginSpec = new PluginSpec(TEST1_PLUGIN_NAME, TEST1_PLUGIN_NAME, Option.none());
		var configuration = Configuration.builder();
		var pluginLoadTry = Plugin.from(pluginSpec, configuration);

		assertTrue(pluginLoadTry.isSuccess());
		assertTrue(configuration.getConfig(CoreConfigKeyEnum.TERRAIN_TYPES).isDefined());
	}

	@Test
	@DisplayName("Given a spec with an invalid package root, should complain when loading plugin")
	public void should_complain_when_package_does_not_exist() {
		var pluginSpec = new PluginSpec("invalidName", "invalidPackage", Option.none());
		var pluginLoadTry = Plugin.from(pluginSpec, Configuration.builder());

		assertTrue(pluginLoadTry.isFailure());
		assertTrue(pluginLoadTry.getCause().getClass().isAssignableFrom(InvalidPluginSpecFileException.class));
	}
}
