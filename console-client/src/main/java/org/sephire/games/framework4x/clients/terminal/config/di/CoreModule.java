package org.sephire.games.framework4x.clients.terminal.config.di;

import dagger.Module;
import dagger.Provides;
import org.sephire.games.framework4x.core.plugins.PluginManager;

import javax.inject.Singleton;

@Module
public class CoreModule {

	@Provides
	@Singleton
	public PluginManager providePluginManager() {
		return new PluginManager();
	}
}
