package org.sephire.games.framework4x.clients.terminal.ui;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.screen.Screen;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.sephire.games.framework4x.core.model.map.Location;

/**
 * An abstraction of the terminal painter. Allows to easily draw character and widgets with the appropriate
 * transformations.
 */
@RequiredArgsConstructor
public class Painter {
	@NonNull
	@Getter
	private Screen screen;

	/**
	 * Return a TerminalPosition from an UI Element location.
	 *
	 * @param location
	 * @return
	 */
	public static TerminalPosition terminalPositionFrom(Location location) {
		return new TerminalPosition(location.getX(), location.getY());
	}

	/**
	 * Draws a character in a location, if it is visible inside a viewport.
	 * Just a convenience method to avoid writing boilerplate for each
	 * drawn character.
	 *
	 * @param location
	 * @param character
	 * @param viewport
	 */
	public void drawChar(Location location, char character, Viewport viewport) {
		if (viewport.isLocationVisible(location))
			screen.setCharacter(location.getX(), location.getY(), new TextCharacter(character));
	}

	/**
	 * @param x
	 * @param y
	 * @param character
	 * @param viewport
	 * @see Painter#drawChar(Location, char, Viewport)
	 */
	public void drawChar(int x, int y, char character, Viewport viewport) {
		drawChar(new Location(x, y), character, viewport);
	}
}
