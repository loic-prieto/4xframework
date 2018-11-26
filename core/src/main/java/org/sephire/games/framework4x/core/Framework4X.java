package org.sephire.games.framework4x.core;

import io.vavr.collection.List;
import io.vavr.control.Try;
import org.sephire.games.framework4x.core.plugins.Plugin;
import org.sephire.games.framework4x.core.plugins.PluginLoadingException;

import static io.vavr.control.Try.failure;
import static io.vavr.control.Try.success;

/**
 * This is the main framework 4x component, it initializes every plugin and their configurations.
 * It holds the global data configuration that make up a game. It reloads itself when plugin configuration
 * changes.
 */
public class Framework4X {


    private List<Plugin> plugins;

    private Framework4X(List<Plugin> plugins) {
        this.plugins = plugins;
    }

    public static Try<Framework4X> buildWithPlugins(String... pluginsNames) {
        if(pluginsNames.length == 0) {
            return failure(new IllegalArgumentException("At least one plugin must be specified"));
        }

        var loadedPlugins = List.of(pluginsNames)
                .map(Plugin::of);

        return loadedPlugins.find(Try::isFailure).isDefined() ?
                failure(new PluginLoadingException(loadedPlugins.map(Try::getCause))) :
                success(new Framework4X(loadedPlugins.map(Try::get)));
    }

    /**
     * Returns whether the specified plugin has been loaded into the framework.
     *
     * @param pluginName
     * @return
     */
    public boolean hasPlugin(String pluginName) {
        return plugins
                .find((plugin -> plugin.getName().equals(pluginName)))
                .isDefined();
    }
}
