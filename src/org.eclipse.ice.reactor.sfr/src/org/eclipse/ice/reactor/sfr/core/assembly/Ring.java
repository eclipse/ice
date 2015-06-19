/*******************************************************************************
 * Copyright (c) 2013, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.reactor.sfr.core.assembly;

import org.eclipse.ice.reactor.sfr.base.ISFRComponentVisitor;
import org.eclipse.ice.reactor.sfr.base.SFRComponent;
import org.eclipse.ice.reactor.sfr.core.Material;

/**
 * <p>
 * The ring class represents a single instance of a material at a particular
 * radial coordinate within a cylindrical location of a SFRPin or SFRod. The
 * height variable on this class should uniformly represent the height from the
 * bottom of the MaterialBlock (or z-displacement) to help compensate for
 * varying types of materials throughout a vertical segment of a pin or rod.
 * </p>
 * 
 * @author Anna Wojtowicz
 */
public class Ring extends SFRComponent implements Comparable<Ring> {
	/**
	 * <p>
	 * Material the ring is composed of.
	 * </p>
	 * 
	 */
	private Material material;
	/**
	 * <p>
	 * Height of the ring within the material block (z=0 at the bottom of the
	 * material block); must be equal to or greater than 0.
	 * </p>
	 * 
	 */
	private double height;
	/**
	 * <p>
	 * Inner radius of the ring, must equal to or greater than zero.
	 * </p>
	 * 
	 */
	private double innerRadius;
	/**
	 * <p>
	 * Outer radius of the ring; must be greater than zero.
	 * </p>
	 * 
	 */
	private double outerRadius;

	/**
	 * <p>
	 * Nullary constructor.
	 * </p>
	 * 
	 */
	public Ring() {

		// Set the ring name, description and ID
		setName("Ring 1");
		setDescription("Ring 1 Description");
		setId(1);

		// Set the height (z-displacement), inner radius, outer radius and
		// material
		height = 0.0;
		innerRadius = 0.0;
		outerRadius = 1.0;
		material = new Material();

	}

	/**
	 * <p>
	 * Parameterized constructor with name specified.
	 * </p>
	 * 
	 * @param name
	 *            Name of the ring.
	 */
	public Ring(String name) {

		// Call the nullary constructor
		this();

		// Set the passed name
		setName(name);

	}

	/**
	 * <p>
	 * Parameterized constructor with name, material, ring height and outer
	 * radius specified.
	 * </p>
	 * 
	 * @param name
	 *            Name of the ring.
	 * @param material
	 *            Material the ring is composed of.
	 * @param height
	 *            Height of the ring within the material block.
	 * @param innerRadius
	 *            Inner radius of the ring. Must be non-negative.
	 * @param outerRadius
	 *            Outer radius of the ring. Must be non-negative.
	 */
	public Ring(String name, Material material, double height,
			double innerRadius, double outerRadius) {

		// Call the nullary constructor
		this();

		// Set the name, material, height and radii
		setName(name);
		setMaterial(material);
		setHeight(height);
		setInnerRadius(innerRadius);
		setOuterRadius(outerRadius);

	}

	/**
	 * <p>
	 * Sets the ring height (z-displacement within the material block).
	 * </p>
	 * 
	 * @param height
	 *            The height of the ring. Must be non-negative.
	 */
	public void setHeight(double height) {

		// If height is non-negative, set height
		if (height >= 0.0) {
			this.height = height;
		}
	}

	/**
	 * <p>
	 * Returns the ring height (z-displacement within the material block) as a
	 * double.
	 * </p>
	 * 
	 * @return The height of the ring.
	 */
	public double getHeight() {

		return height;

	}

	/**
	 * <p>
	 * Sets the ring inner radius.
	 * </p>
	 * 
	 * @param innerRadius
	 *            The inner radius of the ring. Must be non-negative.
	 */
	public void setInnerRadius(double innerRadius) {

		// If inner radius is non-negative
		if (innerRadius >= 0.0) {
			this.innerRadius = innerRadius;
		}
	}

	/**
	 * <p>
	 * Returns the ring inner radius as a double.
	 * </p>
	 * 
	 * @return The inner radius of the ring.
	 */
	public double getInnerRadius() {

		return innerRadius;

	}

	/**
	 * <p>
	 * Sets the ring outer radius.
	 * </p>
	 * 
	 * @param outerRadius
	 *            The outer radius of the ring. Must be non-negative.
	 */
	public void setOuterRadius(double outerRadius) {

		// If outer radius is non-negative
		if (outerRadius >= 0.0) {
			this.outerRadius = outerRadius;
		}
	}

	/**
	 * <p>
	 * Returns the ring's outer radius as a double.
	 * </p>
	 * 
	 * @return The outer radius of the ring.
	 */
	public double getOuterRadius() {

		return this.outerRadius;

	}

