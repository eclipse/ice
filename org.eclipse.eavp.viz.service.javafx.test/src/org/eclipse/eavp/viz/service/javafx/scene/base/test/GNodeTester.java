/*******************************************************************************
 * Copyright (c) 2016 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Robert Smith
 *******************************************************************************/
package org.eclipse.eavp.viz.service.javafx.scene.base.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.eavp.viz.service.javafx.scene.base.GNode;
import org.junit.Test;

/**
 * A class to test the functionality of GNode.
 * 
 * @author Robert Smith
 *
 */
public class GNodeTester {

	/**
	 * Check that the GNode manages its children correctly.
	 */
	@Test
	public void checkChildren() {

		// Create some nodes for the test
		GNode root = new GNode();
		GNode node1 = new GNode();
		node1.setProperty("Name", "node1");
		GNode node2 = new GNode();
		node2.setProperty("Name", "node2");
		GNode node3 = new GNode();
		node3.setProperty("Name", "node3");

		// Add a node to the root and check that its there and has had the root
		// registered as a parent
		root.addChild(node1);
		assertTrue(root.getChildren(false).contains(node1));
		assertTrue(node1.getParent() == root);

		// Add the other nodes and check that they are also put in the list
		root.addChild(node2);
		root.addChild(node3);
		assertTrue(root.getChildren(false).contains(node2));
		assertTrue(root.getChildren(false).contains(node3));

		// Remove a child and make sure it alone was removed
		root.removeChild(node1);
		assertFalse(root.getChildren(false).contains(node1));
		assertTrue(root.getChildren(false).contains(node2));
		assertTrue(root.getChildren(false).contains(node3));

		// Check that node1's parent is now null
		assertNull(node1.getParent());

		// Check that the root always gives out the same reference to the list
		// of children when not creating a copy
		assertTrue(root.getChildren(false) == root.getChildren(false));

		// Check that the root gives a new list of children each time it is
		// asked for a copy
		assertTrue(root.getChildren(true) != root.getChildren(true));

		// Check that GNode can remove all its children
		root.removeAllChildren();
		assertTrue(root.getChildren(false).isEmpty());
	}
}
