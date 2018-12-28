package org.sephire.games.framework4x.testing.testPlugin1;

import io.vavr.control.Try;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.plugins.configuration.PluginLifecycleHandler;
import org.sephire.games.framework4x.core.plugins.configuration.PluginLoadingHook;

import static org.sephire.games.framework4x.testing.testPlugin1.TestPlugin1ConfigKeys.TEST_VALUE;

@PluginLifecycleHandler
public class TestPlugin1Initializer {

	@PluginLoadingHook
	public Try<Void> handlePluginLoadingHook(Configuration.Builder configuration) {
		configuration.addConfig(TEST_VALUE,"someValue");

		return Try.success(null);
	}
}
