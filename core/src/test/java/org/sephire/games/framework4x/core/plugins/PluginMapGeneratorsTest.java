/**
 * 4X Framework - Core library - The core library on which to base the game
 * Copyright © 2018 Loïc Prieto Dehennault (loic.sephiroth@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.sephire.games.framework4x.core.plugins;

import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import io.vavr.control.Try;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.model.config.CoreConfigKeyEnum;
import org.sephire.games.framework4x.core.plugins.map.MapGeneratorWrapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
		var pluginSpec = PluginSpec.builder().withPluginName(MAP_GENERATOR_PLUGIN_NAME)
		  .withRootPackage(MAP_GENERATOR_PLUGIN_NAME)
		  .build();
		assertTrue(pluginSpec.isSuccess());

		var configurationBuilder = Configuration.builder();

		var pluginLoadingTry = Plugin.from(pluginSpec.get(),configurationBuilder);
		var configuration = configurationBuilder.build();

		assertTrue(pluginLoadingTry.isSuccess());

		var mapGeneratorsOperation = configuration.getConfiguration(CoreConfigKeyEnum.MAPS, Set.class);
		assertTrue(mapGeneratorsOperation.isSuccess());
		assertTrue(mapGeneratorsOperation.get().isDefined());

		var mapGenerators = mapGeneratorsOperation.get().get();
		assertEquals(2, mapGenerators.size());
	}

	@Test
	@DisplayName("When loading map generators from a plugin, name and i18n display key of the generator should be retrieved")
	public void should_have_retrieved_name_and_displayKey_from_generators() {
		var pluginSpec = PluginSpec.builder().withPluginName(MAP_GENERATOR_PLUGIN_NAME)
		  .withRootPackage(MAP_GENERATOR_PLUGIN_NAME)
		  .build();
		assertTrue(pluginSpec.isSuccess());

		var configurationBuilder = Configuration.builder();

		var pluginLoadingTry = Plugin.from(pluginSpec.get(),configurationBuilder);
		var configuration = configurationBuilder.build();

		assertTrue(pluginLoadingTry.isSuccess());

		var mapGeneratorsOperation = configuration.getConfiguration(CoreConfigKeyEnum.MAPS, Set.class);
		assertTrue(mapGeneratorsOperation.isSuccess());
		assertTrue(mapGeneratorsOperation.get().isDefined());

		Set<MapGeneratorWrapper> mapGenerators = mapGeneratorsOperation.get().get();
		var expectedGeneratorNames = HashSet.of("plugin2.map_generator_1","plugin2.map_generator_2");
		var actualGeneratorNames = mapGenerators.map(MapGeneratorWrapper::getName);
		assertEquals(expectedGeneratorNames,actualGeneratorNames);

		var expectedDisplayKeys = HashSet.of("plugin2.map_generators.1.displayName","plugin2.map_generators.2.displayName");
		var actualDisplayKeys = mapGenerators.map(MapGeneratorWrapper::getDisplayKey);
		assertEquals(expectedDisplayKeys,actualDisplayKeys);
	}

	@Test
	@DisplayName("The loaded map generators should be able to produce a map given a configuration")
	public void map_generators_should_produce_maps() {
		var pluginSpec = PluginSpec.builder().withPluginName(MAP_GENERATOR_PLUGIN_NAME)
		  .withRootPackage(MAP_GENERATOR_PLUGIN_NAME)
		  .build();
		assertTrue(pluginSpec.isSuccess());

		var configurationBuilder = Configuration.builder();

		var pluginLoadingTry = Plugin.from(pluginSpec.get(),configurationBuilder);
		var configuration = configurationBuilder.build();
		assertTrue(pluginLoadingTry.isSuccess());

		var mapGeneratorsOperation = configuration.getConfiguration(CoreConfigKeyEnum.MAPS, Set.class);
		assertTrue(mapGeneratorsOperation.isSuccess());
		assertTrue(mapGeneratorsOperation.get().isDefined());

		Set<MapGeneratorWrapper> mapGenerators = mapGeneratorsOperation.get().get().map(MapGeneratorWrapper.class::cast);

		var mapBuildingOperation = Try.sequence(mapGenerators.map(mapGeneratorWrapper -> mapGeneratorWrapper.buildMap(configuration)));
		assertTrue(mapBuildingOperation.isSuccess());
	}
}
