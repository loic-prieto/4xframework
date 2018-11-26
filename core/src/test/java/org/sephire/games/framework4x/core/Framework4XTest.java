package org.sephire.games.framework4x.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sephire.games.framework4x.core.plugins.PluginLoadingException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Framework4XTest {

    private static final String DUMMY_PLUGIN_NAME = "org.sephire.games.framework4x.testing.dummyPlugin";

    @Test
    @DisplayName("Should complain if a requested plugin doesn't exist in classpath when loading framework")
    public void should_complain_if_plugin_doesnt_exist_in_classpath() {
        var frameworkTry = Framework4X.buildWithPlugins("not_existent_package");

        assertTrue(frameworkTry.isFailure());
        assertTrue(frameworkTry.getCause().getClass().isAssignableFrom(PluginLoadingException.class));
    }

    @Test
    @DisplayName("Should complain if no plugin is specified while building the framework")
    public void should_complain_if_no_plugin_is_specified() {
        var frameworkTry = Framework4X.buildWithPlugins();

        assertTrue(frameworkTry.isFailure());
        assertTrue(frameworkTry.getCause().getClass().isAssignableFrom(IllegalArgumentException.class));
    }

    @Test
    @DisplayName("Should load successfully a plugin if its package is present in the classpath")
    public void should_load_successfully_plugin_present_on_classpath() {
        var frameworkTry = Framework4X.buildWithPlugins(DUMMY_PLUGIN_NAME);

        assertTrue(frameworkTry.isSuccess());
        assertTrue(frameworkTry.get().hasPlugin(DUMMY_PLUGIN_NAME));
    }

}
