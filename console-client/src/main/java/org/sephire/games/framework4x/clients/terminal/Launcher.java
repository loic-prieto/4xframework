package org.sephire.games.framework4x.clients.terminal;

import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.List;

public class Launcher {

	public static void main(String[] args) {

		try {

			Terminal term = new DefaultTerminalFactory().createTerminal();
			Screen screen = new TerminalScreen(term);
			WindowBasedTextGUI gui = new MultiWindowTextGUI(screen);
			screen.startScreen();
			Window menuWindow = new MenuWindow();
			menuWindow.setHints(List.of(Window.Hint.FULL_SCREEN));
			gui.addWindowAndWait(menuWindow);
			screen.stopScreen();

		} catch (IOException ioe) {
			System.out.print(ioe);
		}

	}

}
