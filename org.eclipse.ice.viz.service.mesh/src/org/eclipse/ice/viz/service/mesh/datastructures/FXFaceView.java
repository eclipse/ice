/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Robert Smith
 *******************************************************************************/
package org.eclipse.ice.viz.service.mesh.datastructures;

import org.eclipse.ice.viz.service.modeling.AbstractMeshComponent;
import org.eclipse.ice.viz.service.modeling.AbstractView;

import javafx.scene.Group;

/**
 * A class which provides and manages a simple empty node for a JavaFX part,
 * under which the face's children that have graphical representations (such as
 * edges and vertices), can be grouped.
 * 
 * @author Robert Smith
 *
 */
public class FXFaceView extends AbstractView {

	/**
	 * The node which will contain the polygon's children.
	 */
	Group node;

	/**
	 * The default constructor.
	 * 
	 * @param model
	 */
	public FXFaceView(AbstractMeshComponent model) {
		super();

		// // Instantiate the node
		// node = new Group();
		//
		// // Set the initial graphical representations
		// refresh(model);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.viz.service.modeling.AbstractView#getRepresentation()
	 */
	@Override
	public Object getRepresentation() {
		return node;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.viz.service.modeling.AbstractView#refresh(org.eclipse.ice
	 * .viz.service.modeling.AbstractMeshComponent)
	 */
	@Override
	public void refresh(AbstractMeshComponent model) {

		// // Clear the list of children so it can be rebuilt
		// node.getChildren().clear();
		//
		// // Add each edge to the node
		// for (AbstractController edge : model.getEntitiesByCategory("Edges"))
		// {
		// node.getChildren().add((Node) edge.getRepresentation());
		// }
		//
		// // Add each vertex to the node
		// for (AbstractController vertex : model
		// .getEntitiesByCategory("Vertices")) {
		// if (!node.getChildren().contains(vertex.getRepresentation())) {
		// node.getChildren().add((Node) vertex.getRepresentation());
		// }
		// }
	}
}
