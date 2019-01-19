package org.sephire.games.framework4x.core.utils;

import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.collection.List;
import io.vavr.collection.Set;
import io.vavr.control.Try;

/**
 * <p>Represents a tree whose root is a container for the main branches, which means that the value of the root node
 * itself has no meaning.</p>
 * <p>Once built, the tree is immutable</p>
 * <p>The nodes of the tree may either be base nodes or non base nodes:<ul>
 *     <li>Base nodes are nodes that have no parent. These will be the main branches of the tree</li>
 *     <li>non base nodes are nodes that have a parent. They may act as parents to other nodes</li>
 * </ul></p>
 * <p>Since the main relation between nodes is that they may have a parent, we need two functions for the
 * generic tree:<ul>
 *     <li>A predicate to check whether a node is a base node</li>
 *     <li>A predicate to check whether a node is a parent of another node</li>
 * </ul>
 * Thus a rootless generic tree needs to be built: a set of items, and two predicates to stablish the relationship
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
	 *
	 * @param isParentPredicate (item,potentialParentItem) -> Boolean
	 * @param isBaseItemPredicate (item) -> Boolean
	 */
	public RootlessTree(Function2<T, T, Boolean> isParentPredicate, Function1<T, Boolean> isBaseItemPredicate) {
		this.isParentPredicate = isParentPredicate;
		this.isBaseItemPredicate = isBaseItemPredicate;
		this.rootNode = new TreeNode<>(null);
	}

	/**
	 * <p>Get a list of independent branche of items, ordered by topological relations between
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
	public Try<Void> addItem(T item) {
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
	 * <p>From a set of itemw, build a rootless tree, honouring the parent-child relationship between
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
			if(!isBaseItemPredicate.apply(item)){
				var parentItem = items.find((i)->isParentPredicate.apply(item,i))
				  .getOrElseThrow(()->new IllegalArgumentException("Tried to add an item whose parent has not been added yet to the tree"));
				// If the parent has already been inserted, it doesn't get added twice
				var addParentTry = addItem(parentItem,tree,items,isBaseItemPredicate,isParentPredicate);
				if(addParentTry.isFailure()) {
					throw addParentTry.getCause();
				}
			}

			var itemAddTry = tree.addItem(item);
			if(itemAddTry.isFailure()) {
				throw itemAddTry.getCause();
			}

			return null;
		});
	}

}
