package org.sephire.games.framework4x.clients.terminal.gui.gamewindow;

import com.googlecode.lanterna.input.KeyStroke;
import io.vavr.Tuple;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.sephire.games.framework4x.clients.terminal.gui.gamewindow.map.MapDirection;

/**
 * <p>This event is fired when the game window detects the arrow keys pressing,and will understand that as
 * a scroll event for the map.</p>
 * <p>The ctrl key must be pressed for a map scroll. Not pressing the ctrl key will produce, instead, a cursor
 * movement inside the map. See {@link CursorMoveEvent}</p>
 * <p>When using shift + arrow, the map will scroll 10 times more</p>
 */
@AllArgsConstructor
@Getter
public class MapScrollEvent {
	private MapDirection direction;
	private int distance;

	private static final int DEFAULT_DISTANCE = 1;
	private static final int LONG_DISTANCE = 10;

	/**
	 * Calculates a map scroll event from a pressed key stroke.
	 * @param keyStroke
	 * @return
	 */
	public static Option<MapScrollEvent> fromKeyStroke(KeyStroke keyStroke) {
		return getDirectionFromKey(keyStroke)
		  .map((direction)-> Tuple.of(direction,getDistanceFromKey(keyStroke)))
		  .map((t)->new MapScrollEvent(t._1,t._2));
	}

	private static int getDistanceFromKey(KeyStroke keyStroke) {
		return keyStroke.isShiftDown() ? LONG_DISTANCE : DEFAULT_DISTANCE;
	}

	private static Option<MapDirection> getDirectionFromKey(KeyStroke keyStroke) {
		var keyType = keyStroke.getKeyType();
		return keyStroke.isAltDown()? MapDirection.fromKeyType(keyType) : Option.none();
	}
}
