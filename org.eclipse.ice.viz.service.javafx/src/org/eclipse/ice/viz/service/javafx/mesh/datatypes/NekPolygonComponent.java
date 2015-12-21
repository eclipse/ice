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
package org.eclipse.ice.viz.service.javafx.mesh.datatypes;

import java.util.List;

import org.eclipse.ice.viz.service.modeling.AbstractController;
import org.eclipse.ice.viz.service.modeling.Edge;
import org.eclipse.ice.viz.service.modeling.EdgeAndVertexFaceComponent;

/**
 * A component for Faces within the Mesh Editor, making use of FaceEdges to
 * maintain a traversable list from edges to faces.
 * 
 * @author Robert Smith
 *
 */
public class NekPolygonComponent extends EdgeAndVertexFaceComponent {

	/**
	 * The default constructore
	 */
	public NekPolygonComponent() {
		super();
	}

	/**
	 * A constructor for specifying the child entities.
	 * 
	 * @param entities
	 *            The child entities comprising the face
	 */
	public NekPolygonComponent(List<AbstractController> entities) {
		super(entities);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.viz.service.modeling.AbstractMeshComponent#addEntity(org.
	 * eclipse.ice.viz.service.modeling.AbstractController)
	 */
	@Override
	public void addEntity(AbstractController entity) {

		// If adding an edge, handle it apprioriately
		if (entity instanceof Edge) {

			// Queue messages from adding the entity
			updateManager.enqueue();

			// Add the edge to the Edges cagtegory by default
			super.addEntity(entity);

			// If the controller already exists, give a reference to it to the
			// edge.
			if (controller != null) {
				entity.addEntityByCategory(controller, "Faces");
			}

			// Send own update along with the new edge's, if there was one
			updateManager.flushQueue();

		} else {
			super.addEntity(entity);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 *
	 * org.eclipse.ice.viz.service.modeling.AbstractMeshComponent#setController(
	 * org.eclipse.ice.viz.service.modeling.AbstractController)
	 */
	@Override
	public void setController(AbstractController controller) {
		super.setController(controller);

		// Give a reference to the controller to the edge's faces
		List<AbstractController> edges = entities.get("Edges");
		if (edges != null) {

			// Queue messages from all edges
			updateManager.enqueue();

			for (AbstractController edge : edges) {
				edge.addEntity(controller);
			}

			// Send messages from all changed edges
			updateManager.flushQueue();
		}

	}

}
