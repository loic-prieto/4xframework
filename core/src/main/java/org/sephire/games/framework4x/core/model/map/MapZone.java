package org.sephire.games.framework4x.core.model.map;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Try;
import lombok.Getter;

import static io.vavr.collection.List.range;

/**
 * A map map is divided into zones. They are meant to represent maps of the same map universe,
 * meaning a zone can represent another planet, a pocket plane, an underground level,
 * a space orbit around a planet, a galactic map where the scope is now planets instead of cities.
 * <p>
 * Zones have a name, and a set of map cells which holds the map tiles with their 2D position.
 */
@Getter
public class MapZone {
	private String name;
	private Map<Location, MapCell> cells;
	private Size size;

	private MapZone(String name,Map<Location, MapCell> cells) {
		this.name = name;
		this.cells = cells;
		buildSize();
	}

	/**
	 * Updates the size of the zone based on the current map cells.
	 *
	 * @return
	 */
	private void buildSize() {
		int maxXPosition = cells.keySet()
		  .map(Location::getX)
		  .reduce((previousX, currentX) -> currentX < previousX ? previousX : currentX);
		int maxYPosition = cells.keySet()
		  .map(Location::getY)
		  .reduce((previousY, currentY) -> currentY < previousY ? previousY : currentY);

		this.size = new Size(maxXPosition, maxYPosition);
	}

	/**
	 * Gets the builder for this class
	 * @return
	 */
	public static MapZoneBuilderNameField builder() {
		return new Builder();
	}

	public static class Builder implements MapZoneBuilderNameField, MapZoneBuilderCellsField,MapZoneBuilderBuild {
		private String name;
		private Map<Location, MapCell> cells;

		@Override
		public MapZoneBuilderCellsField withName(String name) {
			this.name = name;
			return this;
		}

		@Override
		public MapZoneBuilderBuild withCells(MapCell... cells) {
			this.cells = List.of(cells)
			  .map((cell)-> Tuple.of(cell.getLocation(), cell))
			  .collect(HashMap.collector());

			return this;
		}

		@Override
		public MapZoneBuilderBuild withDefaultCells(Size size, TerrainTypeEnum defaultTerrainType) {
			java.util.Map<Location,MapCell> mutableMap = new java.util.HashMap<>();

			range(0,size.getHeight()).forEach((y)->{
				range(0,size.getWidth()).forEach((x)->{
					mutableMap.put(Location.of(x,y),new MapCell(Location.of(x,y),defaultTerrainType));
				});
			});

			this.cells = HashMap.ofAll(mutableMap);

			return this;
		}

		@Override
		public Try<MapZone> build() {
			return Try.of(()->{
				if(name == null || "".equals(name)) {
					throw new IllegalArgumentException("Name field cannot be null or blank");
				}

				if(cells == null || cells.length() < 1) {
					throw new IllegalArgumentException("The map must have cells");
				}

				return new MapZone(name,cells);
			});
		}
	}

	public interface MapZoneBuilderNameField {
		MapZoneBuilderCellsField withName(String name);
	}

	public interface MapZoneBuilderCellsField {
		MapZoneBuilderBuild withDefaultCells(Size size, TerrainTypeEnum defaultTerrainType);
		MapZoneBuilderBuild withCells(MapCell... cells);
	}

	public interface MapZoneBuilderBuild {
		Try<MapZone> build();
	}
}
