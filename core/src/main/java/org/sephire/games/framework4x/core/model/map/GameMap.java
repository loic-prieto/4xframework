package org.sephire.games.framework4x.core.model.map;

import io.vavr.collection.List;
import io.vavr.collection.Map;
import lombok.Getter;

import static org.sephire.games.framework4x.core.model.map.MapLayerHeightComparator.heightComparator;

/**
 * A map holds information of every items on which
 * the game is played.
 */
public class GameMap {

    @Getter
    private List<MapLayer> layers;

    public GameMap(List<MapLayer> layers) {
        this.layers = layers.sorted(heightComparator());
    }

    public GameMap() {
        this.layers = List.empty();
    }

    public GameMap addLayer(MapLayer layer) {
        return new GameMap(layers.push(layer));
    }

    public Map<Location, Item> getVisibleActiveItems(Range range) {
        return layers.map((layer) -> layer.getItemsInRange(range))
                .reduce((itemMap1, itemMap2) -> itemMap1.merge(itemMap2));
    }

}
