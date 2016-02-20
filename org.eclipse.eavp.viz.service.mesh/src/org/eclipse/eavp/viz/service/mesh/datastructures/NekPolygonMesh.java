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
package org.eclipse.eavp.viz.service.mesh.datastructures;

import java.util.List;

import org.eclipse.eavp.viz.service.modeling.AbstractController;
import org.eclipse.eavp.viz.service.modeling.EdgeAndVertexFaceMesh;
import org.eclipse.eavp.viz.service.modeling.FaceEdgeController;

/**
 * A component for Faces within the Mesh Editor, making use of FaceEdges to
 * maintain a traversable list from edges to faces.
 * 
 * @author Robert Smith
 *
 */
public class NekPolygonMesh extends EdgeAndVertexFaceMesh {

	/**
	 * The default constructore
	 */
	public NekPolygonMesh() {
		super();
	}

	/**
	 * A constructor for specifying the child entities.
	 * 
	 * @param entities
	 *            The child entities comprising the face
	 */
	public NekPolygonMesh(List<AbstractController> entities) {
		super(entities);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.modeling.AbstractMeshComponent#addEntity(org.
	 * eclipse.ice.viz.service.modeling.AbstractController)
	 */
	@Override
	public void addEntityByCategory(AbstractController entity,
			String category) {

		// If adding an edge, handle it apprioriately
		if ("Edges".equals(category)) {

			// Queue messages from adding the entity
			updateManager.enqueue();

			// Add the edge to the Edges category by default
			super.addEntityByCategory(entity, category);

			// If the controller already exists, give a reference to it to the
			// edge.
			if (entity instanceof FaceEdgeController && controller != null) {
				entity.addEntityByCategory(controller, "Faces");
			}

			// Send own update along with the new edge's, if there was one
			updateManager.flushQueue();

		} else {
			super.addEntityByCategory(entity, category);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 *
	 * org.eclipse.eavp.viz.service.modeling.AbstractMeshComponent#setController(
	 * org.eclipse.eavp.viz.service.modeling.AbstractController)
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
				edge.addEntityByCategory(controller, "Edges");
			}

			// Send messages from all changed edges
			updateManager.flushQueue();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.modeling.AbstractMeshComponent#clone()
	 */
	@Override
	public Object clone() {
		// Create a new component, and make it a copy of this one.
		NekPolygonMesh clone = new NekPolygonMesh();
		clone.copy(this);
		return clone;
	}

}
