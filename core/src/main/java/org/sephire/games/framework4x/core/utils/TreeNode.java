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
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Set;
import io.vavr.control.Option;

/**
 * <p>This is a quick and dirty mutable tree implementation whose sole purpose is to
 * provide a list of items ordered by parentage, parents starting first.</p>
 * <p>I didn't find a Tree implementation that did simply what I needed.</p>
 * <p>Only the children are linked, no way to get the parent node.</p>
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

	public Set<TreeNode<NODE_TYPE>> getChildren() { return children; }

	public NODE_TYPE getValue() {
		return this.value;
	}

	/**
	 * <p>Starting from this node, provide a list of items contained
	 * in this tree, ordered by a breadth-first traversal.<br/>
	 * This means that the order is: parent -> siblings -> children</p>
	 *
	 * <p>This is useful for cases in which we want a list of items ordered
	 * by descendant parentage hierarchy.</p>
	 *
	 * <p>The values of the nodes are returned, not the nodes themselves.</p>
	 *
	 * <p>The root node may be excluded from the set if its just a
	 * container for its children without value itself</p>
	 * @return
	 */
	public List<NODE_TYPE> toOrderedListByBreadthFirstTraversal(boolean excludeRootNode) {

		List<NODE_TYPE> result = excludeRootNode? List.empty() : List.of(value);

		result = result.appendAll(
		  children.flatMap(TreeNode::toOrderedListByBreadthFirstTraversal)
		);

		return result;
	}
	public List<NODE_TYPE> toOrderedListByBreadthFirstTraversal() {
		return toOrderedListByBreadthFirstTraversal(false);
	}


	/**
	 * <p>Starting from this node, find the first node that matches the predicate.
	 * The search is performed breadth-first.</p>
	 * <p>The initial node may be excluded from the search, if, for example, the root node
	 * of the tree is a fake container for the rest of the nodes</p>
	 *
	 * @param predicate the predicate to search by
	 * @param excludeFirstNode whether to exclude the first node on the search
	 * @return
	 */
	public Option<TreeNode<NODE_TYPE>> findFirstNode(Function1<NODE_TYPE,Boolean> predicate,boolean excludeFirstNode) {
		Option<TreeNode<NODE_TYPE>> result = excludeFirstNode? Option.none() :
		  predicate.apply(value) ? Option.of(this) :
			Option.none();

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
	public Option<TreeNode<NODE_TYPE>> findFirstNode(Function1<NODE_TYPE,Boolean> predicate) {
		return findFirstNode(predicate,false);
	}

}
