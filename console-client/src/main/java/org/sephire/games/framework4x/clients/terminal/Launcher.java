package org.sephire.games.framework4x.clients.terminal;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.TerminalFactory;

import java.io.IOException;

public class Launcher {

    public static void main(String[] args) {
        TerminalFactory factory = new DefaultTerminalFactory();

        try (Terminal terminal = factory.createTerminal()) {

            boolean shouldExit = false;

            terminal.enterPrivateMode();
            TerminalScreen screen = new TerminalScreen(terminal);
            screen.setCursorPosition(null);
            TerminalSize terminalSize = terminal.getTerminalSize();

            screen.startScreen();
            while (!shouldExit) {
                KeyStroke keyStroke = screen.pollInput();
                if (keyStroke != null && (keyStroke.getKeyType() == KeyType.Escape || keyStroke.getKeyType() == KeyType.EOF)) {
                    shouldExit = true;
                }

                TerminalSize newSize = screen.doResizeIfNecessary();
                if (newSize != null) {
                    terminalSize = newSize;
                }

                drawMenuBar(screen, terminalSize);
                drawBackground(screen, terminalSize);

                screen.refresh();

                Thread.yield();
            }

        } catch (IOException e) {
            System.out.println("Error while using terminal: " + e.getMessage());
        }
    }

    public static void drawMenuBar(Screen screen, TerminalSize size) {
        TextGraphics text = screen.newTextGraphics();
        text.setBackgroundColor(TextColor.Indexed.fromRGB(100, 100, 255));
        text.setForegroundColor(TextColor.Indexed.fromRGB(50, 50, 155));
        text.drawRectangle(TerminalPosition.TOP_LEFT_CORNER, size, ' ');
    }

    public static void drawBackground(Screen screen, TerminalSize size) {
        TextGraphics text = screen.newTextGraphics();
        text.setBackgroundColor(TextColor.Indexed.fromRGB(200, 200, 200));
        text.fillRectangle(
                new TerminalPosition(0, 1),
                new TerminalSize(size.getColumns(), size.getRows() - 1),
                ' ');
    }
}
