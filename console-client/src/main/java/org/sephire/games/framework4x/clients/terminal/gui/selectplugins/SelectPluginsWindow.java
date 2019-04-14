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
package org.sephire.games.framework4x.clients.terminal.gui.selectplugins;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.sephire.games.framework4x.clients.terminal.gui.Basic4XWindow;
import org.sephire.games.framework4x.clients.terminal.gui.components.MessagePanel;
import org.sephire.games.framework4x.clients.terminal.gui.gamewindow.TranslationNotFoundException;
import org.sephire.games.framework4x.clients.terminal.gui.startgame.StartGameWindow;
import org.sephire.games.framework4x.clients.terminal.utils.Terminal;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.plugins.PluginManager;
import org.sephire.games.framework4x.core.plugins.PluginSpec;

import java.util.List;
import java.util.Locale;

import static com.googlecode.lanterna.gui2.Borders.singleLine;
import static com.googlecode.lanterna.gui2.LinearLayout.Alignment.Fill;
import static java.lang.String.format;
import static org.sephire.games.framework4x.clients.terminal.gui.components.MessagePanel.MessageType.ERROR;
import static org.sephire.games.framework4x.clients.terminal.gui.components.MessagePanel.MessageType.INFO;
import static org.sephire.games.framework4x.clients.terminal.utils.Terminal.Dimensions.sizeWithWidthToPercent;

/**
 * This window is shown when starting a game, to select which plugins will be active for
 * a game.
 *
 * This screen ensures that no bad combination of plugins can be made. Once all is good, the next step is
 * to show the CreateGameWindow.
 */
@Slf4j
public class SelectPluginsWindow extends Basic4XWindow {

	private PluginManager pluginManager;

	/**
	 * The set of selected plugins by the user inside the plugins checkbox list.
	 * The data is feed from listening to interacted plugins event.
	 * It is used to know if the create game button can be interacted with.
	 */
	private Set<PluginSpec> selectedPlugins;

	private SelectPluginsWindow(PluginManager pluginManager, WindowBasedTextGUI textGUI) throws Throwable {
		super(textGUI);

		this.pluginManager = pluginManager;
		this.selectedPlugins = HashSet.empty();

		setupWindowFrame()
		  .andThen(this::setupComponents)
		  .andThen(this::setupEvents)
		  .getOrElseThrow(t -> t);
	}

	public static Try<SelectPluginsWindow> of(PluginManager pluginManager, WindowBasedTextGUI textGUI) {
		return Try.of(() -> {
			return new SelectPluginsWindow(pluginManager, textGUI);
		});
	}

	private Try<Void> setupEvents() {
		return Try.of(() -> {
			registerEventListener(PluginInteractedEvent.class, (event) -> {
				if (event.isSelected()) {
					selectedPlugins = selectedPlugins.add(event.getPlugin());
				} else {
					selectedPlugins = selectedPlugins.remove(event.getPlugin());
				}

				fireEvent(new SelectedPluginsListUpdatedEvent(selectedPlugins));
			});

			return null;
		});
	}

	private Try<Void> setupWindowFrame() {
		return Try.of(() -> {
			setHints(List.of(Window.Hint.FULL_SCREEN));
			setTitle(getTranslationFor("selectPluginWindow.title"));
			setCloseWindowWithEscape(true);
			return null;
		});
	}

	private Try<Void> setupComponents() {
		return Try.of(() -> {
			Panel backgroundPanel = new Panel();
			backgroundPanel.setLayoutManager(new BorderLayout());
			backgroundPanel.setPreferredSize(getSize());

			buildPluginsListPanel(backgroundPanel)
			  .andThen(() -> buildInfoPanel(backgroundPanel))
			  .andThen(() -> buildPluginInfoPanel(backgroundPanel))
			  .getOrElseThrow(t -> t);

			setComponent(backgroundPanel);

			return null;
		});
	}

