package org.sephire.games.framework4x.clients.terminal.ui.size;

/**
 * Convenience Size class to operate only with fixed sizes
 * instead of having to specify qualified values.
 * Useful in the developing stages of the framework.
 */
public class FixedSize extends Size {
	public FixedSize(int width, int height) {
		super(new QualifiedSizeValue(height,SizeUnit.CHARACTER), new QualifiedSizeValue(width,SizeUnit.CHARACTER));
	}

	/**
	 * Returns the height of the size assuming the
	 * size type is fixed.
	 * @return
	 */
	public int getFixedHeight() {
		return getHeight().getValue();
	}

	/**
	 * Returns the width of the size assuming the
	 * size type is fixed.
	 * @return
	 */
	public int getFixedWidth() {
		return getWidth().getValue();
	}
}
