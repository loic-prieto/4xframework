package org.sephire.games.framework4x.core.plugins;

import io.vavr.API;
import io.vavr.collection.List;
import io.vavr.control.Try;
import org.sephire.games.framework4x.core.utils.FunctionalUtils;
import org.sephire.games.framework4x.core.utils.FunctionalUtils.Reduce;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.jar.JarFile;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.Predicates.instanceOf;

/**
 * This class provides for utility functions that can be used by clients to inspect what plugins are available,
 * and load them.
 */
public class PluginManager {

	public static final String PLUGIN_NAME_MANIFEST_ENTRY_LABEL = "X-4XPlugin-Name";

	private File pluginFolder;

	public PluginManager(File pluginFolder) {
		this.pluginFolder = pluginFolder;
	}

	/**
	 * Return a list of plugins names available to load from the plugin folder.
	 * @return
	 */
	public Try<List<String>> getAvailablePluginsNames(){
		return Try.of(()->{
			var pluginsJars = List.of(this.pluginFolder.list((dir,name)-> name.endsWith("jar")))
			  .map(File::new)
			  .map(PluginManager::convertToJarFile)
			  .filter((jarfileTry)-> jarfileTry.isFailure() || has4XPluginInformation(jarfileTry.get()));

			if(!pluginsJars.filter(Try::isFailure).isEmpty()) {
				throw new PluginLoadingException(pluginsJars.filter(Try::isFailure).map(Try::getCause));
			}

			return pluginsJars.map(Try::get)
			  .map(JarFile::getManifest)
			  .map()


		});

	}

	public static Try<PluginManager> fromFolder(Path folderPath) {
		return Try.of(()->{
			if(!folderPath.toFile().exists() && !folderPath.toFile().isDirectory()) {
				throw new IllegalArgumentException(String.format("The path %s is not a valid folder",folderPath));
			}

			return new PluginManager(folderPath.toFile());
		});
	}

	/**
	 *
	 */
	private static Try<JarFile> convertToJarFile(File file) {
		return Try.of(()->new JarFile(file))
		  .mapFailure(
		  	Case($(instanceOf(IOException.class)),t -> new PluginFileCouldNotBeReadException(t.getMessage()))
		  );
	}
	/**
	 * Checks whether a jar file is a 4X plugin.
	 *
	 * @param jarFile
	 * @return
	 */
	private static boolean has4XPluginInformation(JarFile jarFile) {
		boolean is4XPlugin = false;

		try {
			var manifest = jarFile.getManifest();
			if(manifest != null) {
				is4XPlugin = manifest.getEntries().get(PLUGIN_NAME_MANIFEST_ENTRY_LABEL) != null;
			}
		}catch(IOException ioe) {}

		return is4XPlugin;
	}

	private static Try<String> getPluginNameFromManifest(JarFile jarFile) {
		return Try.of(()->{
			var manifest = jarFile.getManifest();
			if(manifest == null) {
				throw new InvalidPluginJarException(jarFile.getName(),"There is no manifest in the jar file");
			}

			var name = manifest.getEntries().get(PLUGIN_NAME_MANIFEST_ENTRY_LABEL);
			if(name == null) {
				throw new InvalidPluginJarException(jarFile.getName(),"The manifest does not contain the "+PLUGIN_NAME_MANIFEST_ENTRY_LABEL+" entry");
			}

			// To be implemented
			return null;
		});
	}
}
