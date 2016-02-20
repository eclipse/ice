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
package org.eclipse.eavp.viz.service.javafx.geometry.test;

import org.eclipse.eavp.viz.service.javafx.geometry.FXGeometryAttachment;
import org.eclipse.eavp.viz.service.javafx.geometry.FXGeometryAttachmentManager;
import org.eclipse.eavp.viz.service.javafx.geometry.datatypes.FXShapeController;
import org.eclipse.eavp.viz.service.javafx.geometry.datatypes.FXShapeView;
import org.eclipse.eavp.viz.service.modeling.ShapeMesh;
import org.junit.Test;

/**
 * A class to test the functionality of FXGeometryAttachment.
 * 
 * @author Robert Smith
 *
 */
public class FXGeometryAttachmentTester {

	/**
	 * Checks that the attachment handles updates correctly.
	 */
	@Test
	public void checkUpdates() {

		// Create an attachment
		FXGeometryAttachment attachment = new FXGeometryAttachment(
				new FXGeometryAttachmentManager());

		// Create a sphere shape
		ShapeMesh mesh = new ShapeMesh();
		mesh.setProperty("Type", "Sphere");
		FXShapeController shape = new FXShapeController(mesh,
				new FXShapeView(mesh));

		// Set the shape to the attachment
		attachment.addGeometry(shape);

		// The attachment's JavaFX node should now contain the shape's JavaFX
		// representation
		attachment.getFxNode().getChildren()
				.contains(shape.getRepresentation());
	}
}
