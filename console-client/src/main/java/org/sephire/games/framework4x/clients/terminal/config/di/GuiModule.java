package org.sephire.games.framework4x.clients.terminal.config.di;

import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import dagger.Module;
import dagger.Provides;
import org.sephire.games.framework4x.clients.terminal.gui.MenuWindow;
import org.sephire.games.framework4x.clients.terminal.gui.gamewindow.GameWindow;
import org.sephire.games.framework4x.clients.terminal.gui.selectplugins.SelectPluginsWindow;
import org.sephire.games.framework4x.clients.terminal.gui.startgame.StartGameWindow;
import org.sephire.games.framework4x.clients.terminal.utils.UITranslationService;
import org.sephire.games.framework4x.core.plugins.PluginManager;

import javax.inject.Provider;

@Module(includes = {ConsoleClientModule.class, LanternaModule.class, CoreModule.class})
public class GuiModule {

	@Provides
	public SelectPluginsWindow provideSelectPluginsWindow(MultiWindowTextGUI textGUI,
														  UITranslationService i18n,
														  PluginManager pluginManager,
														  Provider<StartGameWindow> startGameWindow) {
		return new SelectPluginsWindow(pluginManager, textGUI, i18n, startGameWindow);
	}

	@Provides
	public GameWindow provideGameWindow(MultiWindowTextGUI textGUI,
										UITranslationService i18n) {
		return new GameWindow(textGUI,i18n);
	}

	@Provides
	public StartGameWindow provideStartGameWindow(UITranslationService i18n,
												  Provider<GameWindow> gameWindowProvider) {
		return new StartGameWindow(i18n, gameWindowProvider);
	}

	@Provides
	public MenuWindow provideMenuWindow(MultiWindowTextGUI textGUI,
										UITranslationService i18nService,
										PluginManager pluginManager,
										Provider<SelectPluginsWindow> selectPluginsWindow) {
		return new MenuWindow(textGUI, i18nService, pluginManager, selectPluginsWindow);
	}
}
