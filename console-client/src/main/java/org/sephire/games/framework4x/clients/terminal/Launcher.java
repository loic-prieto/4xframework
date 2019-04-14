/**
 * 4X Framework - Console client - A terminal-based client for the 4X framework
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
package org.sephire.games.framework4x.clients.terminal;

import lombok.extern.slf4j.Slf4j;
import org.sephire.games.framework4x.clients.terminal.config.di.DaggerConsoleClientWindowsFactory;
import org.sephire.games.framework4x.clients.terminal.config.di.DaggerLanternaFactory;

@Slf4j
public class Launcher {

	public static void main(String[] args) {

		try {
			var lanternaFactory = DaggerLanternaFactory.create();
			var screen = lanternaFactory.buildScreen();
			var gui = lanternaFactory.buildGUI();

			var menuWindow = DaggerConsoleClientWindowsFactory.create()
			  .buildMenuWindow();

			screen.startScreen();
			gui.addWindowAndWait(menuWindow);

		} catch(Throwable e) {
			System.out.println(String.format("There was an error while launching the application: %s",e.getMessage()));
			System.exit(1);
		}

	}

}
