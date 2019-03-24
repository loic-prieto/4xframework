package org.sephire.games.framework4x.clients.terminal.gui.gamewindow.topmenu;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.Panel;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.sephire.games.framework4x.clients.terminal.gui.components.MultiStyleLabel;
import org.sephire.games.framework4x.clients.terminal.gui.gamewindow.GameWindow;
import org.sephire.games.framework4x.clients.terminal.gui.gamewindow.MenuActivationRequestedEvent;
import org.sephire.games.framework4x.clients.terminal.utils.IntegerRange;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.model.game.GameCommands;
import org.sephire.games.framework4x.core.utils.FunctionalUtils;

import java.util.Locale;

import static org.sephire.games.framework4x.core.model.config.CoreConfigKeyEnum.GAME_COMMANDS;
import static org.sephire.games.framework4x.core.utils.Validations.areArgumentsNotNull;

/**
 * <p>This is the top panel in the game window. It contains a menu that corresponds to the general categorization
 * of game commands, with a submenu for each root category at the top.</p>
 * <p>Menus are opened with a global shortcut corresponding to the first letter of the label, then the user navigates
 * the submenus with arrows. The shortcut to opening a menu is [alt+first letter of label]</p>
 */
@Slf4j
public class TopMenuComponent extends Panel {
	private GameCommands gameCommands;
	private GameCommandsTopMenu menus;

	private TopMenuComponent(Configuration configuration, GameWindow parent) throws Throwable {
		gameCommands = configuration.getConfiguration(GAME_COMMANDS,GameCommands.class)
		  .getOrElseThrow(t->t)
		  .getOrElse(new GameCommands());
		gameCommands.addGameCommandCategory(DefaultGameCommands.defaultGameCommands());

		this.menus = GameCommandsTopMenu.builder()
		  .using(configuration)
		  .forLocale(Locale.ENGLISH)
		  .withCommands(gameCommands)
		  .build().getOrElseThrow(t->t);

		buildTopMenus(this).getOrElseThrow(t -> t);

		setupEventHandling(parent);
	}

	public static BuilderGameWindow builder() {
		return new Builder();
	}

	private void setupEventHandling(GameWindow parent) {
		parent.registerEventListener(MenuActivationRequestedEvent.class, (event) -> {
			menus.getTopCategories()
			  .find(c -> c.getShortcutKey().equals("" + event.getCharacter()))
			  .peek(c -> MenuDialog.show(c, parent.getOverridenTextGui()));
		});
	}

	private Try<Void> buildTopMenus(Panel panel) {
		return Try.of(()->{
			menus.getTopCategories()
			  .map(this::fromMenuItem).collect(FunctionalUtils.Collectors.toTry()).getOrElseThrow(t->t)
			  .forEach(panel::addComponent);

			return null;
		});
	}

	private Try<MultiStyleLabel> fromMenuItem(GameCommandMenuItem menuItem) {
		return Try.of(()->{ ;
			return MultiStyleLabel.builder()
			  .forText(menuItem.getLabel())
			  .addStyledRange(new IntegerRange(0,1), TextColor.ANSI.YELLOW,null).getOrElseThrow(t->t)
			  .addStyledRange(new IntegerRange(1,menuItem.getLabel().length())).getOrElseThrow(t->t)
			  .build().getOrElseThrow(t->t);
		});
	}

	private void openMenu(GameCommandMenuItem menuItem) {

	}

	public interface BuilderGameWindow {
		BuilderConfiguration withParent(GameWindow parent);
	}

	public interface BuilderConfiguration {
		BuilderBuilder withConfiguration(Configuration configuration);
	}

	public interface BuilderBuilder {
		Try<TopMenuComponent> build();
	}

	public static class Builder implements BuilderBuilder, BuilderConfiguration, BuilderGameWindow {
		private Configuration configuration;
		private GameWindow parent;

		@Override
		public BuilderConfiguration withParent(GameWindow parent) {
			this.parent = parent;
			return this;
		}

		@Override
		public BuilderBuilder withConfiguration(Configuration configuration) {
			this.configuration = configuration;
			return this;
		}

		@Override
		public Try<TopMenuComponent> build() {
			return Try.of(()->{
				areArgumentsNotNull(configuration, parent).getOrElseThrow(t -> t);

				return new TopMenuComponent(configuration, parent);
			});
		}
	}
}
