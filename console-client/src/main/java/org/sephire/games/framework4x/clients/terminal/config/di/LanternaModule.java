package org.sephire.games.framework4x.clients.terminal.config.di;

import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import dagger.Module;
import dagger.Provides;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;
import java.io.IOException;

@Module
@Slf4j
public class LanternaModule {

	@Provides @Singleton
	public Screen provideScreen() {
		Screen screen = null;

		try {
			screen = new DefaultTerminalFactory().createScreen();
		}catch (IOException e) {
			log.error(String.format("Error while instantiating a terminal screen: %s",e.getMessage()),e);
			System.exit(1);
		}

		return screen;
	}

	@Provides @Singleton
	public MultiWindowTextGUI provideTextGUI(Screen screen) {
		return new MultiWindowTextGUI(screen);
	}
}
