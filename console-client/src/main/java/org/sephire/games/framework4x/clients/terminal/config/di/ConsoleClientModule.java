package org.sephire.games.framework4x.clients.terminal.config.di;

import dagger.Module;
import dagger.Provides;
import org.sephire.games.framework4x.clients.terminal.utils.UITranslationService;

import javax.inject.Singleton;

@Module
public class ConsoleClientModule {

	@Provides @Singleton
	public UITranslationService provideTranslationService() {
		return new UITranslationService();
	}
}
