/**
 * 4X Framework - Console client - A terminal-based client for the 4X framework
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
package org.sephire.games.framework4x.clients.terminal.gui.gamewindow;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import io.vavr.control.Try;
import org.sephire.games.framework4x.clients.terminal.gui.Basic4XWindow;
import org.sephire.games.framework4x.clients.terminal.gui.components.map.MapComponent;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.model.game.Game;
import org.sephire.games.framework4x.core.plugins.PluginManager;

import java.util.List;

/**
 * Represents the window of a game. Holds the map, the information/actions side panel, the top menu and the bottom
 * status bar.
 */
public class GameWindow extends Basic4XWindow {

	private Game game;
	private Configuration configuration;

	private GameWindow(Game game, WindowBasedTextGUI textGUI) throws Throwable {
		super("Game Window",textGUI);
		setHints(List.of(Window.Hint.FULL_SCREEN));

		this.game = game;
		this.configuration = game.getConfiguration();

		var backgroundPanel = new Panel();
		backgroundPanel.setLayoutManager(new BorderLayout());

		var mapComponentTry = MapComponent.of(game);
		if(mapComponentTry.isFailure()) {
			throw mapComponentTry.getCause();
		}
		backgroundPanel.addComponent(mapComponentTry.get(),BorderLayout.Location.CENTER);

		setComponent(backgroundPanel);
	}

	public static Try<GameWindow> of(Game game,WindowBasedTextGUI textGUI) {
		return Try.of(()->
		  new GameWindow(game,textGUI)
		);
	}
}
