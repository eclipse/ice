/*******************************************************************************
 * Copyright (c) 2016 UT-Battelle, LLC.
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
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.eavp.viz.service.datastructures.VizObject.IManagedUpdateable;
import org.eclipse.eavp.viz.service.datastructures.VizObject.IManagedUpdateableListener;
import org.eclipse.eavp.viz.service.datastructures.VizObject.UpdateableSubscriptionManager;

/**
 * A interface which is responsible for user interactions with the underlying
 * data structures which represent a part in a model. An IController is
 * associated with an IMesh, which is the internal representation of the part
 * and any associated data, and an IView, which holds the graphical
 * representation of the object in the native data types of the rendering
 * engine.
 * 
 * The IController is meant to completely encapsulate the functionality of the
 * part. All client interactions with a part should take place through function
 * calls to the part's IController, without regard as to whether they are
 * internally delegated to the model, the view, or both. The controller should
 * not expose the model or view, or their implementation details, to the client.
 * 
 * @author Robert Smith
 */
public interface IController
		extends IManagedUpdateable, IManagedUpdateableListener {

	/**
	 * Alerts the view to refresh itself based on changes in the model
	 * 
	 * @generated NOT
	 */
	void refresh();

	/**
	 * Checks whether or not this part and its resources have been disposed.
	 * 
	 * @return
	 */
	AtomicBoolean getDisposed();

	/**
	 * Set the the controller as being disposed.
	 * 
	 * @newDisposed Whether or not the controller is disposed.
	 */
	void setDisposed(boolean newDisposed);

	/**
	 * Getter method for the part's model
	 * 
	 * @return The controller's managed model.
	 */
	IMesh getModel();

	/**
	 * Getter method for the part's view.
	 * 
	 * @return The controller's managed view.
	 */
	IView getView();

	/**
	 * Setter method for the model's view.
	 * 
	 * @generated
	 */
	void setView(BasicView newView);

	/**
	 * Adds an entity to the model.
	 * 
	 * @generated NOT
	 */
	void addEntity(IController newEntity);

	/**
	 * Dispose the controller and its resources.
	 * 
	 * @generated NOT
	 */
	void dispose();

	/**
	 * Gets a list of all the part's children entities.
	 * 
	 * @generated NOT
	 */
	ArrayList<IController> getEntities();

	/**
	 * Returns all the part's child entities of the given category
	 * 
	 * @category The category of entities to get.
	 * @generated NOT
	 */
	ArrayList<IController> getEntitiesFromCategory(IMeshCategory category);

	/**
	 * Return all of the part's children entities of a given category cast to a
	 * list of the specified return type.
	 * 
	 * @param category
	 *            The category of entities to return
	 * @param returnType
	 *            The class to which members of the category belong.
	 * @return All the entities in the map for the specified category, cast to
	 *         the given type.
	 */
	<T extends IController> ArrayList<T> getEntitiesFromCategory(
			IMeshCategory category, Class<T> returnType);

	/**
	 * Get the specified property's value.
	 * 
	 * @generated NOT
	 * 
	 * @property The property to get.
	 */
	String getProperty(IMeshProperty property);

	/**
	 * Get a wrapper containing the part's representation as an object specific
	 * to the application rendering it.
	 * 
	 * @generated NOT
	 */
	Representation getRepresentation();

	/**
	 * Get the part's rotation in the x, y, and z directions
	 * 
	 * @generated NOT
	 */
	double[] getRotation();

	/**
	 * Get the part's scale in the x, y, and z directions.
	 * 
	 * @generated NOT
	 */
	double[] getScale();

	/**
	 * Get the part's size.
	 * 
	 * @generated NOT
	 */
	double getSize();

	/**
	 * Get the part's skew in the x, y, and z directions
	 * 
	 * @generated NOT
	 */
	double[] getSkew();

	/**
	 * Get the part's transformation.
	 * 
	 * @generated NOT
	 */
	Transformation getTransformation();

	/**
	 * Get the part's translation in the x, y, and z directions
	 * 
	 * @generated NOT
	 */
	double[] getTranslation();

	/**
	 * Remove an entity from the part's child entities.
	 * 
	 * @generated NOT
	 * 
	 * @entity The entity to be removed
	 */
	void removeEntity(IController entity);

	/**
	 * Set the given property to a new value
	 * 
	 * @generated NOT
	 * 
	 * @property The property to modify
	 * @value The property's new value
	 */
	void setProperty(IMeshProperty property, String value);

	/**
	 * Set the part's rotation in the x, y, and x directions.
	 * 
	 * @generated NOT
	 * 
	 * @x The amount of x rotation to apply
	 * @y The amount of y rotation to apply
	 * @z The amount of z rotation to apply
	 */
	void setRotation(double x, double y, double z);

	/**
	 * Set the part's scale in the x, y, and x directions.
	 * 
	 * @generated NOT
	 * 
	 * @x The part's x scale
	 * @y The part's y scale
	 * @z The part's z scale
	 */
	void setScale(double x, double y, double z);

	/**
	 * Set the part's size
	 * 
	 * @generated NOT
	 * 
	 * @newSize The new multiplier for the part's size
	 */
	void setSize(double newSize);

	/**
	 * Set the part's skew in the x, y, and x directions.
	 * 
	 * @generated NOT
	 * 
	 * @x The amount of x skew to apply
	 * @y The amount of y skew to apply
	 * @z The amount of z skew to apply
	 */
	void setSkew(double x, double y, double z);

	/**
	 * Set the part's transformation
	 * 
	 * @generated NOT
	 * 
	 * @newTransformation The transformation to apply to the part.
	 */
	void setTransformation(Transformation newTransformation);

	/**
	 * Set the part's translation in the x, y, and x directions.
	 * 
	 * @generated NOT
	 * 
	 * @x The amount of x translation to apply
	 * @y The amount of y translation to apply
	 * @z The amount of z translation to apply
	 */
	void setTranslation(double x, double y, double z);

	/**
	 * Add an entity to the part under the given category.
	 * 
	 * @generated NOT
	 * 
	 * @param newEntity
	 *            The child entity to be added to this part
	 * @param category
	 *            The category under which to add the new child entity
	 */
	void addEntityToCategory(IController newEntity, IMeshCategory category);

	/**
	 * Checks whether the part is the root node of the tree. A convenience
	 * method equivalent to getProperty(MeshProperty.ROOT) except it returns a
	 * boolean instead of a String.
	 * 
	 * @return True if the part is selected and false if it is not.
	 */
	boolean isRoot();

	/**
	 * Checks whether the part is selected. A convenience method equivalent to
	 * getProperty(MeshProperty.SELECTED) except it returns a boolean instead of
	 * a String.
	 * 
	 * @return True if the part is selected and false if it is not.
	 */
	boolean isSelected();

	/**
	 * Set the part as being the root node of the tree. A convenience method
	 * equivalent to setProperty(MeshProperty.ROOT, ___) except it takes a
	 * boolean instead of a String.
	 * 
	 * @param root
	 *            Whether the part is the root node or not
	 */
	void setRoot(boolean root);

	/**
	 * Set the part as being selected. A convenience method equivalent to
	 * setProperty(MeshProperty.SELECTED, ___) except it takes a boolean instead
	 * of a String.
	 * 
	 * @param selected
	 *            Whether the part is selected or not.
	 */
	void setSelected(boolean selected);

	/**
	 * Getter method for the update manager.
	 * 
	 * @return The update subscription manager
	 */
	UpdateableSubscriptionManager getUpdateManager();

	/**
	 * Deep copy the given object's data into this one.
	 * 
	 * @param otherObject
	 *            The object to copy into this one.
	 */
	void copy(IController otherObject);

}