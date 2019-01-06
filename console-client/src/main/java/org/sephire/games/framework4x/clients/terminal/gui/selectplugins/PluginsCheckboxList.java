package org.sephire.games.framework4x.clients.terminal.gui.selectplugins;

import com.googlecode.lanterna.gui2.CheckBoxList;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.input.KeyStroke;
import io.vavr.Function1;
import io.vavr.Tuple;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.collection.Set;
import lombok.extern.slf4j.Slf4j;
import org.sephire.games.framework4x.clients.terminal.gui.Basic4XWindow;
import org.sephire.games.framework4x.clients.terminal.utils.ToStringDecorator;
import org.sephire.games.framework4x.core.plugins.PluginSpec;

import java.util.Locale;

import static com.googlecode.lanterna.input.KeyType.ArrowDown;
import static com.googlecode.lanterna.input.KeyType.ArrowUp;
import static io.vavr.API.*;
import static io.vavr.Predicates.isIn;

@Slf4j
public class PluginsCheckboxList extends CheckBoxList<ToStringDecorator<PluginSpec>> {

	private Map<String, ToStringDecorator<PluginSpec>> pluginCache;

	private static final Function1<PluginSpec,String> pluginSpecStringifier = (pluginSpec ->
	  (pluginSpec.isBasePlugin() ? "[Base] " : "").concat(pluginSpec.getTitle(Locale.ENGLISH).get()));

	public PluginsCheckboxList(Set<PluginSpec> plugins) {

		this.pluginCache = plugins.toSortedSet()
		  .map((plugin)-> new ToStringDecorator<PluginSpec>(plugin,pluginSpecStringifier))
		  .map((plugin)-> Tuple.of(plugin.getWrappedObject().getPluginName(),plugin))
		  .collect(HashMap.collector());

		this.pluginCache.values().forEach(this::addItem);

		setupListeners();
	}

	private void setupListeners() {
		addListener((itemIndex, checked) -> {
			var selectedPlugin = getItemAt(itemIndex).getWrappedObject();
			if (checked && !selectedPlugin.isBasePlugin()) {
				// If a child plugin is selected, check also the parent if found
				var parentPluginSearch = pluginCache.get(selectedPlugin.getParentPlugin().get());
				if (parentPluginSearch.isDefined()) {
					this.setChecked(parentPluginSearch.get(),true);
				} else {
					var textGUI = ((Basic4XWindow)getBasePane()).getOverridenTextGui();
					var errorMessage = "The parent of this plugin could not be found, make sure it is present in the plugins folder";
					MessageDialog.showMessageDialog(textGUI,"Error",errorMessage, MessageDialogButton.OK);
					log.error("A child plugin was selected but it's parent is not in the list of available plugins");
					this.setChecked(this.getItemAt(itemIndex),false);
				}
			} else {
				// If a parent is unchecked, uncheck the children too
				if(!checked && selectedPlugin.isBasePlugin()) {
					pluginCache.values()
					  .filter((p)->!p.getWrappedObject().isBasePlugin())
					  .filter((child)->child.getWrappedObject().getParentPlugin().get().equals(selectedPlugin.getPluginName()))
					  .forEach((child)->this.setChecked(child,false));
				}
			}

			((Basic4XWindow)getBasePane()).fireEvent(new PluginInteractedEvent(selectedPlugin,checked));
		});
	}

	@Override
	public synchronized Result handleKeyStroke(KeyStroke keyStroke) {
		var superReturn = super.handleKeyStroke(keyStroke);

		Match(keyStroke.getKeyType()).of(
		  	Case($(isIn(ArrowDown, ArrowUp)),this::fireMoveEvent),
		  	Case($(),()->null)
		);

		return superReturn;
	}

	public Object fireMoveEvent() {
		var selectedPlugin = this.getSelectedItem().getWrappedObject();
		((Basic4XWindow)getBasePane()).fireEvent(new PluginTraversedEvent(selectedPlugin));
		return null;
	}
}
