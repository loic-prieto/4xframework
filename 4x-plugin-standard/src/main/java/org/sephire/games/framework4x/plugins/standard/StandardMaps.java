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
			var cells = (MapCell[])range(0,20)
			  .flatMap((x)->range(0,20).map((y)-> Tuple.of(x,y)))
			  .map((xyTuple)-> new MapCell(Location.of(xyTuple._1,xyTuple._2),getRandomTerrainType()))
			  .toJavaArray();

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
				.withDefaultCells(new Size(20,20), StandardTerrainTypes.HILL)
				.build()
				.get())
			  .withDefaultZone("level0")
			  .build()
			  .get();
		});
	}
}
