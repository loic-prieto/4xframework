package org.sephire.games.framework4x.core.plugins;

import io.vavr.control.Option;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.jar.Attributes;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

public class PluginCreationUtils {

	public static final String TEMP_PLUGIN_FILE_PREFIX = "plugin-";

	/**
	 * <p>Build a non plugin jar file.</p>
	 * <p>A jar file that is not a plugin just contains a manifest with the version of the manifest,
	 * which means it cannot be loaded by the plugin manager.</p>
	 *
	 * @param directory
	 * @return
	 * @throws IOException
	 */
	public static Path buildNonPluginJar(Path directory) throws IOException {
		Path jarFilePath = Files.createTempFile(directory,TEMP_PLUGIN_FILE_PREFIX,".jar");
		Manifest manifest = new Manifest();
		manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
		JarOutputStream jarStream = new JarOutputStream(new FileOutputStream(jarFilePath.toFile()),manifest);
		jarStream.close();
		return jarFilePath;
	}

	/**
	 * <p>Builds a very primitive jar file with only a manifest and no classes inside.</p>
	 * <p>The manifest contains the metadata of a plugin</p>
	 *
	 * @param directory the directory to be used to put the jar in
	 * @param pluginName the name of the plugin if it is one
	 * @param parentPlugin an optional containing the name of the parent plugin if any
	 * @return
	 * @throws IOException
	 */
	public static Path buildPluginJar(Path directory, String pluginName, Option<String> parentPlugin) throws IOException {
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
