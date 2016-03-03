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
package org.eclipse.eavp.viz.service.modeling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.eavp.viz.service.datastructures.VizObject.SubscriptionType;

/**
 * A mesh component representing a polygon.
 * 
 * @author Robert Smith
 *
 */
public class FaceMesh extends BasicMesh {

	/**
	 * The default constructor.
	 */
	public FaceMesh() {
		super();
	}

	/**
	 * A constructor for specifying the child entities.
	 * 
	 * @param entities
	 *            The child entities comprising the face
	 */
	public FaceMesh(List<IController> entities) {
		super(entities);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() {

		// Create a new component, and make it a copy of this one.
		FaceMesh clone = new FaceMesh();
		clone.copy(this);
		return clone;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.modeling.FaceMesh#copy(org.eclipse.eavp.viz.
	 * service.modeling.AbstractMesh)
	 */
	@Override
	public void copy(IMesh otherObject) {

		// Copy only if the other object is an EdgeAndVertexFaceComponent
		if (otherObject instanceof FaceMesh) {

			// Cast the object
			FaceMesh castObject = (FaceMesh) otherObject;

			// Queue messages from the new edges added
			updateManager.enqueue();

			// Create a list of shared vertices for use by the cloned edges
			List<IController> sharedVertices = new ArrayList<IController>();

			// Create clones of all the vertices. This should be done first, so
			// the copies can be used to construct the edges
			for (IController edge : otherObject
					.getEntitiesFromCategory(MeshCategory.EDGES)) {
				for (IController vertex : edge
						.getEntitiesFromCategory(MeshCategory.VERTICES)) {
					if (!sharedVertices.contains(vertex)) {
						sharedVertices.add(vertex);
					}
				}

				edge.getEntitiesFromCategory(MeshCategory.CHILDREN);
			}

			// Deep copy each category of child entities
			for (IMeshCategory category : castObject.entities.keySet()) {

				// Clone each edge, making use of the vertices clones above as
				// their endpoints.
				if (MeshCategory.EDGES.equals(category)) {

					// Copy each edge
					for (IController edge : otherObject
							.getEntitiesFromCategory(category)) {

						// Create a clone of the edge
						EdgeController newEdge = (EdgeController) ((BasicController) edge)
								.clone();

						// Get the clone's vertices
						List<IController> tempVertices = edge
								.getEntitiesFromCategory(MeshCategory.VERTICES);

						// Remove the vertices from the cloned edge and add an
						// equivalent one in their place
						for (IController tempVertex : tempVertices) {
							newEdge.removeEntity(tempVertex);

							// Search the copied vertices from above for the
							// equivalent vertices which should belong to the
							// edge.
							for (IController vertex : sharedVertices)
								if (tempVertex.equals(vertex)) {
									newEdge.addEntity(vertex);
								}

						}

						// Save the cloned edge to the map
						addEntity(newEdge);

					}
				}

				// Vertices were copied above, so ignore them
				else if (MeshCategory.VERTICES.equals(category)) {
					continue;
				}

				// For other categories, clone all the child entities
				else {
					for (IController entity : otherObject
							.getEntitiesFromCategory(category)) {
						addEntityToCategory(
								(EdgeController) ((BasicController) entity)
										.clone(),
								category);
					}
				}
			}

			// Copy the rest of the object data
			// Copy each of the other component's data members
			type = castObject.type;
			properties = new HashMap<IMeshProperty, String>(
					castObject.properties);

			// Notify listeners of the change
			SubscriptionType[] eventTypes = { SubscriptionType.ALL };
			updateManager.notifyListeners(eventTypes);

			// Fire an update
			updateManager.flushQueue();
		}
	}
}
