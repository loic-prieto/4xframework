package org.sephire.games.framework4x.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sephire.games.framework4x.core.plugins.PluginLoadingException;
import org.sephire.games.framework4x.core.plugins.PluginSpecFileNotFound;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.sephire.games.framework4x.testing.dummyPlugin.DummyPluginConfigKeyEnum.KEY1;

public class GameTest {

	private static final String DUMMY_PLUGIN_NAME = "org.sephire.games.framework4x.testing.dummyPlugin";

	@Test
	@DisplayName("Should complain if a requested plugin doesn't exist in classpath when loading framework")
	public void should_complain_if_plugin_doesnt_exist_in_classpath() {
		var gameBuildingTry = new Game.Builder()
		  .withPlugins("not_existent_plugin")
		  .build();

		assertTrue(gameBuildingTry.isFailure());
		assertTrue(gameBuildingTry.getCause().getClass().isAssignableFrom(PluginLoadingException.class));

		var exceptions = PluginLoadingException.class.cast(gameBuildingTry.getCause()).getExceptions();
		assertTrue(exceptions.exists((e) -> PluginSpecFileNotFound.class.isAssignableFrom(e.getClass())));
	}

	@Test
	@DisplayName("Should complain if no plugin is specified while building the framework")
	public void should_complain_if_no_plugin_is_specified() {
		var gameBuildingTry = new Game.Builder()
		  .withPlugins()
		  .build();

		assertTrue(gameBuildingTry.isFailure());
		assertTrue(gameBuildingTry.getCause().getClass().isAssignableFrom(PluginLoadingException.class));

		var exceptions = PluginLoadingException.class.cast(gameBuildingTry.getCause()).getExceptions();
		assertTrue(exceptions.exists((e) -> IllegalArgumentException.class.isAssignableFrom(e.getClass())));
	}

	@Test
	@DisplayName("Should load successfully a plugin if it is present in the classpath")
	public void should_load_successfully_plugin_present_on_classpath() {
		var gameBuildingTry = new Game.Builder()
		  .withPlugins(DUMMY_PLUGIN_NAME)
		  .build();

		assertTrue(gameBuildingTry.isSuccess());
		assertTrue(gameBuildingTry.get().hasPlugin(DUMMY_PLUGIN_NAME));
	}

	@Test
	@DisplayName("Should have filled the configuration when the plugin was loaded")
	public void should_load_configuration_of_plugin_when_loaded() {
		var gameBuildingTry = new Game.Builder()
		  .withPlugins(DUMMY_PLUGIN_NAME)
		  .build();

		assertTrue(gameBuildingTry.get().getConfiguration().getConfiguration(KEY1).isDefined());
	}
}
