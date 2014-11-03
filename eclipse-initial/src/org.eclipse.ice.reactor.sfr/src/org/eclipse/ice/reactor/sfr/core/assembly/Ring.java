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
 * <!-- begin-UML-doc -->
 * <p>
 * The ring class represents a single instance of a material at a particular
 * radial coordinate within a cylindrical location of a SFRPin or SFRod. The
 * height variable on this class should uniformly represent the height from the
 * bottom of the MaterialBlock (or z-displacement) to help compensate for
 * varying types of materials throughout a vertical segment of a pin or rod.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author w5q
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class Ring extends SFRComponent implements Comparable<Ring> {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Material the ring is composed of.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private Material material;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Height of the ring within the material block (z=0 at the bottom of the
	 * material block); must be equal to or greater than 0.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private double height;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Inner radius of the ring, must equal to or greater than zero.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private double innerRadius;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Outer radius of the ring; must be greater than zero.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private double outerRadius;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Nullary constructor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Ring() {
		// begin-user-code

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

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Parameterized constructor with name specified.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param name
	 *            Name of the ring.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Ring(String name) {
		// begin-user-code

		// Call the nullary constructor
		this();

		// Set the passed name
		setName(name);

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Parameterized constructor with name, material, ring height and outer
	 * radius specified.
	 * </p>
	 * <!-- end-UML-doc -->
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
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Ring(String name, Material material, double height,
			double innerRadius, double outerRadius) {
		// begin-user-code

		// Call the nullary constructor
		this();

		// Set the name, material, height and radii
		setName(name);
		setMaterial(material);
		setHeight(height);
		setInnerRadius(innerRadius);
		setOuterRadius(outerRadius);

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the ring height (z-displacement within the material block).
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param height
	 *            The height of the ring. Must be non-negative.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setHeight(double height) {
		// begin-user-code

		// If height is non-negative, set height
		if (height >= 0.0) {
			this.height = height;
		}
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the ring height (z-displacement within the material block) as a
	 * double.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return The height of the ring.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public double getHeight() {
		// begin-user-code

		return height;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the ring inner radius.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param innerRadius
	 *            The inner radius of the ring. Must be non-negative.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setInnerRadius(double innerRadius) {
		// begin-user-code

		// If inner radius is non-negative
		if (innerRadius >= 0.0) {
			this.innerRadius = innerRadius;
		}
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the ring inner radius as a double.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return The inner radius of the ring.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public double getInnerRadius() {
		// begin-user-code

		return innerRadius;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the ring outer radius.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param outerRadius
	 *            The outer radius of the ring. Must be non-negative.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setOuterRadius(double outerRadius) {
		// begin-user-code

		// If outer radius is non-negative
		if (outerRadius >= 0.0) {
			this.outerRadius = outerRadius;
		}
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the ring's outer radius as a double.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return The outer radius of the ring.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public double getOuterRadius() {
		// begin-user-code

		return this.outerRadius;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the ring material.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param material
	 *            The material the ring is composed of. Cannot be null.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setMaterial(Material material) {
		// begin-user-code

		// If material is non-null
		if (material != null) {
			this.material = material;
		}
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the material of the ring.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Material getMaterial() {
		// begin-user-code

		return material;

		// end-user-code
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
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
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
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the hashcode of the object.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return The hashcode of the object.
	 */
	public int hashCode() {
		// begin-user-code

		// Hash based upon superclass hash
		int hash = super.hashCode();

		// Add the hashes for all of the variables.
		hash += 31 * height;
		hash += 31 * innerRadius;
		hash += 31 * outerRadius;
		hash += 31 * material.hashCode();

		// Return hash
		return hash;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Compares the contents of objects and returns true if that are identical.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param otherObject
	 *            The other object being compared against.
	 * @return Returns true if the both objects are equal, otherwise false.
	 */
	public boolean equals(Object otherObject) {
		// begin-user-code

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

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Deep copies the contents of the object.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param otherObject
	 *            The other object to copy the contents of.
	 */
	public void copy(Ring otherObject) {
		// begin-user-code

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
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Deep copies and returns a newly instantiated object.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return The newly instantiated object.
	 */
	public Object clone() {
		// begin-user-code

		// Create a new ring
		Ring ring = new Ring();

		// Copy the contents from this ring into the new one
		ring.copy(this);

		// Return the new ring
		return ring;

		// end-user-code
	}

	/**
	 * Overrides the default behavior (ignore) from SFRComponent and implements
	 * the accept operation for this SFRComponent's type.
	 */
	@Override
	public void accept(ISFRComponentVisitor visitor) {
		// begin-user-code

		if (visitor != null) {
			visitor.visit(this);
		}

		return;
		// end-user-code
	}
}