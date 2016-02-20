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
public class FaceMesh extends AbstractMesh {

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
	public FaceMesh(List<AbstractController> entities) {
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
	public void copy(AbstractMesh otherObject) {

		// Copy only if the other object is an EdgeAndVertexFaceComponent
		if (otherObject instanceof FaceMesh) {

			// Queue messages from the new edges added
			updateManager.enqueue();

			// Create a list of shared vertices for use by the cloned edges
			List<AbstractController> sharedVertices = new ArrayList<AbstractController>();

			// Create clones of all the vertices. This should be done first, so
			// the copies can be used to construct the edges
			for (AbstractController edge : otherObject
					.getEntitiesByCategory("Edges")) {
				for (AbstractController vertex : edge
						.getEntitiesByCategory("Vertices")) {
					if (!sharedVertices.contains(vertex)) {
						sharedVertices.add(vertex);
					}
				}
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
							for (AbstractController vertex : sharedVertices)
								if (tempVertex.equals(vertex)) {
									newEdge.addEntity(vertex);
								}

						}

						// Save the cloned edge to the map
						addEntity(newEdge);

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
			SubscriptionType[] eventTypes = {
					SubscriptionType.ALL };
			updateManager.notifyListeners(eventTypes);

			// Fire an update
			updateManager.flushQueue();
		}
	}
}
