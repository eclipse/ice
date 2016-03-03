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

import org.eclipse.eavp.viz.service.modeling.DetailedFaceMesh;
import org.eclipse.eavp.viz.service.modeling.DetailedEdgeController;
import org.eclipse.eavp.viz.service.modeling.IController;
import org.eclipse.eavp.viz.service.modeling.IController;
import org.eclipse.eavp.viz.service.modeling.IMeshCategory;
import org.eclipse.eavp.viz.service.modeling.MeshCategory;

/**
 * A component for Faces within the Mesh Editor, making use of FaceEdges to
 * maintain a traversable list from edges to faces.
 * 
 * @author Robert Smith
 *
 */
public class NekPolygonMesh extends DetailedFaceMesh {

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
	public NekPolygonMesh(List<IController> entities) {
		super(entities);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.modeling.AbstractMeshComponent#addEntity(
	 * org. eclipse.ice.viz.service.modeling.IController)
	 */
	@Override
	public void addEntityToCategory(IController entity,
			IMeshCategory category) {

		// If adding an edge, handle it apprioriately
		if (MeshCategory.EDGES.equals(category)) {

			// Queue messages from adding the entity
			updateManager.enqueue();

			// Add the edge to the Edges category by default
			super.addEntityToCategory(entity, category);

			// If the controller already exists, give a reference to it to the
			// edge.
			if (entity instanceof DetailedEdgeController && controller != null) {
				entity.addEntityToCategory(controller, MeshCategory.FACES);
			}

			// Send own update along with the new edge's, if there was one
			updateManager.flushQueue();

		} else {
			super.addEntityToCategory(entity, category);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 *
	 * org.eclipse.eavp.viz.service.modeling.AbstractMeshComponent#
	 * setController( org.eclipse.eavp.viz.service.modeling.IController)
	 */
	@Override
	public void setController(IController controller) {
		super.setController(controller);

		// Give a reference to the controller to the edge's faces
		List<IController> edges = entities.get(MeshCategory.EDGES);
		if (edges != null) {

			// Queue messages from all edges
			updateManager.enqueue();

			for (IController edge : edges) {
				edge.addEntityToCategory(controller, MeshCategory.EDGES);
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
