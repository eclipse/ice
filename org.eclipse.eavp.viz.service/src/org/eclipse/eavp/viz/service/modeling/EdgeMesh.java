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

import org.eclipse.eavp.viz.service.datastructures.VizObject.IManagedUpdateableListener;
import org.eclipse.eavp.viz.service.datastructures.VizObject.SubscriptionType;

/**
 * A mesh component representing a line between two Vertices.
 * 
 * @author Robert Smith
 */
public class EdgeMesh extends AbstractMesh {

	/**
	 * The edge's length.
	 */
	public double length;

	/**
	 * The default constructor.
	 */
	public EdgeMesh() {
		super();
	}

	/**
	 * The default constructor. It creates an edge between the two specified
	 * vertices.
	 * 
	 * @param start
	 * @param end
	 */
	public EdgeMesh(VertexController start, VertexController end) {
		super();

		// Add the vertices to the list of entities.
		addEntityByCategory(start, "Vertices");
		addEntityByCategory(end, "Vertices");
	}

	/**
	 * Gets the edge's length
	 * 
	 * @return The edge's length
	 */
	public double getLength() {
		return length;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.modeling.AbstractMeshComponent#
	 * addEntityByCategory(org.eclipse.eavp.viz.service.modeling.
	 * AbstractController, java.lang.String)
	 */
	@Override
	public void addEntityByCategory(AbstractController entity,
			String category) {

		// When a vertex is added, take action to ensure the edge maintains a
		// proper state
		if ("Vertices".equals(category)) {

			// The number of vertices
			List<AbstractController> vertices = entities.get("Vertices");
			int verticesNum = (vertices != null ? vertices.size() : 0);

			// If the object is a vertex and the edge already has both vertices,
			// fail silently.
			if (verticesNum >= 2) {
				return;
			}

			// Add the entity
			super.addEntityByCategory(entity, category);

			// If this was the second vertex, calculate the edge's new length.
			if (verticesNum == 1) {
				length = calculateLength();
			}
		}

		// Otherwise, add the entity normally
		else {
			super.addEntityByCategory(entity, category);
		}
	}

	/**
	 * Calculates the length of the edge, saving the value in this object as
	 * well as returning it to the caller. This method does nothing by default,
	 * and is intended to be overwritten by subclasses.
	 * 
	 * @return The edge's current length
	 */
	public double calculateLength() {

		length = 0;

		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.modeling.AbstractMeshComponent#register(org.
	 * eclipse.ice.viz.service.datastructures.VizObject.IVizUpdateableListener)
	 */
	@Override
	public void register(IManagedUpdateableListener listener) {

		// Do not register the edge's vertices, as the edge will listen to them
		// instead
		List<AbstractController> vertices = entities.get("Vertices");
		if (vertices == null || !entities.get("Vertices").contains(listener)) {
			super.register(listener);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.modeling.AbstractMeshComponent#setController(
	 * org.eclipse.eavp.viz.service.modeling.AbstractController)
	 */
	@Override
	public void setController(AbstractController controller) {
		super.setController(controller);

		// Give a reference to the controller to the edge's vertices
		List<AbstractController> vertices = entities.get("Vertices");

		if (vertices != null) {
			for (AbstractController vertex : vertices) {
				vertex.addEntity(controller);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.modeling.AbstractMeshComponent#clone()
	 */
	@Override
	public Object clone() {

		// Create a new object
		EdgeMesh clone = new EdgeMesh();

		// Make it a copy of this and return it
		clone.copy(this);
		return clone;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.modeling.AbstractMeshComponent#copy(org.
	 * eclipse.ice.viz.service.modeling.AbstractMeshComponent)
	 */
	@Override
	public void copy(AbstractMesh otherObject) {

		// Queue messages from all the vertices being added
		updateManager.enqueue();

		// Clone each child entity
		for (String category : otherObject.entities.keySet()) {
			for (AbstractController entity : otherObject
					.getEntitiesByCategory(category)) {
				addEntityByCategory((AbstractController) entity.clone(),
						category);
			}
		}

		// Copy each of the other component's data members
		type = otherObject.type;
		properties = new HashMap<String, String>(otherObject.properties);

		// Calculate the new length
		calculateLength();

		// Notify listeners of the change
		SubscriptionType[] eventTypes = { SubscriptionType.ALL };
		updateManager.notifyListeners(eventTypes);

		// Release all queued messages
		updateManager.flushQueue();
	}

	/**
	 * Gets the location for the edge's first vertex.
	 *
	 * @return A list of the vertex's 3D coordinates
	 */
	public double[] getStartLocation() {
		List<AbstractController> vertices = getEntitiesByCategory("Vertices");
		return (vertices != null && !vertices.isEmpty())
				? ((VertexController) vertices.get(0)).getLocation()
				: new double[3];
	}

	/**
	 * Gets the location for the edge's second vertex
	 * 
	 * @return A list of the vertex's 3D coordinates
	 */
	public double[] getEndLocation() {
		List<AbstractController> vertices = getEntitiesByCategory("Vertices");
		return (vertices != null && vertices.size() > 1)
				? ((VertexController) vertices.get(1)).getLocation()
				: new double[3];
	}
}
