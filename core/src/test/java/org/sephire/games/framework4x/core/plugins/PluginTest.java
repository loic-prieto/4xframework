package org.sephire.games.framework4x.core.plugins;

import io.vavr.collection.Map;
import io.vavr.control.Option;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.model.config.CoreConfigKeyEnum;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.sephire.games.framework4x.testing.testPlugin1.TestPlugin1ConfigKeys.TEST_VALUE;

public class PluginTest {

	private static final String TEST1_PLUGIN_NAME = "org.sephire.games.framework4x.testing.testPlugin1";
	private static final String TEST_PLUGIN_WITH_I18N_NAME = "org.sephire.games.framework4x.testing.testPluginWithI18NResources";
	private static final String I18N_RESOURCE = "org.sephire.games.framework4x.testing.testPluginWithI18NResources.testKey";


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
	@DisplayName("When loading a plugin, it should ensure the i18n resources of its bundles are put into the configuration")
	public void should_load_i18n_resources_when_loading_pluginb(){
		var pluginSpec = new PluginSpec(TEST_PLUGIN_WITH_I18N_NAME, TEST_PLUGIN_WITH_I18N_NAME, Option.none());
		var configuration = Configuration.builder();
		var pluginLoadTry = Plugin.from(pluginSpec, configuration);
		var finalConfiguration = configuration.build();

		assertTrue(pluginLoadTry.isSuccess());

		var i18nRawResourcesTry = finalConfiguration.getConfiguration(CoreConfigKeyEnum.I18N, Map.class);
		assertTrue(i18nRawResourcesTry.isSuccess());
		assertTrue(i18nRawResourcesTry.get().isDefined());

		var i18nResource = finalConfiguration.getTranslationFor(Locale.ENGLISH, I18N_RESOURCE);
		assertTrue(i18nResource.isDefined());
		assertEquals("test value", i18nResource.get());

		i18nResource = finalConfiguration.getTranslationFor(Locale.ENGLISH, "non existing resource");
		assertTrue(i18nResource.isEmpty());
	}
}
