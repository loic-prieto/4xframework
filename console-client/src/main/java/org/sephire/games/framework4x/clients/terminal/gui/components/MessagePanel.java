/**
 * 4X Framework - Console client - A terminal-based client for the 4X framework
 * Copyright © 2018 Loïc Prieto Dehennault (loic.sephiroth@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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


