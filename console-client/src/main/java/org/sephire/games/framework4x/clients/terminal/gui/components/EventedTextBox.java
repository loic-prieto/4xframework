package org.sephire.games.framework4x.clients.terminal.gui.components;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.TextBox;
import com.googlecode.lanterna.input.KeyStroke;
import io.vavr.Function1;
import io.vavr.control.Option;
import io.vavr.control.Try;

import static org.sephire.games.framework4x.core.utils.Validations.areArgumentsNotNull;

/**
 * Adds custom event handling to the basic TextBox.
 */
public class EventedTextBox extends TextBox {

	private Function1<String, Void> keyStrokeHandler;

	private EventedTextBox(Function1<String, Void> keyStrokeHandler, Style style) {
		super("", style);
		this.keyStrokeHandler = keyStrokeHandler;
	}

	@Override
	public synchronized Result handleKeyStroke(KeyStroke keyStroke) {
		var superResult = super.handleKeyStroke(keyStroke);
		keyStrokeHandler.apply(getText());

		return superResult;
	}

	public static BuilderHandler builder() { return new Builder();}

	public static class Builder implements BuilderStyle,BuilderHandler {
		private Function1<String, Void> keyStrokeHandler;
		private Option<TerminalSize> size = Option.none();
		private Option<TextBox.Style> style = Option.none();

		@Override
		public BuilderStyle withHandler(Function1<String, Void> handler) {
			this.keyStrokeHandler = handler;
			return this;
		}

		@Override
		public BuilderStyle enableMultiline() {
			this.style = Option.of(Style.MULTI_LINE);
			return this;
		}

		@Override
		public BuilderStyle withSize(int columns, int rows) {
			this.size = Option.of(new TerminalSize(columns,rows));
			return this;
		}

		@Override
		public Try<EventedTextBox> build() {
			return Try.of(()->{
				areArgumentsNotNull(keyStrokeHandler).getOrElseThrow(t -> t);
				if(style.isEmpty()){
					style = Option.of(Style.SINGLE_LINE);
				}

				var textBox = new EventedTextBox(keyStrokeHandler,style.get());
				if(size.isDefined()){
					textBox.setPreferredSize(size.get());
				}

				return textBox;
			});
		}
	}

	public interface BuilderHandler { BuilderStyle withHandler(Function1<String,Void> handler);}
	public interface BuilderStyle {
		BuilderStyle enableMultiline();
		BuilderStyle withSize(int columns,int rows);
		Try<EventedTextBox> build();
	}
}
