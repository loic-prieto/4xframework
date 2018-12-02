package org.sephire.games.framework4x.clients.terminal;

import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import lombok.extern.slf4j.Slf4j;
import org.sephire.games.framework4x.clients.terminal.gui.MenuWindow;

import java.io.IOException;
import java.util.List;

@Slf4j
public class Launcher {

	public static void main(String[] args) {

		Terminal terminal = null;
		Screen screen = null;
		try {

			terminal = new DefaultTerminalFactory().createTerminal();
			screen = new TerminalScreen(terminal);

			WindowBasedTextGUI gui = new MultiWindowTextGUI(screen);
			screen.startScreen();

			Window menuWindow = new MenuWindow();
			menuWindow.setHints(List.of(Window.Hint.FULL_SCREEN));
			gui.addWindowAndWait(menuWindow);

		} catch (IOException ioe) {
			log.error("Error at the highest level: %s",ioe.getMessage());
		} finally {
			if(screen != null) {
				try {
					screen.stopScreen();
				} catch(IOException ioe){
					log.error("Could not close the screen successfully %s",ioe.getMessage());
				}
			}

		}

	}

}
