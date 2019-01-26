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
package org.sephire.games.framework4x.clients.terminal.gui.gamewindow;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.input.KeyStroke;
import io.vavr.control.Try;
import org.sephire.games.framework4x.clients.terminal.gui.Basic4XWindow;
import org.sephire.games.framework4x.clients.terminal.gui.gamewindow.map.MapComponent;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.model.game.Game;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

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

		setupFrame();

		var backgroundPanel = new Panel();
		backgroundPanel.setLayoutManager(new BorderLayout());

		var mapComponentTry = MapComponent.of(game,this);
		if(mapComponentTry.isFailure()) {
			throw mapComponentTry.getCause();
		}
		backgroundPanel.addComponent(mapComponentTry.get(),BorderLayout.Location.CENTER);

		setupBottomBar(backgroundPanel);

		setComponent(backgroundPanel);
	}

	private void setupBottomBar(Panel container) {
		var bottomBar = new BottomBarComponent(game,this);
		bottomBar.setLayoutData(BorderLayout.Location.BOTTOM);
		container.addComponent(bottomBar);
	}

	private void setupFrame(){

		addWindowListener(new WindowListenerAdapter() {
			@Override
			public void onInput(Window basePane, KeyStroke keyStroke, AtomicBoolean deliverEvent) {
				// React to map scroll events
				var potentialMapScrollEvent = MapScrollEvent.fromKeyStroke(keyStroke);
				if(potentialMapScrollEvent.isDefined()){
					fireEvent(potentialMapScrollEvent.get());
				}

				// React to cursor movement
				var potentialCursorMovement = CursorMoveEvent.fromKeyStroke(keyStroke);
				if(potentialCursorMovement.isDefined()){
					fireEvent(potentialCursorMovement.get());
				}
			}
		});
	}

	public static Try<GameWindow> of(Game game,WindowBasedTextGUI textGUI) {
		return Try.of(()->
		  new GameWindow(game,textGUI)
		);
	}
}
