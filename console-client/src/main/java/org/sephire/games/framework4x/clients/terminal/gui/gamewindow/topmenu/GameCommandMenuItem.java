package org.sephire.games.framework4x.clients.terminal.gui.gamewindow.topmenu;

import io.vavr.collection.List;
import io.vavr.control.Option;
import lombok.Getter;
import org.sephire.games.framework4x.core.model.game.GameCommand;

public class GameCommandMenuItem {
	/**
	 * A game command menu item may contain either a command or a list of commands,
	 * meaning it may be a menu item or a submenu.
	 */
	@Getter
	private Option<List<GameCommandMenuItem>> commands;
	@Getter
	private Option<GameCommand> command;
	@Getter
	private String shortcutKey;
	@Getter
	private String label;

	/**
	 * In this mode, a game command menu item just contains the command to execute
	 * @param label
	 * @param shortcutKey
	 * @param command
	 */
	protected GameCommandMenuItem(String label,String shortcutKey,GameCommand<?> command) {
		this.shortcutKey = shortcutKey;
		this.label = label;
		this.command = Option.of(command);
		this.commands = Option.none();
	}

	/**
	 * In this mode, a game command menu item is a container of other commands
	 * @param label
	 * @param shortcutKey
	 * @param commands
	 */
	protected GameCommandMenuItem(String label,String shortcutKey,List<GameCommandMenuItem> commands) {
		this.shortcutKey = shortcutKey;
		this.label = label;
		this.command = Option.none();
		this.commands = Option.of(commands);
	}

	public boolean isSubmenu() {
		return commands.isDefined();
	}

}
