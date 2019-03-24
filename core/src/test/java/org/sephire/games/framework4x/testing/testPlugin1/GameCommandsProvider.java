package org.sephire.games.framework4x.testing.testPlugin1;

import io.vavr.collection.List;
import io.vavr.control.Try;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.model.game.GameCommand;
import org.sephire.games.framework4x.core.model.game.GameCommandCategory;
import org.sephire.games.framework4x.core.plugins.commands.GameCommandGenerator;

/**
 * Exercises the three ways to add game commands and categories to a game from a plugin.
 */
public class GameCommandsProvider {

	@GameCommandGenerator
	public Try<List<GameCommandCategory>> buildCategories(Configuration.Builder configuration) {
		return Try.of(()->List.of(
		  new GameCommandCategory("game","game.label"),
		  new GameCommandCategory("stats","stats.label"),
		  new GameCommandCategory("help","help.label")
		));
	}

	@GameCommandGenerator
	public Try<GameCommandCategory> buildHelpTopicCategory(Configuration.Builder configuration) {
		return Try.of(()->{
			var helpTopicsCategory = new GameCommandCategory("help.topics","help.topics.label","help");
			var techTopic = new GameCommand<Void>("help.topics.tech",
			  "help.topics.tech.label",
			  game->null,
			  helpTopicsCategory.getIdentifier());
			helpTopicsCategory.addGameCommand(techTopic);

			return helpTopicsCategory;
		});
	}

	@GameCommandGenerator
	public Try<GameCommand<Void>> buildSaveGameCommand(Configuration.Builder configuration){
		return Try.of(()->{
			var parentCategory = "game";
			return new GameCommand<Void>("game.save", "game.save.label", game -> null, parentCategory);
		});
	}
}
