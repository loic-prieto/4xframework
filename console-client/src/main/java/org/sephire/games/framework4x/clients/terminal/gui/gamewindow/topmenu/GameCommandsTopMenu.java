package org.sephire.games.framework4x.clients.terminal.gui.gamewindow.topmenu;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.Getter;
import org.sephire.games.framework4x.clients.terminal.gui.gamewindow.TranslationNotFoundException;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.model.game.GameCommand;
import org.sephire.games.framework4x.core.model.game.GameCommandCategory;
import org.sephire.games.framework4x.core.model.game.GameCommands;

import java.util.*;

import static org.sephire.games.framework4x.core.utils.Validations.areArgumentsNotNull;

/**
 * <p>This class is a wrapper for the game commands for the terminal clients, that maps
 * each game category and item to a key shortcut.</p>
 * <p>This mapping is done by taking the first available letter of the game command label that hasn't been
 * taken yet by its siblings.</p>
 * <p>For example, given the top menus "Game" and "Civilization", the shortcuts for opening the menus are g and c
 * respectively.<br/>
 * Given the top menus "Game" and "Grades", the shortcuts for opening the menus are g and r respectively.</p>
 */
public class GameCommandsTopMenu {
	@Getter
	private List<GameCommandMenuItem> topCategories;

	private GameCommandsTopMenu(List<GameCommandMenuItem> topCategories) {
		this.topCategories = topCategories;
	}



	public static BuilderConfiguration builder() {
		return new Builder();
	}

	public static class Builder implements BuilderConfiguration, BuilderBuild, BuilderCommands, BuilderLocale {
		private Configuration configuration;
		private GameCommands gameCommands;
		private Locale locale;

		@Override
		public BuilderLocale using(Configuration configuration) {
			this.configuration = configuration;
			return this;
		}

		@Override
		public BuilderBuild withCommands(GameCommands gameCommands) {
			this.gameCommands = gameCommands;
			return this;
		}

		@Override
		public BuilderCommands forLocale(Locale locale) {
			this.locale = locale;
			return this;
		}

		@Override
		public Try<GameCommandsTopMenu> build() {
			return Try.of(()->{
				areArgumentsNotNull(configuration,locale,gameCommands).getOrElseThrow(t->t);

				var topCategoriesTakenShortcuts = new HashSet<String>();
				var topCategories = gameCommands.getRootCategories()
				  .map(c->Tuple.of(c,topCategoriesTakenShortcuts))
				  .map(this::toCategoryMenuItem);

				return new GameCommandsTopMenu(topCategories);
			});
		}

		private GameCommandMenuItem toCategoryMenuItem(Tuple2<GameCommandCategory,HashSet<String>> args) {
			var takenShortcuts = args._2;
			var gameCommandCategory = args._1;

			var label = getTranslationForGameCommand(gameCommandCategory,Locale.ENGLISH)
			  .getOrElseThrow(()->new TranslationNotFoundException(gameCommandCategory.getLabel()));
			var shortcutKey = findShortcutCharacter(label,takenShortcuts)
			  .getOrElseThrow(()->new NotEnoughCharactersForShortcutKeyException(label));
			takenShortcuts.add(shortcutKey);

			var itemsShortcuts = new HashSet<String>();
			var submenus = gameCommandCategory.getSubcategories()
			  .map(s->Tuple.of(s,itemsShortcuts))
			  .map(this::toCategoryMenuItem);

			var commandItems = gameCommandCategory.getCommands()
			  .map(c->Tuple.of((GameCommand)c,itemsShortcuts))
			  .map(this::toCommandMenuItem);

			var menuItems = submenus.appendAll(commandItems);

			return new GameCommandMenuItem(label,shortcutKey,menuItems);
		}

		private GameCommandMenuItem toCommandMenuItem(Tuple2<GameCommand,HashSet<String>> args) {
			var gameCommand = args._1;
			var takenShortcuts = args._2;

			var label = getTranslationForGameCommand(gameCommand,Locale.ENGLISH)
			  .getOrElseThrow(()->new TranslationNotFoundException(gameCommand.getLabel()));

			var shortcutKey = findShortcutCharacter(label,takenShortcuts)
			  .getOrElseThrow(()->new NotEnoughCharactersForShortcutKeyException(label));
			takenShortcuts.add(shortcutKey);

			return new GameCommandMenuItem(label,shortcutKey,gameCommand);
		}

		private Option<String> getTranslationForGameCommand(GameCommand command,Locale locale) {
			return command.getLabel().startsWith("game") ?
			  getUiTranslation(command.getLabel(),locale) :
			  configuration.getTranslationFor(locale,command.getLabel());
		}
		private Option<String> getTranslationForGameCommand(GameCommandCategory category,Locale locale) {
			return category.getLabel().startsWith("game") ?
			  getUiTranslation(category.getLabel(),locale) :
			  configuration.getTranslationFor(locale,category.getLabel());
		}

		private static Option<String> getUiTranslation(String labelKey, Locale locale) {
			ResourceBundle bundle = PropertyResourceBundle.getBundle("i18n.BasicUI",locale);
			return Option.of(bundle.getString(labelKey));
		}

		private static Option<String> findShortcutCharacter(String label, Set<String> takenShortcuts) {
			return  List.ofAll(label.toCharArray())
			  .filter(c-> c != ' ') // We don't want to shortcut to space key
			  .map(c->""+c)
			  .find(character -> !takenShortcuts.contains(character))
			  .map(String::toLowerCase);
		}
	}

	interface BuilderConfiguration { BuilderLocale using(Configuration configuration);}
	interface BuilderLocale { BuilderCommands forLocale(Locale locale);}
	interface BuilderCommands { BuilderBuild withCommands(GameCommands gameCommands);}
	interface BuilderBuild { Try<GameCommandsTopMenu> build();}
}
