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
package org.sephire.games.framework4x.clients.terminal.gui.gamewindow;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.input.KeyStroke;
import io.vavr.control.Try;
import lombok.Getter;
import org.sephire.games.framework4x.clients.terminal.api.ui.gamewindow.GameWindowAPI;
import org.sephire.games.framework4x.clients.terminal.api.ui.gamewindow.GameWindowUtils;
import org.sephire.games.framework4x.clients.terminal.gui.Basic4XWindow;
import org.sephire.games.framework4x.clients.terminal.gui.gamewindow.map.MapComponent;
import org.sephire.games.framework4x.clients.terminal.gui.gamewindow.map.MapScrollEvent;
import org.sephire.games.framework4x.clients.terminal.gui.gamewindow.topmenu.TopMenuComponent;
import org.sephire.games.framework4x.clients.terminal.utils.UITranslationService;
import org.sephire.games.framework4x.core.model.game.Game;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Represents the window of a game. Holds the map, the information/actions side panel, the top menu and the bottom
 * status bar.
 */
public class GameWindow extends Basic4XWindow implements GameWindowAPI {

	private Game game;
	private UITranslationService i18n;
	@Getter
	private WindowBasedTextGUI originalTextGUI;

	public GameWindow(WindowBasedTextGUI textGUI,
					  UITranslationService i18n) {
		super();
		this.i18n = i18n;
		this.originalTextGUI = textGUI;
	}

	public Try<GameWindow> build(Game game) {
		return Try.of(()->{
			this.game = game;

			setupFrame().getOrElseThrow(t->t);

			var backgroundPanel = new Panel();
			backgroundPanel.setLayoutManager(new BorderLayout());

			var mapComponent = MapComponent.of(game, this).getOrElseThrow(t->t);
			backgroundPanel.addComponent(mapComponent, BorderLayout.Location.CENTER);

			setupBottomBar(backgroundPanel);
			setupTopBar(backgroundPanel).getOrElseThrow(t->t);

			setComponent(backgroundPanel);

			GameWindowUtils.setCurrentGameWindow(this);

			return this;
		});
	}

	private void setupBottomBar(Panel container) {
		var bottomBar = new BottomBarComponent(game, this);
		bottomBar.setLayoutData(BorderLayout.Location.BOTTOM);
		container.addComponent(bottomBar);
	}

	private Try<Void> setupTopBar(Panel container) {
		return Try.of(()->{
			var topMenuBar = TopMenuComponent.builder()
			  .withParent(this)
			  .withConfiguration(game.getConfiguration())
			  .build().getOrElseThrow(t -> t);

			topMenuBar.setLayoutData(BorderLayout.Location.TOP);
			container.addComponent(topMenuBar);

			return null;
		});
	}

	private Try<GameWindow> setupFrame() {

		return Try.of(()->{
			setHints(List.of(Window.Hint.FULL_SCREEN));
			setTitle(i18n.getTranslationFor(Locale.ENGLISH,"gamewindow.title")
			  .getOrElseThrow(()->new TranslationNotFoundException("gamewindow.title")));

			addWindowListener(new WindowListenerAdapter() {
				@Override
				public void onInput(Window basePane, KeyStroke keyStroke, AtomicBoolean deliverEvent) {

					// React to top menu activations
					var potentialMenuActivationEvent = MenuActivationRequestedEvent.from(keyStroke);
					if (potentialMenuActivationEvent.isDefined()) {
						fireEvent(potentialMenuActivationEvent.get());
						return;
					}

					// React to map scroll events
					var potentialMapScrollEvent = MapScrollEvent.fromKeyStroke(keyStroke);
					if (potentialMapScrollEvent.isDefined()) {
						fireEvent(potentialMapScrollEvent.get());
						return;
					}

					// React to cursor movement
					var potentialCursorMovement = CursorMoveEvent.fromKeyStroke(keyStroke);
					if (potentialCursorMovement.isDefined()) {
						fireEvent(potentialCursorMovement.get());
						return;
					}
				}
			});

			return this;
		});
	}

	@Override
	public Try<Void> closeWindow() {
		return Try.of(() -> {
			this.close();
			return null;
		});
	}
}
