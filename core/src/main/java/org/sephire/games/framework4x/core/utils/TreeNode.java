package org.sephire.games.framework4x.core.utils;

import io.vavr.Function1;
import io.vavr.collection.*;
import io.vavr.control.Option;

public class TreeNode<NODE_TYPE> {
	private Set<TreeNode<NODE_TYPE>> children;
	private NODE_TYPE value;

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

	public List<NODE_TYPE> toOrderedListByBreadthFirstTraversal() {

		List<NODE_TYPE> result = List.of(value);
		result = result.appendAll(children.map(TreeNode::getValue));
		result = result.appendAll(
		  children
			.map(TreeNode::toOrderedListByBreadthFirstTraversal)
			.reduce(List::appendAll)
		);

		return result;
	}

	public Option<TreeNode<NODE_TYPE>> findNode(Function1<NODE_TYPE,Boolean> predicate) {
		Option<TreeNode<NODE_TYPE>> result = predicate.apply(value) ? Option.of(this) : Option.none();

		if(!result.isDefined()) {
			result = children.find((node)->predicate.apply(node.value));

			if(!result.isDefined()) {

				result = children
				  .map((child)->child.findNode(predicate))
				  .find(Option::isDefined)
				  .map(Option::get);
			}
		}

		return result;
	}

}
