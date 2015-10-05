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
import com.jme3.scene.shape.Sphere;

/**
 * <p >
 * This class provides a basic view for a Vertex, i.e., a single sphere on the
 * grid.
 * </p>
 * 
 * @author Jordan H. Deyton
 */
public class VertexView extends AbstractMeshView {

	/**
	 * <p>
	 * The default constructor.
	 * </p>
	 * 
	 */
	public VertexView(String name, Material material) {
		super(name, material);

		// Create a Sphere mesh for the geometry.
		Sphere sphere = new Sphere(30, 30, 0.2f);
		geometry.setMesh(sphere);

		return;
	}

	/**
	 * <p>
	 * Sets the location of the view's Geometry in the jME3 scene.
	 * </p>
	 * 
	 * @param location
	 *            <p>
	 *            The new location of the Vertex's Geometry in the jME3 scene.
	 *            </p>
	 */
	public void setLocation(Vector3f location) {

		geometry.setLocalTranslation(location);

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
		((Sphere) geometry.getMesh()).updateGeometry(30, 30, size);
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
		return super.equals(otherObject);
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
		return super.hashCode();
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
	public void copy(VertexView view) {
		super.copy(view);
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
		//Create a new VertexView, initialized with this object's data
		VertexView object = new VertexView(geometry.getName(), geometry.getMaterial());
		
		//Clone this object
		object.copy(this);
		
		return object;
		
		
	}
}