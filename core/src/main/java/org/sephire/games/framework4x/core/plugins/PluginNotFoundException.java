package org.sephire.games.framework4x.core.plugins;

import org.sephire.games.framework4x.core.Framework4XException;

/**
 * When a plugin could not be found when trying to load it
 */
public class PluginNotFoundException extends Framework4XException {

    private String pluginName;

    public PluginNotFoundException(String pluginName) {
        this.pluginName = pluginName;
    }

    @Override
    public String getMessage() {
        return "Package "+pluginName+" has not been found in the classpath";
    }
}
