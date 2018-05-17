package org.sephire.games.framework4x.clients.terminal.ui.menu;

import io.vavr.Function0;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Represents a menu item inside a menu.
 * A menu item has a label and an action to be invoked.
 */
@Getter
@RequiredArgsConstructor
public class MenuItem {
	@NonNull
	private String label;
	@NonNull
	private Function0<Void> action;
}
