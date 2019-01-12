/**
 * 4X Framework - Standard plugin terminal client adapter - A console client adapter/bridge for the 4X Framework Standard Plugin
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
package org.sephire.games.framework4x.plugins.standard.terminal;

import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.sephire.games.framework4x.clients.terminal.api.config.ConsoleClientConfigKeyEnum;
import org.sephire.games.framework4x.clients.terminal.api.config.TerrainsMapping;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.plugins.configuration.ConfigLoader;
import org.sephire.games.framework4x.core.plugins.configuration.PluginLifecycleHandler;
import org.sephire.games.framework4x.core.plugins.configuration.PluginLoadingHook;

import static org.sephire.games.framework4x.core.utils.ResourceLoading.packageToFolderPath;

@Slf4j
@PluginLifecycleHandler
public class Main {

	private static String getPackageName() {
		return Main.class.getPackageName();
	}

	@PluginLoadingHook
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
		  .peek((terrainsMappings) -> configuration.putConfig(ConsoleClientConfigKeyEnum.TERRAIN_CHARACTER_MAPPING, terrainsMappings));
	}

}