	private Try<Void> buildPluginsListPanel(Panel backgroundPanel) {
		return Try.of(() -> {
			var pluginListPanel = new Panel();
			pluginListPanel.setLayoutData(BorderLayout.Location.CENTER);
			pluginListPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

			var pluginsListLabel = new Label(getTranslationFor("selectPluginWindow.pluginList.description"));
			pluginsListLabel.setLayoutData(LinearLayout.createLayoutData(Fill));
			pluginListPanel.addComponent(pluginsListLabel);

			var pluginCheckBoxList = new PluginsCheckboxList(pluginManager.getAvailablePlugins());
			pluginCheckBoxList.setLayoutData(LinearLayout.createLayoutData(Fill));
			pluginListPanel.addComponent(pluginCheckBoxList);

			var selectButton = new Button(getTranslationFor("selectPluginWindow.pluginList.startButton"));
			selectButton.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.End));
			selectButton.setEnabled(false);
			selectButton.addListener((button) -> {
				var configuration = Configuration.builder();

				var loadingTry = pluginManager.loadPlugins(selectedPlugins.map(PluginSpec::getPluginName), configuration);
				if (loadingTry.isFailure()) {
					var errorMessage = getTranslationFor("selectPluginWindow.errors.gameFail");

					MessageDialog.showMessageDialog(getOverridenTextGui(), "Error", errorMessage, MessageDialogButton.OK);
					log.error(format("The plugin manager could not load plugins: %s ", loadingTry.getCause().getMessage()));
					return;
				}

				var startGameWindow = StartGameWindow.from(pluginManager, configuration, getOverridenTextGui());
				if (startGameWindow.isFailure()) {
					var errorMessage = getTranslationFor("selectPluginWindow.errors.startWindowFail");

					MessageDialog.showMessageDialog(getOverridenTextGui(), "Error", errorMessage, MessageDialogButton.OK);
					log.error(format("The start game window could not be created: %s", startGameWindow.getCause().getMessage()));
					return;
				}
				this.getTextGUI().addWindow(startGameWindow.get());
				this.getTextGUI().setActiveWindow(startGameWindow.get());
			});
			pluginListPanel.addComponent(selectButton);
			registerEventListener(SelectedPluginsListUpdatedEvent.class, (event) -> {
				var isBasePluginSelected = event.getSelectedPlugins().exists(PluginSpec::isBasePlugin);
				selectButton.setEnabled(isBasePluginSelected);
			});

			backgroundPanel.addComponent(pluginListPanel
			  .withBorder(singleLine(
				getTranslationFor("selectPluginWindow.pluginList.title"))));

			return null;
		});

	}

	private Try<Void> buildPluginInfoPanel(Panel backgroundPanel) {
		return Try.of(() -> {
			Panel pluginInfoPanel = new Panel();
			pluginInfoPanel.setPreferredSize(sizeWithWidthToPercent(getOverridenTextGui().getScreen().getTerminalSize(), 0.4f));
			pluginInfoPanel.setLayoutData(BorderLayout.Location.RIGHT);
			pluginInfoPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

			var headerLabel = new Label(getTranslationFor("selectPluginWindow.pluginInfoPanel.description"));
			headerLabel.setLayoutData(LinearLayout.createLayoutData(Fill));
			pluginInfoPanel.addComponent(headerLabel);

			var pluginInfoLabel = new Label("");
			pluginInfoLabel.setLabelWidth(pluginInfoPanel.getPreferredSize().getColumns() - 3);
			pluginInfoLabel.setLayoutData(LinearLayout.createLayoutData(Fill));
			pluginInfoPanel.addComponent(pluginInfoLabel);

			var firstSelectedPlugin = Option.of(this.pluginManager.getAvailablePlugins().toSortedSet().getOrElse((PluginSpec) null));
			if (firstSelectedPlugin.isDefined()) {
				pluginInfoLabel.setText("\n" + firstSelectedPlugin.get().getDescription(Locale.ENGLISH).get());
			}

			registerEventListener(PluginTraversedEvent.class, (event) -> {
				pluginInfoLabel.setText("\n" + event.getSelectedPlugin().getDescription(Locale.ENGLISH).get());
			});

			backgroundPanel.addComponent(pluginInfoPanel.withBorder(singleLine()));

			return null;
		});
	}

	private Try<Void> buildInfoPanel(Panel backgroundPanel) {
		return Try.of(() -> {
			Panel infoPanel = new Panel();
			infoPanel.setPreferredSize(getOverridenTextGui().getScreen().getTerminalSize().withRows(4));
			infoPanel.setLayoutData(BorderLayout.Location.BOTTOM);
			infoPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

			var messagePanel = new MessagePanel();
			messagePanel.setLayoutData(LinearLayout.createLayoutData(Fill));
			infoPanel.addComponent(messagePanel);

			messagePanel.addMessage(getTranslationFor("selectPluginWindow.messagePanel.defaultMessage"), INFO);
			messagePanel.addMessage(getTranslationFor("selectPluginWindow.messagePanel.needBasePlugin"), ERROR);

			registerEventListener(SelectedPluginsListUpdatedEvent.class, (event) -> {
				var needsBasePluginSelected = !event.getSelectedPlugins().exists(PluginSpec::isBasePlugin);
				if (needsBasePluginSelected) {
					messagePanel.addMessageIfNotExists(
					  getTranslationFor("selectPluginWindow.messagePanel.needBasePlugin"), ERROR);
				} else {
					messagePanel.removeMessage(
					  getTranslationFor("selectPluginWindow.messagePanel.needBasePlugin"));
				}
			});


			backgroundPanel.addComponent(infoPanel.withBorder(singleLine()));

			return null;
		});
	}

	private static String getTranslationFor(String labelKey) throws TranslationNotFoundException {
		return Terminal.Translation.getTranslationFor(Locale.ENGLISH, labelKey)
		  .getOrElseThrow(() -> new TranslationNotFoundException(labelKey));
	}
}
