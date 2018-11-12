package org.sephire.games.framework4x.clients.terminal;

import com.googlecode.lanterna.gui2.*;
import io.vavr.Function1;
import io.vavr.control.Option;

import static java.lang.System.out;
import static org.sephire.games.framework4x.clients.terminal.utils.Functions.wrap;

public class MenuWindow extends BasicWindow {
	public MenuWindow() {
		super("4X Framework Menu");

		Panel backgroundPanel = new Panel();
		backgroundPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
		backgroundPanel.setPreferredSize(getSize());

		Panel menuPanel = new Panel();
		menuPanel.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
		io.vavr.CheckedConsumer
		menuPanel.addComponent(buttonFor("Start Game", wrap((b)-> out.println("Start game activated"))));
		menuPanel.addComponent(buttonFor("Load Game",wrap((b)-> out.println("Load game activated"))));
		menuPanel.addComponent(buttonFor("Manage Plugins",wrap((b)-> out.println("Manage Plugins activated"))));
		menuPanel.addComponent(buttonFor("Configuration",wrap((b)-> out.println("Configuration activated"))));
		menuPanel.addComponent(buttonFor("Exit",wrap((b)->{
			out.println("Exit activated");
			close();
		})));
		backgroundPanel.addComponent(wrapInBorder(menuPanel));

		setComponent(wrapInBorder(backgroundPanel));
	}

	private static Button buttonFor(String label, Function1<Button,Void> buttonAction) {
		Button menuItem = new Button(label);
		menuItem.addListener(buttonAction::apply);

		return menuItem;
	}

	private static Border wrapInBorder(Component component) {
		return wrapInBorder(component,Option.none());
	}
	private static Border wrapInBorder(Component component, Option<String> optionalTitle) {
		Border border = optionalTitle.map(Borders::doubleLine)
			.getOrElse(Borders::doubleLine);
		border.setComponent(component);

		return border;
	}

}
