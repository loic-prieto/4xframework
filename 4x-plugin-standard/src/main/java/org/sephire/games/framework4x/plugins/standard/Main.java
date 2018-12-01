package org.sephire.games.framework4x.plugins.standard;

import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.plugins.PluginInitializer;

@Slf4j
public class Main implements PluginInitializer {
	@Override
	public Try<Void> pluginLoad(Configuration.Builder configuration) {
		return Try.of(() -> (Void) null)
		  .onSuccess((result) -> log.info("Plugin " + this.getClass().getPackageName() + " loaded"));
	}
}
