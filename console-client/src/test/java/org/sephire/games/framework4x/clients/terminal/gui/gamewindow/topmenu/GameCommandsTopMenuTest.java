package org.sephire.games.framework4x.clients.terminal.gui.gamewindow.topmenu;

import io.vavr.control.Option;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.model.game.GameCommand;
import org.sephire.games.framework4x.core.model.game.GameCommandCategory;
import org.sephire.games.framework4x.core.model.game.GameCommands;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GameCommandsTopMenuTest {

	private Configuration mockConfiguration;

	@BeforeEach
	public void setupMocks(){
		mockConfiguration = mock(Configuration.class);
		when(mockConfiguration.getTranslationFor(any(),anyString(),any())).then((args)->{
			String labelKey = args.getArgument(1);
			return Option.of(labelKey);
		});
	}


	@Test
	@DisplayName("Given a set of game commands, it should map correctly to shortcut keys in the root categories")
	public void should_map_menu_items_to_shortcuts_correctly() {

		var gameCommandsTopMenusTry = GameCommandsTopMenu.builder()
		  .using(mockConfiguration)
		  .forLocale(Locale.ENGLISH)
		  .withCommands(buildSampleGameCommands())
		  .build();

		assertTrue(gameCommandsTopMenusTry.isSuccess());
		var gameCommandsTopMenus = gameCommandsTopMenusTry.get();
		assertEquals(2,gameCommandsTopMenus.getTopCategories().size());

		var gameMenuItem = gameCommandsTopMenus.getTopCategories().find(c->c.getLabel().equals("game"));
		assertTrue(gameMenuItem.isDefined());
		assertEquals("g",gameMenuItem.get().getShortcutKey());
		assertTrue(gameMenuItem.get().isSubmenu());

		var civMenuItem = gameCommandsTopMenus.getTopCategories().find(c->c.getLabel().equals("civilization"));
		assertTrue(civMenuItem.isDefined());
		assertEquals("c",civMenuItem.get().getShortcutKey());
		assertTrue(civMenuItem.get().isSubmenu());
	}

	@Test
	@DisplayName("Given a set of game commands, it should correctly create the whole command tree")
	public void should_map_submenus_correctly(){
		var gameCommandsTopMenusTry = GameCommandsTopMenu.builder()
		  .using(mockConfiguration)
		  .forLocale(Locale.ENGLISH)
		  .withCommands(buildSampleGameCommands())
		  .build();
		assertTrue(gameCommandsTopMenusTry.isSuccess());
		var gameCommandsTopMenus = gameCommandsTopMenusTry.get();


		var gameMenuItemTry = gameCommandsTopMenus.getTopCategories().find(c->c.getLabel().equals("game"));
		assertTrue(gameMenuItemTry.isDefined());
		var gameMenuItem = gameMenuItemTry.get();
		assertTrue(gameMenuItem.getCommands().isDefined());
		assertEquals(3,gameMenuItem.getCommands().get().size());
		assertTrue(gameMenuItem.getCommands().get().find(GameCommandMenuItem::isSubmenu).isEmpty());

		var civilizationMenuItemTry = gameCommandsTopMenus.getTopCategories().find(c->c.getLabel().equals("civilization"));
		assertTrue(civilizationMenuItemTry.isDefined());
		var civilizationMenuItem = civilizationMenuItemTry.get();
		assertTrue(civilizationMenuItem.getCommands().isDefined());
		assertEquals(1,civilizationMenuItem.getCommands().get().size());
		assertTrue(civilizationMenuItem.getCommands().get().find(GameCommandMenuItem::isSubmenu).isEmpty());

	}

	private static GameCommands buildSampleGameCommands() {
		var gameCommands = new GameCommands();
		gameCommands.addGameCommandCategory(new GameCommandCategory("game","game"));
		gameCommands.addGameCommand(new GameCommand<Void>("game.save","game.save",null,"game"));
		gameCommands.addGameCommand(new GameCommand<Void>("game.load","game.load",null,"game"));
		gameCommands.addGameCommand(new GameCommand<Void>("game.quit","game.quit",null,"game"));
		gameCommands.addGameCommandCategory(new GameCommandCategory("civilization","civilization"));
		gameCommands.addGameCommand(new GameCommand<Void>("civilization.tech","civilization.tech",null,"civilization"));

		return gameCommands;
	}
}