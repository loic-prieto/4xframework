package org.sephire.games.framework4x.clients.terminal.ui;

import lombok.NonNull;
import lombok.Value;

/**
 * Represents a size in a 2d space.
 */
@Value
public class Size {
	@NonNull
	private int height;
	@NonNull
	private int width;
}
