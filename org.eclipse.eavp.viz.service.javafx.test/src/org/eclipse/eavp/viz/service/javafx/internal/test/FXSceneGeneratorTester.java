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
package org.eclipse.eavp.viz.service.javafx.internal.test;

import static org.junit.Assert.assertTrue;

import org.eclipse.eavp.viz.service.javafx.internal.FXSceneGenerator;
import org.eclipse.eavp.viz.service.javafx.internal.Util;
import org.eclipse.eavp.viz.service.javafx.scene.base.GNode;
import org.eclipse.eavp.viz.service.javafx.scene.model.INode;
import org.junit.Test;

/**
 * A class to test the functionality of the FXSceneGenerator.
 * 
 * @author Robert Smith
 *
 */
public class FXSceneGeneratorTester {

	/**
	 * Test that the content provider is generating the scene correctly.
	 */
	@Test
	public void checkSceneGeneration() {

		// Create a provider
		FXSceneGenerator generator = new FXSceneGenerator();

		// Create some nodes
		GNode root = new GNode();
		GNode node1 = new GNode();
		GNode node2 = new GNode();
		GNode node3 = new GNode();

		// Create a tree from the nodes
		root.addChild(node1);
		root.addChild(node2);
		node1.addChild(node3);

		// Generate a scene from the tree
		generator.generateScene(root);

		// Check that the tree of JavaFX nodes mirrors the tree of corresponding
		// GNodes
		assertTrue(Util.getFxGroup(root).getChildren()
				.contains(Util.getFxGroup(node1)));
		assertTrue(Util.getFxGroup(root).getChildren()
				.contains(Util.getFxGroup(node2)));
		assertTrue(Util.getFxGroup(node1).getChildren()
				.contains(Util.getFxGroup(node3)));

		// Check that all the JavaFX nodes have references to the containing
		// GNode
		assertTrue(
				Util.getFxGroup(root).getProperties().get(INode.class) == root);
		assertTrue(Util.getFxGroup(node1).getProperties()
				.get(INode.class) == node1);
		assertTrue(Util.getFxGroup(node2).getProperties()
				.get(INode.class) == node2);
		assertTrue(Util.getFxGroup(node3).getProperties()
				.get(INode.class) == node3);

	}
}
