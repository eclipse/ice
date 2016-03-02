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
package org.eclipse.eavp.viz.service.javafx.canvas.test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Map;

import org.eclipse.eavp.viz.service.javafx.canvas.BasicViewer;
import org.eclipse.eavp.viz.service.javafx.canvas.FXContentProvider;
import org.eclipse.eavp.viz.service.javafx.canvas.FXViewer;
import org.eclipse.eavp.viz.service.javafx.internal.Util;
import org.eclipse.eavp.viz.service.javafx.scene.base.GNode;
import org.eclipse.eavp.viz.service.javafx.scene.base.ICamera;
import org.eclipse.eavp.viz.service.javafx.scene.model.IAttachment;
import org.eclipse.eavp.viz.service.javafx.scene.model.INode;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.Test;

import javafx.scene.Group;

/**
 * A class to test the functionality of the FXContentProvider.
 * 
 * @author Robert Smith
 *
 */
public class FXContentProviderTester {

	/**
	 * Test that the content provider is generating the scene correctly.
	 */
	@Test
	public void checkSceneGeneration() {

		// Create a provider
		FXContentProvider provider = new FXContentProvider();

		// Create some nodes
		GNode root = new GNode();
		GNode node1 = new GNode();
		GNode node2 = new GNode();
		GNode node3 = new GNode();

		// Create a tree from the nodes
		root.addChild(node1);
		root.addChild(node2);
		node1.addChild(node3);

		// Give the provider some input
		provider.inputChanged(new FXViewer(
				new Composite(new Shell(Display.getDefault()), SWT.NONE)), root,
				new GNode());

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

	/**
	 * Check that the content provider is correctly validating its input.
	 */
	@Test
	public void checkValidation() {

		// Create a provider
		FXContentProvider provider = new FXContentProvider();

		// Try sending in an invalid viewer
		try {
			provider.inputChanged(
					new TestViewer(
							new Composite(new Shell(new Display()), SWT.NONE)),
					new GNode(), new GNode());

			// If we made it here, then the provider accepted the invalid input
			// without throwing an error.
			fail();
		} catch (Exception e) {
			// If we made it here, then an exception was thrown. Continue the
			// test.
		}

		// Try using invalid nodes
		try {
			provider.inputChanged(
					new TestViewer(
							new Composite(new Shell(new Display()), SWT.NONE)),
					new TestNode(), new GNode());

			// If we made it here, then the provider accepted the invalid input
			// without throwing an error.
			fail();
		} catch (Exception e) {
			// If we made it here, then an exception was thrown. Continue the
			// test.
		}

		// Try sending in an invalid viewer
		try {
			provider.inputChanged(
					new TestViewer(
							new Composite(new Shell(new Display()), SWT.NONE)),
					new GNode(), new TestNode());

			// If we made it here, then the provider accepted the invalid input
			// without throwing an error.
			fail();
		} catch (Exception e) {
			// If we made it here, then an exception was thrown. Continue the
			// test.
		}

	}

	/**
	 * A dummy implementation of INode for testing.
	 * 
	 * @author Robert Smith
	 *
	 */
	private class TestNode implements INode {

		@Override
		public INode getParent() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setParent(INode parent) {
			// TODO Auto-generated method stub

		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void addChild(INode node) {
			// TODO Auto-generated method stub

		}

		@Override
		public void removeChild(INode node) {
			// TODO Auto-generated method stub

		}

		@Override
		public void removeAllChildren() {
			// TODO Auto-generated method stub

		}

		@Override
		public List<INode> getChildren(boolean copy) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setVisible(boolean visible) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean isVisible() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void attach(IAttachment attachment) {
			// TODO Auto-generated method stub

		}

		@Override
		public void detach(IAttachment attachment) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean hasAttachment(Class<?> attachmentClass) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean supports(IAttachment attachment) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Map<Class<?>, List<IAttachment>> getAttachments() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<IAttachment> getAttachments(Class<?> clazz) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Map<String, Object> getProperties() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object getProperty(String key) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setProperty(String key, Object value) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean hasProperty(String key) {
			// TODO Auto-generated method stub
			return false;
		}

	}

	/**
	 * A dummy AbstractViewer for testing.
	 * 
	 * @author Robert Smith
	 *
	 */
	private class TestViewer extends BasicViewer {

		public TestViewer(Composite parent) {
			super(parent);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void createControl(Composite parent) {
			// TODO Auto-generated method stub

		}

		@Override
		protected void updateCamera(ICamera camera) {
			// TODO Auto-generated method stub

		}

		@Override
		public Group getRoot() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Control getControl() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void refresh() {
			// TODO Auto-generated method stub

		}

	}
}
