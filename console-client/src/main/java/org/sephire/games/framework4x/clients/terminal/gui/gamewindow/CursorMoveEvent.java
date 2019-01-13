package org.sephire.games.framework4x.clients.terminal.gui.gamewindow;

import com.googlecode.lanterna.input.KeyStroke;
import io.vavr.Tuple;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.sephire.games.framework4x.clients.terminal.gui.gamewindow.map.MapDirection;

/**
 * This event is thrown when in the game window the cursor inside the map must move.
 */
@AllArgsConstructor
@Getter
public class CursorMoveEvent {
	private final MapDirection direction;
	private final int distance;

	private static final int DEFAULT_DISTANCE = 1;
	private static final int LONG_DISTANCE = 10;

	/**
	 * Calculates a cursor move event from a pressed key stroke.
	 * @param keyStroke
	 * @return
	 */
	public static Option<CursorMoveEvent> fromKeyStroke(KeyStroke keyStroke) {
		return getDirectionFromKey(keyStroke)
		  .map((direction)-> Tuple.of(direction,getDistanceFromKey(keyStroke)))
		  .map((t)->new CursorMoveEvent(t._1,t._2));
	}

	private static int getDistanceFromKey(KeyStroke keyStroke) {
		return keyStroke.isShiftDown() ? LONG_DISTANCE : DEFAULT_DISTANCE;
	}

	private static Option<MapDirection> getDirectionFromKey(KeyStroke keyStroke) {
		var keyType = keyStroke.getKeyType();
		return !keyStroke.isAltDown()? MapDirection.fromKeyType(keyType) : Option.none();
	}
}
