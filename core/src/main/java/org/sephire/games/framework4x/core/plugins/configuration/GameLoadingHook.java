package org.sephire.games.framework4x.core.plugins.configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is to be used on methods of a @PluginLifecycleHandler annotated class that
 * handle the event of a game using this plugin being created.
 *
 * This method is called when all other parent plugins have been called, and is a last chance to
 * add additional configuration for a game. All game parameters defined by this plugin and its parents
 * are loaded in the configuration at this point, so they can be used to manipulate the game.
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
public @interface GameLoadingHook {
}
