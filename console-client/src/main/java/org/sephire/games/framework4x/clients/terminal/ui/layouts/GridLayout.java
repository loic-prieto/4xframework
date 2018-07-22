package org.sephire.games.framework4x.clients.terminal.ui.layouts;

import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.sephire.games.framework4x.clients.terminal.ui.components.Container;
import org.sephire.games.framework4x.clients.terminal.ui.layouts.gridlayout.GridCell;
import org.sephire.games.framework4x.clients.terminal.ui.size.FixedSize;
import org.sephire.games.framework4x.clients.terminal.ui.size.Size;
import org.sephire.games.framework4x.clients.terminal.utils.MutableValue;
import org.sephire.games.framework4x.core.model.map.Location;

import static io.vavr.collection.List.range;
import static java.util.function.Function.identity;

/**
 * This is the layout to distribute components along a conceptual 2D grid.
 * This is the most generic grid-based container.
 *
 * The grid layout divides a container into rows and columns and creates a cell for each (row,column) coordinate.
 * Child items are put into the container declaring on which cell they are put.
 * The child are then put into the cell 2D space by absolute positioning given by the child coordinates.
 * The cells themselves are not a container. They are just a positioning grid. If you want to provide a layout
 * into a cell, you have to put a container into it.
 *
 * The cells are not spaced between them.
 *
 * To build it, the {@link org.sephire.games.framework4x.clients.terminal.ui.layouts.gridlayout.Builder} class is
 * recommended.
 */
public class GridLayout extends BaseLayout {

	private int columns;
	private int rows;

	private Map<Location,GridCell> cells;

	public GridLayout(int columns, int rows) {
		super(Option.none());
		this.columns = columns;
		this.rows = rows;
		this.cells = HashMap.empty();
	}

	@Override
	public void setContainer(Container container) {
		// Since we can only create the cells when we know the coordinates
		// of the container, do it here.
		super.setContainer(container);
		updateCells();
	}

	@Override
	public void updateChildrenCoordinates() {
		Container container = getContainer().getOrElseThrow(LayoutMustHaveContainerException::new);
	}

	/**
	 * Given a number of rows and columns and a container size, the grid layout
	 * tries to create equal sized cells.
	 */
	private void updateCells() {
		// This can only be called when the container has been set
		Container container = getContainer().get();

		// This only works for fixed sizes for now
		int averageCellHeight = this.columns / container.getCoordinates().getSize().getHeight().getValue();
		int averageCellWidth = this.columns / container.getCoordinates().getSize().getWidth().getValue();
		boolean hasToCorrectWidth = averageCellWidth * columns != container.getCoordinates().getSize().getWidth().getValue();
		boolean hasToCorrectHeight = averageCellHeight * rows != container.getCoordinates().getSize().getHeight().getValue();

		MutableValue<Map<Location,GridCell>> mCells = new MutableValue<>(cells);

		range(1,rows).forEach((row)-> {
			range(1, columns).forEach((column) -> {
				boolean isLastColumn = column == columns;
				boolean isLastRow = row == rows;

				int width = (isLastColumn && hasToCorrectWidth) ? averageCellWidth+1 : averageCellWidth;
				int height = (isLastRow && hasToCorrectHeight) ? averageCellHeight+1 : averageCellHeight;

				Map<Location,GridCell> cells = mCells.getValue();
				mCells.updateValue(cells.put(
						Location.of(column, row),
						new GridCell(
								Location.of(column, row),
								new FixedSize(width, height))));
			});
		});

		cells = mCells.getValue();

	}

}
