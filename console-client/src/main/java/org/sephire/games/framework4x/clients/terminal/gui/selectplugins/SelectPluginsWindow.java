package org.sephire.games.framework4x.clients.terminal.gui.selectplugins;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import io.vavr.Function1;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import lombok.extern.slf4j.Slf4j;
import org.sephire.games.framework4x.clients.terminal.gui.Basic4XWindow;
import org.sephire.games.framework4x.clients.terminal.gui.selectplugins.PluginInteractedEvent;
import org.sephire.games.framework4x.clients.terminal.gui.selectplugins.PluginsCheckboxList;
import org.sephire.games.framework4x.clients.terminal.utils.ToStringDecorator;
import org.sephire.games.framework4x.core.plugins.PluginManager;
import org.sephire.games.framework4x.core.plugins.PluginSpec;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.googlecode.lanterna.gui2.LinearLayout.Alignment.Fill;
import static org.sephire.games.framework4x.clients.terminal.utils.Terminal.Size.sizeWithWidthToPercent;

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
	private Label pluginInfoLabel;
	private Label infoLabel;

	/**
	 * The set of selected plugins by the user inside the plugins checkbox list.
	 * The data is feed from listening to interacted plugins event.
	 * It is used to know if the create game button can be interacted with.
	 */
	private Set<PluginSpec> selectedPlugins;

	private final static String DEFAULT_DESCRIPTION = "Select a plugin to see it's description";
	private final static String DEFAULT_INFO = "Select the desired plugins to load then press enter to open the create game window, or escape to go back to the menu";
	private final static String PLUGIN_LIST_LABEL = "Select the plugins you wish to load:";

	public SelectPluginsWindow(PluginManager pluginManager, WindowBasedTextGUI textGUI) {
		super("Plugin selection screen",textGUI);
		this.pluginManager = pluginManager;
		this.selectedPlugins = HashSet.empty();

		setupWindowFrame();
		addComponents();
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

		var pluginsListLabel = new Label(PLUGIN_LIST_LABEL);
		pluginsListLabel.setLayoutData(LinearLayout.createLayoutData(Fill));
		pluginListPanel.addComponent(pluginsListLabel);

		var pluginCheckBoxList = new PluginsCheckboxList(pluginManager.getAvailablePlugins());
		pluginCheckBoxList.setLayoutData(LinearLayout.createLayoutData(Fill));
		pluginListPanel.addComponent(pluginCheckBoxList);


		var selectButton = new Button("Start Creating Game");
		selectButton.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.End));
		selectButton.setEnabled(false);
		pluginListPanel.addComponent(selectButton);
		registerEventListener(PluginInteractedEvent.class,(event)->{
			if(event.isSelected()) {
				selectedPlugins = selectedPlugins.add(event.getPlugin());
			} else {
				selectedPlugins = selectedPlugins.remove(event.getPlugin());
			}

			var isBasePluginSelected = selectedPlugins.exists(PluginSpec::isBasePlugin);
			selectButton.setEnabled(isBasePluginSelected);
		});

		backgroundPanel.addComponent(pluginListPanel.withBorder(Borders.singleLine("Plugin list")));
	}

	private void buildPluginInfoPanel(Panel backgroundPanel) {
		Panel pluginInfoPanel = new Panel();
		pluginInfoPanel.setPreferredSize(sizeWithWidthToPercent(getOverridenTextGui().getScreen().getTerminalSize(), 0.4f));
		pluginInfoPanel.setLayoutData(BorderLayout.Location.RIGHT);
		pluginInfoPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
		pluginInfoLabel = new Label(DEFAULT_DESCRIPTION);
		pluginInfoLabel.setLayoutData(LinearLayout.createLayoutData(Fill));
		pluginInfoPanel.addComponent(pluginInfoLabel);

		backgroundPanel.addComponent(pluginInfoPanel.withBorder(Borders.singleLine()));
	}

	private void buildInfoPanel(Panel backgroundPanel) {
		Panel infoPanel = new Panel();
		infoPanel.setPreferredSize(getOverridenTextGui().getScreen().getTerminalSize().withRows(4));
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
