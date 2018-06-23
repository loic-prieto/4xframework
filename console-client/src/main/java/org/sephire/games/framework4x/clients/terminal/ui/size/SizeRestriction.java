package org.sephire.games.framework4x.clients.terminal.ui.size;

import lombok.Value;

/**
 * Represents a size restriction for a layout region or a component inside a region.
 */
@Value
public class SizeRestriction {
	private QualifiedSizeValue value;
	private SizeRestrictionType restrictionType;
}
