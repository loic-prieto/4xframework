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
import io.vavr.collection.List;
import io.vavr.collection.Set;
import io.vavr.control.Option;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.model.config.CoreConfigKeyEnum;
import org.sephire.games.framework4x.testing.testPlugin1.TestPlugin1ConfigKeys;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.sephire.games.framework4x.core.plugins.PluginCreationUtils.buildNonPluginJar;
import static org.sephire.games.framework4x.core.plugins.PluginCreationUtils.buildPluginJar;

public class PluginManagerTest {

	private static Path validPluginsFolder;
	private static Path mixedPluginsFolder;
	private static Path invalidPluginFolder1;
	private static Path invalidPluginFolder2;

	@BeforeAll
	public static void setup() throws IOException {
		// Build valid plugins directory
		validPluginsFolder = Files.createTempDirectory("PluginManagerTest-");
		buildPluginJar(validPluginsFolder,"org.sephire.games.framework4x.testing.testPlugin1",Option.none());
		buildPluginJar(validPluginsFolder,"org.sephire.games.framework4x.testing.testPlugin2",Option.none());
		buildPluginJar(validPluginsFolder,"org.sephire.games.framework4x.testing.testPlugin3",Option.none());
		buildPluginJar(validPluginsFolder,"org.sephire.games.framework4x.testing.testPlugin11",Option.of("org.sephire.games.framework4x.testing.testPlugin1"));
		buildPluginJar(validPluginsFolder,"org.sephire.games.framework4x.testing.testPlugin4",Option.of("nonExistentParent"));

		// Build mixed plugins and non plugins folder (the plugin manager should fail here)
		mixedPluginsFolder = Files.createTempDirectory("PluginManagerTest-Mixed-");
		buildPluginJar(validPluginsFolder,"org.sephire.games.framework4x.testing.testPlugin1",Option.none());
		buildNonPluginJar(mixedPluginsFolder);

		// A folder with an invalid plugin because of plugin defining itself as its parent plugin
		invalidPluginFolder1 = Files.createTempDirectory("PluginManagerTest-Invalid-1-");
		buildPluginJar(invalidPluginFolder1,"org.sephire.games.framework4x.testing.testPlugin1",
		  Option.of("org.sephire.games.framework4x.testing.testPlugin1"));

		// A folder with an invalid plugin because the i18n basic plugin info hasn't been provided
		invalidPluginFolder2 = Files.createTempDirectory("PluginManagerTest-Invalid-2-");
		buildPluginJar(invalidPluginFolder2,"org.sephire.games.framework4x.testing.testPluginWithoutI18N",
		  Option.none());
		buildPluginJar(invalidPluginFolder2,"org.sephire.games.framework4x.testing.testPlugin1",
		  Option.none());

	}

	@Test
	@DisplayName("Should validate successfully a valid plugin folder")
	public void plugin_manager_should_validate_valid_plugin_folder(){
		var pluginManager = new PluginManager();

		assertTrue(pluginManager.isPluginFolderValid(validPluginsFolder));
	}

	@Test
	@DisplayName("The Plugin Manager should declare as invalid a folder where not all jars are plugins")
	public void plugin_manager_should_fail_when_folder_does_not_contain_all_plugins(){
		var pluginManager = new PluginManager();


		assertFalse(pluginManager.isPluginFolderValid(mixedPluginsFolder));
	}

	@Test
	@DisplayName("Given a plugin jar file, if the parent is defined with the same name as the plugin, it should complain")
	public void should_complain_if_plugin_defines_parent_as_itself() {
		var pluginManager = new PluginManager();

		var isPluginFolder1Valid = pluginManager.isPluginFolderValid(invalidPluginFolder1);

		assertFalse(isPluginFolder1Valid);
	}

	@Test
	@DisplayName("When a plugin in the plugin folder is invalid because of i18n, the plugin manager should refuse to initialize")
	public void should_complain_if_a_plugin_is_invalid() {
		var pluginManager = new PluginManager();

		var isPluginFolder1Valid = pluginManager.isPluginFolderValid(invalidPluginFolder2);

		assertFalse(isPluginFolder1Valid);
	}

	@Test
	@DisplayName("Plugin Manager should list successfully plugins from a valid plugin folder")
	public void plugin_manager_should_list_valid_plugins() {
		var pluginManager = new PluginManager();

		var pluginList = pluginManager.getAvailablePlugins(validPluginsFolder);
		assertTrue(pluginList.isSuccess());

		var expectedList = List.of(
		  "org.sephire.games.framework4x.testing.testPlugin1",
		  "org.sephire.games.framework4x.testing.testPlugin2",
		  "org.sephire.games.framework4x.testing.testPlugin3",
		  "org.sephire.games.framework4x.testing.testPlugin4",
		  "org.sephire.games.framework4x.testing.testPlugin11")
		  .sorted().toSet().map((name)->new PluginSpec(name,name,Option.none()));

		assertEquals(expectedList,pluginList.get());
	}

