package org.sephire.games.framework4x.clients.terminal.gui.gamewindow;

import com.googlecode.lanterna.gui2.BorderLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Try;
import org.sephire.games.framework4x.clients.terminal.api.config.ConsoleClientConfigKeyEnum;
import org.sephire.games.framework4x.clients.terminal.api.ui.gamewindow.BottomBarElement;
import org.sephire.games.framework4x.clients.terminal.api.ui.gamewindow.BottomBarPosition;
import org.sephire.games.framework4x.clients.terminal.gui.Basic4XWindow;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.model.game.Game;

import static io.vavr.API.*;

/**
 * <p>Represents a bottom bar component inside the game window.</p>
 * <p>It takes its configuration from the game object</p>
 */
public class BottomBarComponent extends Panel {

	private Game game;
	private Configuration configuration;
	private Basic4XWindow parentContainer;

	public BottomBarComponent(Game game, Basic4XWindow parentContainer) {
		super();
		this.parentContainer = parentContainer;
		this.game = game;
		this.configuration = game.getConfiguration();

		setLayoutManager(new BorderLayout());

		buildElements();
	}

	private Try<Void> buildElements() {
		return Try.of(()->{

			Try.sequence(configuration.getConfiguration(ConsoleClientConfigKeyEnum.BOTTOM_BAR_ELEMENTS, Map.class)
			  .getOrElseThrow(e->e)
			  .map(m-> (Map<BottomBarPosition, List<BottomBarElement>>)m)
			  .getOrElse(HashMap.empty())
			  .values().flatMap((list)->list)
			  .map((element)->BottomBarLabel.of(element,game)))
			  .getOrElseThrow(e->e)
			  .forEach(this::addComponent);

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
			  Case($(BottomBarPosition.Center),BorderLayout.Location.CENTER)
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
