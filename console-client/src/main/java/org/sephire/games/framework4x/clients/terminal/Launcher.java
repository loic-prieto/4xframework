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

import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import lombok.extern.slf4j.Slf4j;
import org.sephire.games.framework4x.clients.terminal.gui.MenuWindow;

import java.io.IOException;
import java.util.List;

@Slf4j
public class Launcher {

	public static void main(String[] args) {

		Terminal terminal = null;
		Screen screen = null;
		try {

			terminal = new DefaultTerminalFactory().createTerminal();
			screen = new TerminalScreen(terminal);

			WindowBasedTextGUI gui = new MultiWindowTextGUI(screen);
			screen.startScreen();

			Window menuWindow = new MenuWindow(gui);
			menuWindow.setHints(List.of(Window.Hint.FULL_SCREEN));
			gui.addWindowAndWait(menuWindow);

		} catch (IOException ioe) {
			log.error("Error at the highest level: %s",ioe.getMessage());
		} finally {
			if(screen != null) {
				try {
					screen.stopScreen();
				} catch(IOException ioe){
					log.error("Could not close the screen successfully %s",ioe.getMessage());
				}
			}

		}

	}

}
