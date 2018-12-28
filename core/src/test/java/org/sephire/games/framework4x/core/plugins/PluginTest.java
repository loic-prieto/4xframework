package org.sephire.games.framework4x.core.plugins;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.model.config.CoreConfigKeyEnum;
import org.sephire.games.framework4x.core.plugins.Plugin;
import org.sephire.games.framework4x.core.plugins.PluginMainClassNotFoundException;
import org.sephire.games.framework4x.core.plugins.PluginSpecFileNotFound;
import org.sephire.games.framework4x.testing.dummyPlugin.DummyPluginConfigKeyEnum;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PluginTest {

	private static final String DUMMY_PLUGIN_NAME = "org.sephire.games.framework4x.testing.dummyPlugin";
	private static final String INVALID_DUMMY_PLUGIN_NAME = "org.sephire.games.framework4x.testing.invalidDummyPlugin";
	private static final String PLUGIN_WITHOUT_SPEC_FILE_NAME = "org.sephire.games.framework4x.testing.dummyPluginWithoutSpecFile";

	/*@Test
	@DisplayName("Should complain when main class of a plugin does not exist")
	public void should_complain_when_main_class_does_not_exist() {
		var pluginLoadingTry = Plugin.of(INVALID_DUMMY_PLUGIN_NAME);

		assertTrue(pluginLoadingTry.isFailure());
		assertTrue(pluginLoadingTry.getCause().getClass().isAssignableFrom(PluginMainClassNotFoundException.class));
	}

	@Test
	@DisplayName("Should complain when spec file doesn't exist while loading a plugin")
	public void should_complain_when_spec_file_doesnt_exist() {
		var pluginLoadTry = Plugin.of(PLUGIN_WITHOUT_SPEC_FILE_NAME);

		assertTrue(pluginLoadTry.isFailure());
		assertTrue(pluginLoadTry.getCause().getClass().isAssignableFrom(PluginSpecFileNotFound.class));
	}

	@Test
	@DisplayName("When all conditions are right, should load plugin successfully")
	public void should_load_successfully() {
		var pluginLoadTry = Plugin.of(DUMMY_PLUGIN_NAME);

		assertTrue(pluginLoadTry.isSuccess());
	}

	@Test
	@DisplayName("Should load terrain types successfully when loading a plugin with terrain data")
	public void should_load_terrain_types_successfully() {
		var pluginLoadTry = Plugin.of(DUMMY_PLUGIN_NAME);

		assertTrue(pluginLoadTry.isSuccess());

		Configuration.Builder configBuilder = new Configuration.Builder();
		pluginLoadTry.get().load(configBuilder);
		Configuration config = configBuilder.build();

		assertTrue(config.getConfiguration(CoreConfigKeyEnum.TERRAIN_TYPES).isDefined());
	}

	@Test
	@DisplayName("Should load custom config when loading plugin successfully")
	public void should_load_custom_config() {
		var pluginLoadTry = Plugin.of(DUMMY_PLUGIN_NAME);

		assertTrue(pluginLoadTry.isSuccess());

		Configuration.Builder configBuilder = new Configuration.Builder();
		pluginLoadTry.get().load(configBuilder);
		Configuration config = configBuilder.build();

		assertTrue(config.getConfiguration(DummyPluginConfigKeyEnum.KEY1).isDefined());
	}*/
}
