package org.sephire.games.framework4x.clients.terminal.config.di;

import dagger.Component;
import org.sephire.games.framework4x.clients.terminal.gui.MenuWindow;

import javax.inject.Singleton;

@Component(modules = {GuiModule.class})
@Singleton
public interface ConsoleClientWindowsFactory {
	MenuWindow buildMenuWindow();
}
