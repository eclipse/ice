/*******************************************************************************
 * Copyright (c) 2016 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Tony McCrary (tmccrary@l33tlabs.com)
 *******************************************************************************/
package org.eclipse.eavp.viz.service.javafx.canvas.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.eclipse.eavp.viz.service.javafx.canvas.Attachment;
import org.eclipse.eavp.viz.service.javafx.scene.model.IAttachment;
import org.eclipse.eavp.viz.service.javafx.scene.model.INode;
import org.junit.Test;

/**
 * A class to test the functionality of Attachment's default implementations.
 * 
 * @author Robert Smith
 *
 */
public class AttachmentTester {

	/**
	 * Test the attachment's ability to attach and detach from a node.
	 */
	@Test
	public void checkAttach() {

		// Create an attachment and node
		TestAttachment attachment = new TestAttachment();
		TestNode node = new TestNode();

		// The attachment should start off unattached
		assertTrue(attachment.getOwner() == null);

		// Attach to the node and check that the parent is properly set
		attachment.attach(node);
		assertTrue(attachment.getOwner() == node);

		// Detach from the node and check that the attachment is now parentless
		attachment.detach(node);
		assertFalse(attachment.getOwner() == node);

	}

	/**
	 * A basic extension of Attachment for testing purposes.
	 * 
	 * @author Robert Smith
	 *
	 */
	private class TestAttachment extends Attachment {

		@Override
		public Class<?> getType() {
			// TODO Auto-generated method stub
			return null;
		}
	}

	/**
	 * A basic implementation of INode.
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
}
