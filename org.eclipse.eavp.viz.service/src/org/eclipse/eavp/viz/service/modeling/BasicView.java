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

import org.eclipse.eavp.viz.service.datastructures.VizObject.IManagedUpdateable;
import org.eclipse.eavp.viz.service.datastructures.VizObject.IManagedUpdateableListener;
import org.eclipse.eavp.viz.service.datastructures.VizObject.IVizUpdateableListener;
import org.eclipse.eavp.viz.service.datastructures.VizObject.SubscriptionType;
import org.eclipse.eavp.viz.service.datastructures.VizObject.UpdateableSubscriptionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A base implementation of IView.
 * 
 * @author Robert Smith
 */
public class BasicView
		implements IManagedUpdateableListener, IManagedUpdateable, IView {

	/**
	 * The transformation representing the part's intended state. This may not
	 * reflect how the graphics program is currently displaying the part. For
	 * that, see previousTransformation.
	 */
	protected Transformation transformation;

	/**
	 * The last transformation which was applied by the rendering engine.
	 */
	private Transformation previousTransformation;

	/**
	 * The list of listeners observing this object.
	 */
	private ArrayList<IVizUpdateableListener> listeners;

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(BasicController.class);

	/**
	 * The listeners registered for updates from this object.
	 */
	protected UpdateableSubscriptionManager updateManager = new UpdateableSubscriptionManager(
			this);

	/**
	 * The default constructor.
	 */
	public BasicView() {
		// Initialize the class variables
		transformation = new Transformation();
		previousTransformation = new Transformation();
		listeners = new ArrayList<IVizUpdateableListener>();

		// Register as a listener to the part's transformation.
		transformation.register(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.modeling.IView#getTransformation()
	 */
	@Override
	public Transformation getTransformation() {
		return transformation;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.modeling.IView#setTransformation(org.eclipse
	 * .eavp.viz.service.modeling.Transformation)
	 */
	@Override
	public void setTransformation(Transformation newTransformation) {

		// If the transformation is null, log an error and fail silently
		if (newTransformation == null) {
			logger.error("An AbstractView's transformation must not be null.");
		}

		transformation = newTransformation;

		// Notify own listeners of the change
		SubscriptionType[] eventTypes = { SubscriptionType.TRANSFORMATION };
		updateManager.notifyListeners(eventTypes);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.modeling.IView#getRepresentation()
	 */
	@Override
	public Representation getRepresentation() {
		// Nothing to do.
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.modeling.IView#refresh(org.eclipse.eavp.viz.
	 * service.modeling.IMesh)
	 */
	@Override
	public void refresh(IMesh model) {
		// Nothing to do
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.datastructures.VizObject.IVizUpdateable#
	 * register(org.eclipse.eavp.viz.service.datastructures.VizObject.
	 * IVizUpdateableListener)
	 */
	@Override
	public void register(IManagedUpdateableListener listener) {
		updateManager.register(listener);

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
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object otherObject) {

		// Check if the objects are the same
		if (this == otherObject) {
			return true;
		}

		// Check that the other object is an abstractview
		if (!(otherObject instanceof BasicView)) {
			return false;
		}

		// Check that the transformations are equal
		if (!(transformation
				.equals(((IView) otherObject).getTransformation()))) {
			return false;
		}

		// If all checks passed, the objects are equal
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.datastructures.VizObject.
	 * IManagedVizUpdateableListener#update(org.eclipse.eavp.viz.service.
	 * datastructures.VizObject.IVizUpdateable,
	 * org.eclipse.eavp.viz.service.datastructures.VizObject.
	 * UpdateableSubscription[])
	 */
	@Override
	public void update(IManagedUpdateable component, SubscriptionType[] type) {

		// Pass the update to own listeners
		updateManager.notifyListeners(type);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() {

		// Create a new AbstractView and make it a copy of this
		IView clone = new BasicView();
		clone.copy(this);

		return clone;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.modeling.IView#copy(org.eclipse.eavp.viz.
	 * service.modeling.AbstractView)
	 */
	@Override
	public void copy(IView otherObject) {

		// Check that the source object is an IView, failing
		// silently if not and casting it if so
		if (!(otherObject instanceof BasicView)) {
			return;
		}
		BasicView castObject = (BasicView) otherObject;

		// Copy the other view's data members
		transformation = (Transformation) castObject.transformation.clone();
		previousTransformation = (Transformation) castObject.previousTransformation
				.clone();

		// Notify own listeners of the change
		SubscriptionType[] eventTypes = { SubscriptionType.PROPERTY };
		updateManager.notifyListeners(eventTypes);
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
		hash += 31 * transformation.hashCode();
		hash += 31 * previousTransformation.hashCode();
		return hash;
	}
}
