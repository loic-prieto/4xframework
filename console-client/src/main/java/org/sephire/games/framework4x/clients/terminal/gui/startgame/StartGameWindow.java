package org.sephire.games.framework4x.clients.terminal.gui.startgame;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import io.vavr.collection.List;
import org.sephire.games.framework4x.clients.terminal.gui.Basic4XWindow;
import org.sephire.games.framework4x.clients.terminal.gui.components.map.FakeTerrainType;
import org.sephire.games.framework4x.core.Game;
import org.sephire.games.framework4x.core.model.ai.AIDifficultyLevel;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.model.gameplay.VictoryCondition;
import org.sephire.games.framework4x.core.model.map.GameMap;
import org.sephire.games.framework4x.core.model.map.MapZone;
import org.sephire.games.framework4x.core.model.map.Size;
import org.sephire.games.framework4x.core.model.research.ResearchCostMultiplier;
import org.sephire.games.framework4x.core.plugins.PluginManager;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.googlecode.lanterna.gui2.Borders.doubleLine;

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
		backgroundPanel.addComponent(optionsPanel.withBorder(doubleLine("Choose game options")));

		var mapSelection = createMapOption();
		optionsPanel.addComponent(mapSelection);

		var statusPanel = new Panel();
		backgroundPanel.addComponent(statusPanel.withBorder(doubleLine()));

		setComponent(backgroundPanel);
	}

	private Panel createMapOption(){

		var mapLabel = new Label(getTranslationFor("startGameWindow.optionsPane.map.label", Locale.ENGLISH));
		var mapSelection = new ComboBox<String>();
		mapSelection.addItem("Default");
		mapSelection.setSelectedIndex(0);

		return new Panel().setLayoutManager(new LinearLayout(Direction.HORIZONTAL))
		  .addComponent(mapLabel)
		  .addComponent(mapSelection);
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
