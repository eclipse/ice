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

import org.eclipse.eavp.viz.service.datastructures.VizObject.SubscriptionType;

/**
 * A point which specifically serves as the endpoint for one or more Edges. It
 * maintains each edge it is associated with in its entities map.
 * 
 * @author Robert Smith
 *
 */
public class VertexMesh extends PointMesh {

	/**
	 * The default constructor
	 */
	public VertexMesh() {
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
	public VertexMesh(double x, double y, double z) {
		super(x, y, z);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 *
	 * org.eclipse.eavp.viz.service.modeling.AbstractMeshComponent#addEntity(
	 * org. eclipse.ice.viz.service.modeling.IController)
	 */
	@Override
	public void addEntity(IController entity) {

		// If not specified, assume all edges go in the Edges category
		if (entity instanceof EdgeController) {
			addEntityToCategory(entity, MeshCategory.EDGES);
		} else {
			super.addEntity(entity);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.eavp.viz.service.modeling.AbstractMeshComponent#
	 * addEntityByCategory(org.eclipse.eavp.viz.service.modeling. IController,
	 * java.lang.String)
	 */
	@Override
	public void addEntityToCategory(IController entity,
			IMeshCategory category) {

		// If the category is Edges, do not register as a listener, as the edge
		// is already listening to this
		if (MeshCategory.EDGES.equals(category)) {
			// Get the entities for the given category
			ArrayList<IController> catList = entities.get(category);

			// If the list is empty, make an empty one
			if (catList == null) {
				catList = new ArrayList<IController>();
			}

			// Add the entity to the list and put it in the map
			catList.add(entity);
			entities.put(category, catList);

			SubscriptionType[] eventTypes = { SubscriptionType.CHILD };
			updateManager.notifyListeners(eventTypes);
		}

		// Otherwise, add the entity normally
		else {
			super.addEntityToCategory(entity, category);
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
		VertexMesh clone = new VertexMesh();
		clone.copy(this);
		return clone;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.modeling.PointMesh#copy(org.eclipse.eavp.
	 * viz. service.modeling.AbstractMesh)
	 */
	@Override
	public void copy(IMesh otherObject) {

		// Fail silently if the other object is not a VertexMesh
		if (!(otherObject instanceof VertexMesh)) {
			return;
		}

		// Cast the object
		VertexMesh castObject = (VertexMesh) otherObject;

		// Copy each of the other component's data members
		type = castObject.type;
		properties = new HashMap<IMeshProperty, String>(castObject.properties);

		// Copy the coordinates
		x = ((PointMesh) otherObject).getX();
		y = ((PointMesh) otherObject).getY();
		z = ((PointMesh) otherObject).getZ();

		// Clone each child entity
		for (IMeshCategory category : castObject.entities.keySet()) {

			// Do not clone the edges containing the vertex
			if (!MeshCategory.EDGES.equals(category)) {
				for (IController entity : otherObject
						.getEntitiesFromCategory(category)) {
					addEntityToCategory(
							(IController) ((BasicController) entity).clone(),
							category);
				}
			}
		}

		// Notify listeners of the change
		SubscriptionType[] eventTypes = { SubscriptionType.PROPERTY };
		updateManager.notifyListeners(eventTypes);
	}
}
