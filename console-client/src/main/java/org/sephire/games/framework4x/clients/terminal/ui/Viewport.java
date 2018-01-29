package org.sephire.games.framework4x.clients.terminal.ui;

import lombok.Value;
import org.sephire.games.framework4x.core.model.map.Range;

/**
 * Represents the viewport of a map.
 * Will be influenced by screen size and overlapping ui elements like
 * a top menu.
 */
@Value
public class Viewport {
    private int xOffset;
    private int yOffset;
    private int width;
    private int height;

    public Range toRange() {
        return new Range(xOffset, yOffset, width, height);
    }
}
