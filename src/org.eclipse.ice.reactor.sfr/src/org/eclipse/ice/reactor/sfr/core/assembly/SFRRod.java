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
import org.eclipse.ice.reactor.sfr.core.assembly.Ring;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * Class representing the solid cylindrical structure found inside radial
 * reflector assemblies.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author w5q
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class SFRRod extends SFRComponent {

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * A solid cylindrical structure of uniform reflector material.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private Ring reflector;

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
	public SFRRod() {
		// begin-user-code

		// Set the default name, description and ID
		setName("SFR Rod 1");
		setDescription("SFR Rod 1's Description");
		setId(1);

		// Define the reflector rod as a stainless steel ring with inner radius
		// = 0
		Material rodMaterial = new Material("SS-316");
		rodMaterial.setDescription("Stainless Steel");

		reflector = new Ring();
		reflector.setMaterial(rodMaterial);
		reflector.setHeight(0.0);
		reflector.setInnerRadius(0.0);
		reflector.setOuterRadius(26.666); // Rod diameter = 2 * pin diameter (?)

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
	 *            Name of the SFRRod.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public SFRRod(String name) {
		// begin-user-code

		// Call nullary constructor
		this();

		// Set name
		setName(name);

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the rod.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param reflector
	 *            The reflector rod.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setReflector(Ring reflector) {
		// begin-user-code

		// Check that the reflector is not null
		if (reflector != null) {
			this.reflector = reflector;
		}
		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the reflector rod as a Ring.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return The reflector rod.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Ring getReflector() {
		// begin-user-code
		return reflector;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the hashcode of the object.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return The hashcode of the object.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int hashCode() {
		// begin-user-code

		// Hash based upon superclass hash
		int hash = super.hashCode();

		// Add hash for local variable
		hash += reflector.hashCode();

		// Return the hash code
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

		// Check if otherObject is invalid
		if (otherObject != null && otherObject instanceof SFRRod) {

			// Cast to a SFRRod so we can access its variables/methods
			SFRRod otherRod = (SFRRod) otherObject;

			// Method will check for equality on two levels: shallow (within the
			// scope of the SFRRod class), and deep (all inherited variables
			// from superclass). Will only return true if both cases are true.

			// Create flags for checking shallow and deep equality, default to
			// false
			boolean shallowEqual = false;
			boolean deepEqual = false;

			// Check if reflectors (shallow scope) are equal
			if (reflector.equals(otherRod.reflector)) {
				shallowEqual = true;
			}
			// Check if all inherited variables are equal (deep scope)
			if (super.equals(otherRod) && shallowEqual) {
				deepEqual = true;
			}

			// Return final result
			return deepEqual;
		}

		// If otherObject was invalid, return false
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
	public void copy(SFRRod otherObject) {
		// begin-user-code

		// Check if otherObject in invalid
		if (otherObject == null) {
			return;
		}
		// Call the superclass copy operation
		super.copy(otherObject);

		// Copy the reflector
		reflector = otherObject.reflector;

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

		// Create a new SFRRod
		SFRRod rod = new SFRRod();

		// Copy the contents from this SFRRod into the new one
		rod.copy(this);

		// Return the new SFRRod
		return rod;

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