/**
 * 4X Framework - Core library - The core library on which to base the game
 * Copyright © 2018 Loïc Prieto Dehennault (loic.sephiroth@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
		var pluginSpec = PluginSpec.builder()
		  .withPluginName(TEST1_PLUGIN_NAME)
		  .withRootPackage(TEST1_PLUGIN_NAME)
		  .build();
		assertTrue(pluginSpec.isSuccess());

		var configuration = Configuration.builder();
		var pluginLoadTry = Plugin.from(pluginSpec.get(), configuration);

		assertTrue(pluginLoadTry.isSuccess());
	}

	@Test
	@DisplayName("Given a valid spec, a plugin should put its post load configuration correctly")
	public void should_load_configuration_successfully() {
		var pluginSpec = PluginSpec.builder()
		  .withPluginName(TEST1_PLUGIN_NAME)
		  .withRootPackage(TEST1_PLUGIN_NAME)
		  .build();
		assertTrue(pluginSpec.isSuccess());

		var configuration = Configuration.builder();
		var pluginLoadTry = Plugin.from(pluginSpec.get(), configuration);

		assertTrue(pluginLoadTry.isSuccess());
		assertTrue(configuration.getConfig(TEST_VALUE).isDefined());
		assertEquals("someValue", configuration.getConfig(TEST_VALUE).get());
	}

	@Test
	@DisplayName("Should load terrain types successfully when loading a plugin with terrain data")
	public void should_load_terrain_types_successfully() {
		var pluginSpec = PluginSpec.builder()
		  .withPluginName(TEST1_PLUGIN_NAME)
		  .withRootPackage(TEST1_PLUGIN_NAME)
		  .build();
		assertTrue(pluginSpec.isSuccess());

		var configuration = Configuration.builder();
		var pluginLoadTry = Plugin.from(pluginSpec.get(), configuration);

		assertTrue(pluginLoadTry.isSuccess());
		assertTrue(configuration.getConfig(CoreConfigKeyEnum.TERRAIN_TYPES).isDefined());
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
