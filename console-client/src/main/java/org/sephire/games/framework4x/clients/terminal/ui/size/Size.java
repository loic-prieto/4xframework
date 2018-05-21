package org.sephire.games.framework4x.clients.terminal.ui.size;

import lombok.NonNull;
import lombok.Value;

/**
 * Represents a size in a 2d space.
 */
@Value
public class Size {
	@NonNull
	private QualifiedSizeValue height;
	@NonNull
	private QualifiedSizeValue width;

	public Size addSize(int addedHeight, int addedWidth) {
		return new Size(height.addValue(addedHeight), width.addValue(addedWidth));
	}
}
