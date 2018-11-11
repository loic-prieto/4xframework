package org.sephire.games.terminal.ui.layouts;

/**
 * This is a marker interface for enums that will serve as keys for parameter to
 * provide when adding a child item to a container.
 */
public interface LayoutParameterKey {
    Class getParameterValueClass();
}
