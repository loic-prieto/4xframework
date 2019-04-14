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
import org.sephire.games.framework4x.core.model.civilization.Civilization;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.model.game.Game;
import org.sephire.games.framework4x.core.plugins.PluginManager;
import org.sephire.games.framework4x.core.plugins.map.MapGeneratorWrapper;

import java.util.Locale;

import static com.googlecode.lanterna.gui2.Borders.doubleLine;
import static com.googlecode.lanterna.gui2.LinearLayout.Alignment.End;
import static java.lang.String.format;
import static org.sephire.games.framework4x.clients.terminal.utils.Terminal.Translation.getTranslationFor;

/**
 * <p>In the start game window,the player selects the options of a game, including the map and the civilization</p>
 */
@Slf4j
public class StartGameWindow extends Basic4XWindow {

	private PluginManager pluginManager;
	private Configuration.Builder configuration;
	private Option<MapGeneratorWrapper> selectedMapGenerator;
	private Option<Civilization> selectedCivilization;
	private Button startButton;

	private StartGameWindow(PluginManager pluginManager,
							Configuration.Builder configuration,
							WindowBasedTextGUI textGUI) throws Throwable {
		super(textGUI);
		this.pluginManager = pluginManager;
		this.configuration = configuration;
		this.selectedCivilization = Option.none();
		this.selectedMapGenerator = Option.none();

		setupComponents().getOrElseThrow(t -> t);

		setupFrameConfig();
		setupEventHandling();
	}

	public static Try<StartGameWindow> from(PluginManager pluginManager,
											Configuration.Builder configuration,
											WindowBasedTextGUI textGUI) {
		return Try.of(() -> {
			return new StartGameWindow(pluginManager, configuration, textGUI);
		});
	}

	private void enableStartButtonIfPreconditionsSucceed() {
		startButton.setEnabled(selectedCivilization.isDefined() && selectedMapGenerator.isDefined());
	}

	private void setupFrameConfig() {
		setHints(java.util.List.of(Window.Hint.FULL_SCREEN));
		setCloseWindowWithEscape(true);
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
				  getTranslationFor(
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
			var startButtonText = getTranslationFor(
			  Locale.ENGLISH,
			  "startGameWindow.OptionsPane.startGamebutton.label")
			  .getOrElseThrow(() -> new TranslationNotFoundException("startGameWindow.OptionsPane.startGamebutton.label"));
			startButton = new Button(startButtonText, () -> {
				var gameTry = Game.builder()
				  .withMapGenerator(selectedMapGenerator.get())
				  .withPluginManager(pluginManager)
				  .withConfiguration(configuration.build())
				  .build();
				if (gameTry.isFailure()) {
					var errorMessage = getTranslationFor(Locale.ENGLISH, "gameWindow.couldNotCreateGame")
					  .getOrElseThrow(() -> new TranslationNotFoundException("gameWindow.couldNotCreateGame"));
					MessageDialog.showMessageDialog(
					  getOverridenTextGui(),
					  "Error",
					  errorMessage,
					  MessageDialogButton.OK);
					log.error(format("Could not create game: %s", gameTry.getCause().getMessage()));

					return;
				}
				var gameWindowTry = GameWindow.of(gameTry.get(), getOverridenTextGui());
				if (gameWindowTry.isFailure()) {
					var errorMessage = getTranslationFor(Locale.ENGLISH, "gameWindow.couldNotCreateWindow")
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
}
