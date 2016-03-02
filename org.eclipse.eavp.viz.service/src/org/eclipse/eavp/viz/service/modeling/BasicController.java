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
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.eavp.viz.service.datastructures.VizObject.IManagedUpdateable;
import org.eclipse.eavp.viz.service.datastructures.VizObject.IManagedUpdateableListener;
import org.eclipse.eavp.viz.service.datastructures.VizObject.SubscriptionType;
import org.eclipse.eavp.viz.service.datastructures.VizObject.UpdateableSubscriptionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A base implementation of IController.
 * 
 * @author Robert Smith
 */
public class BasicController
		implements IManagedUpdateable, IManagedUpdateableListener, IController {

	/**
	 * The internal representation of this part.
	 */
	protected BasicMesh model;

	/**
	 * This part's representation in the graphics rendering program.
	 */
	protected BasicView view;

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
			.getLogger(IController.class);

	/**
	 * The default constructor.
	 */
	public BasicController() {
		disposed = new AtomicBoolean();
		disposed.set(false);
		updateManager = new UpdateableSubscriptionManager(this);
	}

	/**
	 * Constructor for an IController with its associated model and view.
	 * 
	 * @param model
	 *            The model to be managed.
	 * @param view
	 *            The model's view.
	 */
	public BasicController(BasicMesh model, BasicView view) {

		// Check that the model and view are valid
		if (model == null || view == null) {
			throw new IllegalArgumentException(
					"An IController must have a non-null model and view.");
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.modeling.IController#refresh()
	 */
	@Override
	public void refresh() {
		view.refresh(model);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.modeling.IController#getDisposed()
	 */
	@Override
	public AtomicBoolean getDisposed() {
		return disposed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.modeling.IController#setDisposed(boolean)
	 */
	@Override
	public void setDisposed(boolean newDisposed) {

		// Atomically set the controller as disposed
		disposed.getAndSet(newDisposed);

		SubscriptionType[] eventType = new SubscriptionType[1];
		eventType[0] = SubscriptionType.PROPERTY;
		updateManager.notifyListeners(eventType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.modeling.IController#getModel()
	 */
	@Override
	public IMesh getModel() {
		return model;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.modeling.IController#getView()
	 */
	@Override
	public BasicView getView() {
		return view;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.modeling.IController#setView(org.eclipse.
	 * eavp.viz.service.modeling.AbstractView)
	 */
	@Override
	public void setView(BasicView newView) {

		// Log an error and fail silently if the view is null
		if (newView == null) {
			logger.error("Attempted to set an IController's view to null.");
			return;
		}

		view = newView;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.modeling.IController#addEntity(org.eclipse.
	 * eavp.viz.service.modeling.IController)
	 */
	@Override
	public void addEntity(IController newEntity) {
		model.addEntity(newEntity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.modeling.IController#dispose()
	 */
	@Override
	public void dispose() {
		setDisposed(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.modeling.IController#getEntities()
	 */
	@Override
	public ArrayList<IController> getEntities() {
		return model.getEntities();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.modeling.IController#getEntitiesByCategory(
	 * java.lang.String)
	 */
	@Override
	public ArrayList<IController> getEntitiesFromCategory(
			IMeshCategory category) {
		return model.getEntitiesFromCategory(category);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.modeling.IController#getEntitiesFromCategory
	 * (org.eclipse.eavp.viz.service.modeling.IMeshCategory, java.lang.Class)
	 */
	@Override
	public <T extends IController> ArrayList<T> getEntitiesFromCategory(
			IMeshCategory category, Class<T> returnType) {
		return model.getEntitiesFromCategory(category, returnType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.modeling.IController#getProperty(java.lang.
	 * String)
	 */
	@Override
	public String getProperty(IMeshProperty property) {
		return model.getProperty(property);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.modeling.IController#getRepresentation()
	 */
	@Override
	public Representation getRepresentation() {
		return view.getRepresentation();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.modeling.IController#getRotation()
	 */
	@Override
	public double[] getRotation() {
		return view.getTransformation().getRotation();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.modeling.IController#getScale()
	 */
	@Override
	public double[] getScale() {
		return view.getTransformation().getScale();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.modeling.IController#getSize()
	 */
	@Override
	public double getSize() {
		return view.getTransformation().getSize();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.modeling.IController#getSkew()
	 */
	@Override
	public double[] getSkew() {
		return view.getTransformation().getSkew();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.modeling.IController#getTransformation()
	 */
	@Override
	public Transformation getTransformation() {
		return view.getTransformation();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.modeling.IController#getTranslation()
	 */
	@Override
	public double[] getTranslation() {
		return view.getTransformation().getTranslation();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.modeling.IController#removeEntity(org.
	 * eclipse.eavp.viz.service.modeling.IController)
	 */
	@Override
	public void removeEntity(IController entity) {
		model.removeEntity(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.modeling.IController#setProperty(java.lang.
	 * String, java.lang.String)
	 */
	@Override
	public void setProperty(IMeshProperty property, String value) {
		model.setProperty(property, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.modeling.IController#setRotation(double,
	 * double, double)
	 */
	@Override
	public void setRotation(double x, double y, double z) {
		view.getTransformation().setRotation(x, y, z);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.modeling.IController#setScale(double,
	 * double, double)
	 */
	@Override
	public void setScale(double x, double y, double z) {
		view.getTransformation().setScale(x, y, z);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.modeling.IController#setSize(double)
	 */
	@Override
	public void setSize(double newSize) {
		view.getTransformation().setSize(newSize);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.modeling.IController#setSkew(double,
	 * double, double)
	 */
	@Override
	public void setSkew(double x, double y, double z) {
		view.getTransformation().setSkew(x, y, z);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.modeling.IController#setTransformation(org.
	 * eclipse.eavp.viz.service.modeling.Transformation)
	 */
	@Override
	public void setTransformation(Transformation newTransformation) {
		view.setTransformation(newTransformation);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.modeling.IController#setTranslation(double,
	 * double, double)
	 */
	@Override
	public void setTranslation(double x, double y, double z) {
		view.getTransformation().setTranslation(x, y, z);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.modeling.IController#addEntityByCategory(org
	 * .eclipse.eavp.viz.service.modeling.IController, java.lang.String)
	 */
	@Override
	public void addEntityToCategory(IController newEntity,
			IMeshCategory category) {
		model.addEntityToCategory(newEntity, category);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.modeling.IController#getUpdateManager()
	 */
	@Override
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

		// Check that the other object is an IController
		if (!(otherObject instanceof IController)) {
			return false;
		}

		// Cast the other object
		BasicController castObject = (BasicController) otherObject;

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
		BasicController clone = new BasicController();
		clone.copy(this);

		// Refresh the view to be in sync with the model
		clone.refresh();

		return clone;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.modeling.IController#copy(org.eclipse.eavp.
	 * viz.service.modeling.IController)
	 */
	@Override
	public void copy(IController otherObject) {

		// Check that the source object is an AbstractController, failing
		// silently if not and casting it if so
		if (!(otherObject instanceof IController)) {
			return;
		}
		BasicController castObject = (BasicController) otherObject;

		// Create the model and give it a reference to this
		model = (BasicMesh) castObject.model.clone();
		model.setController(this);

		// Copy the other object's data members
		view = (BasicView) castObject.view.clone();

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
	 * @see
	 * org.eclipse.eavp.viz.service.datastructures.VizObject.IVizUpdateable#
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
	 * IManagedVizUpdateable#register(org.eclipse.eavp.viz.service.
	 * datastructures .VizObject.IVizUpdateableListener, java.util.ArrayList)
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
	 * @see org.eclipse.eavp.viz.service.modeling.IController#isRoot()
	 */
	@Override
	public boolean isRoot() {
		return "True".equals(model.getProperty(MeshProperty.ROOT));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.modeling.IController#isSelected()
	 */
	@Override
	public boolean isSelected() {
		return "True".equals(model.getProperty(MeshProperty.SELECTED));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.modeling.IController#setRoot(boolean)
	 */
	@Override
	public void setRoot(boolean root) {

		// Set the model's root property to a string representation of the
		// boolean
		if (root) {
			model.setProperty(MeshProperty.ROOT, "True");
		} else {
			model.setProperty(MeshProperty.ROOT, "False");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.modeling.IController#setSelected(boolean)
	 */
	@Override
	public void setSelected(boolean selected) {

		// Set the model's selected property to a string representation of the
		// boolean
		if (selected) {
			model.setProperty(MeshProperty.SELECTED, "True");
		} else {
			model.setProperty(MeshProperty.SELECTED, "False");
		}
	}
}
