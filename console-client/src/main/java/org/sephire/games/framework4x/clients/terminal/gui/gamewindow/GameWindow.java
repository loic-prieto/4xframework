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

		backgroundPanel.addComponent(buildMapView(),BorderLayout.Location.CENTER);

		setComponent(backgroundPanel);
	}

	private MapComponent buildMapView() {
		var mapComponentTry = MapComponent.of(game);
		if(mapComponentTry.isFailure()){
			MessageDialog.showMessageDialog(getOverridenTextGui(),"Error","gameWindow.couldNotCreateMapView", MessageDialogButton.OK);
			close();
		}

		return mapComponentTry.get();
	}

	public static Try<GameWindow> of(Game game,WindowBasedTextGUI textGUI) {
		return Try.of(()->
		  new GameWindow(game,textGUI)
		);
	}
}
