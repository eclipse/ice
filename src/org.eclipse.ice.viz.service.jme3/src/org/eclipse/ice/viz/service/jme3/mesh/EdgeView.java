/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
 *   Claire Saunders, Matthew Wang, Anna Wojtowicz
 *******************************************************************************/
package org.eclipse.ice.viz.service.jme3.mesh;

import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.shape.Line;

/**
 * <p>
 * This class provides a basic view for an Edge, i.e., a line on the grid.
 * </p>
 * 
 * @author Jordan H. Deyton
 */
public class EdgeView extends AbstractMeshView {

	/**
	 * The mesh for the EdgeView's Geometry. This is currently a straight line.
	 */
	private Line line;

	/**
	 * <p>
	 * The default constructor.
	 * </p>
	 * 
	 */
	public EdgeView(String name, Material material) {
		super(name, material);

		// Create a Line mesh for the Geometry.
		line = new Line(Vector3f.ZERO, Vector3f.UNIT_X);
		line.setLineWidth(5f);
		geometry.setMesh(line);

		return;
	}

	/**
	 * Sets the start and end points for the EdgeView.
	 * 
	 * @param start
	 *            The start of the Edge.
	 * @param end
	 *            The end of the Edge.
	 */
	public void setLine(Vector3f start, Vector3f end) {

		line.updatePoints(start, end);

		return;
	}

	// TODO Add to model.
	/**
	 * Sets the current size of the vertex view's sphere.
	 * 
	 * @param size
	 *            The new radius of the vertex view's sphere.
	 */
	@Override
	public void setSize(float size) {
		line.setLineWidth(size);
	}

	/**
	 * <p>
	 * This operation is used to check equality between this VertexController
	 * and another VertexController. It returns true if the objects are equal
	 * and false if they are not.
	 * </p>
	 * 
	 * @param otherObject
	 *            <p>
	 *            The other object that should be compared with this one.
	 *            </p>
	 * @return <p>
	 *         True if the objects are equal, false otherwise.
	 *         </p>
	 */
	@Override
	public boolean equals(Object otherObject) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * <p>
	 * This operation returns the hash value of the VertexController.
	 * </p>
	 * 
	 * @return <p>
	 *         The hashcode of the object.
	 *         </p>
	 */
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * <p>
	 * This operation copies the contents of a VertexController into the current
	 * object using a deep copy.
	 * </p>
	 * 
	 * @param view
	 *            <p>
	 *            The object from which the values should be copied.
	 *            </p>
	 */
	public void copy(EdgeView view) {
		// TODO Auto-generated method stub

	}

	/**
	 * <p>
	 * This operation returns a clone of the VertexController using a deep copy.
	 * </p>
	 * 
	 * @return <p>
	 *         The new clone.
	 *         </p>
	 */
	@Override
	public Object clone() {
		// TODO Auto-generated method stub
		return null;
	}
}