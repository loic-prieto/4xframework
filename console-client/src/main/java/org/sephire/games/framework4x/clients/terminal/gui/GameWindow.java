package org.sephire.games.framework4x.clients.terminal.gui;

import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Window;
import io.vavr.control.Try;
import org.sephire.games.framework4x.clients.terminal.config.TerrainsMapping;
import org.sephire.games.framework4x.clients.terminal.gui.components.map.MapComponent;
import org.sephire.games.framework4x.core.Game;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.model.map.GameMap;

import java.util.List;

import static org.sephire.games.framework4x.clients.terminal.config.ConsoleClientConfigKeyEnum.TERRAIN_CHARACTER_MAPPING;

/**
 * Represents the window of a game. Holds the map, the information/actions side panel, the top menu and the bottom
 * status bar.
 */
public class GameWindow extends BasicWindow {

	private MapComponent mapComponent;
	private Game game;


	private GameWindow(Game game) throws Throwable {
		super("Game Window");
		setHints(List.of(Window.Hint.FULL_SCREEN));

		this.game = game;

		var mappingsTry = game.getConfiguration().getConfiguration(TERRAIN_CHARACTER_MAPPING, TerrainsMapping.class);
		if(mappingsTry.isFailure()){
			throw mappingsTry.getCause();
		}

		this.mapComponent = new MapComponent(game.getMap(),mappingsTry.get());

		setComponent(mapComponent);
	}

	public static Try<GameWindow> of(Game game) {
		return Try.of(()->new GameWindow(game));
	}
}
