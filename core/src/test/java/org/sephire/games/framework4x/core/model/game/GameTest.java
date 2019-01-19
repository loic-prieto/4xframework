package org.sephire.games.framework4x.core.model.game;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sephire.games.framework4x.core.model.config.Configuration;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameTest {

	@Test
	@DisplayName("When a game has been started, plugins can write state into the game")
	public void should_load_game_state_when_starting_game() {
		var configuration = Configuration.builder().build();

		var gameTry = Game.builder()
		  .withConfiguration(configuration)
		  .withMapGenerator(null)
		  .build();

		assertTrue(gameTry.isSuccess());

		var testPlugin1StateTry = gameTry.get().getState(Plugin1.TestStateEnum.KEY1,String.class);
		assertTrue(testPlugin1StateTry.isSuccess());
		assertTrue(testPlugin1StateTry.get().isDefined());

		var stateValue = testPlugin1StateTry.get().get();
		assertEquals(String.class,stateValue.getClass());
		assertEquals("testValue",stateValue);
	}

}
