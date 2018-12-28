package org.sephire.games.framework4x.core.plugins;

import io.vavr.collection.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sephire.games.framework4x.testing.dummyPlugin.Main;

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
		buildJarFile(validPluginsFolder,"dummyPlugin",Main.class,true);
		buildJarFile(validPluginsFolder,"test2",Main.class,true);

		// Build mixed plugins and non plugins folder (the plugin manager should fail here)
		mixedPluginsFolder = Files.createTempDirectory("PluginManagerTest-Mixed-");
		buildJarFile(mixedPluginsFolder,"test1", String.class,true);
		buildJarFile(mixedPluginsFolder,"test2",Number.class,false);

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

		var expectedList = List.of("test1","test2").sorted().toSet();
		assertEquals(expectedList,pluginList);
	}

	@Test
	@DisplayName("The plugin manager should load plugins successfully")
	public void should_load_plugins_successfully() {
		var pluginManagerTry = PluginManager.fromFolder(validPluginsFolder);
		assertTrue(pluginManagerTry.isSuccess());

		var pluginManager = pluginManagerTry.get();
		var loadedPluginsTry = pluginManager.loadPlugins(List.of("test1","test2"));

		assertTrue(loadedPluginsTry.isSuccess());
	}

	@Test
	@DisplayName("The Plugin Manager should refuse to handle a folder where not all jars are plugins")
	public void plugin_manager_should_fail_when_folder_does_not_contain_all_plugins(){
		var pluginManager = PluginManager.fromFolder(mixedPluginsFolder);

		assertTrue(pluginManager.isFailure());
		assertEquals(PluginLoadingException.class,pluginManager.getCause().getClass());
	}


	/**
	 * Builds a very primitive jar file with only a manifest and no classes inside.
	 *
	 * @param directory the directory to be used to put the jar in
	 * @param pluginName the name of the plugin if it is one
	 * @param isPlugin whether the manifest should contain an entry to define the jar as a 4x framework plugin
	 * @return
	 * @throws IOException
	 */
	private static Path buildJarFile(Path directory, String pluginName, Class mainClass, boolean isPlugin) throws IOException {
		Path jarFilePath = Files.createTempFile(directory,TEMP_PLUGIN_FILE_PREFIX,".jar");

		Manifest manifest = new Manifest();
		manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
		if(isPlugin) {
			manifest.getMainAttributes().put(new Attributes.Name(PluginManager.PLUGIN_NAME_MANIFEST_ENTRY_LABEL), pluginName);
			manifest.getMainAttributes().put(new Attributes.Name(PluginManager.PLUGIN_ROOT_PACKAGE_MANIFEST_ENTRY_LABEL), pluginName);
		}

		JarOutputStream jarStream = new JarOutputStream(new FileOutputStream(jarFilePath.toFile()),manifest);
		jarStream.close();

		return jarFilePath;

	}
}
