package org.sephire.games.framework4x.clients.terminal.ui;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import io.vavr.collection.List;
import lombok.Value;
import org.sephire.games.framework4x.clients.terminal.Drawable;

@Value
public class TopMenu implements Drawable {
    private static TextColor backgroundColor = TextColor.Indexed.fromRGB(100, 100, 255);
    private static TextColor foregroundColor = TextColor.Indexed.fromRGB(200, 200, 200);
    private List<Submenu> submenus;

    public TopMenu(Submenu... submenus) {
        this.submenus = List.of(submenus);
    }

    @Override
    public void draw(Screen screen) {
        TextGraphics g = screen.newTextGraphics();
        int maxSubmenuTitleLength = this.submenus
                .map(submenu -> submenu.getName().length())
                .max()
                .getOrElse(10);
        int currentSubmenu = 0;
        g.setBackgroundColor(backgroundColor);
        g.setForegroundColor(foregroundColor);

        for (Submenu submenu : submenus) {
            int x = currentSubmenu * maxSubmenuTitleLength;
            g.fillRectangle(new TerminalPosition(x, 0), new TerminalSize(maxSubmenuTitleLength, 1), ' ');
            g.putString(x + 1, 0, submenu.getName() + '|');
        }
    }
}
