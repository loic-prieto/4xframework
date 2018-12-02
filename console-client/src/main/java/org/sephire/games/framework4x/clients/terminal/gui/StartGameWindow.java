package org.sephire.games.framework4x.clients.terminal.gui;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import io.vavr.collection.List;
import io.vavr.control.Try;
import org.sephire.games.framework4x.clients.terminal.gui.components.map.FakeTerrainType;
import org.sephire.games.framework4x.core.Game;
import org.sephire.games.framework4x.core.model.ai.AIDifficultyLevel;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.model.gameplay.VictoryCondition;
import org.sephire.games.framework4x.core.model.map.GameMap;
import org.sephire.games.framework4x.core.model.map.MapZone;
import org.sephire.games.framework4x.core.model.map.Size;
import org.sephire.games.framework4x.core.model.research.ResearchCostMultiplier;

import static com.googlecode.lanterna.gui2.Borders.doubleLine;
import static org.sephire.games.framework4x.core.model.map.GameMap.builder;

public class StartGameWindow extends BasicWindow {

	public StartGameWindow() {
		super("Start game");
		setHints(java.util.List.of(Window.Hint.FULL_SCREEN));

		var backgroundPanel = new Panel();
		backgroundPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
		backgroundPanel.setPreferredSize(getSize());

		var optionsPanel = new Panel();
		optionsPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
		backgroundPanel.addComponent(optionsPanel.withBorder(doubleLine("Choose game options")));

		var mapLabel = new Label("Choose map:");
		var mapSelection = new ComboBox<String>();
		mapSelection.addItem("Default");
		mapSelection.setSelectedIndex(0);
		optionsPanel.addComponent(
		  new Panel().setLayoutManager(new LinearLayout(Direction.HORIZONTAL))
			.addComponent(mapLabel)
			.addComponent(mapSelection));

		var difficultyLevelLabel = new Label("Choose difficulty level:");
		var difficultyLevelSelection = new ComboBox<AIDifficultyLevel>();
		List.of(AIDifficultyLevel.values()).forEach(difficultyLevelSelection::addItem);
		difficultyLevelSelection.setSelectedItem(AIDifficultyLevel.NORMAL);
		optionsPanel.addComponent(
		  new Panel().setLayoutManager(new LinearLayout(Direction.HORIZONTAL))
			.addComponent(difficultyLevelLabel)
			.addComponent(difficultyLevelSelection));

		var numberOfEnemiesLabel = new Label("Choose number of enemies:");
		var numberOfEnemiesSelection = new ComboBox<Integer>();
		List.range(0, 21).forEach(numberOfEnemiesSelection::addItem);
		numberOfEnemiesSelection.setSelectedItem(0);
		optionsPanel.addComponent(
		  new Panel().setLayoutManager(new LinearLayout(Direction.HORIZONTAL))
			.addComponent(numberOfEnemiesLabel)
			.addComponent(numberOfEnemiesSelection));

		var researchSpeedLabel = new Label("Choose Research speed:");
		var researchSpeedSelection = new ComboBox<ResearchCostMultiplier>();
		List.of(ResearchCostMultiplier.values()).forEach(researchSpeedSelection::addItem);
		researchSpeedSelection.setSelectedItem(ResearchCostMultiplier.NORMAL);
		optionsPanel.addComponent(
		  new Panel().setLayoutManager(new LinearLayout(Direction.HORIZONTAL))
			.addComponent(researchSpeedLabel)
			.addComponent(researchSpeedSelection));

		var victoryConditionsLabel = new Label("Choose victory conditions: ");
		var victoryConditionsSelection = new CheckBoxList<VictoryCondition>();
		List.of(VictoryCondition.values()).forEach(victoryConditionsSelection::addItem);
		victoryConditionsSelection.setChecked(VictoryCondition.CONQUEST, true);
		optionsPanel.addComponent(
		  new Panel().setLayoutManager(new LinearLayout(Direction.HORIZONTAL))
			.addComponent(victoryConditionsLabel)
			.addComponent(victoryConditionsSelection));

		var pluginsLabel = new Label("Choose active plugins: ");
		var pluginsSelection = new CheckBoxList<String>();
		pluginsSelection.setEnabled(false);
		pluginsSelection.addItem("Base", true);
		optionsPanel.addComponent(
		  new Panel().setLayoutManager(new LinearLayout(Direction.HORIZONTAL))
			.addComponent(pluginsLabel)
			.addComponent(pluginsSelection));

		var startButton = new Button("Start game!",()->{
			var newGameTry = new Game.Builder()
			  .withPlugins(
			    "org.sephire.games.framework4x.plugins.standard"
				,"org.sephire.games.framework4x.plugins.standard.terminal")
			  .withMap(buildFakeMap())
			  .build();
			if(newGameTry.isFailure()) {
				var errorMessage = String.format("Could not create the game: %s",newGameTry.getCause().getMessage());
				MessageDialog.showMessageDialog(this.getTextGUI(),"Error",errorMessage, MessageDialogButton.Close);
				return;
			}

			var gameWindow = GameWindow.of(newGameTry.get());
			if(gameWindow.isFailure()){
				var errorMessage = String.format("Could not create the game: %s",gameWindow.getCause().getMessage());
				MessageDialog.showMessageDialog(this.getTextGUI(),"Error",errorMessage, MessageDialogButton.Close);
				return;
			}

			this.getTextGUI().addWindow(gameWindow.get());
			this.getTextGUI().setActiveWindow(gameWindow.get());
			this.getTextGUI().removeWindow(this);
		});
		optionsPanel.addComponent(startButton);

		var statusPanel = new Panel();
		backgroundPanel.addComponent(statusPanel.withBorder(doubleLine()));

		setComponent(backgroundPanel);
	}

	private GameMap buildFakeMap(){
		return GameMap.builder()
		  .addZone(MapZone.builder()
			.withName("level0")
		  	.withDefaultCells(new Size(20,20), FakeTerrainType.FAKE)
		  	.build()
			.get())
		  .withDefaultZone("level0")
		  .build()
		  .get();
	}
}
