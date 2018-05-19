package org.sephire.games.framework4x.clients.terminal.ui;

import org.sephire.games.framework4x.clients.terminal.FourXFrameworkClientException;

/**
 * All non top containers and components must live inside a container.
 *
 * Components should be designed such that this cannot happen. But they should never the less
 * check for this and throw this error accordingly.
 */
public class ComponentMustBeInsideContainerException extends FourXFrameworkClientException {
}
