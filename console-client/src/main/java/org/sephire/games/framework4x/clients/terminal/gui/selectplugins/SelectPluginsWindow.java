package org.sephire.games.framework4x.clients.terminal.gui.selectplugins;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import lombok.extern.slf4j.Slf4j;
import org.sephire.games.framework4x.clients.terminal.gui.Basic4XWindow;
import org.sephire.games.framework4x.clients.terminal.gui.components.MessagePanel;
import org.sephire.games.framework4x.core.plugins.PluginManager;
import org.sephire.games.framework4x.core.plugins.PluginSpec;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.googlecode.lanterna.gui2.LinearLayout.Alignment.Fill;
import static org.sephire.games.framework4x.clients.terminal.gui.components.MessagePanel.MessageType.ERROR;
import static org.sephire.games.framework4x.clients.terminal.gui.components.MessagePanel.MessageType.INFO;
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

	/**
	 * The set of selected plugins by the user inside the plugins checkbox list.
	 * The data is feed from listening to interacted plugins event.
	 * It is used to know if the create game button can be interacted with.
	 */
	private Set<PluginSpec> selectedPlugins;

	public SelectPluginsWindow(PluginManager pluginManager, WindowBasedTextGUI textGUI) {
		super(textGUI);
		setTitle(getTranslationFor("selectPluginWindow.title", Locale.ENGLISH));
		this.pluginManager = pluginManager;
		this.selectedPlugins = HashSet.empty();

		setupWindowFrame();
		addComponents();
		setupEvents();
	}

	private void setupEvents() {
		registerEventListener(PluginInteractedEvent.class,(event)->{
			if(event.isSelected()) {
				selectedPlugins = selectedPlugins.add(event.getPlugin());
			} else {
				selectedPlugins = selectedPlugins.remove(event.getPlugin());
			}

			fireEvent(new SelectedPluginsListUpdatedEvent(selectedPlugins));
		});
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

		var pluginsListLabel = new Label(getTranslationFor("selectPluginWindow.pluginList.description",Locale.ENGLISH));
		pluginsListLabel.setLayoutData(LinearLayout.createLayoutData(Fill));
		pluginListPanel.addComponent(pluginsListLabel);

		var pluginCheckBoxList = new PluginsCheckboxList(pluginManager.getAvailablePlugins());
		pluginCheckBoxList.setLayoutData(LinearLayout.createLayoutData(Fill));
		pluginListPanel.addComponent(pluginCheckBoxList);


		var selectButton = new Button(getTranslationFor("selectPluginWindow.pluginList.startButton",Locale.ENGLISH));
		selectButton.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.End));
		selectButton.setEnabled(false);
		pluginListPanel.addComponent(selectButton);
		registerEventListener(SelectedPluginsListUpdatedEvent.class,(event)->{
			var isBasePluginSelected = event.getSelectedPlugins().exists(PluginSpec::isBasePlugin);
			selectButton.setEnabled(isBasePluginSelected);
		});

		backgroundPanel.addComponent(pluginListPanel.withBorder(Borders.singleLine(getTranslationFor("selectPluginWindow.pluginList.title",Locale.ENGLISH))));
	}

	private void buildPluginInfoPanel(Panel backgroundPanel) {
		Panel pluginInfoPanel = new Panel();
		pluginInfoPanel.setPreferredSize(sizeWithWidthToPercent(getOverridenTextGui().getScreen().getTerminalSize(), 0.4f));
		pluginInfoPanel.setLayoutData(BorderLayout.Location.RIGHT);
		pluginInfoPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

		var headerLabel = new Label(getTranslationFor("selectPluginWindow.pluginInfoPanel.description",Locale.ENGLISH));
		headerLabel.setLayoutData(LinearLayout.createLayoutData(Fill));
		pluginInfoPanel.addComponent(headerLabel);

		var pluginInfoLabel = new Label("");
		pluginInfoLabel.setLayoutData(LinearLayout.createLayoutData(Fill));
		pluginInfoPanel.addComponent(pluginInfoLabel);

		registerEventListener(PluginTraversedEvent.class,(event)->{
			pluginInfoLabel.setText(event.getSelectedPlugin().getDescription(Locale.ENGLISH).get());
		});

		backgroundPanel.addComponent(pluginInfoPanel.withBorder(Borders.singleLine()));
	}

	private void buildInfoPanel(Panel backgroundPanel) {
		Panel infoPanel = new Panel();
		infoPanel.setPreferredSize(getOverridenTextGui().getScreen().getTerminalSize().withRows(4));
		infoPanel.setLayoutData(BorderLayout.Location.BOTTOM);
		infoPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

		var messagePanel = new MessagePanel();
		messagePanel.setLayoutData(LinearLayout.createLayoutData(Fill));
		infoPanel.addComponent(messagePanel);

		messagePanel.addMessage(getTranslationFor("selectPluginWindow.messagePanel.defaultMessage",Locale.ENGLISH), INFO);
		messagePanel.addMessage(getTranslationFor("selectPluginWindow.messagePanel.needBasePlugin",Locale.ENGLISH),ERROR);

		registerEventListener(SelectedPluginsListUpdatedEvent.class,(event)->{
			var needsBasePluginSelected = !event.getSelectedPlugins().exists(PluginSpec::isBasePlugin);
			if(needsBasePluginSelected) {
				messagePanel.addMessageIfNotExists(getTranslationFor("selectPluginWindow.messagePanel.needBasePlugin",Locale.ENGLISH),ERROR);
			} else {
				messagePanel.removeMessage(getTranslationFor("selectPluginWindow.messagePanel.needBasePlugin",Locale.ENGLISH));
			}
		});


		backgroundPanel.addComponent(infoPanel.withBorder(Borders.singleLine()));
	}
}
