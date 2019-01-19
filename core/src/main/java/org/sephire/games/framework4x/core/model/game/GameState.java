package org.sephire.games.framework4x.core.model.game;

import io.vavr.control.Option;
import io.vavr.control.Try;

import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>Holds the state of a game.</p>
 * <p>The class acts as a map, a container of states stored and retrieved much like the configuration object,
 * except that the game state is mutable.</p>
 * <p>This state is thread-safe, using a ConcurrentHashMap as its backend storage.</p>
 */
public class GameState {
	private ConcurrentHashMap<GameStateEnumKey,Object> state;

	public GameState() {
		this.state = new ConcurrentHashMap<>();
	}

	public void put(GameStateEnumKey key,Object value) {
		this.state.put(key,value);
	}

	public <T> Try<Option<T>> get(GameStateEnumKey key, Class<T> valueType) {
		return Try.of(()->{
			var value = state.get(key);
			if(value == null) {
				return Option.none();
			}

			return Option.of(valueType.cast(value));
		});
	}
}
