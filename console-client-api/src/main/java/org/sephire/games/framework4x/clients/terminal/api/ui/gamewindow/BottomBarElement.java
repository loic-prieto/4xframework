/**
 * 4X Framework - Console client - API - The console client offers an API that plugins that interact with the console can consume. This mainly
        avoid having the client and the related plugin have a cyclic dependency.
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
package org.sephire.games.framework4x.clients.terminal.api.ui.gamewindow;

import io.vavr.Function2;
import io.vavr.control.Try;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.model.game.Game;

/**
 * <p>Represents an UI element to be positioned in the bottom bar, with both
 * a position in one of the three slots, as defined in {@link BottomBarPosition},
 * and a value generator (String) which will be used to get the text to show for
 * that element.</p>
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class BottomBarElement {
	private BottomBarPosition position;
	private Function2<Configuration, Game,Try<String>> valueGenerator;

	public static BuilderElement builder(){
		return new Builder();
	}

	public static class Builder implements BuilderElement,BuilderPosition,BuilderBuilder {
		private BottomBarPosition position;
		private Function2<Configuration, Game,Try<String>> valueGenerator;

		public BuilderPosition from(Function2<Configuration, Game,Try<String>> valueGenerator) { this.valueGenerator = valueGenerator; return this;}
		public BuilderBuilder inPosition(BottomBarPosition position) {this.position = position;return this;}
		public Try<BottomBarElement> build() {
			return Try.of(()->{
				if(position == null || valueGenerator == null){
					throw new IllegalArgumentException("position and valueGenerator cannot be null");
				}

				return new BottomBarElement(position,valueGenerator);
			});
		}
	}
	public interface BuilderPosition { BuilderBuilder inPosition(BottomBarPosition position); }
	public interface BuilderElement { BuilderPosition from(Function2<Configuration, Game,Try<String>> valueGenerator); }
	public interface BuilderBuilder { Try<BottomBarElement> build(); }
}
