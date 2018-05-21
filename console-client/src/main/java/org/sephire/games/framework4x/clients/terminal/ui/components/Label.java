package org.sephire.games.framework4x.clients.terminal.ui.components;

import lombok.Getter;
import lombok.NonNull;
import org.sephire.games.framework4x.clients.terminal.ui.ComponentMustBeInsideContainerException;
import org.sephire.games.framework4x.clients.terminal.ui.Coordinates;
import org.sephire.games.framework4x.clients.terminal.ui.Painter;
import org.sephire.games.framework4x.clients.terminal.ui.Viewport;
import org.sephire.games.framework4x.clients.terminal.ui.size.Size;
import org.sephire.games.framework4x.core.model.map.Location;

/**
 * Represents a text label inside the screen.
 *
 * It will just display itself, honoring viewport bounds.
 */
@Getter
public class Label extends UIElement {

	@NonNull
	private String text;

	public Label(String text, Location location, Container containerParent) {
		super(new Coordinates(location,new Size(1,text.length())),containerParent);
		this.text = text;
	}

	@Override
	public void draw(Painter painter) {
		Viewport vp = getContainerParent().getOrElseThrow(ComponentMustBeInsideContainerException::new)
				.getViewport();

		char[] textChars = text.toCharArray();
		for(int i=0; i < textChars.length; i++) {
			painter.drawChar(getCoordinates().getLocation().add(i,0),textChars[i],vp);
		}
	}

	public void setText(String newText) {
		this.text = newText;
		updateSizeFromText();
	}

	/**
	 * Each time the text is updated, we should update the size of this component.
	 */
	private void updateSizeFromText(){
		this.setCoordinates(this.getCoordinates().withSize(buildSizeFromText()));
	}

	/**
	 * Constructs a Size object from the length of the text of this label.
	 *
	 * This will assume that each character will occupy 1 terminal block. Perhaps
	 * it is not so in non-latin alphabets.
	 */
	private Size buildSizeFromText() {
		return new Size(1,this.text.length());
	}
}
