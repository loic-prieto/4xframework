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
	private Function2<Configuration, Game,String> valueGenerator;

	public static BuilderElement builder(){
		return new Builder();
	}

	public static class Builder implements BuilderElement,BuilderPosition,BuilderBuilder {
		private BottomBarPosition position;
		private Function2<Configuration, Game,String> valueGenerator;

		public BuilderPosition from(Function2<Configuration, Game,String> valueGenerator) { this.valueGenerator = valueGenerator; return this;}
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
	public interface BuilderElement { BuilderPosition from(Function2<Configuration, Game,String> valueGenerator); }
	public interface BuilderBuilder { Try<BottomBarElement> build(); }
}
