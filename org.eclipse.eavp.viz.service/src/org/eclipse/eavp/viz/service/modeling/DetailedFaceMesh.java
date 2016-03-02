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

import java.util.HashMap;
import java.util.List;

import org.eclipse.eavp.viz.service.datastructures.VizObject.SubscriptionType;

/**
 * A Face component which keeps both its Edges and Vertices as its child
 * entities.
 *
 * @author Robert Smith
 */
public class DetailedFaceMesh extends FaceMesh {

	/**
	 * The default constructor
	 */
	public DetailedFaceMesh() {
		super();
	}

	/**
	 * A constructor taking a list of entities.
	 * 
	 * @param entities
	 */
	public DetailedFaceMesh(List<IController> entities) {
		super(entities);
	}

	/**
	 * An implementation of addEntity that adds both the Edge and its Vertices.
	 * 
	 */
	@Override
	public void addEntityToCategory(IController newEntity,
			IMeshCategory category) {

		// If the new entity is an edge, add it and its vertices under the
		// appropriate categories.
		if (MeshCategory.EDGES.equals(category)) {

			// Queue updates for all the new children
			updateManager.enqueue();

			// Add the edge under the Edges category
			super.addEntityToCategory(newEntity, MeshCategory.EDGES);

			// Add each of the edge's vertices under the "Vertices" category
			for (IController vertex : newEntity
					.getEntitiesFromCategory(MeshCategory.VERTICES)) {
				super.addEntityToCategory(vertex, MeshCategory.VERTICES);
			}

			// Send notifications for all children
			updateManager.flushQueue();
		}

		// Otherwise, add the entity normally
		super.addEntityToCategory(newEntity, category);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.modeling.AbstractMesh#removeEntity(org.
	 * eclipse.ice.viz.service.modeling.AbstractController)
	 */
	@Override
	public void removeEntity(IController entity) {

		// Ignore requests to remove a vertex, to keep the Vertex category
		// consistent with the Edges category
		if (!getEntitiesFromCategory(MeshCategory.VERTICES).contains(entity)) {

			// Queue messages from all the removals
			updateManager.enqueue();

			// If the entity is an edge, also remove its vertices
			if (getEntitiesFromCategory(MeshCategory.EDGES).contains(entity)) {
				for (IController vertex : entity
						.getEntitiesFromCategory(MeshCategory.VERTICES)) {

					// Whether or not the vertex is incident upon another edge
					boolean found = false;

					// Search all the other edges to see if any of them have
					// this vertex
					for (IController edge : getEntitiesFromCategory(
							MeshCategory.EDGES)) {
						if (edge != entity && edge
								.getEntitiesFromCategory(MeshCategory.VERTICES)
								.contains(vertex)) {
							found = true;
							break;
						}
					}

					// If no other edge had this vertex, remove it
					if (!found) {
						super.removeEntity(vertex);
					}
				}
			}

			// Remove the entity as normal
			super.removeEntity(entity);

			// Flush the message queue
			updateManager.flushQueue();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() {

		// Create a new component, and make it a copy of this one.
		DetailedFaceMesh clone = new DetailedFaceMesh();
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
		if (otherObject instanceof DetailedFaceMesh) {

			// Cast the object
			DetailedFaceMesh castObject = (DetailedFaceMesh) otherObject;

			// Queue messages from the new edges added
			updateManager.enqueue();

			// Create clones of all the vertices. This should be done first, so
			// the copies can be used to construct the edges
			for (IController entity : otherObject
					.getEntitiesFromCategory(MeshCategory.VERTICES)) {
				addEntityToCategory(
						(VertexController) ((BasicController) entity)
								.clone(),
						MeshCategory.VERTICES);
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
							for (IController vertex : entities
									.get(MeshCategory.VERTICES))
								if (tempVertex.equals(vertex)) {
									newEdge.addEntityToCategory(vertex,
											MeshCategory.VERTICES);
								}

						}

						// Save the cloned edge to the map
						addEntityToCategory(newEdge, MeshCategory.EDGES);

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