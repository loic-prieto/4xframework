package org.sephire.games.framework4x.clients.terminal.gui.startgame;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import io.vavr.collection.List;
import io.vavr.collection.Set;
import lombok.extern.slf4j.Slf4j;
import org.sephire.games.framework4x.clients.terminal.gui.Basic4XWindow;
import org.sephire.games.framework4x.clients.terminal.gui.components.map.FakeTerrainType;
import org.sephire.games.framework4x.clients.terminal.utils.ToStringDecorator;
import org.sephire.games.framework4x.core.Game;
import org.sephire.games.framework4x.core.model.ai.AIDifficultyLevel;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.model.config.CoreConfigKeyEnum;
import org.sephire.games.framework4x.core.model.gameplay.VictoryCondition;
import org.sephire.games.framework4x.core.model.map.GameMap;
import org.sephire.games.framework4x.core.model.map.MapZone;
import org.sephire.games.framework4x.core.model.map.Size;
import org.sephire.games.framework4x.core.model.research.ResearchCostMultiplier;
import org.sephire.games.framework4x.core.plugins.PluginManager;
import org.sephire.games.framework4x.core.plugins.map.MapGenerator;
import org.sephire.games.framework4x.core.plugins.map.MapGeneratorWrapper;
import org.sephire.games.framework4x.core.utils.FunctionalUtils;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.googlecode.lanterna.gui2.Borders.doubleLine;

@Slf4j
public class StartGameWindow extends Basic4XWindow {

	private PluginManager pluginManager;

	public StartGameWindow(PluginManager pluginManager,WindowBasedTextGUI textGUI) {
		super(textGUI);
		this.pluginManager = pluginManager;
		setupFrameConfig();
		setupComponents();
	}

	private void setupComponents(){
		var backgroundPanel = new Panel();
		backgroundPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
		backgroundPanel.setPreferredSize(getSize());

		var optionsPanel = new Panel();
		optionsPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
		backgroundPanel.addComponent(optionsPanel.withBorder(doubleLine(getTranslationFor("startGameWindow.optionsPane.label"))));

		optionsPanel.addComponent(createMapOption());

		setComponent(backgroundPanel);
	}

	private Panel createMapOption(){
		Panel mapOptionPanel = new Panel();
		mapOptionPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

		var mapGenerators = pluginManager.getLoadedConfiguration().map((config)->config.getConfiguration(CoreConfigKeyEnum.MAPS, Set.class));
		if(mapGenerators.isDefined() && mapGenerators.get().isSuccess() && mapGenerators.get().get().isDefined()){
			var mapLabel = new Label(getTranslationFor("startGameWindow.optionsPane.map.label"));
			var mapSelection = new ComboBox<ToStringDecorator<MapGeneratorWrapper>>();
			mapGenerators.get().get()
			  .map(s->(Set<MapGeneratorWrapper>)s).get()
			  .map(this::stringifyMapGenerator)
			  .forEach(mapSelection::addItem);

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
		return new ToStringDecorator<>(mapGenerator,(m)->getTranslationFor(m.getDisplayKey()));
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
