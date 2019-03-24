package org.sephire.games.framework4x.clients.terminal.gui.gamewindow.topmenu;

import org.sephire.games.framework4x.clients.terminal.api.ui.gamewindow.GameWindowUtils;
import org.sephire.games.framework4x.core.model.game.GameCommand;
import org.sephire.games.framework4x.core.model.game.GameCommandCategory;

/**
 * This class holds the default game commands to be loaded by the top menu bar.
 */
public class DefaultGameCommands {

	public static GameCommandCategory defaultGameCommands() {
		var gameCategory = new GameCommandCategory("game","gamewindow.topmenu.game.label");
		var quitCommand = new GameCommand<Void>("game.quit","gamewindow.topmenu.game.quit.label",(game)->{
			GameWindowUtils.getCurrentGameWindow().get().closeWindow();
			return null;
		},"game");
		var saveCommand = new GameCommand<Void>("game.save","gamewindow.topmenu.game.save.label",(game)->{
			return null;
		},"game");
		var loadCommand = new GameCommand<Void>("game.load","gamewindow.topmenu.game.load.label",(game)->{
			return null;
		},"game");

		gameCategory.addGameCommand(saveCommand);
		gameCategory.addGameCommand(loadCommand);
		gameCategory.addGameCommand(quitCommand);

		return gameCategory;
	}
}
