package org.sephire.games.framework4x.core.plugins;

import io.vavr.control.Try;
import org.sephire.games.framework4x.core.model.config.Configuration;

import static io.vavr.control.Try.success;

/**
 * The main class of a plugin must implement this interface, which provides for callbacks
 * into the plugin loading process.
 */
public interface PluginInitializer {

	/**
	 * Implement this method to get a hook into the plugin load process when all automatic configuration
	 * has been loaded.
	 * <p>
	 * This is the moment to add additional programmatic configuration to the game. The method must return either a
	 * success or a failure loading the plugin. If the plugin returns a failure, all dependent plugins won't be loaded
	 * and the error will be registered.
	 * <p>
	 * By default, this method returns a success and performs nothing.
	 *
	 * @param configuration
	 * @return
	 */
	default Try<Void> pluginLoad(Configuration.Builder configuration) {
		return success(null);
	}
}
