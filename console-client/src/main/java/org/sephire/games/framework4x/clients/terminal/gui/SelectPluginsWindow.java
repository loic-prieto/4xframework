package org.sephire.games.framework4x.clients.terminal.gui;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import io.vavr.Function1;
import io.vavr.Tuple;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import lombok.extern.slf4j.Slf4j;
import org.sephire.games.framework4x.clients.terminal.utils.ToStringDecorator;
import org.sephire.games.framework4x.core.plugins.Plugin;
import org.sephire.games.framework4x.core.plugins.PluginManager;
import org.sephire.games.framework4x.core.plugins.PluginSpec;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.googlecode.lanterna.gui2.LinearLayout.Alignment.Fill;
import static java.util.function.Predicate.not;
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
	// The purpose of this object is to store the position of plugin specs inside the checkbox, and to be able
	// to search parents quickly.
	private Map<String,PluginSpec> pluginCache;
	private CheckBoxList<ToStringDecorator<PluginSpec>> pluginCheckBoxList;
	private Button selectButton;

	private final static String DEFAULT_DESCRIPTION = "Select a plugin to see it's description";
	private final static String DEFAULT_INFO = "Select the desired plugins to load then press enter to open the create game window, or escape to go back to the menu";
	private final static String PLUGIN_LIST_LABEL = "Select the plugins you wish to load:";

	public SelectPluginsWindow(PluginManager pluginManager, WindowBasedTextGUI textGUI) {
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
				if (keyStroke.getKeyType() == KeyType.Escape) {
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

		// Build the cache of plugins inside the checkbox
		pluginCache = pluginManager.getAvailablePlugins()
		  .map((p) -> Tuple.of(p.getPluginName(), p))
		  .collect(HashMap.collector());

		this.pluginCheckBoxList = new CheckBoxList<>();
		pluginCheckBoxList.setLayoutData(LinearLayout.createLayoutData(Fill));
		pluginCheckBoxList.addListener((itemIndex, checked) -> {
			var selectedPlugin = pluginCheckBoxList.getItemAt(itemIndex).getWrappedObject();
			log.trace("The plugin {} was checked? {}",selectedPlugin.getPluginName(),checked);
			if (checked && !selectedPlugin.isBasePlugin()) {
				// If a child plugin is selected, check also the parent if found
				var parentPluginSearch = pluginCache.get(selectedPlugin.getParentPlugin().get());
				if (parentPluginSearch.isDefined()) {
					pluginCheckBoxList.setChecked(wrap(parentPluginSearch.get()),true);
				} else {
					var errorMessage = "The parent of this plugin could not be found, make sure it is present in the plugins folder";
					MessageDialog.showMessageDialog(textGUI,"Error",errorMessage, MessageDialogButton.OK);
					log.error("A child plugin was selected but it's parent is not in the list of available plugins");
					pluginCheckBoxList.setChecked(wrap(selectedPlugin),false);
				}
			} else {
				// If a parent is unchecked, uncheck the children too
				if(!checked && selectedPlugin.isBasePlugin()) {
					pluginCache.values()
					  .filter(not(PluginSpec::isBasePlugin))
					  .filter((child)->child.getParentPlugin().get().equals(selectedPlugin.getPluginName()))
					  .forEach((child)->pluginCheckBoxList.setChecked(wrap(child),false));
				}
			}

			updateSelectButtonState();
		});
		pluginCache.values().map(SelectPluginsWindow::wrap).forEach(pluginCheckBoxList::addItem);
		pluginListPanel.addComponent(pluginCheckBoxList);
		

		selectButton = new Button("Start Creating Game");
		selectButton.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.End));
		selectButton.setEnabled(false);
		pluginListPanel.addComponent(selectButton);

		backgroundPanel.addComponent(pluginListPanel.withBorder(Borders.singleLine("Plugin list")));
	}

	/**
	 * This method sets the state of the Selected button, which allows to go to the next step in creating
	 * a game.
	 * @return
	 */
	private void updateSelectButtonState() {
		var isBasePluginSelected = pluginCheckBoxList.getCheckedItems().stream()
		  .map(SelectPluginsWindow::unwrap)
		  .anyMatch(PluginSpec::isBasePlugin);

		selectButton.setEnabled(isBasePluginSelected);
	}

	private void buildPluginInfoPanel(Panel backgroundPanel) {
		Panel pluginInfoPanel = new Panel();
		pluginInfoPanel.setPreferredSize(sizeWithWidthToPercent(textGUI.getScreen().getTerminalSize(), 0.4f));
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

	// The code below only exists because ToStringDecorator is not elegant to see
	private static final Function1<PluginSpec,String> pluginSpecStringifier = PluginSpec::getPluginName;
	private static ToStringDecorator<PluginSpec> wrap(PluginSpec pluginSpec) {
		return new ToStringDecorator<>(pluginSpec,pluginSpecStringifier);
	}
	private static PluginSpec unwrap(ToStringDecorator<PluginSpec> pluginSpecToStringDecorator) {
		return pluginSpecToStringDecorator.getWrappedObject();
	}

}
