package org.sephire.games.framework4x.clients.terminal.ui;

import io.vavr.collection.List;

/**
 * An UI Element that contains other elements that extend beyond
 * what is visible. Can occupy the whole screen, or part of it.
 * Will only draw the visible content of childrens.
 *
 */
public class Container extends UIElement {
    /**
     * The viewport of the container.
     */
    private Viewport viewport;

    private List<UIElement> children;

    @Override
    public void draw(Painter painter) {

    }
}
