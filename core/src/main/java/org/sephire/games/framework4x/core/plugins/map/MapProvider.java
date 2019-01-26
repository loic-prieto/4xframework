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
 * This annotation is to be used on a class that will act as a map generator, to let the core
 * framework know that it can use it to get map generators that can be used by clients to let the user choose
 * a map.
 * </p>
 *
 * Example:
 * <pre>
 * &#64;MapProvider
 * public class PluginTestMapGenerator {
 *
 * 	&#64;MapGenerator(name="hills_generator",displayName="plugintest.i18n.hills_generator_name")
 * 	public Try&lt;GameMap&gt; generateRandomMap() {
 * 		return GameMap.builder()
 *           .addZone(MapZone.builder()
 *             .withName("level0")
 *             .withDefaultCells(new Size(20,20), PluginTerrainType.HILL)
 *             .build().get())
 *           .withDefaultZone("level0")
 *           .build()
 *           .get();
 * 	}
 * }
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MapProvider {
}
