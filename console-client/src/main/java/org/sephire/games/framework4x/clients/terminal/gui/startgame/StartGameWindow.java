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
package org.sephire.games.framework4x.clients.terminal.gui.startgame;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.sephire.games.framework4x.clients.terminal.gui.Basic4XWindow;
import org.sephire.games.framework4x.clients.terminal.gui.gamewindow.GameWindow;
import org.sephire.games.framework4x.clients.terminal.gui.gamewindow.TranslationNotFoundException;
import org.sephire.games.framework4x.clients.terminal.utils.UITranslationService;
import org.sephire.games.framework4x.core.model.civilization.Civilization;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.model.game.Game;
import org.sephire.games.framework4x.core.plugins.map.MapGeneratorWrapper;

import javax.inject.Provider;
import java.util.Locale;

import static com.googlecode.lanterna.gui2.Borders.doubleLine;
import static com.googlecode.lanterna.gui2.LinearLayout.Alignment.End;
import static java.lang.String.format;

/**
 * <p>In the initialize game window,the player selects the options of a game, including the map and the civilization</p>
 */
@Slf4j
public class StartGameWindow extends Basic4XWindow {

	private Configuration.Builder configuration;
	private Option<MapGeneratorWrapper> selectedMapGenerator;
	private Option<Civilization> selectedCivilization;
	private Button startButton;
	private UITranslationService i18n;
	private Provider<GameWindow> gameWindowProvider;

	public StartGameWindow(UITranslationService i18n,
						   WindowBasedTextGUI textGUI,
						   Provider<GameWindow> gameWindowProvider) {
		super(textGUI);
		this.i18n = i18n;
		this.selectedCivilization = Option.none();
		this.selectedMapGenerator = Option.none();
		this.gameWindowProvider = gameWindowProvider;
	}

	public Try<StartGameWindow> build(Configuration.Builder configuration) {
		return Try.of(() -> {
			this.configuration = configuration;
			setupComponents().getOrElseThrow(t -> t);
			setupFrameConfig();
			setupEventHandling();

			return this;
		});
	}

	private Try<Void> setupComponents() {
		return Try.of(() -> {
			var backgroundPanel = new Panel();
			backgroundPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
			backgroundPanel.setPreferredSize(getSize());

			var optionsPanel = new Panel();
			optionsPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
			backgroundPanel.addComponent(optionsPanel
			  .withBorder(
				doubleLine(
				  i18n.getTranslationFor(
					Locale.ENGLISH,
					"startGameWindow.optionsPane.label")
					.getOrElseThrow(() -> new TranslationNotFoundException("startGameWindow.optionsPane.label")))));

			// Select Map panel
			optionsPanel.addComponent(SelectMapPanel.builder()
			  .withConfiguration(configuration)
			  .withParent(this)
			  .build()
			  .getOrElseThrow(t -> t));

			// Select Civilization panel
			optionsPanel.addComponent(SelectCivilizationPanel.builder()
			  .withConfiguration(configuration)
			  .withParent(this)
			  .build()
			  .getOrElseThrow(t -> t));

			// Start game button
			var startButtonText = i18n.getTranslationFor(
			  Locale.ENGLISH,
			  "startGameWindow.OptionsPane.startGamebutton.label")
			  .getOrElseThrow(() -> new TranslationNotFoundException("startGameWindow.OptionsPane.startGamebutton.label"));
			startButton = new Button(startButtonText, () -> {
				var gameTry = Game.builder()
				  .withMapGenerator(selectedMapGenerator.get())
				  .withConfiguration(configuration.build())
				  .build();

				if (gameTry.isFailure()) {
					var errorMessage = i18n.getTranslationFor(Locale.ENGLISH, "gameWindow.couldNotCreateGame")
					  .getOrElseThrow(() -> new TranslationNotFoundException("gameWindow.couldNotCreateGame"));
					MessageDialog.showMessageDialog(
					  getOverridenTextGui(),
					  "Error",
					  errorMessage,
					  MessageDialogButton.OK);
					log.error(format("Could not create game: %s", gameTry.getCause().getMessage()));

					return;
				}

				var gameWindowTry = gameWindowProvider.get().build(gameTry.get());
				if (gameWindowTry.isFailure()) {
					var errorMessage = i18n.getTranslationFor(Locale.ENGLISH, "gameWindow.couldNotCreateWindow")
					  .getOrElseThrow(() -> new TranslationNotFoundException("gameWindow.couldNotCreateWindow"));
					MessageDialog.showMessageDialog(
					  getOverridenTextGui(),
					  "Error",
					  errorMessage,
					  MessageDialogButton.OK);
					log.error(format("Could not create game window: %s", gameWindowTry.getCause().getMessage()));

					return;
				}

				var gameWindow = gameWindowTry.get();
				getOverridenTextGui().addWindow(gameWindow);
				getOverridenTextGui().setActiveWindow(gameWindow);
				close();
			});
			startButton.setEnabled(false);
			optionsPanel.addComponent(startButton, LinearLayout.createLayoutData(End));

			setComponent(backgroundPanel);

			return null;
		});
	}

	private void setupFrameConfig() {
		setHints(java.util.List.of(Window.Hint.FULL_SCREEN));
		setCloseWindowWithEscape(true);
	}

	private void setupEventHandling() {
		registerEventListener(MapGeneratorSelectedEvent.class, (event) -> {
			this.selectedMapGenerator = Option.of(event.getMapGenerator());
			enableStartButtonIfPreconditionsSucceed();
		});
		registerEventListener(CivilizationSelectedEvent.class, (event) -> {
			this.selectedCivilization = Option.of(event.getCivilization());
			enableStartButtonIfPreconditionsSucceed();
		});
	}

	private void enableStartButtonIfPreconditionsSucceed() {
		startButton.setEnabled(selectedCivilization.isDefined() && selectedMapGenerator.isDefined());
	}
}
