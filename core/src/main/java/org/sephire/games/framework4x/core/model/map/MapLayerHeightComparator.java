package org.sephire.games.framework4x.core.model.map;

import java.util.Comparator;

public class MapLayerHeightComparator implements Comparator<MapLayer> {
    private static MapLayerHeightComparator singleton = new MapLayerHeightComparator();

    public static MapLayerHeightComparator heightComparator() {
        return singleton;
    }

    @Override
    public int compare(MapLayer layer1, MapLayer layer2) {
        return Integer.compare(layer1.getZOrder(), layer2.getZOrder());
    }
}
