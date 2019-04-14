package org.sephire.games.framework4x.clients.terminal.config.di;

import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.screen.Screen;
import dagger.Component;

import javax.inject.Singleton;

@Component(modules = {LanternaModule.class})
@Singleton
public interface LanternaFactory {
	Screen buildScreen();
	MultiWindowTextGUI buildGUI();
}
