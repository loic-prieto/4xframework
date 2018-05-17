package org.sephire.games.framework4x.clients.terminal.scenes;

import com.googlecode.lanterna.screen.Screen;
import org.sephire.games.framework4x.clients.terminal.ui.Menu;
import org.sephire.games.framework4x.clients.terminal.ui.Painter;
import org.sephire.games.framework4x.clients.terminal.ui.Viewport;
import org.sephire.games.framework4x.core.model.map.GameMap;
import org.sephire.games.framework4x.core.model.map.Item;

/**
 * This is the main scene of the game.
 * Here the global map is drawn, with all it's different layers.
 * The map has:
 * - The main map view, filling almost every tile of the screen.
 * - A menu on top to bring up submenus to handle the game
 * - a status bar with quick information for the player
 */
public class MapScene implements Scene {
	private GameMap map;
	private Menu topMenu;
	private Painter painter;

	public MapScene(Screen screen, GameMap gameMap) {
		map = gameMap;
		painter = new Painter(
				screen,
				new Viewport(
						0,
						0,
						screen.getTerminalSize().getColumns(),
						screen.getTerminalSize().getRows())
		);
	}

	public void draw() {
		for (Item item : map.getVisibleActiveItems(viewport.toRange()).values()) {

		}
	}

}
