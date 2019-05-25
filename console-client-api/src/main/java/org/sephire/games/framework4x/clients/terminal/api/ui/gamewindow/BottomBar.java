/**
 * 4X Framework - Console client - API - The console client offers an API that plugins that interact with the console can consume. This mainly
        avoid having the client and the related plugin have a cyclic dependency.
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
package org.sephire.games.framework4x.clients.terminal.api.ui.gamewindow;

import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Try;
import org.sephire.games.framework4x.core.model.config.Configuration;

import static org.sephire.games.framework4x.clients.terminal.api.config.ConsoleClientConfigKeyEnum.BOTTOM_BAR_ELEMENTS;

/**
 * Utility functions to manipulate the bottom bar element in the Game Window.
 */
public class BottomBar {

	/**
	 * <p>Adds an UI element to the bottom bar, in one of the available slots.</p>
	 * <p>The element and position is stored in the configuration object and will be handled
	 * by the BottomBar element in the GameWindow as it initializes</p>
	 * <p>The plugins may use this function while they initialize, in the plugin initialize hook which is when they
	 * have access to the Configuration.Builder object</p>
	 * <p>Calling this function outside of the plugin loading hook will have no effect, since the configuration
	 * will have been built and it is immutable</p>
	 * @param element
	 * @param configuration must be the Configuration.Builder specifically used while calling the plugin initialization
	 *                      hook.
	 * @return
	 */
	public static Try<Void> addElementToBottomBar(BottomBarElement element, Configuration.Builder configuration) {
		return Try.of(()->{
			var position = element.getPosition();
			var elements = configuration.getConfig(BOTTOM_BAR_ELEMENTS, Map.class)
			  .getOrElseThrow(e->e)
			  .map(config->(Map<BottomBarPosition, List<BottomBarElement>>)config)
			  .getOrElse(HashMap.empty());
			var elementsInPosition = elements.get(position).getOrElse(List.empty());

			configuration.putConfig(BOTTOM_BAR_ELEMENTS,elements.put(position,elementsInPosition.append(element)));

			return null;
		});


	}
}
