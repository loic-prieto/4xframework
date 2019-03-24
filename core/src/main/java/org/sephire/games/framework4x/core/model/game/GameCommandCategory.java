package org.sephire.games.framework4x.core.model.game;

import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

/**
 * <p>A category of game commands</p>
 * <p>May contain game commands and subcategories</p>
 */
@EqualsAndHashCode(of = {"identifier"})
@Getter
@AllArgsConstructor
public class GameCommandCategory {
	@NonNull private String identifier;
	private List<GameCommandCategory> subcategories;
	private List<GameCommand<?>> commands;
	private Option<String> parentCategoryIdentifier;
	/**
	 * The i18n key for the label of the category
	 */
	@NonNull private String label;

	public GameCommandCategory(String identifier,String label) {
		this(identifier,List.empty(),List.empty(),Option.none(),label);
	}

	public GameCommandCategory(String identifier,String label,String parentIdentifier) {
		this(identifier,List.empty(),List.empty(),Option.of(parentIdentifier),label);
	}

	/**
	 * <p>Adds a subcategory in the appropriate place of this command tree, including
	 * any subcategory of the tree.</p>
	 * <p>May return the following errors: <ul>
	 *     <li>IllegalArgumentException if the category has not defined a parent category to belong to</li>
	 *     <li>ParentCategoryDoesntExistException if the defined parent doesn't exist in this command tree</li>
	 * </ul></p>
	 * @param gameCommandCategory
	 * @return
	 */
	public Try<Void> addSubcategory(GameCommandCategory gameCommandCategory) {
		return Try.of(()->{
			var parentIdentifier = gameCommandCategory.getParentCategoryIdentifier()
			  .getOrElseThrow(()->new IllegalArgumentException("A category cannot be added as a subcategory if it has no parent defined"));

			var parent = findParent(parentIdentifier)
			  .getOrElseThrow(()->new ParentCategoryDoesntExistException(gameCommandCategory));

			// Beware here, using the addSubcategory method would create an infinite loop
			parent.subcategories = parent.subcategories.append(gameCommandCategory);

			return null;
		});
	}

	/**
	 * <p>Adds a game command in the appropriate place of this command tree, including
	 * any subcategory of the tree.</p>
	 * <p>May return the following errors: <ul>
	 *     <li>ParentCategoryDoesntExistException if the defined parent doesn't exist in this command tree</li>
	 * </ul></p>
	 * @param gameCommand
	 * @return
	 */
	public Try<Void> addGameCommand(GameCommand<?> gameCommand) {
		return Try.of(()->{
			var parentCategory = findParent(gameCommand.getCategoryIdentifier())
			  .getOrElseThrow(()->new ParentCategoryDoesntExistException(gameCommand));

			// Beware here, using the addGameCommand method would create an infinite loop
			parentCategory.commands = parentCategory.commands.append(gameCommand);

			return null;
		});
	}

	private Option<GameCommandCategory> findParent(String parentIdentifier) {
		return identifier.equals(parentIdentifier) ?

		  Option.of(this) :

		  this.subcategories.find((category)->category.findParent(parentIdentifier).isDefined())
			.flatMap((category)->category.findParent(parentIdentifier));
	}

	/**
	 * <p>Checks whether this category contains the given category identifier</p>
	 * @param categoryIdentifier
	 * @return true if the category itself or one of its subcategories contains the given category identifier
	 */
	public boolean containsCategory(String categoryIdentifier) {
		return findParent(categoryIdentifier).isDefined();
	}
}
