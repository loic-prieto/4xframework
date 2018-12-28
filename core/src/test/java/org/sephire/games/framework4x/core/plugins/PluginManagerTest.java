package org.sephire.games.framework4x.core.plugins;

import io.vavr.collection.List;
import io.vavr.control.Option;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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

	@BeforeAll
	public static void setup() throws IOException {
		// Build valid plugins directory
		validPluginsFolder = Files.createTempDirectory("PluginManagerTest-");
		buildPluginJar(validPluginsFolder,"org.sephire.games.framework4x.testing.testPlugin1",Option.none());
		buildPluginJar(validPluginsFolder,"org.sephire.games.framework4x.testing.testPlugin2",Option.none());
		buildPluginJar(validPluginsFolder,"org.sephire.games.framework4x.testing.testPlugin3",Option.none());
		buildPluginJar(validPluginsFolder,"org.sephire.games.framework4x.testing.testPlugin11",Option.of("org.sephire.games.framework4x.testing.testPlugin1"));

		// Build mixed plugins and non plugins folder (the plugin manager should fail here)
		mixedPluginsFolder = Files.createTempDirectory("PluginManagerTest-Mixed-");
		buildPluginJar(validPluginsFolder,"org.sephire.games.framework4x.testing.testPlugin1",Option.none());
		buildNonPluginJar(mixedPluginsFolder);

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
		  .getAvailablePluginsNames();

		var expectedList = List.of(
		  "org.sephire.games.framework4x.testing.testPlugin1",
		  "org.sephire.games.framework4x.testing.testPlugin2",
		  "org.sephire.games.framework4x.testing.testPlugin3",
		  "org.sephire.games.framework4x.testing.testPlugin11")
		  .sorted().toSet();

		assertEquals(expectedList,pluginList);
	}

	@Test
	@DisplayName("The plugin manager should load plugins successfully")
	public void should_load_plugins_successfully() {
		var pluginManagerTry = PluginManager.fromFolder(validPluginsFolder);
		assertTrue(pluginManagerTry.isSuccess());

		var pluginManager = pluginManagerTry.get();
		var loadedPluginsTry = pluginManager.loadPlugins(List.of(
		  "org.sephire.games.framework4x.testing.testPlugin1",
		  "org.sephire.games.framework4x.testing.testPlugin2"));

		assertTrue(loadedPluginsTry.isSuccess());
	}

	@Test
	@DisplayName("Given a list of plugins, if a plugin dependency is not included in the list, should complain")
	public void should_reject_loading_plugins_if_dependencies_not_satisfied() {
		var pluginManagerTry = PluginManager.fromFolder(validPluginsFolder);
		assertTrue(pluginManagerTry.isSuccess());

		var pluginManager = pluginManagerTry.get();
		var loadedPluginsTry = pluginManager.loadPlugins(List.of(
		  "org.sephire.games.framework4x.testing.testPlugin2",
		  "org.sephire.games.framework4x.testing.testPlugin11"));

		assertTrue(loadedPluginsTry.isFailure());
		assertEquals(PluginDependencyNotIncludedException.class,loadedPluginsTry.getCause().getClass());
	}

	@Test
	@DisplayName("The Plugin Manager should refuse to handle a folder where not all jars are plugins")
	public void plugin_manager_should_fail_when_folder_does_not_contain_all_plugins(){
		var pluginManager = PluginManager.fromFolder(mixedPluginsFolder);

		assertTrue(pluginManager.isFailure());
		assertEquals(PluginLoadingException.class,pluginManager.getCause().getClass());
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
