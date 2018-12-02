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

	public static class Builder {

		private Option<List<String>> pluginNames = Option.none();
		private Option<GameMap> map = Option.none();

		public Builder withPlugins(String... pluginsNames) {
			this.pluginNames = Option.of(List.of(pluginsNames));
			return this;
		}

		public Builder withMap(GameMap map) {
			this.map = Option.of(map);
			return this;
		}

		/**
		 * Builds the game with the given parameters.
		 * May return:
		 *  - PluginLoadingException
		 *  - IllegalArgumentException
		 * @return
		 */
		public Try<Game> build() {
			return Try.of(() -> {
				var configuration = new Configuration.Builder();

				var loadedPluginsTry = loadPlugins(this.pluginNames,configuration);
				if(loadedPluginsTry.isFailure()){
					throw loadedPluginsTry.getCause();
				}

				var selectedMap = map.getOrElseThrow(()->new IllegalArgumentException("The map is mandatory"));

				return new Game(
				  loadedPluginsTry.get(),
				  selectedMap,
				  configuration.build());
			});
		}

		/**
		 * Loads the list of plugins selected by the user.
		 * May return:
		 *  - PluginLoadingException
		 * @param plugins
		 * @param configuration
		 * @return
		 */
		private Try<List<Plugin>> loadPlugins(Option<List<String>> plugins,Configuration.Builder configuration) {
			return Try.of(()->{

				var loadedPlugins = plugins
				  .getOrElseThrow(() -> new PluginLoadingException("At least one plugin must be loaded", new IllegalArgumentException()))
				  .map(Plugin::of);
				if (loadedPlugins.length() == 0) {
					throw new PluginLoadingException("At least one plugin must be loaded", new IllegalArgumentException());
				}
				if (loadedPlugins.find(Try::isFailure).isDefined()) {
					throw new PluginLoadingException(loadedPlugins.filter(Try::isFailure).map(Try::getCause));
				}

				var configLoadingResult = loadedPlugins
				  .map(Try::get)
				  .map((plugin -> plugin.load(configuration)));
				if (configLoadingResult.find(Try::isFailure).isDefined()) {
					throw new PluginLoadingException(configLoadingResult
					  .filter(Try::isFailure)
					  .map(Try::getCause));
				}

				return loadedPlugins.map(Try::get);
			});
		}
	}
}
