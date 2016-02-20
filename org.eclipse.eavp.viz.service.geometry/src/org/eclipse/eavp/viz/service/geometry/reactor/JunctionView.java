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
package org.eclipse.eavp.viz.service.geometry.reactor;

import org.eclipse.eavp.viz.service.modeling.AbstractView;

/**
 * An interface for AbstractViews which represent JunctionComponents.
 * 
 * @author Robert Smith
 *
 */
public class JunctionView extends AbstractView {

	/**
	 * The x coordinate of the junction's center
	 */
	protected double centerX = 0;

	/**
	 * The y coordinate of the junction's center
	 */
	protected double centerY = 0;

	/**
	 * The z coordinate of the junction's center
	 */
	protected double centerZ = 0;

	/**
	 * Gets the center of the box representing the Junction
	 * 
	 * @return An array of the coordinates of the junciton's center, in the
	 *         order x, y, z
	 */
	public double[] getCenter() {

		double[] center = new double[3];
		center[0] = centerX;
		center[1] = centerY;
		center[2] = centerZ;

		return center;

	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() {

		// Create a new component, and make it a copy of this one.
		JunctionView clone = new JunctionView();
		clone.copy(this);
		return clone;
	}
}
