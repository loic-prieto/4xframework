package org.sephire.games.framework4x.clients.terminal;

import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Panel;
import org.sephire.games.framework4x.core.model.map.GameMap;

public class GameWindow extends BasicWindow {

	private GameMap map;

	public GameWindow(GameMap map) {
		super("Game Window");
		this.map = map;


		var mapPanel = new Panel();



		setComponent(mapPanel);
	}
}
