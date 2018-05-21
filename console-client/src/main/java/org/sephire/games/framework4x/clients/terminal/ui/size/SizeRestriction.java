package org.sephire.games.framework4x.clients.terminal.ui.size;

/**
 * Represents a size restriction for a layout region or a component inside a region.
 * An element can have a combination of size restrictions, but they must make sense.
 * For example, a fixed size restriction cannot be combined with an at_least or at_most
 * size restriction.
 * This is left to the client.
 */
public class SizeRestriction {
	private QualifiedSizeValue value;
	private SizeRestrictionType restrictionType;
}
