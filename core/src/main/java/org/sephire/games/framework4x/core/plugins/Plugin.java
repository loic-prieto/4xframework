package org.sephire.games.framework4x.core.plugins;

import io.vavr.control.Try;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.Predicates.instanceOf;

public class Plugin {
    private static final String DEFAULT_MAIN_CLASS_NAME = "Main";

    private Package pluginPackage;

    private Plugin(Package pluginPackage) {
        this.pluginPackage = pluginPackage;
    }

    /**
     * Given a plugin name, it will try to load it from the classpath.
     * A plugin name is the name of its root java package, in which the Main class resides.
     * This will be changed in the future to be able to specify the main class to load.
     *
     * @param pluginName
     * @return
     */
    public static Try<Plugin> of(String pluginName) {
        // Load the main class of the plugin, for now a fixed class name at the root of the plugin/package name
        var mainPluginClass = Try.of(()->ClassLoader.getSystemClassLoader().loadClass(pluginName+"."+DEFAULT_MAIN_CLASS_NAME));

        // The unchecked ignore is needed for the mapFailure method
        //noinspection unchecked
        return mainPluginClass
                .mapFailure(Case($(instanceOf(ClassNotFoundException.class)),t -> new PluginNotFoundException(pluginName)))
                .map(Class::getPackage)
                .map(Plugin::new);
    }

    public String getName() {
        return pluginPackage.getName();
    }
}
