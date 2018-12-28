package org.sephire.games.framework4x.core.utils;

import io.vavr.Function1;
import io.vavr.collection.*;
import io.vavr.control.Option;

/**
 * This is a quick and dirty mutable tree implementation whose sole purpose is to
 * provide a list of items ordered by parentage, parents starting first.
 *
 * I didn't find a Tree implementation that did simply what I needed.
 *
 * Only the children are linked, no way to get the parent node.
 *
 * @param <NODE_TYPE> the type of the value contained in the tree nodes.
 */
public class TreeNode<NODE_TYPE> {
	private Set<TreeNode<NODE_TYPE>> children;
	private NODE_TYPE value;

	/**
	 * Creates a node for the tree. Any node is a tree, which means any node
	 * can be a root node.
	 * @param value
	 * @param children
	 */
	public TreeNode(NODE_TYPE value, TreeNode<NODE_TYPE> ...children ) {
		this.children = HashSet.of(children);
		this.value = value;
	}

	public void addChildren(TreeNode<NODE_TYPE> ...children) {
		this.children = this.children.union(HashSet.of(children));
	}

	public void addChildren(Seq<TreeNode<NODE_TYPE>> children) {
		this.children = this.children.union(HashSet.ofAll(children));
	}

	public NODE_TYPE getValue() {
		return this.value;
	}

	/**
	 * Starting from this node, provide a list of items contained
	 * in this tree, ordered by a breadth-first traversal.
	 * This means that the order is: parent -> siblings -> children
	 *
	 * This is useful for cases in which we want a list of items ordered
	 * by descendant parentage hierarchy.
	 *
	 * The values of the nodes are returned, not the nodes themselves.
	 * @return
	 */
	public List<NODE_TYPE> toOrderedListByBreadthFirstTraversal() {

		List<NODE_TYPE> result = List.of(value);
		result = result.appendAll(
		  children.flatMap(TreeNode::toOrderedListByBreadthFirstTraversal)
		);

		return result;
	}

	/**
	 * Starting from this node, find the first node that matches the predicate.
	 * The search is performed breadth-first.
	 * @param predicate
	 * @return
	 */
	public Option<TreeNode<NODE_TYPE>> findFirstNode(Function1<NODE_TYPE,Boolean> predicate) {
		Option<TreeNode<NODE_TYPE>> result = predicate.apply(value) ? Option.of(this) : Option.none();

		if(!result.isDefined()) {
			result = children.find((node)->predicate.apply(node.value));

			if(!result.isDefined()) {

				result = children
				  .map((child)->child.findFirstNode(predicate))
				  .find(Option::isDefined)
				  .map(Option::get);
			}
		}

		return result;
	}

}
