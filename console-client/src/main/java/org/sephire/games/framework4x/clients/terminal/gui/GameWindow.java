package org.sephire.games.framework4x.clients.terminal.gui;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import io.vavr.control.Try;
import org.sephire.games.framework4x.clients.terminal.api.config.TerrainsMapping;
import org.sephire.games.framework4x.clients.terminal.gui.components.map.MapComponent;
import org.sephire.games.framework4x.core.Game;

import java.util.List;

import static org.sephire.games.framework4x.clients.terminal.api.config.ConsoleClientConfigKeyEnum.TERRAIN_CHARACTER_MAPPING;

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

		var backgroundPanel = new Panel();
		backgroundPanel.setLayoutManager(new BorderLayout());

		var mappingsTry = game.getConfiguration().getConfiguration(TERRAIN_CHARACTER_MAPPING, TerrainsMapping.class);
		if(mappingsTry.isFailure()){
			throw mappingsTry.getCause();
		}

		this.mapComponent = new MapComponent(game.getMap(),mappingsTry.get());
		this.mapComponent.setLayoutData(BorderLayout.Location.CENTER);
		backgroundPanel.addComponent(mapComponent);

		setComponent(backgroundPanel);
	}

	public static Try<GameWindow> of(Game game) {
		return Try.of(()->new GameWindow(game));
	}
}
