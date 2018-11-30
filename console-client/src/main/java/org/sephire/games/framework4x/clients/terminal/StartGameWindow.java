package org.sephire.games.framework4x.clients.terminal;

import com.googlecode.lanterna.gui2.*;
import io.vavr.collection.List;
import org.sephire.games.framework4x.core.model.ai.AIDifficultyLevel;
import org.sephire.games.framework4x.core.model.gameplay.VictoryCondition;
import org.sephire.games.framework4x.core.model.research.ResearchCostMultiplier;

import static com.googlecode.lanterna.gui2.Borders.doubleLine;

public class StartGameWindow extends BasicWindow {
	public StartGameWindow() {
		super("Start game");

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
				.addComponent(difficultyLevelLabel)
				.addComponent(difficultyLevelSelection));

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

		var statusPanel = new Panel();
		backgroundPanel.addComponent(statusPanel.withBorder(doubleLine()));

		setComponent(backgroundPanel);

	}
}
