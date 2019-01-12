/**
 * 4X Framework - Standard plugin - The standard base plugin for a game with a civ-like approach
 * Copyright © 2018 Loïc Prieto Dehennault (loic.sephiroth@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.sephire.games.framework4x.plugins.standard;

import io.vavr.Tuple;
import io.vavr.control.Try;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.model.map.*;
import org.sephire.games.framework4x.core.plugins.map.MapGenerator;
import org.sephire.games.framework4x.core.plugins.map.MapProvider;

import static io.vavr.collection.List.range;

@MapProvider
public class StandardMaps {

	@MapGenerator(name = "standard.random",displayKey = "org.sephire.games.framework4x.plugins.standard.map-generators.random.title")
	public Try<GameMap> randomMapGenerator(Configuration configuration){
		return Try.of(()->{
			var cells = range(0,500)
			  .flatMap((x)->range(0,500).map((y)-> Tuple.of(x,y)))
			  .map((xyTuple)-> new MapCell(Location.of(xyTuple._1,xyTuple._2),getRandomTerrainType()));

			return GameMap.builder()
			  .addZone(MapZone.builder()
				.withName("level0")
				.withCells(cells)
				.build()
				.get())
			  .withDefaultZone("level0")
			  .build()
			  .get();
		});
	}

	private TerrainTypeEnum getRandomTerrainType() {
		var randomNumber = (int)(Math.random()*Integer.MAX_VALUE)%StandardTerrainTypes.values().length;
		return StandardTerrainTypes.values()[randomNumber];
	}

	@MapGenerator(name = "standard.sametiles",displayKey = "org.sephire.games.framework4x.plugins.standard.map-generators.sametiles.title")
	public Try<GameMap> sameTileMapGenerator(Configuration configuration){
		return Try.of(()->{
			return GameMap.builder()
			  .addZone(MapZone.builder()
				.withName("level0")
				.withDefaultCells(new Size(300,300), StandardTerrainTypes.HILL)
				.build()
				.get())
			  .withDefaultZone("level0")
			  .build()
			  .get();
		});
	}
}
