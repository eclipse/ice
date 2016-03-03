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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.eavp.viz.service.javafx.canvas.BasicAttachmentManager;
import org.eclipse.eavp.viz.service.javafx.canvas.FXAttachment;
import org.eclipse.eavp.viz.service.javafx.scene.base.GNode;
import org.eclipse.eavp.viz.service.javafx.scene.model.IAttachment;
import org.eclipse.eavp.viz.service.modeling.BasicController;
import org.eclipse.eavp.viz.service.modeling.BasicMesh;
import org.eclipse.eavp.viz.service.modeling.BasicView;
import org.junit.Test;

/**
 * A class to test the functionality of the FXAttachment.
 * 
 * @author Robert Smith
 *
 */
public class FXAttachmentTester {

	/**
	 * Test that the Attachment is correctly handling the JavaFX data
	 * structures.
	 */
	@Test
	public void checkGeometry() {

		// Create the attachment
		FXAttachment attachment = new FXAttachment(
				new BasicAttachmentManager() {

					@Override
					public IAttachment allocate() {
						// TODO Auto-generated method stub
						return null;
					}

				});

		// Give the attachment a root IController
		attachment.addGeometry(
				new BasicController(new BasicMesh(), new BasicView()));

		// Create a node for it to attach to
		GNode node = new GNode();
		node.attach(attachment);

		// Test that the attachment's visibility can be toggled
		attachment.setVisible(false);
		assertFalse(attachment.getFxNode().isVisible());
		attachment.setVisible(true);
		assertTrue(attachment.getFxNode().isVisible());

		// Add a shape without a representation.
		BasicController empty = new BasicController(new BasicMesh(),
				new BasicView());
		attachment.addGeometry(empty);
		assertTrue(attachment.getKnownParts().contains(empty));

	}
}
