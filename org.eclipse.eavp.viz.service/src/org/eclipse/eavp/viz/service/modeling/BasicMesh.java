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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.eavp.viz.service.datastructures.VizObject.IManagedUpdateable;
import org.eclipse.eavp.viz.service.datastructures.VizObject.IManagedUpdateableListener;
import org.eclipse.eavp.viz.service.datastructures.VizObject.SubscriptionType;
import org.eclipse.eavp.viz.service.datastructures.VizObject.UpdateableSubscriptionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * base implementation of IMesh.
 * 
 * @author Robert Smith
 */
public class BasicMesh
		implements IManagedUpdateableListener, IManagedUpdateable, IMesh {

	/**
	 * The mesh's type, which defines how the part internally stores its data.
	 */
	protected MeshType type;

	/**
	 * A list of other mesh components which are connected to this one, such as
	 * children.
	 */
	protected Map<IMeshCategory, ArrayList<IController>> entities;

	/**
	 * A map of properties for the component.
	 */
	protected Map<IMeshProperty, String> properties;

	/**
	 * The listeners registered for updates from this object.
	 */
	protected UpdateableSubscriptionManager updateManager;

	/**
	 * The controller which manages this component
	 */
	protected IController controller;

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(IController.class);

	/**
	 * The default constructor
	 */
	public BasicMesh() {
		// Instantiate the class variables
		entities = new HashMap<IMeshCategory, ArrayList<IController>>();
		properties = new HashMap<IMeshProperty, String>();
		type = MeshType.SIMPLE;
		updateManager = new UpdateableSubscriptionManager(this);
	}

	/**
	 * A constructor allowing the user to specify the mesh's type.
	 * 
	 * @param type
	 *            The type of mesh this component models.
	 */
	public BasicMesh(MeshType type) {

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
	public BasicMesh(List<IController> entities) {
		// Create a map of entities
		this.entities = new HashMap<IMeshCategory, ArrayList<IController>>();

		// Add the input into the map of entities
		for (IController entity : entities) {
			addEntity(entity);
		}

		// Instantiate the class variables
		properties = new HashMap<IMeshProperty, String>();
		type = MeshType.SIMPLE;
		updateManager = new UpdateableSubscriptionManager(this);
	}

	/**
	 * A constructor which provides a set of child entities and a mesh type
	 * 
	 * @param entities
	 *            The list of initial entities
	 * @param type
	 *            The mesh's type
	 */
	public BasicMesh(ArrayList<IController> entities, MeshType type) {
		// Create a list of entities
		this.entities = new HashMap<IMeshCategory, ArrayList<IController>>();

		this.entities.put(MeshCategory.DEFAULT, entities);

		// Instantiate the class variables
		properties = new HashMap<IMeshProperty, String>();
		this.type = type;
		updateManager = new UpdateableSubscriptionManager(this);
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
	public BasicMesh(Map<Object, Object> input, MeshType type) {
		// Instantiate the class variables
		this();
		this.type = type;

		// If the type is a custom part, the input should be a properties map
		if (type == MeshType.CUSTOM_PART) {

			// For each property, create an entry and add it to the list
			for (Object property : input.keySet()) {
				properties.put((IMeshProperty) property,
						(String) input.get(property));
			}
		}

		// Otherwise the input is a list of entities
		else {

			// For each category, create an entry and add it to the map
			for (Object category : input.keySet()) {
				List<IController> tempList = (List<IController>) input
						.get(category);
				entities.put((IMeshCategory) category,
						(ArrayList<IController>) input.get(category));
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.modeling.IMesh#getType()
	 */
	@Override
	public MeshType getType() {
		return type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.modeling.IMesh#setType(org.eclipse.eavp.viz.
	 * service.modeling.MeshType)
	 */
	@Override
	public void setType(MeshType type) {

		// Log an error and fail silently if the type is null
		if (type == null) {
			logger.error("An IMesh's type must not be null.");
			return;
		}

		this.type = type;
		SubscriptionType[] eventTypes = { SubscriptionType.PROPERTY };
		updateManager.notifyListeners(eventTypes);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.modeling.IMesh#getEntities()
	 */
	@Override
	public ArrayList<IController> getEntities() {

		// A temporary list of entities
		ArrayList<IController> entityList = new ArrayList<IController>();

		// Add the entities of each category to the list
		for (IMeshCategory category : entities.keySet()) {
			entityList.addAll(entities.get(category));
		}

		return entityList;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.modeling.IMesh#getEntitiesByCategory(java.
	 * lang.String)
	 */
	@Override
	public ArrayList<IController> getEntitiesFromCategory(
			IMeshCategory category) {

		// Get the entities under the given category
		ArrayList<IController> temp = entities.get(category);

		// If the list is null, return an empty list instead
		return (temp != null ? new ArrayList<IController>(temp)
				: new ArrayList<IController>());
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.modeling.IMesh#getEntitiesFromCategory(org.eclipse.eavp.viz.service.modeling.IMeshCategory, java.lang.Class)
	 */
	@Override
	public <T extends IController> ArrayList<T> getEntitiesFromCategory(IMeshCategory category, Class<T> returnType) {

		//The list of cast entities
		ArrayList<T> temp = new ArrayList<T>();
		
		//Add each of the entities in category to the list
		for(IController entity : entities.get(category)){	
			try{
				temp.add((T) entity);
			}
			
			//If an entity could not be cast to T, log an error and return an empty list.
			catch(ClassCastException e){
				logger.error("IMesh attempted to cast entity, but entity was not a subtype of the specified return type. Returning empty list instead.");
				return new ArrayList<T>();
			}
		}
		
		return temp;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.modeling.IMesh#getProperty(java.lang.String)
	 */
	@Override
	public String getProperty(IMeshProperty property) {
		return properties.get(property);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.modeling.IMesh#setProperty(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void setProperty(IMeshProperty property, String value) {

		// Whether the property was actually changed
		boolean changed = true;

		if (property.equals(properties.get(property))) {
			changed = false;
		}

		properties.put(property, value);

		// If a change occurred, send an update
		if (changed) {

			SubscriptionType[] eventTypes = new SubscriptionType[1];

			// Check if the changed property was selection to send the proper
			// update event.
			if (!MeshProperty.SELECTED.equals(property)) {
				eventTypes[0] = SubscriptionType.PROPERTY;
			} else {
				eventTypes[0] = SubscriptionType.SELECTION;
			}
			updateManager.notifyListeners(eventTypes);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.modeling.IMesh#addEntity(org.eclipse.eavp.
	 * viz.service.modeling.IController)
	 */
	@Override
	public void addEntity(IController newEntity) {
		addEntityToCategory(newEntity, MeshCategory.DEFAULT);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.modeling.IMesh#removeEntity(org.eclipse.eavp
	 * .viz.service.modeling.IController)
	 */
	@Override
	public void removeEntity(IController entity) {

		// Do not try to add null objects to the map
		if (entity == null) {
			return;
		}

		// Whether or not the entity was found in the map
		boolean found = false;

		// If the map contains the given entity
		for (IMeshCategory category : entities.keySet()) {
			if (entities.get(category).contains(entity)) {

				// Remove all copies of the entity from the map
				entities.values().removeAll(Collections.singleton(entity));

				entities.get(category).remove(entity);

				// Unregister from the entity
				entity.unregister(this);

				found = true;
			}
		}

		if (found) {
			SubscriptionType[] eventTypes = { SubscriptionType.CHILD };
			updateManager.notifyListeners(eventTypes);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.modeling.IMesh#addEntityByCategory(org.
	 * eclipse.eavp.viz.service.modeling.IController, java.lang.String)
	 */
	@Override
	public void addEntityToCategory(IController newEntity,
			IMeshCategory category) {

		// Get the entities for the given category
		ArrayList<IController> catList = entities.get(category);

		// If the list is null, make an empty one
		if (catList == null) {
			catList = new ArrayList<IController>();
		}

		// Prevent a part from being added multiple times
		else if (catList.contains(newEntity)) {
			return;
		}

		// If the entity is already present in this category, don't add a second
		// entry for it
		else
			for (IController entity : catList) {
				if (entity == newEntity) {
					return;
				}
			}

		// Add the entity to the list and put it in the map
		catList.add(newEntity);
		entities.put(category, catList);

		// Register with the entity
		newEntity.register(this);

		SubscriptionType[] eventTypes = { SubscriptionType.CHILD };
		updateManager.notifyListeners(eventTypes);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.datastructures.VizObject.IVizUpdateable#
	 * unregister(org.eclipse.eavp.viz.service.datastructures.VizObject.
	 * IVizUpdateableListener)
	 */
	@Override
	public void unregister(IManagedUpdateableListener listener) {
		updateManager.unregister(listener);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.datastructures.VizObject.
	 * IManagedVizUpdateableListener#update(org.eclipse.eavp.viz.service.
	 * datastructures.VizObject.IVizUpdateable,
	 * org.eclipse.eavp.viz.service.datastructures.VizObject.
	 * UpdateableSubscription)
	 */
	@Override
	public void update(IManagedUpdateable component, SubscriptionType[] type) {

		// Pass the update to own listeners
		updateManager.notifyListeners(type);

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

		// Check if the other object is an IMeshComponent and cast it
		if (!(otherObject instanceof IMesh)) {
			return false;
		}

		BasicMesh castObject = (BasicMesh) otherObject;

		// Check the types and properties for equality
		if (type != castObject.type
				|| !properties.equals(castObject.properties)) {
			return false;
		}

		// If this object has and child entities, check them for equality with
		// the other object's
		if (!entities.keySet().isEmpty()) {

			// For each category, check that the two objects' lists of child
			// entities in that category are equal.
			for (IMeshCategory category : entities.keySet()) {

				// Get the lists for this category
				List<IController> cat = entities.get(category);
				List<IController> otherCat = castObject.entities.get(category);

				// Handle the case where the category is not found in the first
				// object
				if (cat == null || cat.isEmpty()) {

					// If both objects have nothing for this category, the
					// categories are equal
					if (otherCat == null || otherCat.isEmpty()) {
						continue;
					}

					// If the second object has something in this category, the
					// two
					// are not equal
					else {
						return false;
					}
				}

				// If the category is not found in the second object when it was
				// in
				// the first, the two are not equal
				else if (otherCat == null || otherCat.isEmpty()) {
					return false;
				}

				// Otherwise, compare the lists. If they are not equal, then the
				// meshes are not equal
				else if (!cat.containsAll(otherCat)
						|| !otherCat.containsAll(cat)) {
					return false;
				}
			}
		}

		// If the other object has a non-empty entity set while this object does
		// not, then they are not equal
		else if (!castObject.entities.keySet().isEmpty()) {
			return false;

		}

		// All checks passed, so the objects are equal
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() {

		// Create a new component, and make it a copy of this one.
		BasicMesh clone = new BasicMesh();
		clone.copy(this);
		return clone;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.modeling.IMesh#copy(org.eclipse.eavp.viz.
	 * service.modeling.IMesh)
	 */
	@Override
	public void copy(IMesh otherObject) {

		/**
		 * If the other object is not an abstract mesh, fail silently.
		 */
		if (!(otherObject instanceof IMesh)) {
			return;
		}

		BasicMesh castObject = (BasicMesh) otherObject;

		// Copy each of the other component's data members
		type = castObject.type;
		properties = new HashMap<IMeshProperty, String>(castObject.properties);

		// Clone each child entity
		for (IMeshCategory category : castObject.entities.keySet()) {
			for (IController entity : otherObject
					.getEntitiesFromCategory(category)) {
				addEntityToCategory(
						(IController) ((BasicController) entity).clone(),
						category);
			}
		}

		// Notify listeners of the change
		SubscriptionType[] eventTypes = SubscriptionType.values();
		updateManager.notifyListeners(eventTypes);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.modeling.IMesh#getController()
	 */
	@Override
	public IController getController() {
		return controller;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.modeling.IMesh#setController(org.eclipse.
	 * eavp.viz.service.modeling.IController)
	 */
	@Override
	public void setController(IController controller) {

		// If the controller is null, log an error and fail
		if (controller == null) {
			logger.error("An IMesh's controller must not be null.");
		}

		this.controller = controller;

		// Set the manager's parent as well
		updateManager.setParent(controller.getUpdateManager());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.datastructures.VizObject.
	 * IManagedVizUpdateable#register(org.eclipse.eavp.viz.service.
	 * datastructures .VizObject.IManagedVizUpdateableListener)
	 */
	@Override
	public void register(IManagedUpdateableListener listener) {
		updateManager.register(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.datastructures.VizObject.
	 * IManagedVizUpdateableListener#getSubscriptions(org.eclipse.eavp.viz.
	 * service.datastructures.VizObject.IVizUpdateable)
	 */
	@Override
	public ArrayList<SubscriptionType> getSubscriptions(
			IManagedUpdateable source) {
		ArrayList<SubscriptionType> types = new ArrayList<SubscriptionType>();
		types.add(SubscriptionType.ALL);
		return types;
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
			for (IController entity : getEntitiesFromCategory(category)) {
				hash += 31 * entity.hashCode();
			}
		}
		hash += 31 * properties.hashCode();
		return hash;
	}



}
