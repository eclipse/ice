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
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.eavp.viz.service.datastructures.VizObject.IManagedUpdateable;
import org.eclipse.eavp.viz.service.datastructures.VizObject.IManagedUpdateableListener;
import org.eclipse.eavp.viz.service.datastructures.VizObject.SubscriptionType;
import org.eclipse.eavp.viz.service.datastructures.VizObject.UpdateableSubscriptionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class which is responsible for user interactions with the underlying data
 * structures which represent a part in a model. An AbstractController is
 * associated with an AbstractMeshComponent, which is the internal
 * representation of the part and any associated data, and an AbstractView,
 * which holds the graphical representation of the object in the native data
 * types of the rendering engine.
 * 
 * The AbstractController is meant to completely encapsulate the functionality
 * of the part. All client interactions with a part should take place through
 * function calls to the part's AbstractController, without regard as to whether
 * they are internally delegated to the model, the view, or both. The controller
 * should not expose the model or view, or their implementation details, to the
 * client.
 * 
 * @author Robert Smith
 */
public class AbstractController
		implements IManagedUpdateable, IManagedUpdateableListener {

	/**
	 * The internal representation of this part.
	 */
	protected AbstractMesh model;

	/**
	 * This part's representation in the graphics rendering program.
	 */
	protected AbstractView view;

	/**
	 * A flag for whether or not the part has been disposed.
	 */
	private AtomicBoolean disposed;

	/**
	 * The manager for the part's updates.
	 */
	protected UpdateableSubscriptionManager updateManager;

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(AbstractController.class);

	/**
	 * The default constructor.
	 */
	public AbstractController() {
		disposed = new AtomicBoolean();
		disposed.set(false);
		updateManager = new UpdateableSubscriptionManager(this);
	}

	/**
	 * Constructor for an AbstractController with its associated model and view.
	 * 
	 * @param model
	 *            The model to be managed.
	 * @param view
	 *            The model's view.
	 */
	public AbstractController(AbstractMesh model, AbstractView view) {

		// Check that the model and view are valid
		if (model == null || view == null) {
			throw new IllegalArgumentException(
					"An AbstractController must have a non-null model and view.");
		}

		// Initialize the class variables
		this.model = model;
		this.view = view;
		disposed = new AtomicBoolean();
		disposed.set(false);
		updateManager = new UpdateableSubscriptionManager(this);

		// Give model a reference to its controller
		model.setController(this);

		// Register as a listener to the model and view
		model.register(this);
		view.register(this);
	}

	/**
	 * Alerts the view to refresh itself based on changes in the model
	 * 
	 * @generated NOT
	 */
	public void refresh() {
		view.refresh(model);
	}

	/**
	 * Informs the controller that the graphics engine's rendering of the part
	 * has been synchronized with the controller's state.
	 * 
	 * @generated NOT
	 */
	public void setSynched() {
		view.setSynched();
	}

	/**
	 * Checks whether or not this part and its resources have been disposed.
	 * 
	 * @return
	 */
	public AtomicBoolean getDisposed() {
		return disposed;
	}

	/**
	 * Set the the controller as being disposed.
	 * 
	 * @newDisposed Whether or not the controller is disposed.
	 */
	public void setDisposed(boolean newDisposed) {

		// Atomically set the controller as disposed
		disposed.getAndSet(newDisposed);

		SubscriptionType[] eventType = new SubscriptionType[1];
		eventType[0] = SubscriptionType.PROPERTY;
		updateManager.notifyListeners(eventType);
	}

	/**
	 * Getter method for the part's model
	 * 
	 * @return The controller's managed model.
	 */
	public AbstractMesh getModel() {
		return model;
	}

	/**
	 * Getter method for the part's view.
	 * 
	 * @return The controller's managed view.
	 */
	public AbstractView getView() {
		return view;
	}

	/**
	 * Setter method for the model's view.
	 * 
	 * @generated
	 */
	public void setView(AbstractView newView) {

		// Log an error and fail silently if the view is null
		if (newView == null) {
			logger.error(
					"Attempted to set an AbstractController's view to null.");
			return;
		}

		view = newView;
	}

	/**
	 * Adds an entity to the model.
	 * 
	 * @generated NOT
	 */
	public void addEntity(AbstractController newEntity) {
		model.addEntity(newEntity);
	}

	/**
	 * Dispose the controller and its resources.
	 * 
	 * @generated NOT
	 */
	public void dispose() {
		setDisposed(true);
	}

	/**
	 * Gets a list of all the part's children entities.
	 * 
	 * @generated NOT
	 */
	public List<AbstractController> getEntities() {
		return model.getEntities();
	}

	/**
	 * Returns all the part's child entities of the given category
	 * 
	 * @category The category of entities to get.
	 * @generated NOT
	 */
	public List<AbstractController> getEntitiesByCategory(String category) {
		return model.getEntitiesByCategory(category);
	}

	/**
	 * Gets the last transformation fully applied to this part.
	 * 
	 * @generated NOT
	 */
	public Transformation getPreviousTransformation() {
		return view.getPreviousTransformation();
	}

	/**
	 * Get the specified property's value.
	 * 
	 * @generated NOT
	 * 
	 * @property The property to get.
	 */
	public String getProperty(String property) {
		return model.getProperty(property);
	}

	/**
	 * Get the part's representation as an object specific to the application
	 * rendering it.
	 * 
	 * @generated NOT
	 */
	public Object getRepresentation() {
		return view.getRepresentation();
	}

	/**
	 * Get the part's rotation in the x, y, and z directions
	 * 
	 * @generated NOT
	 */
	public double[] getRotation() {
		return view.getTransformation().getRotation();
	}

	/**
	 * Get the part's scale in the x, y, and z directions.
	 * 
	 * @generated NOT
	 */
	public double[] getScale() {
		return view.getTransformation().getScale();
	}

	/**
	 * Get the part's size.
	 * 
	 * @generated NOT
	 */
	public double getSize() {
		return view.getTransformation().getSize();
	}

	/**
	 * Get the part's skew in the x, y, and z directions
	 * 
	 * @generated NOT
	 */
	public double[] getSkew() {
		return view.getTransformation().getSkew();
	}

	/**
	 * Get the part's transformation.
	 * 
	 * @generated NOT
	 */
	public Transformation getTransformation() {
		return view.getTransformation();
	}

	/**
	 * Get the part's translation in the x, y, and z directions
	 * 
	 * @generated NOT
	 */
	public double[] getTranslation() {
		return view.getTransformation().getTranslation();
	}

	/**
	 * Remove an entity from the part's child entities.
	 * 
	 * @generated NOT
	 * 
	 * @entity The entity to be removed
	 */
	public void removeEntity(AbstractController entity) {
		model.removeEntity(entity);
	}

	/**
	 * Set the given property to a new value
	 * 
	 * @generated NOT
	 * 
	 * @property The property to modify
	 * @value The property's new value
	 */
	public void setProperty(String property, String value) {
		model.setProperty(property, value);
	}

	/**
	 * Set the part's rotation in the x, y, and x directions.
	 * 
	 * @generated NOT
	 * 
	 * @x The amount of x rotation to apply
	 * @y The amount of y rotation to apply
	 * @z The amount of z rotation to apply
	 */
	public void setRotation(double x, double y, double z) {
		view.getTransformation().setRotation(x, y, z);
	}

	/**
	 * Set the part's scale in the x, y, and x directions.
	 * 
	 * @generated NOT
	 * 
	 * @x The part's x scale
	 * @y The part's y scale
	 * @z The part's z scale
	 */
	public void setScale(double x, double y, double z) {
		view.getTransformation().setScale(x, y, z);
	}

	/**
	 * Set the part's size
	 * 
	 * @generated NOT
	 * 
	 * @newSize The new multiplier for the part's size
	 */
	public void setSize(double newSize) {
		view.getTransformation().setSize(newSize);
	}

	/**
	 * Set the part's skew in the x, y, and x directions.
	 * 
	 * @generated NOT
	 * 
	 * @x The amount of x skew to apply
	 * @y The amount of y skew to apply
	 * @z The amount of z skew to apply
	 */
	public void setSkew(double x, double y, double z) {
		view.getTransformation().setSkew(x, y, z);
	}

	/**
	 * Set the part's transformation
	 * 
	 * @generated NOT
	 * 
	 * @newTransformation The transformation to apply to the part.
	 */
	public void setTransformation(Transformation newTransformation) {
		view.setTransformation(newTransformation);
	}

	/**
	 * Set the part's translation in the x, y, and x directions.
	 * 
	 * @generated NOT
	 * 
	 * @x The amount of x translation to apply
	 * @y The amount of y translation to apply
	 * @z The amount of z translation to apply
	 */
	public void setTranslation(double x, double y, double z) {
		view.getTransformation().setTranslation(x, y, z);
	}

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
	public void addEntityByCategory(AbstractController newEntity,
			String category) {
		model.addEntityByCategory(newEntity, category);
	}

	/**
	 * Getter method for the update manager.
	 * 
	 * @return The update subscription manager
	 */
	public UpdateableSubscriptionManager getUpdateManager() {
		return updateManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object otherObject) {

		// Check if the two references are to the same object
		if (this == otherObject) {
			return true;
		}

		// Check that the other object is an AbstractController
		if (!(otherObject instanceof AbstractController)) {
			return false;
		}

		// Cast the other object
		AbstractController castObject = (AbstractController) otherObject;

		// Check that the model and view are equal
		if (!model.equals(castObject.model) || !view.equals(castObject.view)) {
			return false;
		}

		// All checks passed, so the two are equal.
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() {

		// Create a copy of the model
		AbstractController clone = new AbstractController();
		clone.copy(this);

		// Refresh the view to be in sync with the model
		clone.refresh();

		return clone;
	}

	/**
	 * Deep copy the given object's data into this one.
	 * 
	 * @param otherObject
	 *            The object to copy into this one.
	 */
	public void copy(AbstractController otherObject) {

		// Create the model and give it a reference to this
		model = new AbstractMesh();
		model.setController(this);

		// Copy the other object's data members
		model.copy(otherObject.model);
		view = (AbstractView) otherObject.view.clone();

		// Register as a listener to the model and view
		model.register(this);
		view.register(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {

		int hash = 9;
		hash += 31 * model.hashCode();
		hash += 31 * view.hashCode();

		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.datastructures.VizObject.IVizUpdateable#
	 * unregister(org.eclipse.eavp.viz.service.datastructures.VizObject.
	 * IVizUpdateableListener)
	 */
	@Override
	public void unregister(IManagedUpdateableListener listener) {

		// Remove the listener from the list
		updateManager.unregister(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.datastructures.VizObject.
	 * IManagedVizUpdateableListener#update(org.eclipse.eavp.viz.service.
	 * datastructures.VizObject.IVizUpdateable,
	 * org.eclipse.eavp.viz.service.datastructures.VizObject.
	 * UpdateableSubscriptionType[])
	 */
	@Override
	public void update(IManagedUpdateable component, SubscriptionType[] type) {

		// Queue any messages from the view refresh
		updateManager.enqueue();

		// Refresh the view, if one exists
		if (view != null) {
			view.refresh(model);
		}

		// Notify own listeners of the change.
		updateManager.notifyListeners(type);
		updateManager.flushQueue();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.datastructures.VizObject.
	 * IManagedVizUpdateable#register(org.eclipse.eavp.viz.service.datastructures
	 * .VizObject.IVizUpdateableListener, java.util.ArrayList)
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
}
