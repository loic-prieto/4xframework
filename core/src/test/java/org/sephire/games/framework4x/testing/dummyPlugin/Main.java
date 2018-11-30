package org.sephire.games.framework4x.testing.dummyPlugin;

import io.vavr.control.Try;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.plugins.PluginInitializer;

import static io.vavr.control.Try.success;
import static org.sephire.games.framework4x.testing.dummyPlugin.DummyPluginConfigKeyEnum.KEY1;

/**
 * The main plugin class for the Dummy test plugin
 */
public class Main implements PluginInitializer {

	@Override
	public Try<Void> pluginLoad(Configuration.Builder configuration) {
		configuration.addConfig(KEY1, "test");
		return success(null);
	}
}
