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

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import io.vavr.Function1;
import lombok.extern.slf4j.Slf4j;
import org.sephire.games.framework4x.clients.terminal.gui.selectplugins.SelectPluginsWindow;
import org.sephire.games.framework4x.core.plugins.PluginManager;

import java.nio.file.Path;
import java.util.List;

import static com.googlecode.lanterna.gui2.Borders.doubleLine;
import static java.lang.System.out;
import static org.sephire.games.framework4x.clients.terminal.utils.Functions.wrap;

@Slf4j
public class MenuWindow extends BasicWindow {

	private static final String DEFAULT_PLUGIN_FOLDER = "plugins";

	private WindowBasedTextGUI textGUI;

	public MenuWindow(WindowBasedTextGUI textGUI) {
		super("4X Framework Menu");
		this.textGUI = textGUI;

		setHints(List.of(Window.Hint.FULL_SCREEN));

		Panel backgroundPanel = new Panel();
		backgroundPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
		backgroundPanel.setPreferredSize(getSize());

		Panel menuPanel = new Panel();
		menuPanel.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
		menuPanel.addComponent(buttonFor("Start Game", wrap((b) -> {
			log.info("Selected start game");

			var pluginManagerBuilding = PluginManager.fromFolder(Path.of(".",DEFAULT_PLUGIN_FOLDER));
			if(pluginManagerBuilding.isFailure()) {
				var errorMessage = "Could not load the list of plugins, a game cannot be created. Check logs to see detailed error";
				MessageDialog.showMessageDialog(this.getTextGUI(),"Error",errorMessage, MessageDialogButton.OK);
				log.error("The plugin manager could not load: ",pluginManagerBuilding.getCause().getMessage());
				return;
			}

			var selectPluginsWindow = new SelectPluginsWindow(pluginManagerBuilding.get(),this.textGUI);
			this.getTextGUI().addWindow(selectPluginsWindow);
			this.getTextGUI().setActiveWindow(selectPluginsWindow);
	  	})));

		menuPanel.addComponent(buttonFor("Load Game", wrap((b) -> out.println("Load game activated"))));
		menuPanel.addComponent(buttonFor("Manage Plugins", wrap((b) -> out.println("Manage Plugins activated"))));
		menuPanel.addComponent(buttonFor("Configuration", wrap((b) -> out.println("Configuration activated"))));
		menuPanel.addComponent(buttonFor("Exit", wrap((b) -> {
			out.println("Exit activated");
			close();
		})));
		backgroundPanel.addComponent(menuPanel.withBorder(doubleLine()));

		setComponent(backgroundPanel.withBorder(doubleLine()));
	}

	private static Button buttonFor(String label, Function1<Button, Void> buttonAction) {
		Button menuItem = new Button(label);
		menuItem.addListener(buttonAction::apply);

		return menuItem;
	}

}
