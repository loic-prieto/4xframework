/**
 * 4X Framework - Core library - The core library on which to base the game
 * Copyright © 2018 Loïc Prieto Dehennault (loic.sephiroth@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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

	public void addChildren(Set<TreeNode<NODE_TYPE>> children) {
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
