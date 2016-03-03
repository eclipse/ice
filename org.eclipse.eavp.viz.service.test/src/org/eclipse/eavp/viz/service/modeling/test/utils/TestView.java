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
package org.eclipse.eavp.viz.service.modeling.test.utils;

import org.eclipse.eavp.viz.service.datastructures.VizObject.IManagedUpdateable;
import org.eclipse.eavp.viz.service.datastructures.VizObject.SubscriptionType;
import org.eclipse.eavp.viz.service.datastructures.VizObject.VizObject;
import org.eclipse.eavp.viz.service.modeling.BasicView;
import org.eclipse.eavp.viz.service.modeling.IMesh;
import org.eclipse.eavp.viz.service.modeling.Representation;

/**
 * A simple AbstractView implementation that keeps track of whether it has been
 * updated for testing purposes.
 * 
 * @author Robert Smith
 *
 */
public class TestView extends BasicView {

	/**
	 * A dummy variable allowing two TestViews to be equal or inequal to one
	 * another.
	 */
	private int data;

	/**
	 * Whether the view has been refreshed since the last time it was checked.
	 */
	private boolean refreshed = false;

	/**
	 * Whether the mesh has received an update since the last time it was
	 * tested.
	 */
	private boolean updated = false;

	/**
	 * The default constructor
	 */
	public TestView() {
		super();
		data = 0;
	}

	/**
	 * Set the test view's data member.
	 */
	public void setData(int data) {
		this.data = data;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.modeling.AbstractView#clone()
	 */
	@Override
	public Object clone() {

		// Create a new view
		TestView clone = new TestView();

		// Copy this object's data into the clone and return it
		clone.copy(this);
		clone.data = data;
		return clone;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.modeling.AbstractView#equals(java.lang.
	 * Object)
	 */
	@Override
	public boolean equals(Object otherObject) {

		// If the other object is null or not a TestView, they are not equal
		if (otherObject == null || !(otherObject instanceof TestView)) {
			return false;
		}

		// If the views have identical data, they are equal
		if (data == ((TestView) otherObject).data) {
			return true;
		}

		// Otherwise they are not
		else {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.modeling.AbstractView#getRepresentation()
	 */
	@Override
	public Representation<VizObject> getRepresentation() {
		return new Representation<VizObject>(new VizObject());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.modeling.AbstractView#refresh(org.eclipse.
	 * ice .viz.service.modeling.AbstractMesh)
	 */
	@Override
	public void refresh(IMesh mesh) {
		refreshed = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.modeling.AbstractView#update(org.eclipse.
	 * ice. viz.service.datastructures.VizObject.IManagedVizUpdateable,
	 * org.eclipse.eavp.viz.service.datastructures.VizObject.
	 * UpdateableSubscriptionType[])
	 */
	@Override
	public void update(IManagedUpdateable component, SubscriptionType[] type) {
		updated = true;
		super.update(component, type);
	}

	/**
	 * Checks whether the view has been refreshed and returns it to its default
	 * state.
	 * 
	 * @return True if the refresh() function has been invoked since the last
	 *         time this function was called. False if it has not.
	 */
	public boolean isRefreshed() {
		boolean temp = refreshed;
		refreshed = false;
		return temp;
	}

	/**
	 * Checks whether the view has been updated and returns it to its default
	 * state.
	 * 
	 * @return True if the mesh has received an update since the last time this
	 *         function was called. False if it has not.
	 */
	public boolean isUpdated() {
		boolean temp = updated;
		updated = false;
		return temp;
	}
}
