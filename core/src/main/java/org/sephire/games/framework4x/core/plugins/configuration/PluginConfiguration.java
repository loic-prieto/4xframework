package org.sephire.games.framework4x.core.plugins.configuration;

import io.vavr.collection.List;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.model.game.GameParameter;

/**
 * This interface must be implemented by the main class of a plugin, so that it
 * can hook itself into the plugin loading process, and the game loading process.
 *
 * All methods are default methods with an empty implementation, so that the implementing
 * class can adopt gradually what it needs from this interface.
 */
public interface PluginConfiguration {

	/**
	 * This method is called when the framework is loading the plugin.
	 * It is guaranteed to be called after its parent plugin has been called, which
	 * means that it will have access to the configuration the parent plugin wrote.
	 * It will be called after the automatic resources have been loaded, too.
	 *
	 * It is the place to initializer resources and to set special configurations for
	 * the application. Per game configurations should be put in the post/pre game start
	 * methods.
	 *
	 * @param configuration
	 */
	default void pluginLoading(Configuration.Builder configuration) {}

	/**
	 * This method is called when the game is being loaded. This is the place to put
	 * specific per-game configuration while creating the game.
	 *
	 * The game parameters will have already been loaded into the configuration by that
	 * point, and also the configuration of the plugins to which this one is depending on.
	 *
	 * Game parameters may be retrieved under the key of the parameter in the configuration
	 * object.
	 *
	 * @param configuration
	 */
	default void gameLoading(Configuration.Builder configuration) {}

	/**
	 * This method will be called by the framework to know which parameters must be filled by the user
	 * when creating a game.
	 *
	 * They will be available to the plugin inside the gameLoading callback method in the configuration object.
	 *
	 * @see GameParameter
	 * @return
	 */
	default List<? extends GameParameter> buildGameParameters() { return List.empty(); }

}
