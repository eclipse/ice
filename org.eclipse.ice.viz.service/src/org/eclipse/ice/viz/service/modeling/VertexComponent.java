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
package org.eclipse.ice.viz.service.modeling;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ice.viz.service.datastructures.VizObject.UpdateableSubscriptionType;

/**
 * A point which specifically serves as the endpoint for one or more Edges. It
 * maintains each edge it is associated with in its entities map.
 * 
 * @author Robert Smith
 *
 */
public class VertexComponent extends PointComponent {

	/**
	 * The default constructor
	 */
	public VertexComponent() {
		super();
	}

	/**
	 * A constructor specifying the vertex's location
	 * 
	 * @param x
	 *            The vertex's x coordinate
	 * @param y
	 *            The vertex's y coordinate
	 * @param z
	 *            The vertex's z coordinate
	 */
	public VertexComponent(double x, double y, double z) {
		super(x, y, z);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 *
	 * org.eclipse.ice.viz.service.modeling.AbstractMeshComponent#addEntity(org.
	 * eclipse.ice.viz.service.modeling.AbstractController)
	 */
	@Override
	public void addEntity(AbstractController entity) {

		// If not specified, assume all edges go in the Edges category
		if (entity instanceof Edge) {
			addEntityByCategory(entity, "Edges");
		} else {
			super.addEntity(entity);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ice.viz.service.modeling.AbstractMeshComponent#
	 * addEntityByCategory(org.eclipse.ice.viz.service.modeling.
	 * AbstractController, java.lang.String)
	 */
	@Override
	public void addEntityByCategory(AbstractController entity,
			String category) {

		// If the category is Edges, do not register as a listener, as the edge
		// is already listening to this
		if ("Edges".equals(category)) {
			// Get the entities for the given category
			List<AbstractController> catList = entities.get(category);

			// If the list is empty, make an empty one
			if (catList == null) {
				catList = new ArrayList<AbstractController>();
			}

			// Add the entity to the list and put it in the map
			catList.add(entity);
			entities.put(category, catList);

			UpdateableSubscriptionType[] eventTypes = {
					UpdateableSubscriptionType.Child };
			updateManager.notifyListeners(eventTypes);
		}

		// Otherwise, add the entity normally
		else {
			super.addEntityByCategory(entity, category);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.viz.service.modeling.AbstractMeshComponent#equals(java.
	 * lang.Object)
	 */
	@Override
	public boolean equals(Object otherObject) {

		// Check if the objects are the same
		if (this == otherObject) {
			return true;
		}

		// Check if the other object is an AbstractMeshComponent and cast it
		if (!(otherObject instanceof VertexComponent)) {
			return false;
		}

		VertexComponent castObject = (VertexComponent) otherObject;

		// Check the types, properties, and entity category for equality
		if (type != castObject.type || !properties.equals(castObject.properties)
				|| !entities.keySet().equals(castObject.entities.keySet())) {
			return false;
		}

		// Check that the positions are equal
		if (x != castObject.getX() || y != castObject.getY()
				|| z != castObject.getZ()) {
			return false;
		}

		// For each category, check that the two objects' lists of child
		// entities in that category are equal.
		for (String category : entities.keySet()) {

			// Do not require that the vertices be used in the same edges in
			// order for them to be equal.
			if (!"Edges".equals(category)) {
				if (!entities.get(category)
						.containsAll(castObject.entities.get(category))
						|| !castObject.entities.get(category)
								.containsAll(entities.get(category))) {
					return false;
				}
			}
		}

		// All checks passed, so the objects are equal
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = type.hashCode();
		for (String category : entities.keySet()) {

			// Ignore the Edges to prevent circular hashing
			if ("Edges".equals(category)) {
				continue;
			}

			for (AbstractController entity : getEntitiesByCategory(category)) {
				hash += entity.hashCode();
			}
		}
		hash += properties.hashCode();
		hash += x;
		hash += y;
		hash += z;
		return hash;
	}
}
