package org.sephire.games.framework4x.core.plugins;

import io.vavr.control.Option;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.model.config.CoreConfigKeyEnum;

import java.util.Locale;
import java.util.regex.Pattern;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.sephire.games.framework4x.testing.testPlugin1.TestPlugin1ConfigKeys.TEST_VALUE;

public class PluginTest {

	private static final String TEST1_PLUGIN_NAME = "org.sephire.games.framework4x.testing.testPlugin1";
	private static final String TEST_PLUGIN_WITH_I18N_NAME = "org.sephire.games.framework4x.testing.testPluginWithI18NResources";
	private static final String TEST_PLUGIN_WITHOUT_I18N_NAME = "org.sephire.games.framework4x.testing.testPluginWithoutI18N";

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
		assertTrue(pluginLoadTry.getCause().getClass().isAssignableFrom(InvalidPluginSpecException.class));
	}

	@Test
	@DisplayName("Given an initialized plugin, it should retrieve its title and description for a given locale")
	public void should_retrieve_plugin_title_and_description() {
		var pluginSpec = new PluginSpec(TEST_PLUGIN_WITH_I18N_NAME, TEST_PLUGIN_WITH_I18N_NAME, Option.none());
		var configuration = Configuration.builder();
		var pluginLoadTry = Plugin.from(pluginSpec, configuration);

		assertTrue(pluginLoadTry.isSuccess());
		var plugin = pluginLoadTry.get();

		var expectedTitle = "Test plugin with I18N resources";
		var expectedDescription = "A test plugin that contains an i18n resource so that its name and description can be retrieved";
		var title = plugin.getTitle(Locale.ENGLISH);
		var description = plugin.getDescription(Locale.ENGLISH);

		assertTrue(title.isSuccess());
		assertTrue(description.isSuccess());
		assertEquals(expectedTitle,title.get());
		assertEquals(expectedDescription,description.get());
	}

	@Test
	@DisplayName("If a plugin doest not have basic i18n metadata, it should be invalid")
	public void if_basic_plugin_i18n_is_not_provided_complain() {
		var pluginSpec = new PluginSpec(TEST_PLUGIN_WITHOUT_I18N_NAME, TEST_PLUGIN_WITHOUT_I18N_NAME, Option.none());
		var configuration = Configuration.builder();
		var pluginLoadTry = Plugin.from(pluginSpec, configuration);

		assertTrue(pluginLoadTry.isFailure());
		assertTrue(InvalidPluginException.class.isAssignableFrom(pluginLoadTry.getCause().getClass()));
	}

	@Test
	@DisplayName("When loading a plugin, it should ensure the i18n resources of its bundles are put into the configuration")
	public void should_load_i18n_resources_when_loading_pluginb(){
		var pluginSpec = new PluginSpec(TEST_PLUGIN_WITH_I18N_NAME, TEST_PLUGIN_WITH_I18N_NAME, Option.none());
		var configuration = Configuration.builder();
		var pluginLoadTry = Plugin.from(pluginSpec, configuration);

		assertTrue(pluginLoadTry.isSuccess());
		var plugin = pluginLoadTry.get();
		assertTrue(false);
	}
}
