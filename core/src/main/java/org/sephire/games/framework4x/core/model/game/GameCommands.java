package org.sephire.games.framework4x.core.model.game;

import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;

/**
 * <p>This is the root aggregate for all game commands of the game.</p>
 * <p>Game commands form a tree of categories and subcategories of commands that are
 * executed in the context of a running game. This categorization can be used to show
 * these commands by category, but they are not meant to be shown exclusively this way.</p>
 */
public class GameCommands {

	/**
	 * These are the root categories that belong to no other category.
	 */
	private List<GameCommandCategory> categories;

	public GameCommands() {
		this.categories = List.empty();
	}

	/**
	 * <p>Adds a game command to the tree, in the appropriate subcategory</p>
	 *
	 * <p>May return the following errors:
	 * <ul>
	 *     <li>{@link ParentCategoryDoesntExistException} if the category a game command belongs to doesn't exist</li>
	 * </ul>
	 * </p>
	 * @param gameCommand
	 * @return
	 */
	public Try<Void> addGameCommand(GameCommand<?> gameCommand) {
		return Try.of(()->{
			var parentCategory = findGameCategory(gameCommand.getCategoryIdentifier())
			  .getOrElseThrow(()->new ParentCategoryDoesntExistException(gameCommand));
			parentCategory.addGameCommand(gameCommand);

			return null;
		});
	}

	/**
	 * <p>Adds a game command category to the tree (in the appropriate subcategory if it has a defined parent)</p>
	 *
	 * <p>May return the following errors:
	 * <ul>
	 *     <li>{@link ParentCategoryDoesntExistException} if the category this category belongs to doesn't exist</li>
	 * </ul>
	 * </p>
	 * @param gameCommandCategory
	 * @return
	 */
	public Try<Void> addGameCommandCategory(GameCommandCategory gameCommandCategory) {
		return Try.of(()->{
			if(gameCommandCategory.getParentCategoryIdentifier().isEmpty()){
				categories = categories.append(gameCommandCategory);
			} else {
				findGameCategory(gameCommandCategory.getParentCategoryIdentifier().get())
				  .getOrElseThrow(()->new ParentCategoryDoesntExistException(gameCommandCategory))
				  .addSubcategory(gameCommandCategory)
				  .getOrElseThrow((t)->t);
			}

			return null;
		});
	}

	/**
	 * Returns the list of root categories. Empty list if there are none.
	 * @return
	 */
	public List<GameCommandCategory> getRootCategories() {
		return this.categories;
	}

	private Option<GameCommandCategory> findGameCategory(String identifier) {
		return categories.find((category)->category.containsCategory(identifier));
	}
}
