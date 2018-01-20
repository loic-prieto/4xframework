package org.sephire.games.framework4x.core.model.map;

import lombok.Value;

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
