package org.sephire.games.framework4x.core.model.map;

import io.vavr.collection.Map;
import lombok.Value;

/**
 * A map holds information of every items on which
 * the game is played.
 */
@Value
public class GameMap {
    private Map<Location,MapCell> cells;
}
