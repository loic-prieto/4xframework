package org.sephire.games.framework4x.clients.terminal.gui;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import lombok.extern.slf4j.Slf4j;
import org.sephire.games.framework4x.core.plugins.PluginManager;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.googlecode.lanterna.gui2.LinearLayout.Alignment.Fill;
import static org.sephire.games.framework4x.clients.terminal.utils.Terminal.Size.sizeWithHeightToPercent;
import static org.sephire.games.framework4x.clients.terminal.utils.Terminal.Size.sizeWithWidthToPercent;

/**
 * This window is shown when starting a game, to select which plugins will be active for
 * a game.
 *
 * This screen ensures that no bad combination of plugins can be made. Once all is good, the next step is
 * to show the CreateGameWindow.
 */
@Slf4j
public class SelectPluginsWindow extends BasicWindow {

	private PluginManager pluginManager;
	private Label pluginInfoLabel;
	private Label infoLabel;
	private WindowBasedTextGUI textGUI;

	private final static String DEFAULT_DESCRIPTION = "Select a plugin to see it's description";
	private final static String DEFAULT_INFO = "Select the desired plugins to load then press enter to open the create game window, or escape to go back to the menu";
	private final static String PLUGIN_LIST_LABEL = "Select the plugins you wish to load:";

	public SelectPluginsWindow(PluginManager pluginManager,WindowBasedTextGUI textGUI) {
		super("Plugin selection screen");
		this.textGUI = textGUI;
		this.pluginManager = pluginManager;

		setupWindowFrame();
		addComponents();


	}

	private void setupWindowFrame() {
		setHints(List.of(Window.Hint.FULL_SCREEN));

		var self = this; // JS is coming to Java!
		addWindowListener(new WindowListenerAdapter() {
			@Override
			public void onInput(Window basePane, KeyStroke keyStroke, AtomicBoolean deliverEvent) {
				if(keyStroke.getKeyType() == KeyType.Escape) {
					log.info("The escape key was pressed");
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

		var pluginsListLabel = new Label(PLUGIN_LIST_LABEL);
		pluginsListLabel.setLayoutData(LinearLayout.createLayoutData(Fill));
		pluginListPanel.addComponent(pluginsListLabel);

		var pluginsCheckboxList = new CheckBoxList<String>();
		pluginsCheckboxList.setLayoutData(LinearLayout.createLayoutData(Fill));
		pluginManager.getAvailablePluginsNames().forEach(pluginsCheckboxList::addItem);
		pluginListPanel.addComponent(pluginsCheckboxList);

		var nextStepButton = new Button("Select plugins");
		nextStepButton.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.End));
		pluginListPanel.addComponent(nextStepButton);

		backgroundPanel.addComponent(pluginListPanel.withBorder(Borders.singleLine("Plugin list")));
	}

	private void buildPluginInfoPanel(Panel backgroundPanel) {
		Panel pluginInfoPanel = new Panel();
		pluginInfoPanel.setPreferredSize(sizeWithWidthToPercent(textGUI.getScreen().getTerminalSize(),0.4f));
		pluginInfoPanel.setLayoutData(BorderLayout.Location.RIGHT);
		pluginInfoPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
		pluginInfoLabel = new Label(DEFAULT_DESCRIPTION);
		pluginInfoLabel.setLayoutData(LinearLayout.createLayoutData(Fill));
		pluginInfoPanel.addComponent(pluginInfoLabel);

		backgroundPanel.addComponent(pluginInfoPanel.withBorder(Borders.singleLine()));
	}

	private void buildInfoPanel(Panel backgroundPanel) {
		Panel infoPanel = new Panel();
		infoPanel.setPreferredSize(textGUI.getScreen().getTerminalSize().withRows(4));
		infoPanel.setLayoutData(BorderLayout.Location.BOTTOM);
		infoPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
		infoLabel = new Label(DEFAULT_INFO);
		infoLabel.setLayoutData(LinearLayout.createLayoutData(Fill));
		infoPanel.addComponent(infoLabel);

		backgroundPanel.addComponent(infoPanel.withBorder(Borders.singleLine()));
	}

	private void updatePluginInfoLabel(String description) {
		pluginInfoLabel.setText(description);
	}

	private void updateInfoLabel(String description) {
		infoLabel.setText(description);
	}

}
