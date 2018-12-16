package org.sephire.games.framework4x.core.plugins.configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is to be used on methods of a @PluginLifecycleHandler annotated class that
 * handle the event of this plugin being loaded.
 * This method is called after all automatic resources have been loaded, and the parent plugins
 * have been loaded too, as a chance to add some more configuration values. All other providers
 * have been called before this method.
 *
 * There can only be one method in the class with this annotation, a design decision to simplify
 * the API.
 *
 * The signature of the method must accept a Configuration.Builder parameter that will hold all
 * the configuration data loaded up until that point. The method must return a Try object, whose
 * correct value won't be used.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PluginLoadingHook {
}
