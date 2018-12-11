package org.sephire.games.framework4x.core.plugins;

import org.junit.jupiter.api.BeforeAll;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class PluginManagerTest {

	private static final String TEMP_PLUGIN_DIRECTORY_PREFIX = "PluginManagerTest-";
	private static final String TEMP_PLUGIN_FILE_PREFIX = "plugin-";

	@BeforeAll
	public void setup() {
		// Build two jar
	}

	private void buildJarFile(boolean isPlugin) throws IOException {
		Path tmpDirectory = Files.createTempDirectory(TEMP_PLUGIN_DIRECTORY_PREFIX);

		Files.createTempFile(tmpDirectory,TEMP_PLUGIN_FILE_PREFIX,)

	}
}
