package org.sephire.games.framework4x.plugins.standard;

import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.plugins.configuration.PluginLifecycleHandler;
import org.sephire.games.framework4x.core.plugins.configuration.PluginLoadingHook;

@Slf4j
@PluginLifecycleHandler
public class Main {

	@PluginLoadingHook
	public Try<Void> pluginLoad(Configuration.Builder configuration) {
		return Try.of(() -> (Void) null)
		  .onSuccess((result) -> log.info("Plugin " + this.getClass().getPackageName() + " loaded"));
	}
}
