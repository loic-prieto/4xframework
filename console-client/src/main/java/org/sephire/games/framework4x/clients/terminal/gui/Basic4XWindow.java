/**
 * 4X Framework - Console client - A terminal-based client for the 4X framework
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
package org.sephire.games.framework4x.clients.terminal.gui;

import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import lombok.Getter;
import org.sephire.games.framework4x.clients.terminal.api.events.WindowEventBus;

import java.util.function.Consumer;

/**
 * Provides some resources to lanterna windows that extend this one
 */
public abstract class Basic4XWindow extends BasicWindow {
	@Getter
	private WindowEventBus eventBus;
	private WindowBasedTextGUI textGUI;

	public Basic4XWindow(WindowBasedTextGUI textGUI) {
		this.textGUI = textGUI;
		this.eventBus = new WindowEventBus();
	}

	public Basic4XWindow(String title,WindowBasedTextGUI textGUI) {
		super(title);
		this.eventBus = new WindowEventBus();
		this.textGUI = textGUI;
	}

	/**
	 * See {@link WindowEventBus}
	 * @param eventClass
	 * @param listener
	 * @param <T>
	 */
	public <T> void registerEventListener(Class<T> eventClass, Consumer<T> listener) {
		eventBus.registerListener(eventClass,listener);
	}

	/**
	 * See {@link WindowEventBus}
	 * @param event
	 * @param <T>
	 */
	public <T> void fireEvent(T event) {
		eventBus.fireEvent(event);
	}

	/**
	 * I haven't been able to discover yet why the original WindowBasedTextGUI is always null,
	 * so I'm passing it to each window constructor from the original textGui that created the screen.
	 * This is an ugly solution.
	 *
	 * Return the text gui of this window.
	 * @return
	 */
	public WindowBasedTextGUI getOverridenTextGui() {
		return this.textGUI;
	}

}
