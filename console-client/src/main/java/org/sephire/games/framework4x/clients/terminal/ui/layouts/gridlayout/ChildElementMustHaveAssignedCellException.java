package org.sephire.games.framework4x.clients.terminal.ui.layouts.gridlayout;

import lombok.Getter;
import org.sephire.games.framework4x.clients.terminal.FourXFrameworkClientException;
import org.sephire.games.framework4x.clients.terminal.ui.components.UIElement;

/**
 * This exception is thrown when a child element of a container with a GridLayout is being used by the layout but
 * has no {@link GridLayoutParameterKeys.CELL} parameter set, which selects on which cell is the child element set.
 */
public class ChildElementMustHaveAssignedCellException extends FourXFrameworkClientException {
	@Getter
	private UIElement uiElement;

	public ChildElementMustHaveAssignedCellException(UIElement uiElement) {
		super("The ui element ("+
				uiElement.getClass().getName() + ","
				+uiElement.getIdentifier()
				+ ") must have a cell location assigned via the parameter GridLayoutParameterKeys.CELL param key");

		this.uiElement = uiElement;
	}
}
