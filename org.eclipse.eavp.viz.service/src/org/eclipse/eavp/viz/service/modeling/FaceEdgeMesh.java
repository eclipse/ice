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
import java.util.Set;

import org.eclipse.eavp.viz.service.datastructures.VizObject.SubscriptionType;

/**
 * A component representing an Edge for a Face, which keeps a list of all Faces
 * for which it is a boundary in its entities map.
 * 
 * @author Robert Smith
 *
 */
public class FaceEdgeMesh extends EdgeMesh {

	/**
	 * The default constructor.
	 */
	public FaceEdgeMesh() {
		super();
	}

	/**
	 * The default constructor. It creates an edge between the two specified
	 * vertices.
	 * 
	 * @param start
	 * @param end
	 */
	public FaceEdgeMesh(VertexController start, VertexController end) {
		super(start, end);
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

		// If the category is Faces, do not register as a listener, as the face
		// is already listening to this
		if ("Faces".equals(category)) {
			// Get the entities for the given category
			List<AbstractController> catList = entities.get(category);

			// If the list is empty, make an empty one
			if (catList == null) {
				catList = new ArrayList<AbstractController>();
			}

			// Add the entity to the list and put it in the map
			catList.add(entity);
			entities.put(category, catList);

			SubscriptionType[] eventTypes = { SubscriptionType.CHILD };
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
	 * @see org.eclipse.eavp.viz.service.modeling.AbstractMeshComponent#clone()
	 */
	@Override
	public Object clone() {

		// Create a new object
		FaceEdgeMesh clone = new FaceEdgeMesh();

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

			// Do not clone the faces the edge forms
			if (!"Faces".equals(category)) {
				for (AbstractController entity : otherObject
						.getEntitiesByCategory(category)) {
					addEntityByCategory((AbstractController) entity.clone(),
							category);
				}
			}
		}

		// Copy each of the other component's data members
		type = otherObject.type;
		properties = new HashMap<String, String>(otherObject.properties);

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
		if (!(otherObject instanceof FaceEdgeMesh)) {
			return false;
		}

		AbstractMesh castObject = (AbstractMesh) otherObject;

		// Check the types, properties, and entity category for equality
		if (type != castObject.type
				|| !properties.equals(castObject.properties)) {
			return false;
		}

		// Get the two sets of categories, removing Faces from both as the faces
		// an edge belong to are not relevant to whether or not it is equal to
		// another
		Set<String> categories = entities.keySet();
		categories.remove("Faces");
		Set<String> categories2 = castObject.entities.keySet();
		categories2.remove("Faces");

		// Check that they have the same categories
		if (!categories.equals(categories2)) {
			return false;
		}

		// For each category, check that the two objects' lists of child
		// entities in that category are equal.
		for (String category : entities.keySet()) {

			// Two edges should not need to be part of the same face to be equal
			if (!"Faces".equals(category)) {
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
		for (String category : entities.keySet()) {

			// Ignore the faces category to prevent circular hashing
			if ("Faces".equals(category)) {
				continue;
			}

			for (AbstractController entity : getEntitiesByCategory(category)) {
				hash += 31 * entity.hashCode();
			}
		}
		hash += 31 * properties.hashCode();
		return hash;
	}

}
