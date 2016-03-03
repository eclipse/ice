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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.eclipse.eavp.viz.service.javafx.canvas.BasicAttachment;
import org.eclipse.eavp.viz.service.javafx.canvas.BasicAttachmentManager;
import org.eclipse.eavp.viz.service.javafx.canvas.FXAttachment;
import org.eclipse.eavp.viz.service.javafx.scene.model.IAttachment;
import org.eclipse.eavp.viz.service.modeling.IController;
import org.junit.Test;

/**
 * A class for testing the functionality of the AbstractAttachmentManager.
 * 
 * @author Robert Smith
 *
 */
public class BasicAttachmentManagerTester {

	/**
	 * Test the manager's ability to properly handle its attachments.
	 */
	@Test
	public void checkAttachments() {

		// Create a manager that creates AbstractAttachments
		BasicAttachmentManager manager = new TestAttachmentManager() {

			@Override
			public IAttachment allocate() {

				// Create a new attachment
				BasicAttachment attachment = new BasicAttachment() {

					@Override
					public void removeGeometry(IController geom) {
						// TODO Auto-generated method stub

					}

					@Override
					public Class<?> getType() {
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					protected void processShape(IController shape) {
						// TODO Auto-generated method stub

					}

					@Override
					protected void disposeShape(IController shape) {
						// TODO Auto-generated method stub

					}

				};

				// Add it to the list of active attachments and return it
				active.add(attachment);
				return attachment;
			}

		};

		// The manager should start off without attachments
		assertTrue(manager.getAttachments().isEmpty());

		// Allocate a new attachment and check that it is there
		manager.allocate();
		assertFalse(manager.getAttachments().isEmpty());

		// Allocate a second attachment and check that both are contained by the
		// manager
		IAttachment attachment = manager.allocate();
		assertEquals(2, manager.getAttachments().size());

		// Order the manager to destroy the attachment.
		manager.destroy(attachment);
		assertEquals(1, manager.getAttachments().size());

		// Try destroying a missing attachment and updating with nothing to
		// delete. Nothing should happen.
		manager.destroy(attachment);
		manager.update();
		assertEquals(1, manager.getAttachments().size());

	}

	/**
	 * A simple implementation of AbstractAttachmentManager for testing.
	 * 
	 * @author Robert Smith
	 *
	 */
	private class TestAttachmentManager extends BasicAttachmentManager {

		/**
		 * The default constructor.
		 */
		public TestAttachmentManager() {
			active = new ArrayList<IAttachment>();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.eavp.viz.service.javafx.canvas.AbstractAttachmentManager#
		 * allocate()
		 */
		@Override
		public IAttachment allocate() {

			IAttachment attachment = new FXAttachment(this);
			active.add(attachment);
			return attachment;
		}

	}
}
