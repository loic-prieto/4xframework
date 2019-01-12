package org.sephire.games.framework4x.clients.terminal.gui.gamewindow;

import com.googlecode.lanterna.input.KeyStroke;
import io.vavr.Tuple;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.sephire.games.framework4x.clients.terminal.gui.gamewindow.map.MapDirection;

import static com.googlecode.lanterna.input.KeyType.*;
import static io.vavr.API.*;

/**
 * This event is fired when the game window detects the arrow keys pressing,and will understand that as
 * a scroll event for the map.
 * When using shift + arrow, the map will scroll x times more
 */
@AllArgsConstructor
public class MapScrollEvent {

	@Getter
	private MapDirection direction;
	@Getter
	private int distance;

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
		return keyStroke.isShiftDown() ? 10 : 1;
	}

	private static Option<MapDirection> getDirectionFromKey(KeyStroke keyStroke) {
		return Option.of(Match(keyStroke.getKeyType()).of(
		  Case($(ArrowUp),()->MapDirection.UP),
		  Case($(ArrowDown),()->MapDirection.DOWN),
		  Case($(ArrowLeft),()->MapDirection.LEFT),
		  Case($(ArrowRight),()->MapDirection.RIGHT),
		  Case($(),()->null)
		));
	}
}
