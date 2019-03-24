package org.sephire.games.framework4x.clients.terminal.gui.gamewindow.topmenu;

import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.gui2.dialogs.ActionListDialog;
import com.googlecode.lanterna.gui2.dialogs.ActionListDialogBuilder;
import io.vavr.control.Try;

import java.util.Locale;

import static org.sephire.games.framework4x.clients.terminal.utils.Terminal.Translation.getTranslationFor;

/**
 * <p>A modal dialog that shows the elements of a command menu item, navigating to submenus if there are any</p>
 */
public class MenuDialog {

	private ActionListDialog dialog;
	private WindowBasedTextGUI textGUI;

	private MenuDialog(GameCommandMenuItem menu, WindowBasedTextGUI textGUI) {
		if (!menu.isSubmenu()) throw new IllegalArgumentException("The game command menu item must be a submenu");

		this.textGUI = textGUI;

		var builder = new ActionListDialogBuilder()
		  .setTitle(menu.getLabel())
		  .setDescription(getTranslationFor(Locale.ENGLISH, "topMenu.menuPanel.description.label").get())
		  .setCanCancel(true);

		menu.getCommands().get().forEach(c -> {
			var label = c.isSubmenu() ? c.getLabel().concat(" >") : c.getLabel();
			builder.addAction(label, () -> {
				if (c.isSubmenu()) {
					var submenuDialog = new MenuDialog(c, textGUI);
					dialog.close();
					submenuDialog.show();
				} else {
					c.getCommand().get().getCommandExecutionMethod().apply(null);
				}
			});
		});

		this.dialog = builder.build();
	}

	/**
	 * <p>Shows the game command submenu in the form of an action list dialog</p>
	 * <p>May return error:
	 * <ul>
	 *     <li>IllegalArgumentException if the menu is not a submenu</li>
	 * </ul>
	 * </p>
	 * @param menu
	 * @param textGUI
	 * @return
	 */
	public static Try<Void> show(GameCommandMenuItem menu, WindowBasedTextGUI textGUI) {
		return Try.of(() -> {
			new MenuDialog(menu, textGUI).show();
			return null;
		});
	}

	private void show() {
		this.dialog.showDialog(textGUI);
	}

}
