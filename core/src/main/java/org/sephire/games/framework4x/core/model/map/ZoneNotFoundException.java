package org.sephire.games.framework4x.core.model.map;

import lombok.Getter;
import org.sephire.games.framework4x.core.Framework4XException;

public class ZoneNotFoundException extends Framework4XException {
	@Getter
	private String zoneName;

	public ZoneNotFoundException(String zoneName) {
		super("Zone "+zoneName+" not found");
		this.zoneName = zoneName;
	}
}
