package org.sephire.games.framework4x.core.model.map;

import lombok.Value;

/**
 * A 2D grid range of values, defined by an origin top,left point, a width and an height.
 */
@Value
public class Range {
    private int x;
    private int y;
    private int horizontalOffset;
    private int verticalOffset;

    public boolean containsLocation(Location targetLocation) {
        return
                targetLocation.getX() >= x
                        && targetLocation.getX() <= x + horizontalOffset
                        && targetLocation.getY() >= y
                        && targetLocation.getY() <= y + horizontalOffset;

    }
}
