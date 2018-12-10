package org.sephire.games.framework4x.core.plugins.map;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is to be used on the methods of a class marked as a MapProvider. The method must have the signature:
 * public GameMap nameOfMethod().
 *
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MapGenerator {
	/**
	 * The identifier of map provider. Must be unique among all plugins.
	 * It's probably a good idea to prefix it with the package name.
	 */
	String name();

	/**
	 * The i18n resource key that will be shown to the user when displaying
	 * this map generator
	 */
	String label() default "core.map.generator.generic-name";
}
