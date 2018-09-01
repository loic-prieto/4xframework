package org.sephire.games.framework4x.clients.terminal.ui.layouts;

import org.sephire.games.framework4x.clients.terminal.FourXFrameworkClientException;

/**
 * A layout doesn't exist outside of a container.
 * For convenience and easy instantiation of the layouts, the layouts can be created
 * before the container exists, but the user must ensure that a layout is attached to
 * a container before using it.
 * Otherwise, this exception is thrown.
 */
public class LayoutMustHaveContainerException extends FourXFrameworkClientException {
}
