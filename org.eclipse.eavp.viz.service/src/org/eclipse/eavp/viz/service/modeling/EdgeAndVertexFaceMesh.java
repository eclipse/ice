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
public class EdgeAndVertexFaceMesh extends FaceMesh {

	/**
	 * The default constructor
	 */
	public EdgeAndVertexFaceMesh() {
		super();
	}

	/**
	 * A constructor taking a list of entities.
	 * 
	 * @param entities
	 */
	public EdgeAndVertexFaceMesh(List<AbstractController> entities) {
		super(entities);
	}

	/**
	 * An implementation of addEntity that adds both the Edge and its Vertices.
	 * 
	 */
	@Override
	public void addEntityByCategory(AbstractController newEntity,
			String category) {

		// If the new entity is an edge, add it and its vertices under the
		// appropriate categories.
		if ("Edges".equals(category)) {

			// Queue updates for all the new children
			updateManager.enqueue();

			// Add the edge under the Edges category
			super.addEntityByCategory(newEntity, "Edges");

			// Add each of the edge's vertices under the "Vertices" category
			for (AbstractController vertex : newEntity
					.getEntitiesByCategory("Vertices")) {
				super.addEntityByCategory(vertex, "Vertices");
			}

			// Send notifications for all children
			updateManager.flushQueue();
		}

		// Otherwise, add the entity normally
		super.addEntityByCategory(newEntity, category);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.modeling.AbstractMesh#removeEntity(org.
	 * eclipse.ice.viz.service.modeling.AbstractController)
	 */
	@Override
	public void removeEntity(AbstractController entity) {

		// Ignore requests to remove a vertex, to keep the Vertex category
		// consistent with the Edges category
		if (!getEntitiesByCategory("Vertices").contains(entity)) {

			// Queue messages from all the removals
			updateManager.enqueue();

			// If the entity is an edge, also remove its vertices
			if (getEntitiesByCategory("Edges").contains(entity)) {
				for (AbstractController vertex : entity
						.getEntitiesByCategory("Vertices")) {

					// Whether or not the vertex is incident upon another edge
					boolean found = false;

					// Search all the other edges to see if any of them have
					// this vertex
					for (AbstractController edge : getEntitiesByCategory(
							"Edges")) {
						if (edge != entity
								&& edge.getEntitiesByCategory("Vertices")
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
		EdgeAndVertexFaceMesh clone = new EdgeAndVertexFaceMesh();
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
	public void copy(AbstractMesh otherObject) {

		// Copy only if the other object is an EdgeAndVertexFaceComponent
		if (otherObject instanceof EdgeAndVertexFaceMesh) {

			// Queue messages from the new edges added
			updateManager.enqueue();

			// Create clones of all the vertices. This should be done first, so
			// the copies can be used to construct the edges
			for (AbstractController entity : otherObject
					.getEntitiesByCategory("Vertices")) {
				addEntityByCategory((VertexController) entity.clone(),
						"Vertices");
			}

			// Deep copy each category of child entities
			for (String category : otherObject.entities.keySet()) {

				// Clone each edge, making use of the vertices clones above as
				// their endpoints.
				if ("Edges".equals(category)) {

					// Copy each edge
					for (AbstractController edge : otherObject
							.getEntitiesByCategory(category)) {

						// Create a clone of the edge
						EdgeController newEdge = (EdgeController) edge.clone();

						// Get the clone's vertices
						List<AbstractController> tempVertices = edge
								.getEntitiesByCategory("Vertices");

						// Remove the vertices from the cloned edge and add an
						// equivalent one in their place
						for (AbstractController tempVertex : tempVertices) {
							newEdge.removeEntity(tempVertex);

							// Search the copied vertices from above for the
							// equivalent vertices which should belong to the
							// edge.
							for (AbstractController vertex : entities
									.get("Vertices"))
								if (tempVertex.equals(vertex)) {
									newEdge.addEntityByCategory(vertex,
											"Vertices");
								}

						}

						// Save the cloned edge to the map
						addEntityByCategory(newEdge, "Edges");

					}
				}

				// Vertices were copied above, so ignore them
				else if ("Vertices".equals(category)) {
					continue;
				}

				// For other categories, clone all the child entities
				else {
					for (AbstractController entity : otherObject
							.getEntitiesByCategory(category)) {
						addEntityByCategory((EdgeController) entity.clone(),
								category);
					}
				}
			}

			// Copy the rest of the object data
			// Copy each of the other component's data members
			type = otherObject.type;
			properties = new HashMap<String, String>(otherObject.properties);

			// Notify listeners of the change
			SubscriptionType[] eventTypes = { SubscriptionType.ALL };
			updateManager.notifyListeners(eventTypes);

			// Fire an update
			updateManager.flushQueue();
		}
	}
}