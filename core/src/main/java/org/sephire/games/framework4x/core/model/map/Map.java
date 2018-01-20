package org.sephire.games.framework4x.core.model.map;

import io.vavr.collection.List;
import lombok.Getter;

import static org.sephire.games.framework4x.core.model.map.MapLayerHeightComparator.heightComparator;

/**
 * A map holds information of every items on which
 * the game is played.
 */
public class Map {

    @Getter
    private List<MapLayer> layers;

    public Map(List<MapLayer> layers) {
        this.layers = layers.sorted(heightComparator());
    }

    public Map() {
        this.layers = List.empty();
    }

    public Map addLayer(MapLayer layer) {
        return new Map(layers.push(layer));
    }

}
