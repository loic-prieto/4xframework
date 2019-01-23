/**
 * 4X Framework - Standard plugin - The standard base plugin for a game with a civ-like approach
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
package org.sephire.games.framework4x.plugins.standard;

import io.vavr.collection.HashMap;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.model.game.Game;
import org.sephire.games.framework4x.core.plugins.configuration.GameLoadingHook;
import org.sephire.games.framework4x.core.plugins.configuration.PluginLifecycleHandler;
import org.sephire.games.framework4x.core.plugins.configuration.PluginLoadingHook;

import static org.sephire.games.framework4x.plugins.standard.StandardStateKey.*;

@Slf4j
@PluginLifecycleHandler
public class Main {

	@PluginLoadingHook
	public Try<Void> pluginLoad(Configuration.Builder configuration) {
		return Try.of(() -> (Void) null)
		  .onSuccess((result) -> log.info("Plugin " + this.getClass().getPackageName() + " loaded"));
	}

	@GameLoadingHook
	public Try<Void> gameLoad(Game game) {
		return Try.of(()->{
			game.putState(MONEY,0);
			game.putState(RESEARCH, HashMap.of("Black magic basic",34));
			game.putState(CURRENT_RESEARCH,"Black magic basic");

			return null;
		});
	}
}
