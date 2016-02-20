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

import org.eclipse.eavp.viz.service.datastructures.VizObject.IManagedUpdateableListener;
import org.eclipse.eavp.viz.service.datastructures.VizObject.SubscriptionType;

/**
 * A mesh component representing a shape in a Constructive Solid Geometry tree.
 * 
 * @author Robert Smith
 *
 */
public class ShapeMesh extends AbstractMesh {

	/**
	 * The default constructor.
	 */
	public ShapeMesh() {
		super();
		type = MeshType.CONSTRUCTIVE;
		properties.put("Type", "None");
	}

	/**
	 * Set the shape's parent shape. Shapes can have at most one parent, and
	 * this operation will remove any existing parent.
	 * 
	 * @param parent
	 *            The new shape which serves as this shape's parent.
	 */
	public void setParent(AbstractController parent) {

		// Get the current list of parents
		List<AbstractController> parentList = getEntitiesByCategory("Parent");

		// If there is a parent, unregister it as a listener
		if (parentList != null && !parentList.isEmpty()) {
			unregister(parentList.get(0));
		}

		// Put the new Parent in the entities map, replacing any other.
		ArrayList<AbstractController> newParentList = new ArrayList<AbstractController>();

		// If parent is null, an empty list should be saved, essentially
		// removing the parent object.
		if (parent != null) {
			newParentList.add(parent);
		}

		entities.put("Parent", newParentList);

		// Register the parent as a listener and fire an update notification
		register(parent);

		SubscriptionType[] eventTypes = { SubscriptionType.CHILD };
		updateManager.notifyListeners(eventTypes);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.modeling.AbstractMeshComponent#setProperty(
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void setProperty(String property, String value) {

		// Queue updates for all selections
		updateManager.enqueue();

		// Set own property
		super.setProperty(property, value);

		// Select/deselect all children as well
		if ("Selected".equals(property)) {
			if (entities.get("Children") != null) {
				for (AbstractController entity : entities.get("Children")) {
					entity.setProperty("Selected", value);
				}
			}
		}

		// Send updates for all selections
		updateManager.flushQueue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * prototype5.impl.AbstractMeshComponentImpl#addEntity(prototype5.VizObject)
	 */
	@Override
	public void addEntity(AbstractController newEntity) {

		// By default, add an entity as a child shape.
		addEntityByCategory(newEntity, "Children");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.modeling.AbstractMeshComponent#
	 * addEntityByCategory(org.eclipse.eavp.viz.service.modeling.
	 * AbstractController, java.lang.String)
	 */
	@Override
	public void addEntityByCategory(AbstractController newEntity,
			String category) {

		// Fail silently for null objects
		if (newEntity == null) {
			return;
		}

		// If the category is not parent, add the entity normally
		if (category != "Parent") {

			// Set self as parent to any children
			if (category == "Children" && controller != null) {
				((ShapeController) newEntity).setParent((controller));
			}

			super.addEntityByCategory(newEntity, category);
		}

		// Force changes to the parent category to go through the setParent()
		// function
		else {
			setParent(newEntity);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.modeling.AbstractMesh#setController(org.
	 * eclipse.ice.viz.service.modeling.AbstractController)
	 */
	@Override
	public void setController(AbstractController controller) {
		super.setController(controller);

		// Set the new controller as the parent to any children
		for (AbstractController child : getEntitiesByCategory("Children")) {
			((ShapeController) child).setParent(controller);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.modeling.AbstractMeshComponent#clone()
	 */
	@Override
	public Object clone() {

		// Make a new shape component and copy the data into it
		ShapeMesh clone = new ShapeMesh();
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
	public void copy(AbstractMesh source) {

		// Copy the map of entities
		entities = new HashMap<String, List<AbstractController>>();

		// Copy each child in the entities map
		List<AbstractController> children = source.entities.get("Children");
		if (children != null) {
			for (AbstractController entity : children) {
				addEntity((AbstractController) entity.clone());
			}
		}

		// Copy each of the other component's data members
		type = source.type;
		properties = new HashMap<String, String>(source.properties);
		// Notify listeners of the change
		SubscriptionType[] eventTypes = { SubscriptionType.ALL };
		updateManager.notifyListeners(eventTypes);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.modeling.AbstractMeshComponent#equals(java.
	 * lang.Object)
	 */
	@Override
	public boolean equals(Object otherObject) {

		// Check if the objects are the same
		if (this == otherObject) {
			return true;
		}

		// Check if the other object is an AbstractMeshComponent and cast it
		if (!(otherObject instanceof AbstractMesh)) {
			return false;
		}

		AbstractMesh castObject = (AbstractMesh) otherObject;

		// Check the types, properties, and entity category for equality
		if (type != castObject.type || !properties.equals(castObject.properties)
				|| !entities.keySet().equals(castObject.entities.keySet())) {
			return false;
		}

		// For each category, check that the two objects' lists of child
		// entities in that category are equal.
		for (String category : entities.keySet()) {

			// Skip this check for the Parent category. Two parts can be
			// considered equal even if they are in different parts of the
			// model.
			if (!"Parent".equals(category)) {
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

			// Do not hash the parent shape, to avoid circular hashing
			if (!"Parent".equals(category)) {
				for (AbstractController entity : getEntitiesByCategory(
						category)) {
					hash += 31 * entity.hashCode();
				}
			}
		}
		hash += 31 * properties.hashCode();
		return hash;
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

		// Ignore requests to register own children to prevent circular
		// observation
		if (entities.get("Children") == null
				|| !entities.get("Children").contains(listener)) {
			super.register(listener);
		}
	}
}
