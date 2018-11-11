package org.sephire.games.framework4x.clients.terminal.ui.layouts.gridlayout;

import lombok.Getter;
import org.sephire.games.framework4x.clients.terminal.FourXFrameworkClientException;
import org.sephire.games.framework4x.clients.terminal.ui.components.UIElement;

/**
 * This exception is thrown when an UI child Element of a container with a GridLayout has been
 * assigned an invalid cell from the cells of the layout, probably outside the valid column,row
 * range.
 */
public class ChildElementMustHaveValidCellAssignedException extends FourXFrameworkClientException {
	@Getter
	private UIElement childElement;

	public ChildElementMustHaveValidCellAssignedException(UIElement childElement) {
		super("The ui element ("+
				childElement.getClass().getName() + ","
				+childElement.getIdentifier()
				+ ") must have a valid cell location");

		this.childElement = childElement;
	}
}
