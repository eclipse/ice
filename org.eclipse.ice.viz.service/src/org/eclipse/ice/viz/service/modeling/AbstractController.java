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
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.ice.viz.service.datastructures.VizObject.IVizUpdateable;
import org.eclipse.ice.viz.service.datastructures.VizObject.IVizUpdateableListener;

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
public class AbstractController implements IVizUpdateable, IVizUpdateableListener {

	/**
	 * The internal representation of this part.
	 */
	protected AbstractMeshComponent model;

	/**
	 * This part's representation in the graphics rendering program.
	 */
	protected AbstractView view;

	/**
	 * A flag for whether or not the part has been disposed.
	 */
	private AtomicBoolean disposed;

	private List<IVizUpdateableListener> listeners;

	/**
	 * The default constructor.
	 */
	public AbstractController() {
		disposed = new AtomicBoolean();
		disposed.set(false);
		listeners = new ArrayList<IVizUpdateableListener>();
	}

	/**
	 * Constructor for an AbstractController with its associated model and view.
	 * 
	 * @param model
	 *            The model to be managed.
	 * @param view
	 *            The model's view.
	 */
	public AbstractController(AbstractMeshComponent model, AbstractView view) {
		// Initialize the class variables
		this.model = model;
		this.view = view;
		disposed = new AtomicBoolean();
		disposed.set(false);
		listeners = new ArrayList<IVizUpdateableListener>();

		// Give model a reference to its controller
		model.setController(this);

		// Register as a listener to the model and view
		model.register(this);
		view.register(this);
	}

	/**
	 * Notify all listeners of an update.
	 */
	public void notifyListeners() {

		// If the listeners are empty, return
		if (this.listeners == null || this.listeners.isEmpty()) {
			return;
		}

		// Get a reference to self
		final AbstractController self = this;

//		// Create a thread object that notifies all listeners
//
//		Thread notifyThread = new Thread() {
//
//			@Override
//			public void run() {
				// Loop over all listeners and update them
				for (int i = 0; i < listeners.size(); i++) {
					listeners.get(i).update(self);
				}
//			}
//		};

//		// Start the thread
//		notifyThread.start();
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
	public void synched() {
		view.synched();
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
		boolean oldDisposed = disposed.getAndSet(newDisposed);
		notifyListeners();
	}

	/**
	 * Getter method for the part's model
	 * 
	 * @return The controller's managed model.
	 */
	public AbstractMeshComponent getModel() {
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
	public void addEntityByCategory(AbstractController newEntity, String category) {
		model.addEntityByCategory(newEntity, category);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() {

		// Create a new controller, and make it a copy of this
		AbstractController clone = new AbstractController();
		clone.copy(this);
		return clone;
	}

	/**
	 * Deep copy the given object's data into this one.
	 * 
	 * @param otherObject
	 *            The object to copy into this one.
	 */
	public void copy(AbstractController otherObject) {

		// Clone the other object's data members
		model = (AbstractMeshComponent) otherObject.model.clone();
		view = (AbstractView) otherObject.view.clone();
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

		// If the update came from he component, send it to the view so that it
		// can refresh.
		if (component == model) {
			view.refresh(model);
		}

		// Notify own listeners of the change.
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

		// Add the listener to the list if it is not already there.
		if (!listeners.contains(listener)) {
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

		// Remove the lsitener from the list
		listeners.remove(listener);

	}
}
