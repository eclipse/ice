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

/**
 * A mesh component representing a straight line between two Vertices.
 * 
 * @author Robert Smith
 *
 */
public class LinearEdgeComponent extends EdgeComponent {

	/**
	 * The default constructor.
	 */
	public LinearEdgeComponent() {
		super();
	}

	/**
	 * The default constructor. It creates an edge between the two specified
	 * vertices.
	 * 
	 * @param start
	 * @param end
	 */
	public LinearEdgeComponent(Vertex start, Vertex end) {
		super(start, end);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.modeling.Edge#calculateLength()
	 */
	@Override
	public double calculateLength() {

		// Calculate the distance between the start and end points
		double[] startPoint = ((Vertex) entities.get("Vertices").get(0))
				.getLocation();
		double[] endPoint = ((Vertex) entities.get("Vertices").get(1))
				.getLocation();
		length = Math.sqrt(Math.pow(startPoint[0] + endPoint[0], 2)
				+ Math.pow(startPoint[1] + endPoint[1], 2)
				+ Math.pow(startPoint[2] + endPoint[2], 2));

		return length;
	}
}
