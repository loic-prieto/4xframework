package org.sephire.games.framework4x.core;

import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.Getter;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.plugins.Plugin;
import org.sephire.games.framework4x.core.plugins.PluginLoadingException;

/**
 * This is the main framework 4x component, it initializes every plugin and their configurations.
 * It holds the global data configuration that make up a game. It reloads itself when plugin configuration
 * changes.
 */
public class Game {

	@Getter
	private Configuration configuration;

	private Map<String, Plugin> plugins;

	private Game(List<Plugin> plugins, Configuration configuration) {
		this.plugins = HashMap.ofEntries(plugins.map((plugin) -> Map.entry(plugin.getSpecification().getPluginName(), plugin)));
		this.configuration = configuration;
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

		public Builder withPlugins(String... pluginsNames) {
			this.pluginNames = Option.of(List.of(pluginsNames));
			return this;
		}

		public Try<Game> build() {
			return Try.of(() -> {
				var configuration = new Configuration.Builder();

				var loadedPlugins = pluginNames
					.getOrElseThrow(() -> new PluginLoadingException("At least one plugin must be loaded", new IllegalArgumentException()))
					.map(Plugin::of);
				if (loadedPlugins.length() == 0) {
					throw new PluginLoadingException("At least one plugin must be loaded", new IllegalArgumentException());
				}
				if (loadedPlugins.find(Try::isFailure).isDefined()) {
					throw new PluginLoadingException(loadedPlugins.map(Try::getCause));
				}

				var configLoadingResult = loadedPlugins
					.map(Try::get)
					.map((plugin -> plugin.load(configuration)));
				if (configLoadingResult.find(Try::isFailure).isDefined()) {
					throw new PluginLoadingException(configLoadingResult.map(Try::getCause));
				}

				return new Game(
					loadedPlugins.map(Try::get),
					configuration.build());
			});
		}
	}
}
