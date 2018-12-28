package org.sephire.games.framework4x.testing.testPlugin2;

import io.vavr.control.Try;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.plugins.configuration.PluginLifecycleHandler;
import org.sephire.games.framework4x.core.plugins.configuration.PluginLoadingHook;

@PluginLifecycleHandler
public class TestPlugin2Initializer {

	@PluginLoadingHook
	public Try<Void> handlePluginLoadingHook(Configuration.Builder configuration) {
		return Try.success(null);
	}
}
