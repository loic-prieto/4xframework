/**
 * 4X Framework - Core library - The core library on which to base the game
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
package org.sephire.games.framework4x.core.utils;

import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.collection.List;
import io.vavr.collection.Set;
import io.vavr.control.Option;
import io.vavr.control.Try;

/**
 * <p>Represents a tree whose root is a container for the main branches, which means that the value of the root node
 * itself has no meaning.</p>
 * <p>Once the tree has been built, it is immutable</p>
 * <p>The nodes of the tree may either be base nodes or non base nodes:<ul>
 *     <li>Base nodes are nodes that have no parent. These will be the main branches of the tree</li>
 *     <li>non base nodes are nodes that have a parent. They may act as parents to other nodes</li>
 * </ul></p>
 * <p>Since the main relation between nodes is that they may have a parent, we need two functions for the
 * generic tree:<ul>
 *     <li>A predicate to check whether a node is a base node</li>
 *     <li>A predicate to check whether a node is a parent of another node</li>
 * </ul>
 * Thus a rootless generic tree needs to be built: a set of items, and two predicates to establish the relationship
 * between nodes.
 * </p>
 *
 * @param <T> the item type
 */
public class RootlessTree<T> {
	private Function2<T,T,Boolean> isParentPredicate;
	private Function1<T,Boolean> isBaseItemPredicate;
	private TreeNode<T> rootNode;

	/**
	 * This is only to be used by static constructors
	 * @param isParentPredicate (item,potentialParentItem) -> Boolean
	 * @param isBaseItemPredicate (item) -> Boolean
	 */
	private RootlessTree(Function2<T, T, Boolean> isParentPredicate, Function1<T, Boolean> isBaseItemPredicate) {
		this.isParentPredicate = isParentPredicate;
		this.isBaseItemPredicate = isBaseItemPredicate;
		this.rootNode = new TreeNode<>(null);
	}

	/**
	 * <p>Get a list of independent branches of items, ordered by topological relations between
	 * the items.</p>
	 * <p>Each list is independent from the other, which means that parallel operations can
	 * be applied to them</p>
	 * @return
	 */
	public List<List<T>> getTopologicallyOrderedBranches() {
		var basePlugins = rootNode.getChildren();

		return basePlugins
		  .map(TreeNode::toOrderedListByBreadthFirstTraversal)
		  .collect(List.collector());
	}

	/**
	 * <p>Finds the first node of the tree that matches the predicate.</p>
	 * <p>
	 *     TODO: this returns the underlying data structure of the tree, which is mutable, this should be refactored.
	 * </p>
	 *
	 * @param itemFinderPredicate
	 * @return
	 */
	public Option<TreeNode<T>> findFirstItem(Function1<T,Boolean> itemFinderPredicate) {
		return rootNode.findFirstNode(itemFinderPredicate,true);
	}

	/**
	 * <p>Get a list of items from all branches ordered by topological order</p>
	 * @return
	 */
	public List<T> getItemsOrderedTopologically() {
		return rootNode.toOrderedListByBreadthFirstTraversal(true);
	}

	/**
	 * <p>Adds an item to the tree.</p>
	 * <p>If the item has already been added previously, it won't add it again, but it is still considered
	 * a valid operation (so, no Try failure)</p>
	 * <p>May throw the following errors:
	 * <ul>
	 *     <li>IllegalArgumentException, if the parent of the item is not already present in the tree</li>
	 * </ul>
	 * </p>
	 * @param item
	 * @return
	 */
	private Try<Void> addItem(T item) {
		return Try.of(()->{
			if(rootNode.findFirstNode(i->i.equals(item),true).isDefined()) {
				return null;
			}

			if(isBaseItemPredicate.apply(item)) {
				rootNode.addChildren(new TreeNode<>(item));
			} else {
				var parentNodeSearch = rootNode.findFirstNode(i->isParentPredicate.apply(item,i),true)
				  .getOrElseThrow(()->new IllegalArgumentException("Tried to add an item whose parent has not been added yet to the tree"));

				parentNodeSearch.addChildren(new TreeNode<>(item));
			}

			return null;
		});
	}

	/**
	 * <p>From a set of items, build a rootless tree, honouring the parent-child relationship between
	 * items.</p>
	 * <p>The set must include all items needed to resolve the relationship between them</p>
	 * <p>May return the following errors:
	 * <ul>
	 *     <li>IllegalArgumentException, if an item of the set depends on a parent not included in the set</li>
	 * </ul>
	 * </p>
	 * @param items a collection of items to build the tree
	 * @param isBaseItemPredicate (item) -> Boolean
	 * @param isParentPredicate (item,potentialParentItem) -> Boolean
	 * @return
	 */
	public static <T> Try<RootlessTree<T>> fromItemSet(Set<T> items,
													   Function2<T,T,Boolean> isParentPredicate,
													   Function1<T,Boolean> isBaseItemPredicate) {
		return Try.of(()->{
			RootlessTree<T> tree = new RootlessTree<>(isParentPredicate,isBaseItemPredicate);

			Try.sequence(items.map( item-> addItem(item,tree,items,isBaseItemPredicate,isParentPredicate)))
			  .getOrElseThrow(e->e);

			return tree;
		});
	}

	/** Auxiliary recursive function for the fromItems method */
	private static <T> Try<Void> addItem(T item,
										 RootlessTree tree,
										 Set<T> items,
										 Function1<T,Boolean> isBaseItemPredicate,
										 Function2<T,T,Boolean> isParentPredicate) {
		return Try.of(()->{
			// If the item is not a base item, then this will ensure that the parent of this
			// Item has been inserted.
			if(!isBaseItemPredicate.apply(item)){
				// Since this item is not a base item, it will try first to find the
				// From the full set of items, tries to find the parent of the item to insert
				var parentItem = items.find((i)->isParentPredicate.apply(item,i))
				  .getOrElseThrow(()->new IllegalArgumentException("Tried to add an item whose parent has not been added yet to the tree"));
				// If the parent has already been inserted, it doesn't get added twice
				var addParentTry = addItem(parentItem,tree,items,isBaseItemPredicate,isParentPredicate);
				if(addParentTry.isFailure()) {
					throw addParentTry.getCause();
				}
			}

			// Once the parent item is ensured to be inserted, we insert this one
			var itemAddTry = tree.addItem(item);
			if(itemAddTry.isFailure()) {
				throw itemAddTry.getCause();
			}

			return null;
		});
	}

}
