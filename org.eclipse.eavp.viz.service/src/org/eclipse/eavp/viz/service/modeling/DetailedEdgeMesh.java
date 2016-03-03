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
import java.util.Set;

import org.eclipse.eavp.viz.service.datastructures.VizObject.SubscriptionType;

/**
 * A component representing an Edge for a Face, which keeps a list of all Faces
 * for which it is a boundary in its entities map.
 * 
 * @author Robert Smith
 *
 */
public class DetailedEdgeMesh extends EdgeMesh {

	/**
	 * The default constructor.
	 */
	public DetailedEdgeMesh() {
		super();
	}

	/**
	 * The default constructor. It creates an edge between the two specified
	 * vertices.
	 * 
	 * @param start
	 * @param end
	 */
	public DetailedEdgeMesh(VertexController start, VertexController end) {
		super(start, end);
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

		// If the category is Faces, do not register as a listener, as the face
		// is already listening to this
		if (MeshCategory.FACES.equals(category)) {
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
	 * @see org.eclipse.eavp.viz.service.modeling.AbstractMeshComponent#clone()
	 */
	@Override
	public Object clone() {

		// Create a new object
		DetailedEdgeMesh clone = new DetailedEdgeMesh();

		// Make it a copy of this and return it
		clone.copy(this);
		return clone;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.modeling.AbstractMeshComponent#copy(org.
	 * eclipse.ice.viz.service.modeling.AbstractMeshComponent)
	 */
	@Override
	public void copy(IMesh otherObject) {

		// Queue messages from all the vertices being added
		updateManager.enqueue();

		DetailedEdgeMesh castObject = (DetailedEdgeMesh) otherObject;

		// Clone each child entity
		for (IMeshCategory category : castObject.entities.keySet()) {

			// Do not clone the faces the edge forms
			if (!MeshCategory.FACES.equals(category)) {
				for (IController entity : castObject
						.getEntitiesFromCategory(category)) {
					addEntityToCategory(
							(IController) ((BasicController) entity).clone(),
							category);
				}
			}
		}

		// Copy each of the other component's data members
		type = castObject.type;
		properties = new HashMap<IMeshProperty, String>(castObject.properties);

		// Notify listeners of the change
		SubscriptionType[] eventTypes = { SubscriptionType.ALL };
		updateManager.notifyListeners(eventTypes);

		// Release all queued messages
		updateManager.flushQueue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object otherObject) {

		// Check if the objects are the same
		if (this == otherObject) {
			return true;
		}

		// Check if the other object is an AbstractMeshComponent and cast it
		if (!(otherObject instanceof DetailedEdgeMesh)) {
			return false;
		}

		BasicMesh castObject = (BasicMesh) otherObject;

		// Check the types, properties, and entity category for equality
		if (type != castObject.type
				|| !properties.equals(castObject.properties)) {
			return false;
		}

		// Get the two sets of categories, removing Faces from both as the faces
		// an edge belong to are not relevant to whether or not it is equal to
		// another
		Set<IMeshCategory> categories = entities.keySet();
		categories.remove(MeshCategory.FACES);
		Set<IMeshCategory> categories2 = castObject.entities.keySet();
		categories2.remove(MeshCategory.FACES);

		// Check that they have the same categories
		if (!categories.equals(categories2)) {
			return false;
		}

		// For each category, check that the two objects' lists of child
		// entities in that category are equal.
		for (IMeshCategory category : entities.keySet()) {

			// Two edges should not need to be part of the same face to be equal
			if (!MeshCategory.FACES.equals(category)) {
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
		int hash = 9;
		hash += 31 * type.hashCode();
		for (IMeshCategory category : entities.keySet()) {

			// Ignore the faces category to prevent circular hashing
			if (MeshCategory.FACES.equals(category)) {
				continue;
			}

			for (IController entity : getEntitiesFromCategory(category)) {
				hash += 31 * entity.hashCode();
			}
		}
		hash += 31 * properties.hashCode();
		return hash;
	}

}
