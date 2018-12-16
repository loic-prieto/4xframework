package org.sephire.games.framework4x.core.plugins.configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is to be used on classes that will handle the lifecycle events
 * of the plugin.
 *
 * There can only be one such class in a plugin, as of now. There is no particular reason
 * for that, just to simplify plugin configuration from a plugin author perspective.
 *
 * The class must have an empty constructor so that it can be instantiated by the core
 * framework.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PluginLifecycleHandler {
}
