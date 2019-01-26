/**
 * 4X Framework - Core library - The core library on which to base the game
 * Copyright © 2018 Loïc Prieto Dehennault (loic.sephiroth@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.sephire.games.framework4x.testing.testPlugin1;

import io.vavr.control.Try;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.model.game.Game;
import org.sephire.games.framework4x.core.plugins.configuration.GameLoadingHook;
import org.sephire.games.framework4x.core.plugins.configuration.PluginLifecycleHandler;
import org.sephire.games.framework4x.core.plugins.configuration.PluginLoadingHook;

import static org.sephire.games.framework4x.testing.testPlugin1.TestPlugin1ConfigKeys.TEST_VALUE;
import static org.sephire.games.framework4x.testing.testPlugin1.TestPlugin1GameStateKeys.KEY1;

@PluginLifecycleHandler
public class TestPlugin1Initializer {

	@PluginLoadingHook
	public Try<Void> handlePluginLoadingHook(Configuration.Builder configuration) {
		configuration.putConfig(TEST_VALUE,"someValue");

		return Try.success(null);
	}

	@GameLoadingHook
	public Try<Void> handleGameStartHook(Game game) {
		return Try.of(()->{
			game.putState(KEY1,"test");
			return null;
		});
	}
}
