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

import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.control.Option;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sephire.games.framework4x.testing.testPlugin1.TestPlugin1ConfigKeys;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.jar.Attributes;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PluginManagerTest {

	private static final String TEMP_PLUGIN_FILE_PREFIX = "plugin-";

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
	@DisplayName("Should create PluginManager from a valid plugin folder")
	public void plugin_manager_should_be_created_with_valid_folder(){
		var pluginManager = PluginManager.fromFolder(validPluginsFolder);

		assertTrue(pluginManager.isSuccess());
	}

	@Test
	@DisplayName("Plugin Manager should list successfully plugins from a valid plugin folder")
	public void plugin_manager_should_list_valid_plugins() {
		var pluginManager = PluginManager.fromFolder(validPluginsFolder);

		assertTrue(pluginManager.isSuccess());

		var pluginList = pluginManager.get()
		  .getAvailablePlugins();

		var expectedList = List.of(
		  "org.sephire.games.framework4x.testing.testPlugin1",
		  "org.sephire.games.framework4x.testing.testPlugin2",
		  "org.sephire.games.framework4x.testing.testPlugin3",
		  "org.sephire.games.framework4x.testing.testPlugin4",
		  "org.sephire.games.framework4x.testing.testPlugin11")
		  .sorted().toSet().map((name)->new PluginSpec(name,name,Option.none()));

		assertEquals(expectedList,pluginList);
	}

	@Test
	@DisplayName("The plugin manager should load plugins successfully")
	public void should_load_plugins_successfully() {
		var pluginManagerTry = PluginManager.fromFolder(validPluginsFolder);
		assertTrue(pluginManagerTry.isSuccess());

		var pluginManager = pluginManagerTry.get();
		var loadedPluginsTry = pluginManager.loadPlugins(HashSet.of(
		  "org.sephire.games.framework4x.testing.testPlugin1",
		  "org.sephire.games.framework4x.testing.testPlugin2"));

		assertTrue(loadedPluginsTry.isSuccess());
	}

	@Test
	@DisplayName("Given a list of plugins, if a plugin dependency is not included in the list, the plugin manager should add it")
	public void should_complete_list_of_needed_plugins() {
		var pluginManagerTry = PluginManager.fromFolder(validPluginsFolder);
		assertTrue(pluginManagerTry.isSuccess());

		var pluginManager = pluginManagerTry.get();
		var loadedPluginsTry = pluginManager.loadPlugins(HashSet.of(
		  "org.sephire.games.framework4x.testing.testPlugin2",
		  "org.sephire.games.framework4x.testing.testPlugin11"));

		assertTrue(loadedPluginsTry.isSuccess());
		var expectedLoadedPlugins = HashSet.of(
		  "org.sephire.games.framework4x.testing.testPlugin1",
		  "org.sephire.games.framework4x.testing.testPlugin2",
		  "org.sephire.games.framework4x.testing.testPlugin11");
		assertEquals(expectedLoadedPlugins,pluginManager.getLoadedPlugins());
	}

	@Test
	@DisplayName("Given a set of plugins to load, if one of the plugin dependencies is not found, it should raise an error")
	public void should_complain_if_needed_plugin_is_not_found() {
		var pluginManagerTry = PluginManager.fromFolder(validPluginsFolder);
		assertTrue(pluginManagerTry.isSuccess());

		var pluginManager = pluginManagerTry.get();
		var loadedPluginsTry = pluginManager.loadPlugins(HashSet.of(
		  "org.sephire.games.framework4x.testing.testPlugin1",
		  "org.sephire.games.framework4x.testing.testPlugin4"));

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
		var pluginManagerTry = PluginManager.fromFolder(validPluginsFolder);
		assertTrue(pluginManagerTry.isSuccess());

		var pluginLoadingTry = pluginManagerTry.get().loadPlugins(
		  HashSet.of(
		    "org.sephire.games.framework4x.testing.testPlugin1"
			,"nonExistentPlugin"));

		assertTrue(pluginLoadingTry.isFailure());
		assertEquals(PluginsNotFoundException.class,pluginLoadingTry.getCause().getClass());
	}

	@Test
	@DisplayName("The Plugin Manager should refuse to handle a folder where not all jars are plugins")
	public void plugin_manager_should_fail_when_folder_does_not_contain_all_plugins(){
		var pluginManager = PluginManager.fromFolder(mixedPluginsFolder);

		assertTrue(pluginManager.isFailure());
		assertEquals(PluginLoadingException.class,pluginManager.getCause().getClass());
	}

	@Test
	@DisplayName("Given a set of plugins to load, "+
	  "when one of those plugins is a child of another, the child should override configuration from the parent")
	public void configuration_should_be_overriden_by_child_plugins() {
		var pluginManagerTry = PluginManager.fromFolder(validPluginsFolder);
		assertTrue(pluginManagerTry.isSuccess());

		var pluginManager = pluginManagerTry.get();
		var loadedPluginsTry = pluginManager.loadPlugins(HashSet.of(
		  "org.sephire.games.framework4x.testing.testPlugin1",
		  "org.sephire.games.framework4x.testing.testPlugin2",
		  "org.sephire.games.framework4x.testing.testPlugin11"));

		assertTrue(loadedPluginsTry.isSuccess());

		var configuration = loadedPluginsTry.get();
		var expectedConfigValue = "overridenValue";
		var actualConfigValueOperation = configuration.getConfiguration(TestPlugin1ConfigKeys.TEST_VALUE,String.class);

		assertTrue(actualConfigValueOperation.isSuccess());
		assertTrue(actualConfigValueOperation.get().isDefined());
		assertEquals(expectedConfigValue,actualConfigValueOperation.get().get());
	}

	@Test
	@DisplayName("Given a plugin jar file, if the parent is defined with the same name as the plugin, it should complain")
	public void should_complain_if_plugin_defines_parent_as_itself() {
		var pluginManagerTry = PluginManager.fromFolder(invalidPluginFolder1);
		assertTrue(pluginManagerTry.isFailure());
		assertEquals(PluginLoadingException.class,pluginManagerTry.getCause().getClass());

		var exceptions = ((PluginLoadingException)pluginManagerTry.getCause()).getExceptions();
		var isExpectedException = ((PluginLoadingException)pluginManagerTry.getCause()).getExceptions()
		  .exists((t)->InvalidPluginJarException.class.isAssignableFrom(t.getClass()));

		assertTrue(isExpectedException);
		assertEquals(1,exceptions.length());
	}

	@Test
	@DisplayName("When a plugin in the plugin folder is invalid because of i18n, the plugin manager should refuse to start")
	public void should_complain_if_a_plugin_is_invalid() {
		var pluginManagerTry = PluginManager.fromFolder(invalidPluginFolder2);
		assertTrue(pluginManagerTry.isFailure());
		assertEquals(PluginLoadingException.class,pluginManagerTry.getCause().getClass());

		var exceptions = ((PluginLoadingException)pluginManagerTry.getCause()).getExceptions();
		var isExpectedException = ((PluginLoadingException)pluginManagerTry.getCause()).getExceptions()
		  .exists((t)->InvalidPluginException.class.isAssignableFrom(t.getClass()));

		assertTrue(isExpectedException);
		assertEquals(1,exceptions.length());
	}

	/**
	 * Build a non plugin jar file.
	 * A jar file that is not a plugin just contains a manifest with the version of the manifest,
	 * which means it cannot be loaded by the plugin manager.
	 *
	 * @param directory
	 * @return
	 * @throws IOException
	 */
	private static Path buildNonPluginJar(Path directory) throws IOException {
		Path jarFilePath = Files.createTempFile(directory,TEMP_PLUGIN_FILE_PREFIX,".jar");
		Manifest manifest = new Manifest();
		manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
		JarOutputStream jarStream = new JarOutputStream(new FileOutputStream(jarFilePath.toFile()),manifest);
		jarStream.close();
		return jarFilePath;
	}

	/**
	 * Builds a very primitive jar file with only a manifest and no classes inside.
	 * The manifest contains the metadata of a plugin
	 *
	 * @param directory the directory to be used to put the jar in
	 * @param pluginName the name of the plugin if it is one
	 * @param parentPlugin an optional containing the name of the parent plugin if any
	 * @return
	 * @throws IOException
	 */
	private static Path buildPluginJar(Path directory, String pluginName, Option<String> parentPlugin) throws IOException {
		Path jarFilePath = Files.createTempFile(directory,TEMP_PLUGIN_FILE_PREFIX,".jar");

		Manifest manifest = new Manifest();
		manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
		manifest.getMainAttributes().put(new Attributes.Name(PluginManager.PLUGIN_NAME_MANIFEST_ENTRY_LABEL), pluginName);
		manifest.getMainAttributes().put(new Attributes.Name(PluginManager.PLUGIN_ROOT_PACKAGE_MANIFEST_ENTRY_LABEL), pluginName);
		if(parentPlugin.isDefined()){
			manifest.getMainAttributes().put(new Attributes.Name(PluginManager.PLUGIN_PARENT_ENTRY_LABEL), parentPlugin.get());
		}

		JarOutputStream jarStream = new JarOutputStream(new FileOutputStream(jarFilePath.toFile()),manifest);
		jarStream.close();

		return jarFilePath;

	}
}
