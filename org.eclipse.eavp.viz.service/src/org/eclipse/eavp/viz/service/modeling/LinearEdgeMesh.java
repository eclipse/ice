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

/**
 * A mesh component representing a straight line between two Vertices.
 * 
 * @author Robert Smith
 *
 */
public class LinearEdgeMesh extends EdgeMesh {

	/**
	 * The default constructor.
	 */
	public LinearEdgeMesh() {
		super();
	}

	/**
	 * The default constructor. It creates an edge between the two specified
	 * vertices.
	 * 
	 * @param start
	 * @param end
	 */
	public LinearEdgeMesh(VertexController start, VertexController end) {
		super(start, end);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.modeling.Edge#calculateLength()
	 */
	@Override
	public double calculateLength() {

		// If the edge has two vertices, calculate its length
		if (getEntitiesFromCategory(MeshCategory.VERTICES).size() == 2) {

			// Calculate the distance between the start and end points
			double[] startPoint = ((VertexController) entities
					.get(MeshCategory.VERTICES).get(0)).getLocation();
			double[] endPoint = ((VertexController) entities
					.get(MeshCategory.VERTICES).get(1)).getLocation();
			length = Math.sqrt(Math.pow(startPoint[0] - endPoint[0], 2)
					+ Math.pow(startPoint[1] - endPoint[1], 2)
					+ Math.pow(startPoint[2] - endPoint[2], 2));
		}

		// Otherwise it is of length 0
		else {
			length = 0;
		}

		return length;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.modeling.AbstractMeshComponent#clone()
	 */
	@Override
	public Object clone() {

		// Create a new object
		LinearEdgeMesh clone = new LinearEdgeMesh();

		// Make it a copy of this and return it
		clone.copy(this);
		return clone;
	}
}
