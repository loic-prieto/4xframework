package org.sephire.games.framework4x.core.utils;


import io.vavr.collection.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TreeNodeTest {

	@Test
	@DisplayName("Given a tree of nodes, a requested existing node is found")
	public void existingNodeIsFound() {
		var testTree = buildValidTree();

		var searchResult = testTree.findFirstNode((node)->node.equals("child2.2.1"));

		assertTrue(searchResult.isDefined());
		assertEquals("child2.2.1",searchResult.get().getValue());
	}

	@Test
	@DisplayName("Given a tree of nodes, a requested non existing node is not found")
	public void nonExistingNodeIsNotFound() {
		var testTree = buildValidTree();

		var searchResult = testTree.findFirstNode((node)->node.equals("not existing node"));

		assertTrue(searchResult.isEmpty());
	}


	@Test
	@DisplayName("Given a tree of nodes, the nodes are returned breadth first in a list")
	public void nodesAreOrderedByParentage() {
		var testTree = buildValidTree();
		var expectedOrderedList = List.of("root", "child1", "child2", "child1.1", "child2.1", "child1.2", "child2,2", "child2.2.1", "child2.2.2");

		assertEquals(expectedOrderedList,testTree.toOrderedListByBreadthFirstTraversal());
	}

	private static TreeNode<String> buildValidTree() {
		return
		  new TreeNode<>("root",
			new TreeNode<>("child1",
			  new TreeNode<>("child1.1"),
			  new TreeNode<>("child1.2")),
			new TreeNode<>("child2",
			  new TreeNode<>("child2.1"),
			  new TreeNode<>("child2,2",
				new TreeNode<>("child2.2.1"),
				new TreeNode<>("child2.2.2"))));
	}
}
