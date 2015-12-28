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

import org.eclipse.ice.viz.service.datastructures.VizObject.IManagedVizUpdateable;
import org.eclipse.ice.viz.service.datastructures.VizObject.UpdateableSubscriptionType;

/**
 * A controller for an Edge part.
 * 
 * @author Robert Smith
 *
 */
public class Edge extends AbstractController {

	/**
	 * The default constructor.
	 * 
	 * @param model
	 *            The controller's model
	 * @param view
	 *            The controller's view
	 */
	public Edge(EdgeComponent model, AbstractView view) {
		super(model, view);
	}

	/**
	 * Gets the location for the edge's first vertex.
	 *
	 * @return A list of the vertex's 3D coordinates
	 */
	public double[] getStartLocation() {
		return ((EdgeComponent) model).getStartLocation();
	}

	/**
	 * Gets the location for the edge's second vertex
	 * 
	 * @return A list of the vertex's 3D coordinates
	 */
	public double[] getEndLocation() {
		return ((EdgeComponent) model).getEndLocation();
	}

	/**
	 * Get the edge's length.
	 * 
	 * @return The edge's length
	 */
	public double getLength() {
		return ((EdgeComponent) model).getLength();
	}

	@Override
	public void update(IManagedVizUpdateable component,
			UpdateableSubscriptionType[] type) {

		// Recalculate the edge's length
		((EdgeComponent) model).calculateLength();

		super.update(component, type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.modeling.AbstractController#clone()
	 */
	@Override
	public Object clone() {

		// Clone the model and view
		EdgeComponent modelClone = (EdgeComponent) model.clone();
		AbstractView viewClone = (AbstractView) view.clone();

		// Create a new controller for the clones and return it
		Edge clone = new Edge(modelClone, viewClone);
		return clone;
	}
}
