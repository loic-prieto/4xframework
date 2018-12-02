package org.sephire.games.framework4x.clients.terminal.gui.components.map;

import lombok.Getter;
import org.sephire.games.framework4x.clients.terminal.FourXFrameworkClientException;

/**
 * When loading a map zone and when its state or behaviour is invalid, this
 * exception is thrown. For example, when not all cells have been filled.
 */
public class InvalidMapZoneException extends FourXFrameworkClientException {
	@Getter
	private String zoneName;

	public InvalidMapZoneException(String zoneName,String reason) {
		super(reason);
		this.zoneName = zoneName;
	}
}
