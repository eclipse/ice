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
import java.util.HashMap;
import java.util.List;

import org.eclipse.ice.viz.service.datastructures.VizObject.IManagedVizUpdateableListener;
import org.eclipse.ice.viz.service.datastructures.VizObject.UpdateableSubscriptionType;

/**
 * A mesh component representing a shape in a Constructive Solid Geometry tree.
 * 
 * @author Robert Smith
 *
 */
public class ShapeComponent extends AbstractMeshComponent {

	/**
	 * The default constructor.
	 */
	public ShapeComponent() {
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
	public void setParent(Shape parent) {

		// Get the current list of parents
		List<AbstractController> parentList = getEntitiesByCategory("Parent");

		// If there is a parent, unregister it as a listener
		if (parentList != null && !parentList.isEmpty()) {
			unregister(parentList.get(0));
		}

		// Put the new Parent in the entities map, replacing any other.
		ArrayList<AbstractController> newParentList = new ArrayList<AbstractController>();
		newParentList.add(parent);
		entities.put("Parent", newParentList);

		// Register the parent as a listener and fire an update notification
		register(parent);

		UpdateableSubscriptionType[] eventTypes = {
				UpdateableSubscriptionType.Child };
		updateManager.notifyListeners(eventTypes);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.viz.service.modeling.AbstractMeshComponent#setProperty(
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
	 * @see org.eclipse.ice.viz.service.modeling.AbstractMeshComponent#
	 * addEntityByCategory(org.eclipse.ice.viz.service.modeling.
	 * AbstractController, java.lang.String)
	 */
	@Override
	public void addEntityByCategory(AbstractController newEntity,
			String category) {

		// If the category is not parent, add the entity normally
		if (category != "Parent") {

			// Set self as parent to any children
			if (category == "Children") {
				((Shape) newEntity).setParent(((Shape) controller));
			}

			super.addEntityByCategory(newEntity, category);
		}

		// Force changes to the parent category to go through the setParent()
		// funcgtion
		else {
			setParent((Shape) newEntity);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.modeling.AbstractMeshComponent#clone()
	 */
	@Override
	public Object clone() {

		// Make a new shape component and copy the data into it
		ShapeComponent clone = new ShapeComponent();
		clone.copy(this);

		return clone;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.modeling.AbstractMeshComponent#copy(org.
	 * eclipse.ice.viz.service.modeling.AbstractMeshComponent)
	 */
	@Override
	public void copy(AbstractMeshComponent source) {

		// Copy the map of entities
		entities = new HashMap<String, List<AbstractController>>();

		// Copy each child in the entities map
		List<AbstractController> children = source.entities.get("Children");
		if (children != null) {
			for (AbstractController entity : children) {
				addEntity((AbstractController) entity.clone());
			}
		}

		super.copy(source);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.viz.service.modeling.AbstractMeshComponent#register(org.
	 * eclipse.ice.viz.service.datastructures.VizObject.IVizUpdateableListener)
	 */
	@Override
	public void register(IManagedVizUpdateableListener listener) {

		// Ignore requests to register own children to prevent circular
		// observation
		if (entities.get("Children") == null
				|| !entities.get("Children").contains(listener)) {
			super.register(listener);
		}
	}
}
