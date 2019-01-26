/**
 * 4X Framework - Core library - The core library on which to base the game
 * Copyright © 2018 Loïc Prieto Dehennault (loic.sephiroth@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
