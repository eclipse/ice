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

import java.util.List;

import org.eclipse.eavp.viz.service.datastructures.VizObject.IManagedUpdateable;
import org.eclipse.eavp.viz.service.datastructures.VizObject.SubscriptionType;
import org.eclipse.eavp.viz.service.modeling.AbstractController;
import org.eclipse.eavp.viz.service.modeling.AbstractMesh;

/**
 * A simple AbstractMesh implementation that keeps track of whether it has been
 * updated for testing purposes.
 * 
 * @author Robert Smith
 *
 */
public class TestMesh extends AbstractMesh {

	/**
	 * Whether the mesh has received an update since the last time it was
	 * tested.
	 */
	boolean updated = false;
	
	/**
	 * The nullary constructor.
	 */
	public TestMesh(){
		super();
	}

	/**
	 * The default constructor
	 * 
	 * @param entities
	 *            The list of child entities
	 */
	public TestMesh(List<AbstractController> entities) {
		super(entities);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.modeling.AbstractMesh#update(org.eclipse.ice.
	 * viz.service.datastructures.VizObject.IManagedVizUpdateable,
	 * org.eclipse.eavp.viz.service.datastructures.VizObject.
	 * UpdateableSubscriptionType[])
	 */
	@Override
	public void update(IManagedUpdateable component, SubscriptionType[] type) {
		updated = true;
		super.update(component, type);
	}

	/**
	 * Checks whether the controller has been updated and returns it to its
	 * default state.
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