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
package org.eclipse.ice.viz.service.javafx.canvas.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.ice.viz.service.javafx.canvas.AbstractAttachmentManager;
import org.eclipse.ice.viz.service.javafx.canvas.FXAttachment;
import org.eclipse.ice.viz.service.javafx.internal.Util;
import org.eclipse.ice.viz.service.javafx.scene.base.GNode;
import org.eclipse.ice.viz.service.javafx.scene.model.IAttachment;
import org.eclipse.ice.viz.service.modeling.AbstractController;
import org.eclipse.ice.viz.service.modeling.AbstractMesh;
import org.eclipse.ice.viz.service.modeling.AbstractView;
import org.eclipse.ice.viz.service.modeling.ShapeMesh;
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
				new AbstractAttachmentManager() {

					@Override
					public IAttachment allocate() {
						// TODO Auto-generated method stub
						return null;
					}

				});

		// Create a node for it to attach to
		GNode node = new GNode();
		attachment.attach(node);

		// The node's group should now have the attachment's group as a child
		assertTrue(Util.getFxGroup(node).getChildren()
				.contains(attachment.getFxNode()));

		// Test that the attachment's visibility can be toggled
		attachment.setVisible(false);
		assertFalse(attachment.getFxNode().isVisible());
		attachment.setVisible(true);
		assertTrue(attachment.getFxNode().isVisible());

		// Add a shape without a representation.
		AbstractController empty = new AbstractController(new AbstractMesh(),
				new AbstractView());
		attachment.addGeometry(empty);
		assertTrue(attachment.getKnownParts().contains(empty));

		ShapeMesh mesh = new ShapeMesh();
		mesh.setProperty("Type", "Cube");
		FXShapeController controller = new FXShapeController(mesh,
				new FXShapeView(mesh));
	}
}
