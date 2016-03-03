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

import org.eclipse.eavp.viz.service.datastructures.VizObject.IManagedUpdateable;
import org.eclipse.eavp.viz.service.datastructures.VizObject.SubscriptionType;

/**
 * A controller for an Edge part.
 * 
 * @author Robert Smith
 *
 */
public class EdgeController extends BasicController {

	/**
	 * The default constructor.
	 * 
	 * @param model
	 *            The controller's model
	 * @param view
	 *            The controller's view
	 */
	public EdgeController(EdgeMesh model, BasicView view) {
		super(model, view);
	}

	/**
	 * Gets the location for the edge's first vertex.
	 *
	 * @return A list of the vertex's 3D coordinates
	 */
	public double[] getStartLocation() {
		return ((EdgeMesh) model).getStartLocation();
	}

	/**
	 * Gets the location for the edge's second vertex
	 * 
	 * @return A list of the vertex's 3D coordinates
	 */
	public double[] getEndLocation() {
		return ((EdgeMesh) model).getEndLocation();
	}

	/**
	 * Get the edge's length.
	 * 
	 * @return The edge's length
	 */
	public double getLength() {
		return ((EdgeMesh) model).getLength();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.modeling.AbstractController#update(org.
	 * eclipse.ice.viz.service.datastructures.VizObject.IManagedUpdateable,
	 * org.eclipse.eavp.viz.service.datastructures.VizObject.SubscriptionType[])
	 */
	@Override
	public void update(IManagedUpdateable component, SubscriptionType[] type) {

		// If the vertices were changed, recalculate the edge's length
		for (SubscriptionType event : type) {
			if (event == SubscriptionType.CHILD) {
				((EdgeMesh) model).calculateLength();
			}
		}

		super.update(component, type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.modeling.AbstractController#clone()
	 */
	@Override
	public Object clone() {

		// Clone the model and view
		EdgeMesh modelClone = (EdgeMesh) model.clone();
		BasicView viewClone = (BasicView) view.clone();

		// Create a new controller for the clones and return it
		EdgeController clone = new EdgeController(modelClone, viewClone);
		return clone;
	}
}