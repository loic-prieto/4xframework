package org.sephire.games.framework4x.clients.terminal;

import com.googlecode.lanterna.gui2.BasicWindow;
import org.sephire.games.framework4x.clients.terminal.map.MapPanel;
import org.sephire.games.framework4x.core.model.map.GameMap;

public class GameWindow extends BasicWindow {

	private MapPanel mapPanel;


	public GameWindow(GameMap map) {
		super("Game Window");


		this.mapPanel = new MapPanel(map);


		setComponent(mapPanel);
	}
}
