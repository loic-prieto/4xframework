package org.sephire.games.framework4x.clients.terminal.ui.size;

import lombok.*;

/**
 * Represents a size in a 2d space.
 * Both the height and width are qualified with a unit type,
 * such as a percentage or fixed block size.
 */
@RequiredArgsConstructor
@EqualsAndHashCode
public class Size {
	@NonNull @Getter
	private QualifiedSizeValue height;
	@NonNull @Getter
	private QualifiedSizeValue width;

	/**
	 * Returns a new size that is the sum of the given height and width
	 * with the current ones.
	 * @param addedHeight
	 * @param addedWidth
	 * @return
	 */
	public Size addSize(int addedHeight, int addedWidth) {
		return new Size(height.addValue(addedHeight), width.addValue(addedWidth));
	}

}
