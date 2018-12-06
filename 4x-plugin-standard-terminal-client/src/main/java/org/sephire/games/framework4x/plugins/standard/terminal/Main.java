package org.sephire.games.framework4x.plugins.standard.terminal;

import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.sephire.games.framework4x.clients.terminal.api.config.ConsoleClientConfigKeyEnum;
import org.sephire.games.framework4x.clients.terminal.api.config.TerrainsMapping;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.plugins.PluginInitializer;
import org.sephire.games.framework4x.core.plugins.configuration.ConfigLoader;

import static org.sephire.games.framework4x.core.utils.ResourceLoading.packageToFolderPath;

@Slf4j
public class Main implements PluginInitializer {

	private static String getPackageName() {
		return Main.class.getPackageName();
	}

	@Override
	public Try<Void> pluginLoad(Configuration.Builder configuration) {

		return loadTerrainMappings(configuration)
		  .onFailure((error) -> log.error(String.format("Could not load successfully the plugin %s : %s", getPackageName(), error)))
		  .onSuccess((result) -> log.info(String.format("Plugin %s loaded successfully", getPackageName())))
		  // We don't care about the content inside the try, just if it is successful or isn't
		  .map((result) -> null);
	}

	private Try<TerrainsMapping> loadTerrainMappings(Configuration.Builder configuration) {
		var terrainsTypesMappingsFilename = packageToFolderPath(getPackageName()).concat("/terrains-types-mappings.yaml");
		return ConfigLoader.getConfigFor(terrainsTypesMappingsFilename, TerrainsMapping.class)
		  .peek((terrainsMappings) -> configuration.addConfig(ConsoleClientConfigKeyEnum.TERRAIN_CHARACTER_MAPPING, terrainsMappings));
	}

}
