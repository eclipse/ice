/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Tony McCrary (tmccrary@l33tlabs.com), Robert Smith
 *******************************************************************************/
package org.eclipse.eavp.viz.service.javafx.mesh;

import org.eclipse.eavp.viz.service.javafx.canvas.BasicAttachmentManager;
import org.eclipse.eavp.viz.service.javafx.canvas.FXAttachment;
import org.eclipse.eavp.viz.service.modeling.IController;
import org.eclipse.eavp.viz.service.modeling.Representation;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

/**
 * <p>
 * JavaFX implementation of GeometryAttachment.
 * </p>
 * 
 * @author Tony McCrary (tmccrary@l33tlabs.com), Robert Smith
 *
 */
public class FXMeshAttachment extends FXAttachment {

	/**
	 * A box displayed behind the rest of the scene.
	 */
	private Box background;

	/**
	 * <p>
	 * Creates an FXGeometryAttachment instance.
	 * </p>
	 * 
	 * @param manager
	 *            the manager that created this instance.
	 */
	public FXMeshAttachment(BasicAttachmentManager manager) {
		super(manager);

		// Create a grey background box
		background = new Box(96, 48, 1);
		PhongMaterial backgroundMaterial = new PhongMaterial();
		backgroundMaterial.setDiffuseColor(Color.GRAY);
		background.setMaterial(backgroundMaterial);
	}

	@Override
	protected void handleUpdate(IController source) {

		// On update, refresh the list of top level nodes
		fxAttachmentNode.getChildren().clear();

		// For each group which has been added to the attachment
		for (IController group : knownParts) {

			// Get each part which is managed by that controller
			for (IController entity : group.getEntities()) {

				// Add the entity's own representation, if it has one and is not
				// already present in the scene
				Representation<Group> representation = entity
						.getRepresentation();
				if (representation.getData() != null && !fxAttachmentNode
						.getChildren().contains(representation.getData())) {
					fxAttachmentNode.getChildren()
							.add(representation.getData());
				}

				// Add each child of a polygon to the scene, without repeats
				for (IController child : entity.getEntities()) {

					Representation<Group> childRepresentation = child
							.getRepresentation();
					Group render = childRepresentation.getData();

					if (!fxAttachmentNode.getChildren().contains(render)) {
						// Add the representation to the scene's node
						fxAttachmentNode.getChildren().add(render);
					}
				}
			}
		}

		// Add the background
		fxAttachmentNode.getChildren().add(background);
	}

}
