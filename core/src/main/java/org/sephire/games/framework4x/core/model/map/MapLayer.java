package org.sephire.games.framework4x.core.model.map;

import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import lombok.Getter;

public class MapLayer {
    @Getter
    private final String name;
    private final Map<Location, Item> items;
    @Getter
    private int zOrder;

    public MapLayer(String name, int zOrder, Map<Location, Item> items) {
        this.name = name;
        this.items = items;
        this.zOrder = zOrder;
    }

    public MapLayer(String name, int zOrder, Tuple2<Location, Item>... items) {
        this(name, zOrder, HashMap.ofEntries(items));
    }

    public MapLayer(String name) {
        this(name, 0, HashMap.empty());
    }

    public Map<Location, Item> getItemsInRange(Range range) {
        return items.filterKeys(range::containsLocation);
    }

    public MapLayer addEntry(Tuple2<Location, Item> item) {
        return new MapLayer(name, zOrder, items.put(item));
    }

}
