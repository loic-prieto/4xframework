package org.sephire.games.framework4x.core.plugins.map;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is to be used on a class that will act as a map generator, to let the core
 * framework know that it can use it to get map generators that can be used by clients to let the user choose
 * a map.
 *
 * Example:
 * <pre>
 * @MapProvider
 * public class PluginTestMapGenerator {
 *
 * 	@MapGenerator(name="hills_generator",displayName="plugintest.i18n.hills_generator_name")
 * 	public GameMap generateRandomMap() {
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