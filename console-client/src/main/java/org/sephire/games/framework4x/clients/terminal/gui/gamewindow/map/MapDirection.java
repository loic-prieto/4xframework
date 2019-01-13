package org.sephire.games.framework4x.clients.terminal.gui.gamewindow.map;

import com.googlecode.lanterna.input.KeyType;
import io.vavr.control.Option;

import static io.vavr.API.*;

public enum MapDirection {
	UP,
	DOWN,
	LEFT,
	RIGHT;

	/**
	 * Given a keytype from lanterna, returns a Map Direction.
	 * This assumes keys are not re-mappable.
	 * @param keyType
	 * @return
	 */
	public static Option<MapDirection> fromKeyType(KeyType keyType) {
		return Option.of(
		  Match(keyType).of(
			Case($(KeyType.ArrowUp),()->MapDirection.UP),
			Case($(KeyType.ArrowDown),()->MapDirection.DOWN),
			Case($(KeyType.ArrowLeft),()->MapDirection.LEFT),
			Case($(KeyType.ArrowRight),()->MapDirection.RIGHT),
			Case($(),()->null)
		  )
		);
	}
}
