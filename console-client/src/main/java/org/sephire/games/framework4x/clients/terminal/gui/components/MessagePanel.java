package org.sephire.games.framework4x.clients.terminal.gui.components;

import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import io.vavr.collection.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import static com.googlecode.lanterna.TextColor.ANSI.ANSI;
import static io.vavr.API.*;
import static org.sephire.games.framework4x.clients.terminal.gui.components.MessagePanel.MessageType.*;

/**
 * This is a panel to show messages one after the other, like a set of instructions
 * or validations to be updated as the user interacts with the components.
 */
public class MessagePanel extends Panel {
	private List<Message> messages;

	public MessagePanel() {
		super();
		this.messages = List.empty();
		this.setLayoutManager(new LinearLayout(Direction.VERTICAL));
	}

	public void addMessageIfNotExists(String text, MessageType type) {
		if(!messages.exists(m->m.getText().equals(text))) {
			addMessage(text,type);
		}
	}

	public void addMessage(String text, MessageType type) {
		messages = messages.append(new Message(text, type));
		updateLabels();
	}

	public void removeMessage(String text) {
		messages = messages.remove(new Message(text));
		updateLabels();
	}

	/**
	 * Update the list of children to render the messages of this panel.
	 */
	private void updateLabels() {
		this.removeAllComponents();
		this.messages.map(MessagePanel::fromMessage)
		  .forEach(this::addComponent);
	}

	public static Label fromMessage(Message message) {
		var label = new Label(message.text);
		var color = Match(message.type).of(
		  Case($(INFO), ANSI.BLACK),
		  Case($(WARNING), ANSI.YELLOW),
		  Case($(ERROR), ANSI.RED)
		);
		label.setForegroundColor(color);
		label.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Fill));

		return label;
	}

	@AllArgsConstructor
	@Getter
	@EqualsAndHashCode(of = {"text"})
	public static class Message {
		private String text;
		private MessageType type;

		public Message(String text) {
			this(text, INFO);
		}
	}

	public enum MessageType {
		INFO,
		WARNING,
		ERROR;
	}
}


