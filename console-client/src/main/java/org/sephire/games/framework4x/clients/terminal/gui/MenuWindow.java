package org.sephire.games.framework4x.clients.terminal.gui;

import com.googlecode.lanterna.gui2.*;
import io.vavr.Function1;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.googlecode.lanterna.gui2.Borders.doubleLine;
import static java.lang.System.out;
import static org.sephire.games.framework4x.clients.terminal.utils.Functions.wrap;

@Slf4j
public class MenuWindow extends BasicWindow {

	public MenuWindow() {
		super("4X Framework Menu");

		setHints(List.of(Window.Hint.FULL_SCREEN));

		Panel backgroundPanel = new Panel();
		backgroundPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
		backgroundPanel.setPreferredSize(getSize());

		Panel menuPanel = new Panel();
		menuPanel.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
		menuPanel.addComponent(buttonFor("Start Game", wrap((b) -> {
			log.info("Selected start game");
			var startGameWindow = new StartGameWindow();
			this.getTextGUI().addWindow(startGameWindow);
			this.getTextGUI().setActiveWindow(startGameWindow);
	  	})));

		menuPanel.addComponent(buttonFor("Load Game", wrap((b) -> out.println("Load game activated"))));
		menuPanel.addComponent(buttonFor("Manage Plugins", wrap((b) -> out.println("Manage Plugins activated"))));
		menuPanel.addComponent(buttonFor("Configuration", wrap((b) -> out.println("Configuration activated"))));
		menuPanel.addComponent(buttonFor("Exit", wrap((b) -> {
			out.println("Exit activated");
			close();
		})));
		backgroundPanel.addComponent(menuPanel.withBorder(doubleLine()));

		setComponent(backgroundPanel.withBorder(doubleLine()));
	}

	private static Button buttonFor(String label, Function1<Button, Void> buttonAction) {
		Button menuItem = new Button(label);
		menuItem.addListener(buttonAction::apply);

		return menuItem;
	}

}
