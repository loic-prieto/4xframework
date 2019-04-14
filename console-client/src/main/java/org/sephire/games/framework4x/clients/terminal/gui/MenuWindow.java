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
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.sephire.games.framework4x.clients.terminal.gui.gamewindow.TranslationNotFoundException;
import org.sephire.games.framework4x.clients.terminal.gui.selectplugins.SelectPluginsWindow;
import org.sephire.games.framework4x.core.plugins.PluginManager;

import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

import static com.googlecode.lanterna.gui2.Borders.doubleLine;
import static java.lang.String.format;
import static org.sephire.games.framework4x.clients.terminal.utils.Functions.wrap;
import static org.sephire.games.framework4x.clients.terminal.utils.Terminal.Translation.getTranslationFor;

@Slf4j
public class MenuWindow extends BasicWindow {

	private static final String DEFAULT_PLUGIN_FOLDER = "plugins";

	private WindowBasedTextGUI textGUI;

	private MenuWindow(WindowBasedTextGUI textGUI) throws Throwable {
		super(getTranslationFor(Locale.ENGLISH,"menuwindow.title")
		  .getOrElseThrow(()->new TranslationNotFoundException("menuwindow.title")));

		this.textGUI = textGUI;

		setHints(List.of(Window.Hint.FULL_SCREEN));

		Panel backgroundPanel = new Panel();
		backgroundPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
		backgroundPanel.setPreferredSize(getSize());

		Panel menuPanel = new Panel();
		menuPanel.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
		menuPanel.addComponent(buttonFor("menuwindow.startGame", wrap((b) -> {
			var pluginManagerBuilding = PluginManager.fromFolder(Path.of(".",DEFAULT_PLUGIN_FOLDER));
			if(pluginManagerBuilding.isFailure()) {
				var errorMessage = getTranslationFor(Locale.ENGLISH,"menuwindow.startGame.pluginManagerFail")
				  .getOrElseThrow(()->new TranslationNotFoundException("menuwindow.startGame.pluginManagerFail"));
				MessageDialog.showMessageDialog(this.getTextGUI(),"Error",errorMessage, MessageDialogButton.OK);
				log.error(format("The plugin manager could not load: %s",pluginManagerBuilding.getCause().getMessage()));
				return;
			}

			var selectPluginsWindow = SelectPluginsWindow.of(pluginManagerBuilding.get(),this.textGUI);
			if(selectPluginsWindow.isFailure()) {
				var errorMessage = getTranslationFor(Locale.ENGLISH,"menuwindow.startGame.selectPluginsWindowFail")
				  .getOrElseThrow(()->new TranslationNotFoundException("menuwindow.startGame.selectPluginsWindowFail"));
				MessageDialog.showMessageDialog(this.getTextGUI(),"Error",errorMessage, MessageDialogButton.OK);
				log.error(format("The select plugins window could not be created: %s",pluginManagerBuilding.getCause().getMessage()));
				return;
			}

			this.getTextGUI().addWindow(selectPluginsWindow.get());
			this.getTextGUI().setActiveWindow(selectPluginsWindow.get());
	  	})).getOrElseThrow(t->t));

		menuPanel.addComponent(buttonFor("menuwindow.exitGame", wrap((b) -> {
			System.out.println(getTranslationFor(Locale.ENGLISH,"menuwindow.byeMessage"));
			close();

		})).getOrElseThrow(t->t));

		backgroundPanel.addComponent(menuPanel.withBorder(doubleLine()));

		setComponent(backgroundPanel.withBorder(doubleLine()));
	}

	public static Try<MenuWindow> of(WindowBasedTextGUI textGUI) {
		return Try.of(()->{
			return new MenuWindow(textGUI);
		});
	}

	private static Try<Button> buttonFor(String label, Function1<Button, Void> buttonAction) {
		return Try.of(()->{
			var labelText = getTranslationFor(Locale.ENGLISH,label)
			  .getOrElseThrow(()->new TranslationNotFoundException(label));

			Button menuItem = new Button(labelText);
			menuItem.addListener(buttonAction::apply);

			return menuItem;
		});
	}

}