	@Test
	@DisplayName("The plugin manager should load plugins successfully")
	public void should_load_plugins_successfully() {
		var pluginManager = new PluginManager();

		var configuration = Configuration.builder();

		var loadedPluginsTry = pluginManager.loadPlugins(HashSet.of(
		  "org.sephire.games.framework4x.testing.testPlugin1",
		  "org.sephire.games.framework4x.testing.testPlugin2"), validPluginsFolder,configuration);
		assertTrue(loadedPluginsTry.isSuccess());

		var expectedLoadedPlugins = HashSet.of(
		  "org.sephire.games.framework4x.testing.testPlugin1",
		  "org.sephire.games.framework4x.testing.testPlugin2");
		var loadedPluginsNames = configuration.getConfig(CoreConfigKeyEnum.LOADED_PLUGINS, Set.class)
		  .map((optionalSet)-> optionalSet
			.map(pluginSet->((Set<Plugin>)pluginSet)
			  .map(plugin->plugin.getSpecification().getPluginName())));

		assertEquals(expectedLoadedPlugins,loadedPluginsNames.get().get());
	}

	@Test
	@DisplayName("Given a list of plugins, if a plugin dependency is not included in the list, the plugin manager should add it")
	public void should_complete_list_of_needed_plugins() {
		var pluginManager = new PluginManager();

		var configuration = Configuration.builder();

		var pluginLoadingTry = pluginManager.loadPlugins(HashSet.of(
		  "org.sephire.games.framework4x.testing.testPlugin2",
		  "org.sephire.games.framework4x.testing.testPlugin11"), validPluginsFolder,configuration);

		assertTrue(pluginLoadingTry.isSuccess());

		var expectedLoadedPlugins = HashSet.of(
		  "org.sephire.games.framework4x.testing.testPlugin1",
		  "org.sephire.games.framework4x.testing.testPlugin2",
		  "org.sephire.games.framework4x.testing.testPlugin11");
		var loadedPluginsNames = configuration.getConfig(CoreConfigKeyEnum.LOADED_PLUGINS, Set.class)
		  .map((optionalSet)-> optionalSet
			.map(pluginSet->((Set<Plugin>)pluginSet)
			  .map(plugin->plugin.getSpecification().getPluginName())));

		assertTrue(loadedPluginsNames.isSuccess());
		assertTrue(loadedPluginsNames.get().isDefined());
		var names = loadedPluginsNames.get().get();

		assertEquals(expectedLoadedPlugins,names);
	}

	@Test
	@DisplayName("Given a set of plugins to load, if one of the plugin dependencies is not found, it should raise an error")
	public void should_complain_if_needed_plugin_is_not_found() {
		var pluginManager = new PluginManager();

		var configuration = Configuration.builder();

		var loadedPluginsTry = pluginManager.loadPlugins(HashSet.of(
		  "org.sephire.games.framework4x.testing.testPlugin1",
		  "org.sephire.games.framework4x.testing.testPlugin4"), validPluginsFolder,configuration);

		assertTrue(loadedPluginsTry.isFailure());

		assertEquals(ParentPluginsNotFoundException.class,loadedPluginsTry.getCause().getClass());

		var expectedMissingDependency = "nonExistentParent";
		var actualMissingDependency = ((ParentPluginsNotFoundException)loadedPluginsTry.getCause())
		  .getMissingPlugins().get()._2;
		assertEquals(expectedMissingDependency,actualMissingDependency);

		var expectedPluginMissingDependency = "org.sephire.games.framework4x.testing.testPlugin4";
		var actualPluginMissingDependency = ((ParentPluginsNotFoundException)loadedPluginsTry.getCause())
		  .getMissingPlugins().get()._1;
		assertEquals(expectedPluginMissingDependency,actualPluginMissingDependency);

	}

	@Test
	@DisplayName("Given a set of plugins to load, the plugin manager should complain if one of them cannot be found in plugin folder")
	public void should_complain_if_loaded_plugin_does_not_exist() {
		var pluginManager = new PluginManager();

		var configuration = Configuration.builder();

		var pluginLoadingTry = pluginManager.loadPlugins(
		  HashSet.of(
		    "org.sephire.games.framework4x.testing.testPlugin1"
			, "nonExistentPlugin"), validPluginsFolder,configuration);

		assertTrue(pluginLoadingTry.isFailure());
		assertEquals(PluginsNotFoundException.class,pluginLoadingTry.getCause().getClass());
	}


	@Test
	@DisplayName("Given a set of plugins to load, "+
	  "when one of those plugins is a child of another, the child should override configuration from the parent")
	public void configuration_should_be_overriden_by_child_plugins() {
		var pluginManager = new PluginManager();

		var configuration = Configuration.builder();

		var loadedPluginsTry = pluginManager.loadPlugins(HashSet.of(
		  "org.sephire.games.framework4x.testing.testPlugin1",
		  "org.sephire.games.framework4x.testing.testPlugin2",
		  "org.sephire.games.framework4x.testing.testPlugin11"), validPluginsFolder,configuration);

		assertTrue(loadedPluginsTry.isSuccess());

		var finalConfig = configuration.build();
		var expectedConfigValue = "overridenValue";
		var actualConfigValueOperation = finalConfig.getConfiguration(TestPlugin1ConfigKeys.TEST_VALUE, String.class);

		assertTrue(actualConfigValueOperation.isSuccess());
		assertTrue(actualConfigValueOperation.get().isDefined());
		assertEquals(expectedConfigValue,actualConfigValueOperation.get().get());
	}




}
