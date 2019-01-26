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
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import io.vavr.control.Option;
import lombok.extern.slf4j.Slf4j;
import org.sephire.games.framework4x.clients.terminal.gui.Basic4XWindow;
import org.sephire.games.framework4x.clients.terminal.gui.components.MessagePanel;
import org.sephire.games.framework4x.clients.terminal.gui.startgame.StartGameWindow;
import org.sephire.games.framework4x.core.plugins.PluginManager;
import org.sephire.games.framework4x.core.plugins.PluginSpec;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.googlecode.lanterna.gui2.LinearLayout.Alignment.Fill;
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

	public SelectPluginsWindow(PluginManager pluginManager, WindowBasedTextGUI textGUI) {
		super(textGUI);
		setTitle(getTranslationFor("selectPluginWindow.title", Locale.ENGLISH));
		this.pluginManager = pluginManager;
		this.selectedPlugins = HashSet.empty();

		setupWindowFrame();
		addComponents();
		setupEvents();
	}

	private void setupEvents() {
		registerEventListener(PluginInteractedEvent.class,(event)->{
			if(event.isSelected()) {
				selectedPlugins = selectedPlugins.add(event.getPlugin());
			} else {
				selectedPlugins = selectedPlugins.remove(event.getPlugin());
			}

			fireEvent(new SelectedPluginsListUpdatedEvent(selectedPlugins));
		});
	}

	private void setupWindowFrame() {
		setHints(List.of(Window.Hint.FULL_SCREEN));

		var self = this; // JS is coming to Java!
		addWindowListener(new WindowListenerAdapter() {
			@Override
			public void onInput(Window basePane, KeyStroke keyStroke, AtomicBoolean deliverEvent) {
				if (keyStroke.getKeyType() == KeyType.Escape) {
					deliverEvent.set(false);
					self.close();
				}
			}
		});
	}

	private void addComponents() {
		Panel backgroundPanel = new Panel();
		backgroundPanel.setLayoutManager(new BorderLayout());
		backgroundPanel.setPreferredSize(getSize());

		buildPluginsListPanel(backgroundPanel);
		buildPluginInfoPanel(backgroundPanel);
		buildInfoPanel(backgroundPanel);

		setComponent(backgroundPanel);
	}

	private void buildPluginsListPanel(Panel backgroundPanel) {
		var pluginListPanel = new Panel();
		pluginListPanel.setLayoutData(BorderLayout.Location.CENTER);
		pluginListPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

		var pluginsListLabel = new Label(getTranslationFor("selectPluginWindow.pluginList.description",Locale.ENGLISH));
		pluginsListLabel.setLayoutData(LinearLayout.createLayoutData(Fill));
		pluginListPanel.addComponent(pluginsListLabel);

		var pluginCheckBoxList = new PluginsCheckboxList(pluginManager.getAvailablePlugins());
		pluginCheckBoxList.setLayoutData(LinearLayout.createLayoutData(Fill));
		pluginListPanel.addComponent(pluginCheckBoxList);

		var selectButton = new Button(getTranslationFor("selectPluginWindow.pluginList.startButton",Locale.ENGLISH));
		selectButton.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.End));
		selectButton.setEnabled(false);
		selectButton.addListener((button)->{
			var loadingTry = pluginManager.loadPlugins(selectedPlugins.map((p)->p.getPluginName()));
			if(loadingTry.isFailure()){
				var errorMessage = "Could not load the selected plugins, a game cannot be created. Check logs to see detailed error";
				MessageDialog.showMessageDialog(getOverridenTextGui(),"Error",errorMessage, MessageDialogButton.OK);
				log.error("The plugin manager could not load plugins: ",loadingTry.getCause().getMessage());
				return;
			}

			var startGameWindow = new StartGameWindow(pluginManager,getOverridenTextGui());
			this.getTextGUI().addWindow(startGameWindow);
			this.getTextGUI().setActiveWindow(startGameWindow);
		});
		pluginListPanel.addComponent(selectButton);
		registerEventListener(SelectedPluginsListUpdatedEvent.class,(event)->{
			var isBasePluginSelected = event.getSelectedPlugins().exists(PluginSpec::isBasePlugin);
			selectButton.setEnabled(isBasePluginSelected);
		});

		backgroundPanel.addComponent(pluginListPanel.withBorder(Borders.singleLine(getTranslationFor("selectPluginWindow.pluginList.title",Locale.ENGLISH))));
	}

	private void buildPluginInfoPanel(Panel backgroundPanel) {
		Panel pluginInfoPanel = new Panel();
		pluginInfoPanel.setPreferredSize(sizeWithWidthToPercent(getOverridenTextGui().getScreen().getTerminalSize(), 0.4f));
		pluginInfoPanel.setLayoutData(BorderLayout.Location.RIGHT);
		pluginInfoPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

		var headerLabel = new Label(getTranslationFor("selectPluginWindow.pluginInfoPanel.description",Locale.ENGLISH));
		headerLabel.setLayoutData(LinearLayout.createLayoutData(Fill));
		pluginInfoPanel.addComponent(headerLabel);

		var pluginInfoLabel = new Label("");
		pluginInfoLabel.setLabelWidth(pluginInfoPanel.getPreferredSize().getColumns()-3);
		pluginInfoLabel.setLayoutData(LinearLayout.createLayoutData(Fill));
		pluginInfoPanel.addComponent(pluginInfoLabel);

		var firstSelectedPlugin = Option.of(this.pluginManager.getAvailablePlugins().toSortedSet().getOrElse((PluginSpec)null));
		if(firstSelectedPlugin.isDefined()) {
			pluginInfoLabel.setText("\n"+firstSelectedPlugin.get().getDescription(Locale.ENGLISH).get());
		}

		registerEventListener(PluginTraversedEvent.class,(event)->{
			pluginInfoLabel.setText("\n"+event.getSelectedPlugin().getDescription(Locale.ENGLISH).get());
		});

		backgroundPanel.addComponent(pluginInfoPanel.withBorder(Borders.singleLine()));
	}

	private void buildInfoPanel(Panel backgroundPanel) {
		Panel infoPanel = new Panel();
		infoPanel.setPreferredSize(getOverridenTextGui().getScreen().getTerminalSize().withRows(4));
		infoPanel.setLayoutData(BorderLayout.Location.BOTTOM);
		infoPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

		var messagePanel = new MessagePanel();
		messagePanel.setLayoutData(LinearLayout.createLayoutData(Fill));
		infoPanel.addComponent(messagePanel);

		messagePanel.addMessage(getTranslationFor("selectPluginWindow.messagePanel.defaultMessage",Locale.ENGLISH), INFO);
		messagePanel.addMessage(getTranslationFor("selectPluginWindow.messagePanel.needBasePlugin",Locale.ENGLISH),ERROR);

		registerEventListener(SelectedPluginsListUpdatedEvent.class,(event)->{
			var needsBasePluginSelected = !event.getSelectedPlugins().exists(PluginSpec::isBasePlugin);
			if(needsBasePluginSelected) {
				messagePanel.addMessageIfNotExists(getTranslationFor("selectPluginWindow.messagePanel.needBasePlugin",Locale.ENGLISH),ERROR);
			} else {
				messagePanel.removeMessage(getTranslationFor("selectPluginWindow.messagePanel.needBasePlugin",Locale.ENGLISH));
			}
		});


		backgroundPanel.addComponent(infoPanel.withBorder(Borders.singleLine()));
	}
}
