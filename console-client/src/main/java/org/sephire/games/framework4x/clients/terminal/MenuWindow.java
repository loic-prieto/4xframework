package org.sephire.games.framework4x.clients.terminal;

import com.googlecode.lanterna.gui2.*;

public class MenuWindow extends BasicWindow {
	public MenuWindow() {
		super("4X Framework Menu");

		Panel horizontalPanel = new Panel();
		horizontalPanel.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
		Panel leftPanel = new Panel();
		Panel middlePanel = new Panel();
		Panel rightPanel = new Panel();

		horizontalPanel.addComponent(leftPanel);
		Border middlePanelBorder = Borders.singleLine("Panel Title");
		middlePanelBorder.setComponent(middlePanel);
		horizontalPanel.addComponent(middlePanelBorder);
		Border rightPanelBorder = Borders.doubleLineBevel();
		rightPanelBorder.setComponent(rightPanel);
		horizontalPanel.addComponent(rightPanelBorder);

		// This ultimately links in the panels as the window content
		setComponent(horizontalPanel);
	}
}