	/**
	 * <p>
	 * Sets the ring material.
	 * </p>
	 * 
	 * @param material
	 *            The material the ring is composed of. Cannot be null.
	 */
	public void setMaterial(Material material) {

		// If material is non-null
		if (material != null) {
			this.material = material;
		}
	}

	/**
	 * <p>
	 * Returns the material of the ring.
	 * </p>
	 * 
	 * @return A reference to the ring's material.
	 */
	public Material getMaterial() {

		return material;

	}

	/**
	 * <!-- begin-UML-doc --> Compares "this" ring to the input parameter ring
	 * ("that" ring). Returns -1 if this ring is inside that ring; returns +1 if
	 * this ring is outside that ring; returns 0 if the rings overlap, either
	 * partially or completely (a physical impossibility). <!-- end-UML-doc -->
	 * 
	 * @param ring
	 *            The ring being compared to.
	 * @return Returns -1 if this ring is inside that ring; +1 if this ring is
	 *         outside that ring; 0 if the rings overlap.
	 */
	@Override
	public int compareTo(Ring ring) {

		// Check if this ring is inside that ring
		if (outerRadius <= ring.innerRadius) {
			return -1;
		}
		// Check if this ring is outside that ring
		if (innerRadius >= ring.outerRadius) {
			return 1;
		}
		// If this inner < that inner, this ring could be either inside that
		// ring OR overlapping, depending on this.outerRadius
		if (innerRadius < ring.innerRadius) {

			// Check if this ring is inside that ring
			if (outerRadius <= ring.innerRadius) {
				return -1;
			}
			// Otherwise the rings overlap
			else {
				return 0;
			}
		}

		// If this outer > that outer, this ring could be either outside that
		// ring, OR overlapping, depending on this.innerRadius
		if (outerRadius > ring.outerRadius) {

			// Check if this ring is outside that ring
			if (innerRadius >= ring.outerRadius) {
				return 1;
			}
			// Otherwise the rings overlap
			else {
				return 0;
			}
		}

		// Otherwise, all other scenarios, the rings must overlap
		return 0;

	}

	/**
	 * <p>
	 * Returns the hashcode of the object.
	 * </p>
	 * 
	 * @return The hashcode of the object.
	 */
	@Override
	public int hashCode() {

		// Hash based upon superclass hash
		int hash = super.hashCode();

		// Add the hashes for all of the variables.
		hash += 31 * height;
		hash += 31 * innerRadius;
		hash += 31 * outerRadius;
		hash += 31 * material.hashCode();

		// Return hash
		return hash;
	}

	/**
	 * <p>
	 * Compares the contents of objects and returns true if that are identical.
	 * </p>
	 * 
	 * @param otherObject
	 *            The other object being compared against.
	 * @return Returns true if the both objects are equal, otherwise false.
	 */
	@Override
	public boolean equals(Object otherObject) {

		// Check if otherObject is valid
		if (otherObject != null && otherObject instanceof Ring) {

			// Cast otherObject to Ring so we can access its attributes/methods
			Ring otherRing = (Ring) otherObject;

			// Method will check for equality on two levels: shallow (within the
			// scope of the Ring class), and deep (all inherited variables
			// from superclass). Will only return true if both cases are true.

			// Create flags for checking shallow and deep equality, default to
			// false
			boolean shallowEqual = false;
			boolean deepEqual = false;

			// Check if height, radii and material (shallow scope) are equal
			if (height == otherRing.height
					&& innerRadius == otherRing.innerRadius
					&& outerRadius == otherRing.outerRadius
					&& material.equals(otherRing.material)) {
				shallowEqual = true;
			}
			// Check if all inherited variables are equal (deep scope)
			if (super.equals(otherRing) && shallowEqual) {
				deepEqual = true;
			}

			// Return final result
			return deepEqual;
		}

		// Otherwise otherObject was invalid, return false
		else {
			return false;
		}

	}

	/**
	 * <p>
	 * Deep copies the contents of the object.
	 * </p>
	 * 
	 * @param otherObject
	 *            The other object to copy the contents of.
	 */
	public void copy(Ring otherObject) {

		// Check if otherObject in invalid
		if (otherObject == null) {
			return;
		}
		// Call the superclass copy operation
		super.copy(otherObject);

		// Copy the height, radii and material
		height = otherObject.height;
		innerRadius = otherObject.innerRadius;
		outerRadius = otherObject.outerRadius;
		material = otherObject.material;

		return;
	}

	/**
	 * <p>
	 * Deep copies and returns a newly instantiated object.
	 * </p>
	 * 
	 * @return The newly instantiated object.
	 */
	@Override
	public Object clone() {

		// Create a new ring
		Ring ring = new Ring();

		// Copy the contents from this ring into the new one
		ring.copy(this);

		// Return the new ring
		return ring;

	}

	/**
	 * Overrides the default behavior (ignore) from SFRComponent and implements
	 * the accept operation for this SFRComponent's type.
	 */
	@Override
	public void accept(ISFRComponentVisitor visitor) {

		if (visitor != null) {
			visitor.visit(this);
		}

		return;
	}
}