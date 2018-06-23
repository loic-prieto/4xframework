package org.sephire.games.framework4x.clients.terminal.ui.size;

import lombok.Value;

/**
 * Represents a size in a 2d space.
 * Both the height and width are qualified with a unit type,
 * such as a percentage or fixed block size.
 */
@Value
public class Size {
	private QualifiedSizeValue height;
	private QualifiedSizeValue width;

	public Size addSize(int addedHeight, int addedWidth) {
		return new Size(height.addValue(addedHeight), width.addValue(addedWidth));
	}

}
