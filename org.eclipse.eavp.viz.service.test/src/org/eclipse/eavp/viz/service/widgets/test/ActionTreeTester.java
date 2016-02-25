/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
 *   Claire Saunders, Matthew Wang, Anna Wojtowicz
 *******************************************************************************/
package org.eclipse.eavp.viz.service.widgets.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.eclipse.eavp.viz.service.datastructures.VizActionTree;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.junit.Test;

/**
 * This class tests the structure of ActionTrees. This does not test how well
 * the UI actually works.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class ActionTreeTester {

	/**
	 * Tests a tree that is a single node (a button).
	 */
	@Test
	public void checkSingleNode() {
		// Create a JFace Action.
		final Action root = new Action("Root") {
		};

		// Create the ActionTree.
		VizActionTree rootTree = new VizActionTree(root);

		// Get the ActionContributionItem from the root node and check it.
		ActionContributionItem aci = (ActionContributionItem) rootTree
				.getContributionItem();
		assertNotNull(aci);
		assertSame(root, aci.getAction());
		assertEquals("Root", aci.getAction().getText());
		assertTrue(aci.getAction().isEnabled());

		// The returned ArrayList of child nodes should be null.
		List<VizActionTree> children = rootTree.getChildren();
		assertNull(children);

		/* ---- Check enabling/disabling the tree. ---- */
		// Enabling shouldn't change anything.
		rootTree.setEnabled(true);
		assertTrue(aci.getAction().isEnabled());

		// Disabling it should work.
		rootTree.setEnabled(false);
		assertFalse(aci.getAction().isEnabled());

		// Enabling it should work.
		rootTree.setEnabled(true);
		assertTrue(aci.getAction().isEnabled());

		/* -------------------------------------------- */

		return;
	}

	/**
	 * Tests a tree that is made up of more than one node and more than one
	 * level (nested menus).
	 */
	@Test
	public void checkTree() {
		/*
		 * Tree structure: Root |--Action 1 | |--Action 1a | \--Action 1b
		 * |--Action 2 (empty tree) | \ \--Action 3 (not a tree, but an action)
		 */

		// Create the above ActionTree.
		VizActionTree rootTree = new VizActionTree("Root");

		VizActionTree action1Tree = new VizActionTree("Action 1");
		rootTree.add(action1Tree);

		final Action action1a = new Action("Action 1a") {
		};
		VizActionTree actionTree1a = new VizActionTree(action1a);
		action1Tree.add(actionTree1a);
		final Action action1b = new Action("Action 1b") {
		};
		action1Tree.add(new VizActionTree(action1b));

		VizActionTree action2Tree = new VizActionTree("Action 2");
		rootTree.add(action2Tree);

		VizActionTree action3Tree = new VizActionTree(new Action("Action 3") {
		});
		rootTree.add(action3Tree);

		// Variables...
		ActionContributionItem aci;
		VizActionTree tree;
		Action action;
		List<VizActionTree> children;

		// Test root node.
		aci = (ActionContributionItem) rootTree.getContributionItem();
		assertNotNull(aci);
		assertEquals("Root", aci.getAction().getText());
		assertTrue(aci.getAction().isEnabled());

		// The returned ArrayList of child nodes should not be null.
		children = rootTree.getChildren();
		assertNotNull(children);
		assertEquals(3, children.size());

		// Test Action3
		tree = children.get(2);
		assertSame(action3Tree, tree);
		aci = (ActionContributionItem) tree.getContributionItem();
		assertNotNull(aci);
		assertEquals("Action 3", aci.getAction().getText());
		assertTrue(aci.getAction().isEnabled());
		assertNull(tree.getChildren());

		// Test Action2
		tree = children.get(1);
		assertSame(action2Tree, tree);
		aci = (ActionContributionItem) tree.getContributionItem();
		assertNotNull(aci);
		assertEquals("Action 2", aci.getAction().getText());
		assertTrue(!aci.getAction().isEnabled());
		assertNotNull(tree.getChildren());
		assertEquals(0, tree.getChildren().size());

		// Test Action1.
		tree = children.get(0);
		assertSame(action1Tree, tree);
		aci = (ActionContributionItem) tree.getContributionItem();
		assertNotNull(aci);
		assertEquals("Action 1", aci.getAction().getText());
		assertTrue(aci.getAction().isEnabled());
		children = tree.getChildren();
		assertNotNull(children);
		assertEquals(2, children.size());

		// Test Action1a.
		tree = children.get(0);
		aci = (ActionContributionItem) tree.getContributionItem();
		assertNotNull(aci);
		action = (Action) aci.getAction();
		assertSame(action1a, action);
		assertEquals("Action 1a", action.getText());
		assertTrue(action.isEnabled());
		assertNull(tree.getChildren());

		// Test Action1b.
		tree = children.get(1);
		aci = (ActionContributionItem) tree.getContributionItem();
		assertNotNull(aci);
		action = (Action) aci.getAction();
		assertSame(action1b, action);
		assertEquals("Action 1b", action.getText());
		assertTrue(action.isEnabled());
		assertNull(tree.getChildren());

		// Test remove functionality by removing action1a.
		action1Tree.remove(actionTree1a);
		children = action1Tree.getChildren();
		assertEquals(1, children.size());
		aci = (ActionContributionItem) children.get(0).getContributionItem();
		assertSame(action1b, aci.getAction());

		// Test removeAll functionality.
		action1Tree.removeAll();
		children = action1Tree.getChildren();
		assertNotNull(children);
		assertTrue(children.isEmpty());
		aci = (ActionContributionItem) action1Tree.getContributionItem();
		assertTrue(!aci.getAction().isEnabled());

		// Re-add an action to action tree 1 and it should be enabled.
		action1Tree.add(actionTree1a);
		assertEquals(1, children.size());
		assertTrue(aci.getAction().isEnabled());

		// Remove the action from action tree 1 and it should be disabled.
		action1Tree.remove(actionTree1a);
		assertTrue(children.isEmpty());
		assertTrue(!aci.getAction().isEnabled());

		/* ---- Check enabling/disabling the tree. ---- */
		// Make sure that the button stays disabled because its children are
		// gone.
		action1Tree.setEnabled(true);
		assertTrue(children.isEmpty());
		assertTrue(!aci.getAction().isEnabled());

		// Adding a child should enable the tree.
		action1Tree.add(actionTree1a);
		assertFalse(children.isEmpty());
		assertTrue(aci.getAction().isEnabled());

		// Disabling the tree should disable it.
		action1Tree.setEnabled(false);
		assertFalse(children.isEmpty());
		assertFalse(aci.getAction().isEnabled());

		// Enabling the tree should enable it.
		action1Tree.setEnabled(true);
		assertFalse(children.isEmpty());
		assertTrue(aci.getAction().isEnabled());

		// Remove the action from action tree 1 and it should be disabled.
		action1Tree.remove(actionTree1a);
		assertTrue(children.isEmpty());
		assertFalse(aci.getAction().isEnabled());

		// Disabling the tree should keep it disabled.
		action1Tree.setEnabled(false);
		assertTrue(children.isEmpty());
		assertFalse(aci.getAction().isEnabled());

		// Adding a child shouldn't enable it.
		action1Tree.add(actionTree1a);
		assertFalse(children.isEmpty());
		assertFalse(aci.getAction().isEnabled());

		// Enabling the tree should enable it.
		action1Tree.setEnabled(true);
		assertFalse(children.isEmpty());
		assertTrue(aci.getAction().isEnabled());
		/* -------------------------------------------- */

		return;
	}
}
