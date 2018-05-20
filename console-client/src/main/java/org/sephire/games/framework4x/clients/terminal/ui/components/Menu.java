package org.sephire.games.framework4x.clients.terminal.ui.components;

import io.vavr.collection.List;
import org.sephire.games.framework4x.clients.terminal.ui.Coordinates;
import org.sephire.games.framework4x.clients.terminal.ui.components.menu.MenuDirection;
import org.sephire.games.framework4x.clients.terminal.ui.components.menu.MenuItem;
import org.sephire.games.framework4x.clients.terminal.utils.MutableIntCounter;
import org.sephire.games.framework4x.core.model.map.Location;

import static org.sephire.games.framework4x.clients.terminal.ui.components.menu.MenuDirection.HORIZONTAL;

/**
 * This represents a menu panel, which can be horizontal or vertical.
 * If horizontal, it works as a menu bar, much like the application menu of
 * any app.
 * If vertical, it works much as a submenu or contextual menu.
 *
 * The menu contains menu items, which have a label and an action to perform when activated.
 */
public class Menu extends Panel {
	private List<MenuItem> items;
	private MenuDirection direction;

	public Menu(Coordinates coordinates, int borderSize, MenuDirection menuDirection,MenuItem... items ) {
		super(coordinates, borderSize);
		this.items = List.of(items);
		this.direction = menuDirection;

		// Build the child elements from the menu items
		if(direction == HORIZONTAL) {
			// If the direction is horizontal, we need to build the locations based
			// on string length of the previous items.
			MutableIntCounter rightmostPosition = new MutableIntCounter();
			this.setChildren(
				this.items.zip(
					this.items.map((item)->{
						Location itemLocation = new Location(rightmostPosition.getValue(),0);
						rightmostPosition.incrementValue(item.getLabel().length()+1);
						return itemLocation;
					}))
				.map((itemWithLocation)-> new Label(itemWithLocation._1.getLabel(),itemWithLocation._2,this))
			);
		} else {
			// On vertical orientation the location is based on the index
			// of the item.
			this.setChildren(
				this.items.zipWithIndex()
					.map((itemWithIndex)-> {
						Location itemLocation = new Location(0,itemWithIndex._2);
						return new Label(itemWithIndex._1.getLabel(),itemLocation,this);
					})
			);
		}
	}

	/**
	 * A menu item is identified by it's label. To execute an item's action
	 * you can use this function with the label of the item.
	 *
	 * @param itemLabel
	 */
	public void performItemAction(String itemLabel) {
		this.items.find(item -> item.getLabel().equals(itemLabel))
				.peek(item -> item.getAction().apply());
	}


}
