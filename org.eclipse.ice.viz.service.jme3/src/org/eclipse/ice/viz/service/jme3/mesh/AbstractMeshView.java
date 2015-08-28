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
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

/**
 * <p >
 * This class provides a basic view for a component of a mesh.
 * </p>
 * <p >
 * The view is the part that the user sees. However, all interactions with the
 * view are processed via the controller. The view should only be updated within
 * the jME3 application's simpleUpdate() method.
 * </p>
 * 
 * @author Jordan H. Deyton
 */
public abstract class AbstractMeshView {
	/**
	 * <p>
	 * The jME3 Geometry representing this view in the scene.
	 * </p>
	 * 
	 */
	protected Geometry geometry;

	/**
	 * <p>
	 * The default constructor. This creates a jME3 Geometry that can be
	 * attached to the scene.
	 * </p>
	 * 
	 */
	public AbstractMeshView(String name, Material material) {

		// Initialize the Geometry used by this view.
		geometry = new Geometry(name);

		// Set the Geometry's Material.
		geometry.setMaterial(material);

		return;
	}

	/**
	 * <p>
	 * Sets the jME3 Node that contains the view's Geometry.
	 * </p>
	 * 
	 * @param node
	 *            <p>
	 *            The new parent Node.
	 *            </p>
	 */
	public void setParentNode(Node node) {

		if (node != null) {
			node.attachChild(geometry);
		}
		return;
	}

	/**
	 * <p>
	 * Sets the color of the Geometry's material.
	 * </p>
	 * 
	 * @param color
	 *            <p>
	 *            The new color for the view's Geometry.
	 *            </p>
	 */
	public void setColor(ColorRGBA color) {

		geometry.getMaterial().setColor("Color", color);

		return;
	}

	// TODO Add to model.
	/**
	 * <p>
	 * Sets the size of the view.
	 * </p>
	 * 
	 * @param size
	 *            <p>
	 *            The new size of the view.
	 *            </p>
	 */
	public abstract void setSize(float size);

	/**
	 * <p>
	 * Disposes of the AbstractMeshView and its associated jME3 objects.
	 * </p>
	 * 
	 */
	public void dispose() {

		// Remove the Geometry from the parent Node.
		Node parent = geometry.getParent();
		if (parent != null) {
			parent.detachChild(geometry);
		}
		return;
	}

	/**
	 * <p>
	 * This operation is used to check equality between this AbstractMeshView
	 * and another AbstractMeshView. It returns true if the objects are equal
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
	 * This operation returns the hash value of the AbstractMeshView.
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
	 * This operation copies the contents of a AbstractMeshView into the current
	 * object using a deep copy.
	 * </p>
	 * 
	 * @param view
	 *            <p>
	 *            The object from which the values should be copied.
	 *            </p>
	 */
	public void copy(AbstractMeshView view) {
	}

	/**
	 * <p>
	 * This operation returns a clone of the AbstractMeshView using a deep copy.
	 * </p>
	 * 
	 * @return <p>
	 *         The new clone.
	 *         </p>
	 */
	@Override
	public Object clone() {
		return null;
	}
}