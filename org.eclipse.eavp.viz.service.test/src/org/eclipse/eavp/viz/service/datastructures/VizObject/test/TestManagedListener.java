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
package org.eclipse.eavp.viz.service.datastructures.VizObject.test;

import java.util.ArrayList;

import org.eclipse.eavp.viz.service.datastructures.VizObject.IManagedUpdateable;
import org.eclipse.eavp.viz.service.datastructures.VizObject.IManagedUpdateableListener;
import org.eclipse.eavp.viz.service.datastructures.VizObject.SubscriptionType;

/**
 * A basic implementation of IManagedUpdateableListener for testing purposes.
 * 
 * @author Robert Smith
 *
 */
public class TestManagedListener implements IManagedUpdateableListener {

	/**
	 * Whether the listener has received an update of the property type
	 */
	private boolean propertyNotified = false;

	/**
	 * Whether the listener has received an update of the child type
	 */
	private boolean childNotified = false;

	/**
	 * Whether the listener has received an update of the all type
	 */
	private boolean allNotified = false;

	/**
	 * Whether the listener has received an update of the selection type
	 */
	private boolean selectionNotified = false;

	/**
	 * Whether the listener has received an update of the wireframe type
	 */
	private boolean wireframeNotified = false;

	/**
	 * Whether the listener has received an update of the transformation type
	 */
	private boolean transformationNotified = false;

	/**
	 * The list of types of events this listener will receive
	 */
	ArrayList<SubscriptionType> types;

	/**
	 * The default constructor.
	 * 
	 * @param types
	 *            The list of types of events this listener will receive.
	 */
	public TestManagedListener(ArrayList<SubscriptionType> types) {
		this.types = types;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.datastructures.VizObject.
	 * IManagedUpdateableListener#getSubscriptions(org.eclipse.eavp.viz.
	 * service.datastructures.VizObject.IManagedUpdateable)
	 */
	@Override
	public ArrayList<SubscriptionType> getSubscriptions(
			IManagedUpdateable source) {
		return types;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.datastructures.VizObject.
	 * IManagedUpdateableListener#update(org.eclipse.eavp.viz.service.
	 * datastructures.VizObject.IManagedUpdateable,
	 * org.eclipse.eavp.viz.service.datastructures.VizObject.
	 * UpdateableSubscriptionType[])
	 */
	@Override
	public void update(IManagedUpdateable component, SubscriptionType[] types) {

		// For each type in the update, set each notification received to
		// true if it matches one of the checked types
		for (SubscriptionType type : types) {

			if (type == SubscriptionType.PROPERTY) {
				propertyNotified = true;
			}

			else if (type == SubscriptionType.CHILD) {
				childNotified = true;
			}

			else if (type == SubscriptionType.ALL) {
				allNotified = true;
			}

			else if (type == SubscriptionType.SELECTION) {
				selectionNotified = true;
			}

			else if (type == SubscriptionType.WIREFRAME) {
				wireframeNotified = true;
			}

			else if (type == SubscriptionType.TRANSFORMATION) {
				transformationNotified = true;
			}
		}

	}

	/**
	 * Checks if the listener has received an ALL type notification and resets
	 * its state for the ALL type to the original, unnotified state.
	 * 
	 * @return True if the listener has received a notification of type ALL
	 *         since the last time this function was invoked. False otherwise.
	 */
	public boolean gotAll() {
		boolean temp = allNotified;
		allNotified = false;
		return temp;
	}

	/**
	 * Checks if the listener has received a CHILD type notification and resets
	 * its state for the CHILD type to the original, unnotified state.
	 * 
	 * @return True if the listener has received a notification of type CHILD
	 *         since the last time this function was invoked. False otherwise.
	 */
	public boolean gotChild() {
		boolean temp = childNotified;
		childNotified = false;
		return temp;
	}

	/**
	 * Checks if the listener has received an PROPERTY type notification and
	 * resets its state for the PROPERTY type to the original, unnotified state.
	 * 
	 * @return True if the listener has received a notification of type PROPERTY
	 *         since the last time this function was invoked. False otherwise.
	 */
	public boolean gotProperty() {
		boolean temp = propertyNotified;
		propertyNotified = false;
		return temp;
	}

	/**
	 * Checks if the listener has received an SELECTION type notification and
	 * resets its state for the SELECTION type to the original, unnotified
	 * state.
	 * 
	 * @return True if the listener has received a notification of type
	 *         SELECTION since the last time this function was invoked. False
	 *         otherwise.
	 */
	public boolean gotSelection() {
		boolean temp = selectionNotified;
		selectionNotified = false;
		return temp;
	}

	/**
	 * Checks if the listener has received an TRANSFORMATION type notification
	 * and resets its state for the TRANSFORMATION type to the original,
	 * unnotified state.
	 * 
	 * @return True if the listener has received a notification of type
	 *         TRANSFORMATION since the last time this function was invoked.
	 *         False otherwise.
	 */
	public boolean gotTransformation() {
		boolean temp = transformationNotified;
		transformationNotified = false;
		return temp;
	}

	/**
	 * Checks if the listener has received an WIREFRAME type notification and
	 * resets its state for the WIREFRAME type to the original, unnotified
	 * state.
	 * 
	 * @return True if the listener has received a notification of type
	 *         WIREFRAME since the last time this function was invoked. False
	 *         otherwise.
	 */
	public boolean gotWireframe() {
		boolean temp = wireframeNotified;
		wireframeNotified = false;
		return temp;
	}

}