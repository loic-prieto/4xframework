package org.sephire.games.framework4x.core;

import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.Getter;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.model.map.GameMap;
import org.sephire.games.framework4x.core.plugins.Plugin;
import org.sephire.games.framework4x.core.plugins.PluginLoadingException;

/**
 * This is the main framework 4x component, it initializes every plugin and their configurations.
 * It holds the global data configuration that make up a game. It reloads itself when plugin configuration
 * changes.
 */
public class Game {

	@Getter
	private GameMap map;
	@Getter
	private Configuration configuration;
	private Map<String, Plugin> plugins;

	private Game(List<Plugin> plugins, GameMap map,Configuration configuration) {
		this.plugins = HashMap.ofEntries(plugins.map((plugin) -> Map.entry(plugin.getSpecification().getPluginName(), plugin)));
		this.configuration = configuration;
		this.map = map;
	}

	/**
	 * Returns whether the specified plugin has been loaded into the framework.
	 *
	 * @param pluginName
	 * @return
	 */
	public boolean hasPlugin(String pluginName) {
		return plugins
		  .get(pluginName)
		  .isDefined();
	}

	public Option<Plugin> getPlugin(String pluginName) {
		return plugins.get(pluginName);
	}
}
