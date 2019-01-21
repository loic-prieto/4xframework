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
package org.sephire.games.framework4x.plugins.standard_terminal;

import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.sephire.games.framework4x.clients.terminal.api.config.ConsoleClientConfigKeyEnum;
import org.sephire.games.framework4x.clients.terminal.api.config.TerrainsMapping;
import org.sephire.games.framework4x.clients.terminal.api.ui.gamewindow.BottomBar;
import org.sephire.games.framework4x.clients.terminal.api.ui.gamewindow.BottomBarElement;
import org.sephire.games.framework4x.clients.terminal.api.ui.gamewindow.BottomBarPosition;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.model.config.I18NKeyNotFoundException;
import org.sephire.games.framework4x.core.model.game.GameStateNotFoundException;
import org.sephire.games.framework4x.core.plugins.configuration.ConfigLoader;
import org.sephire.games.framework4x.core.plugins.configuration.PluginLifecycleHandler;
import org.sephire.games.framework4x.core.plugins.configuration.PluginLoadingHook;
import org.sephire.games.framework4x.core.utils.FunctionalUtils.Functions;

import java.util.Locale;

import static org.sephire.games.framework4x.core.model.game.CoreGameStateKeys.CURRENT_TURN;
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
		  .andThen(()->loadBottomBarElements(configuration))
		  .onFailure((error) -> log.error(String.format("Could not load successfully the plugin %s : %s", getPackageName(), error)))
		  .onSuccess((result) -> log.info(String.format("Plugin %s loaded successfully", getPackageName())))
		  .map(Functions::toVoid);
	}

	/**
	 * <p>Load the terrain mappings for the console client from the standard plugin terrain types.</p>
	 * @param configuration
	 * @return
	 */
	private Try<TerrainsMapping> loadTerrainMappings(Configuration.Builder configuration) {
		var terrainsTypesMappingsFilename = packageToFolderPath(getPackageName()).concat("/terrains-types-mappings.yaml");
		return ConfigLoader.getConfigFor(terrainsTypesMappingsFilename, TerrainsMapping.class)
		  .peek((terrainsMappings) -> configuration.putConfig(ConsoleClientConfigKeyEnum.TERRAIN_CHARACTER_MAPPING, terrainsMappings));
	}

	/**
	 * <p>Load the specific bottom bar elements for the game window for the console client from the interesting
	 * game state variables of the standard plugin or core framework.</p>
	 * @param configurationBuilder
	 * @return
	 */
	private Try<Void> loadBottomBarElements(Configuration.Builder configurationBuilder) {

		return Try.of(()->{
			var currentTurnElement = BottomBarElement.builder()
			  .from((configuration,game)-> Try.of(()->{
				  var currentTurn = game.getState(CURRENT_TURN, Integer.class)
					.getOrElseThrow((e)->e)
					.getOrElseThrow(()->new GameStateNotFoundException(CURRENT_TURN));

				  var label = configuration.getTranslationFor(Locale.ENGLISH,
					"framework4x.standard_terminal.ui.gamewindow.bottombar.current_turn.label",
					currentTurn)
					.getOrElseThrow(()->new I18NKeyNotFoundException("framework4x.standard_terminal.ui.gamewindow.bottombar.current_turn.label"));

				  return label;
				}))
			  .inPosition(BottomBarPosition.Left)
			  .build()
			  .getOrElseThrow(e->e);

			BottomBar.addElementToBottomBar(currentTurnElement,configurationBuilder).getOrElseThrow(e->e);
			return null;
		});
	}

}
