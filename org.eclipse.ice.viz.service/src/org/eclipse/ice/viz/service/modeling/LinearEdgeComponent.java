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

import java.util.List;

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
		List<Double> startPoint = ((Vertex) entities.get("Vertices").get(0))
				.getLocation();
		List<Double> endPoint = ((Vertex) entities.get("Vertices").get(1))
				.getLocation();
		return Math.sqrt(Math.pow(startPoint.get(0) + endPoint.get(0), 2)
				+ Math.pow(startPoint.get(1) + endPoint.get(1), 2)
				+ Math.pow(startPoint.get(2) + endPoint.get(2), 2));
	}
}
