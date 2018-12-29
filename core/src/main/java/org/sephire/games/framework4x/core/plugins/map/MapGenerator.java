package org.sephire.games.framework4x.core.plugins.map;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * This annotation is to be used on the methods of a class marked as a MapProvider. The method must have the signature:
 *   <pre>
 *   public Try&lt;GameMap&gt; nameOfMethod().
 *   </pre>
 * </p>
 *
 * <p>
 *  Example:
 * 	<pre>
 *    &#64;MapProvider
 *    public class PluginTestMapGenerator {
 *
 * 	  &#64;MapGenerator(name="hills_generator",displayName="plugintest.i18n.hills_generator_name")
 * 	  public Try&lt;GameMap&gt; generateRandomMap() {
 * 		return GameMap.builder()
 *           .addZone(MapZone.builder()
 *             .withName("level0")
 *             .withDefaultCells(new Size(20,20), PluginTerrainType.HILL)
 *             .build().get())
 *           .withDefaultZone("level0")
 *           .build()
 *           .get();
 * 	 }
 *  }
 *  </pre>
 * </p>
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MapGenerator {
	/**
	 * The name of the map generator, should be unique among all plugins.
	 * Perhaps a reverse-dns-like name can be used, as in java classes.
	 * @return
	 */
	String name();

	/**
	 * The i18 resource key to lookup to display the name of the map generator
	 * to the user.
	 * @return
	 */
	String displayKey();
}
