/**
 * 4X Framework - Core library - The core library on which to base the game
 * Copyright © 2018 Loïc Prieto Dehennault (loic.sephiroth@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
