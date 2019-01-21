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
package org.sephire.games.framework4x.clients.terminal.gui.startgame;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import io.vavr.collection.Set;
import lombok.extern.slf4j.Slf4j;
import org.sephire.games.framework4x.clients.terminal.gui.Basic4XWindow;
import org.sephire.games.framework4x.clients.terminal.gui.gamewindow.GameWindow;
import org.sephire.games.framework4x.clients.terminal.utils.ToStringDecorator;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.model.config.CoreConfigKeyEnum;
import org.sephire.games.framework4x.core.model.game.Game;
import org.sephire.games.framework4x.core.plugins.PluginManager;
import org.sephire.games.framework4x.core.plugins.map.MapGeneratorWrapper;
import org.sephire.games.framework4x.core.utils.FunctionalUtils;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.googlecode.lanterna.gui2.Borders.doubleLine;
import static com.googlecode.lanterna.gui2.LinearLayout.Alignment.End;

@Slf4j
public class StartGameWindow extends Basic4XWindow {

	private PluginManager pluginManager;
	private Configuration configuration;
	private MapGeneratorWrapper selectedMapGenerator;

	public StartGameWindow(PluginManager pluginManager,WindowBasedTextGUI textGUI) {
		super(textGUI);
		this.pluginManager = pluginManager;
		checkPreconditions();

		this.configuration = pluginManager.getLoadedConfiguration().get();

		setupFrameConfig();
		setupComponents();
	}

	/**
	 * Check all needed objects of the window are available, so that we cannot be in an incorrect
	 * state when showing this window (and also so that we  don't need to check them every time, which
	 * gets burdensome very fast with options and tries)
	 */
	private void checkPreconditions() {
		if(pluginManager.getLoadedConfiguration().isEmpty()) {
			MessageDialog.showMessageDialog(getOverridenTextGui(),"Error",getTranslationFor("startGameWindow.configurationNotFound"),MessageDialogButton.OK);
			log.error("The configuration object was not found when creating the start game window");
			close();
		}
	}

	private void setupComponents(){
		var backgroundPanel = new Panel();
		backgroundPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
		backgroundPanel.setPreferredSize(getSize());

		var optionsPanel = new Panel();
		optionsPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
		backgroundPanel.addComponent(optionsPanel.withBorder(doubleLine(getTranslationFor("startGameWindow.optionsPane.label"))));
		optionsPanel.addComponent(createMapOption());

		var startGameButton = new Button(getTranslationFor("startGameWindow.OptionsPane.startGamebutton.label"),()->{
			var gameTry = Game.builder()
			  .withMapGenerator(selectedMapGenerator)
			  .withPluginManager(pluginManager)
			  .build();
			if(gameTry.isFailure()){
				MessageDialog.showMessageDialog(getOverridenTextGui(),"Error",
				  getTranslationFor("gameWindow.couldNotCreateGame",gameTry.getCause().getMessage()),
				  MessageDialogButton.OK);
				return;
			}
			var gameWindowTry = GameWindow.of(gameTry.get(),getOverridenTextGui());
			if(gameWindowTry.isFailure()) {
				MessageDialog.showMessageDialog(getOverridenTextGui(),"Error",
				  getTranslationFor("gameWindow.couldNotCreateWindow",gameWindowTry.getCause().getMessage()),
				  MessageDialogButton.OK);
				return;
			}

			var gameWindow = gameWindowTry.get();
			getOverridenTextGui().addWindow(gameWindow);
			getOverridenTextGui().setActiveWindow(gameWindow);
			close();

		});
		optionsPanel.addComponent(startGameButton,LinearLayout.createLayoutData(End));

		setComponent(backgroundPanel);
	}

	private Panel createMapOption(){
		Panel mapOptionPanel = new Panel();
		mapOptionPanel.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));

		var mapGenerators = pluginManager.getLoadedConfiguration().map((config)->config.getConfiguration(CoreConfigKeyEnum.MAPS, Set.class));
		if(mapGenerators.isDefined() && mapGenerators.get().isSuccess() && mapGenerators.get().get().isDefined()){
			var mapLabel = new Label(getTranslationFor("startGameWindow.optionsPane.map.label"));
			var mapSelection = new ComboBox<ToStringDecorator<MapGeneratorWrapper>>();
			mapGenerators.get().get()
			  .map(s->(Set<MapGeneratorWrapper>)s).get()
			  .map(this::stringifyMapGenerator)
			  .forEach(mapSelection::addItem);
			mapSelection.addListener((selectedIndex,previousSelection)->{
				this.selectedMapGenerator = mapSelection.getItem(selectedIndex).getWrappedObject();
			});
			this.selectedMapGenerator = mapSelection.getItem(0).getWrappedObject();

			mapOptionPanel.addComponent(mapLabel);
			mapOptionPanel.addComponent(mapSelection);
		} else {
			var noMapGeneratorFoundLabel = new Label(getTranslationFor("startGameWindow.optionsPane.map.generatorsNotFound"));
			mapOptionPanel.addComponent(noMapGeneratorFoundLabel);
			if(mapGenerators.isEmpty()) {
				log.warn("No map generators were found for the list of plugins: " + pluginManager.getLoadedPlugins().reduce(FunctionalUtils.Reduce.strings()));
			} else {
				log.error("There was an error when retrieving the map generators: "+mapGenerators.get().getCause().getMessage());
			}
		}

		return mapOptionPanel;
	}

	private ToStringDecorator<MapGeneratorWrapper> stringifyMapGenerator(MapGeneratorWrapper mapGenerator){
		return new ToStringDecorator<>(mapGenerator,(m)->
		  configuration.getTranslationFor(Locale.ENGLISH,m.getDisplayKey())
			.getOrElse(getTranslationFor("startGameWindow.mapGeneratorWithInvalidI18N.title")));
	}


	private void setupFrameConfig(){
		setHints(java.util.List.of(Window.Hint.FULL_SCREEN));
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
}
