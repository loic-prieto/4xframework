package org.sephire.games.framework4x.clients.terminal.config.di;

import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import dagger.Module;
import dagger.Provides;
import org.sephire.games.framework4x.clients.terminal.gui.MenuWindow;
import org.sephire.games.framework4x.clients.terminal.utils.UITranslationService;

@Module(includes = {ConsoleClientModule.class,LanternaModule.class})
public class GuiModule {

	@Provides
	public MenuWindow provideMenuWindow(MultiWindowTextGUI textGUI, UITranslationService i18nService) {
		return new MenuWindow(textGUI,i18nService);
	}
}
