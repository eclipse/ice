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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.ice.viz.service.datastructures.VizObject.IVizUpdateable;
import org.eclipse.ice.viz.service.datastructures.VizObject.IVizUpdateableListener;

/**
 * A component of the model. All models are built from collections of components
 * in a hierarchical structure. A component represents some concrete entity
 * which can be displayed inside the graphics engine.
 * 
 * @author Robert Smith
 */
public class AbstractMeshComponent
		implements IVizUpdateableListener, IVizUpdateable {

	/**
	 * The mesh's type, which defines how the part internally stores its data.
	 */
	protected MeshType type;

	/**
	 * A lock for preventing concurrent writes to the mesh.
	 */
	private AtomicBoolean updateLock;

	/**
	 * A list of other mesh components which are connected to this one, such as
	 * children.
	 */
	protected Map<String, List<AbstractController>> entities;

	/**
	 * A map of properties for the component.
	 */
	protected Map<String, String> properties;

	/**
	 * The listeners registered for updates from this object.
	 */
	private List<IVizUpdateableListener> listeners;

	/**
	 * The controller which manages this component
	 */
	protected AbstractController controller;

	/**
	 * Set when the object knows that it will be sending a notification in the
	 * future. When set, the object should refuse to forward notifications to
	 * any listeners, instead firing an update only when all known pending
	 * changes are resolved.
	 */
	protected AtomicBoolean notifyLock;

	/**
	 * The default constructor
	 */
	public AbstractMeshComponent() {
		// Instantiate the class variables
		entities = new HashMap<String, List<AbstractController>>();
		properties = new HashMap<String, String>();
		type = MeshType.SIMPLE;
		updateLock = new AtomicBoolean();
		listeners = new ArrayList<IVizUpdateableListener>();
		notifyLock = new AtomicBoolean();
	}

	/**
	 * A constructor allowing the user to specify the mesh's type.
	 * 
	 * @param type
	 *            The type of mesh this component models.
	 */
	public AbstractMeshComponent(MeshType type) {

		// Instantiate the class variables
		this();
		this.type = type;
	}

	/**
	 * A constructor which provides a set of child entities for the
	 * MeshComponent.
	 * 
	 * @param entities
	 *            The list of initial entities.
	 */
	public AbstractMeshComponent(List<AbstractController> entities) {
		// Create a map of entities
		this.entities = new HashMap<String, List<AbstractController>>();

		// Add the input into the map of entities
		for (AbstractController entity : entities) {
			addEntity(entity);
		}

		// Instantiate the class variables
		properties = new HashMap<String, String>();
		type = MeshType.SIMPLE;
		updateLock = new AtomicBoolean();
		listeners = new ArrayList<IVizUpdateableListener>();
	}

	/**
	 * A constructor which provides a set of child entities and a mesh type
	 * 
	 * @param entities
	 *            The list of initial entities
	 * @param type
	 *            The mesh's type
	 */
	public AbstractMeshComponent(List<AbstractController> entities,
			MeshType type) {
		// Create a list of entities
		this.entities = new HashMap<String, List<AbstractController>>();

		this.entities.put("Default", entities);

		// Instantiate the class variables
		properties = new HashMap<String, String>();
		this.type = type;
		updateLock = new AtomicBoolean();
		listeners = new ArrayList<IVizUpdateableListener>();
	}

	/**
	 * A constructor taking a map of input to initialize the MeshComponent. If
	 * the type is custom part, then the constructor will take a map of initial
	 * properties in the form of a Map<String, String> keyed on property names.
	 * Otherwise, it will take a map of initial entities in the form of a
	 * Map<String, EList<VizObject>> keyed on category names.
	 * 
	 * @param input
	 *            The map representing the child entities and their categories
	 *            or properties, according to the component's type.
	 * @param type
	 *            The type of component the mesh represents.
	 */
	public AbstractMeshComponent(Map<String, Object> input, MeshType type) {
		// Instantiate the class variables
		this();
		this.type = type;

		// If the type is a custom part, the input should be a properties map
		if (type == MeshType.CUSTOM_PART) {

			// For each property, create an entry and add it to the list
			for (String property : input.keySet()) {
				properties.put(property, (String) input.get(property));
			}
		}

		// Otherwise the input is a list of entities
		else {

			// For each category, create an entry and add it to the map
			for (String category : input.keySet()) {
				List<AbstractController> tempList = (List<AbstractController>) input
						.get(category);
				entities.put(category,
						(List<AbstractController>) input.get(category));
			}
		}

	}

	/**
	 * Getter method for type.
	 * 
	 * @return The mesh's type
	 */
	public MeshType getType() {
		return type;
	}

	/**
	 * Setter method for the mesh's type
	 */
	public void setType(MeshType type) {
		this.type = type;
		notifyListeners();
	}

	/**
	 * Send a notification to all listeners.
	 */
	public void notifyListeners() {

		if (!notifyLock.get()) {

			// If the listeners are empty, return
			if (this.listeners == null || this.listeners.isEmpty()) {
				return;
			}

			// Get a reference to self
			final AbstractMeshComponent self = this;

			final List<IVizUpdateableListener> localListeners = new ArrayList<IVizUpdateableListener>(
					listeners);

			// // Create a thread object that notifies all listeners
			//
			// Thread notifyThread = new Thread() {
			//
			// @Override
			// public void run() {
			// Loop over all listeners and update them
			for (int i = 0; i < localListeners.size(); i++) {
				localListeners.get(i).update(self);
			}
			// }
			// };
			//
			// // Start the thread
			// notifyThread.start();
		}
	}

	/**
	 * Returns a list of all related entities.
	 * 
	 * @return All related entities.
	 */
	public List<AbstractController> getEntities() {

		// A temporary list of entities
		List<AbstractController> entityList = new ArrayList<AbstractController>();

		// Add the entities of each category to the list
		for (String category : entities.keySet()) {
			entityList.addAll(entities.get(category));
		}

		return entityList;

	}

	/**
	 * Return all of the part's children entities of a given category.
	 * 
	 * @category The category of entities to return
	 */
	public List<AbstractController> getEntitiesByCategory(String category) {

		// Get the entities under the given category
		List<AbstractController> temp = entities.get(category);

		// If the list is null, return an empty list instead
		return (temp != null ? temp : new ArrayList<AbstractController>());
	}

	/**
	 * Return the value of the given property
	 * 
	 * @property The property to return
	 */
	public String getProperty(String property) {
		return properties.get(property);
	}

	/**
	 * Set the given property, creating it in the map if it is not already
	 * present.
	 * 
	 * @generated NOT
	 * 
	 * @property The property to set
	 * @value The property's new value
	 */
	public void setProperty(String property, String value) {
		notifyLock.set(true);
		properties.put(property, value);
		notifyLock.set(false);
		notifyListeners();
	}

	/**
	 * Add a new entity to the part. A convenience method which allows for the
	 * specification of a default behavior for new entities when no category is
	 * specified.
	 * 
	 * @generated NOT
	 * 
	 * @newEntity The child entity to add to the part.
	 */
	public void addEntity(AbstractController newEntity) {
		addEntityByCategory(newEntity, "Default");
	}

	/**
	 * Removes the given entity from the part's children
	 *
	 * @generated NOT
	 * 
	 * @entity The entity to be removed
	 */
	public void removeEntity(AbstractController entity) {

		notifyLock.set(true);

		// Whether or not the entity was found in the map
		boolean found = false;

		// If the map contains the given entity
		for (String category : entities.keySet()) {
			if (entities.get(category).contains(entity)) {

				// Remove all copies of the entity from the map
				entities.values().removeAll(Collections.singleton(entity));

				entities.get(category).remove(entity);

				// Unregister from the entity
				entity.unregister(this);

				found = true;
			}
		}

		// Notify listeners if a change occurred
		notifyLock.set(false);

		if (found) {
			notifyListeners();
		}
	}

	/**
	 * Adds a new child entity under the given category.
	 *
	 * @generated NOT
	 * 
	 * @param newEntity
	 *            The new child entity to be added
	 * @param category
	 *            The new entity's category
	 */
	public void addEntityByCategory(AbstractController newEntity,
			String category) {

		notifyLock.set(true);

		// Get the entities for the given category
		List<AbstractController> catList = entities.get(category);

		// If the list is empty, make an empty one
		if (catList == null) {
			catList = new ArrayList<AbstractController>();
		}

		// Add the entity to the list and put it in the map
		catList.add(newEntity);
		entities.put(category, catList);

		// Register with the entity
		newEntity.register(this);

		// Send notification that entities have been changed
		notifyLock.set(false);
		notifyListeners();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.datastructures.VizObject.IVizUpdateable#
	 * update(java.lang.String, java.lang.String)
	 */
	@Override
	public void update(String updatedKey, String newValue) {
		// Nothing to do

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.datastructures.VizObject.IVizUpdateable#
	 * register(org.eclipse.ice.viz.service.datastructures.VizObject.
	 * IVizUpdateableListener)
	 */
	@Override
	public void register(IVizUpdateableListener listener) {
		if (listener != null && !listeners.contains(listener)) {
			listeners.add(listener);

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.datastructures.VizObject.IVizUpdateable#
	 * unregister(org.eclipse.ice.viz.service.datastructures.VizObject.
	 * IVizUpdateableListener)
	 */
	@Override
	public void unregister(IVizUpdateableListener listener) {
		listeners.remove(listener);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.datastructures.VizObject.
	 * IVizUpdateableListener#update(org.eclipse.ice.viz.service.datastructures.
	 * VizObject.IVizUpdateable)
	 */
	@Override
	public void update(IVizUpdateable component) {

		// Notify own listeners that an update has occurred.
		notifyListeners();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() {

		// Create a new component, and make it a copy of this one.
		AbstractMeshComponent clone = new AbstractMeshComponent();
		clone.copy(this);
		return clone;
	}

	/**
	 * Copies the contents of another AbstractMeshComponent into this one.
	 * 
	 * @param otherObject
	 *            The object which will be copied into this.
	 */
	public void copy(AbstractMeshComponent otherObject) {

		// Copy each of the other component's data members
		type = otherObject.type;
		properties = new HashMap<String, String>(otherObject.properties);

		// Notify listeners of the change
		notifyListeners();
	}

	/**
	 * Getter method for the controller.
	 * 
	 * @return The AbstractController which manages this component
	 */
	public AbstractController getController() {
		return controller;
	}

	/**
	 * Setter method for the controller.
	 * 
	 * @param controller
	 *            The AbstractController which manages this component
	 */
	public void setController(AbstractController controller) {
		this.controller = controller;
	}
}
