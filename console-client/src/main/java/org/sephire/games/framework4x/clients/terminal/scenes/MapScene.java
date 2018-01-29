package org.sephire.games.framework4x.clients.terminal.scenes;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import io.vavr.collection.Map;
import org.sephire.games.framework4x.clients.terminal.Drawable;
import org.sephire.games.framework4x.clients.terminal.ui.TopMenu;
import org.sephire.games.framework4x.clients.terminal.ui.Viewport;
import org.sephire.games.framework4x.core.model.map.GameMap;
import org.sephire.games.framework4x.core.model.map.Item;
import org.sephire.games.framework4x.core.model.map.Range;

/**
 * This is the main scene of the game.
 * Here the global map is drawn, with all it's different layers.
 * The map has:
 * - The main map view, filling almost every tile of the screen.
 * - A menu on top to bring up submenus to handle the game
 * -
 */
public class MapScene implements Scene, Drawable {
    private Screen screen;
    private GameMap map;
    private TopMenu topMenu;
    private Viewport viewport;
    private Range range;
    private Map<>

    public MapScene(Screen screen, GameMap gameMap) {
        this.screen = screen;
        this.map = gameMap;
    }

    @Override
    public void draw(Screen screen) {
        TextGraphics g = screen.newTextGraphics();

        for (Item item : map.getVisibleActiveItems(viewport.toRange()).values()) {

        }
    }

}
