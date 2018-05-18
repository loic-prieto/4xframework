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

	public Size addWidth(int addedWidth) {
		return new Size(height, width + addedWidth);
	}

	public Size addHeight(int addedHeight) {
		return new Size(height + addedHeight, width);
	}

	public Size addSize(Size size) {
		return addSize(size.height, size.width);
	}

	public Size addSize(int addedHeight, int addedWidth) {
		return new Size(height + addedHeight, width + addedWidth);
	}
}
