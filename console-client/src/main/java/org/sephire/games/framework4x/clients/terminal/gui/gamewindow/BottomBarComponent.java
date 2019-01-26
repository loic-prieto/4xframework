/**
 * 4X Framework - Console client - A terminal-based client for the 4X framework
 * Copyright © 2018 Loïc Prieto Dehennault (loic.sephiroth@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.sephire.games.framework4x.clients.terminal.gui.gamewindow;

import com.googlecode.lanterna.gui2.BorderLayout;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.sephire.games.framework4x.clients.terminal.api.config.ConsoleClientConfigKeyEnum;
import org.sephire.games.framework4x.clients.terminal.api.ui.gamewindow.BottomBarElement;
import org.sephire.games.framework4x.clients.terminal.api.ui.gamewindow.BottomBarPosition;
import org.sephire.games.framework4x.clients.terminal.gui.Basic4XWindow;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.model.game.Game;

import static io.vavr.API.*;
import static java.lang.String.format;

/**
 * <p>Represents a bottom bar component inside the game window.</p>
 * <p>It takes its configuration from the game object</p>
 */
@Slf4j
public class BottomBarComponent extends Panel {

	private Game game;
	private Configuration configuration;
	private Basic4XWindow parentContainer;
	private Panel centerPanel;

	public BottomBarComponent(Game game, Basic4XWindow parentContainer) {
		super();
		this.parentContainer = parentContainer;
		this.game = game;
		this.configuration = game.getConfiguration();

		setLayoutManager(new BorderLayout());

		var buildTry = buildElements();
		if(buildTry.isFailure()){
			var errorMessage = format("Could not load elements in bottom bar component: %s",buildTry.getCause().getMessage());
			MessageDialog.showMessageDialog(parentContainer.getOverridenTextGui(),"Error",errorMessage, MessageDialogButton.OK);
			log.error(errorMessage);
		}
	}

	private Try<Void> buildElements() {
		return Try.of(()->{

			centerPanel = new Panel();
			centerPanel.setLayoutManager(new GridLayout(1));
			centerPanel.setLayoutData(BorderLayout.Location.CENTER);
			addComponent(centerPanel);

			Try.sequence(configuration.getConfiguration(ConsoleClientConfigKeyEnum.BOTTOM_BAR_ELEMENTS, Map.class)
			  .getOrElseThrow(e->e)
			  .map(m-> (Map<BottomBarPosition, List<BottomBarElement>>)m)
			  .getOrElse(HashMap.empty())
			  .values().flatMap((list)->list)
			  .map((element)->BottomBarLabel.of(element,game)))
			  .getOrElseThrow(e->e)
			  .forEach((element)->{
			  	if(element.element.getPosition().equals(BottomBarPosition.Center)){
			  		centerPanel.addComponent(element);
				} else {
			  		addComponent(element);
				}
			  });

			return null;
		});
	}

	public void updateElements() {
		List.of(getChildren())
		  .filter((child)->child.getClass().equals(BottomBarLabel.class))
		  .map((child)->(BottomBarLabel)child)
		  .peek(label->label.updateValue(game));
	}

	private static class BottomBarLabel extends Label {
		private BottomBarElement element;

		private BottomBarLabel(BottomBarElement element,Game game) throws Throwable {
			super(element.getValueGenerator()
			  .apply(game.getConfiguration(),game)
			  .getOrElseThrow((e)->e));

			this.element = element;
			var labelLocation = Match(element.getPosition()).of(
			  Case($(BottomBarPosition.Left),BorderLayout.Location.LEFT),
			  Case($(BottomBarPosition.Right),BorderLayout.Location.RIGHT),
			  Case($(BottomBarPosition.Center),GridLayout.createLayoutData(
			    GridLayout.Alignment.CENTER,
				GridLayout.Alignment.CENTER,
				true,
				false)
			  )
			);
			this.setLayoutData(labelLocation);
		}

		public static Try<BottomBarLabel> of(BottomBarElement element,Game game) {
			return Try.of(()->new BottomBarLabel(element,game));
		}

		public Try<Void> updateValue(Game game) {
			return Try.of(()->{
				this.setText(element.getValueGenerator().apply(game.getConfiguration(),game).getOrElseThrow(e->e));
				return null;
			});
		}
	}


}
