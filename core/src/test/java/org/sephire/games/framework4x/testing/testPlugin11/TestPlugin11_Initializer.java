package org.sephire.games.framework4x.testing.testPlugin11;

import io.vavr.control.Try;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.plugins.configuration.PluginLifecycleHandler;
import org.sephire.games.framework4x.core.plugins.configuration.PluginLoadingHook;

@PluginLifecycleHandler
public class TestPlugin11_Initializer {

	@PluginLoadingHook
	public Try<Void> handlePluginLoadingHook(Configuration.Builder configuration) {
		return Try.success(null);
	}
}
