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
package org.eclipse.ice.reactor.sfr.core;

import org.eclipse.ice.reactor.sfr.base.ISFRComponentVisitor;
import org.eclipse.ice.reactor.sfr.base.SFRComponent;

/**
 * <p>
 * Class representing the properties of any material that may be present
 * throughout the reactor. Can include solid, liquid and gaseous states.
 * </p>
 * 
 * @author w5q
 */
public class Material extends SFRComponent {
	/**
	 * <p>
	 * Nullary constructor.
	 * </p>
	 * 
	 */
	public Material() {

		// Set the name, description and ID
		setName("Material 1");
		setDescription("Material 1 Description");
		setId(1);

	}

	/**
	 * <p>
	 * Parameterized constructor specifying the material's name.
	 * </p>
	 * 
	 * @param name
	 *            Name of the material.
	 */
	public Material(String name) {

		// Call the nullary constructor
		this();

		// Set the specified name
		setName(name);

	}

	/**
	 * <p>
	 * Compares the contents of objects and returns true if they are identical,
	 * otherwise returns false.
	 * </p>
	 * 
	 * @param otherObject
	 *            The other object to compare against.
	 * @return Returns true if the two objects are equal, otherwise false.
	 */
	public boolean equals(Object otherObject) {

		// Check if otherObject is invalid
		if (otherObject == null) {
			return false;
		}
		// Create an equality flag, default to false
		boolean areEqual = false;

		// Since this class has no variables in addition to those inherited from
		// the superclass, we can just call the super.equals() method
		if (super.equals(otherObject)) {
			areEqual = true;
		}

		// Return final result
		return areEqual;

	}

	/**
	 * <p>
	 * Returns the hashcode of the object.
	 * </p>
	 * 
	 * @return The hashcode of the object.
	 */
	public int hashCode() {

		// Hash based upon superclass hash
		int hash = super.hashCode();

		// Return hash
		return hash;

	}

	/**
	 * <p>
	 * Deep copies the contents of the object.
	 * </p>
	 * 
	 * @param otherObject
	 *            The other object to copy the contents of.
	 */
	public void copy(Material otherObject) {

		// Check the material is valid
		if (otherObject == null) {
			return;
		}
		// Call the superclass copy operation
		super.copy(otherObject);

		return;
	}

	/**
	 * <p>
	 * Deep copies and returns a newly instantiated object.
	 * </p>
	 * 
	 * @return The newly instantiated object.
	 */
	public Object clone() {

		// Create a new material
		Material material = new Material();

		// Copy the contents from this material into the new one
		material.copy(this);

		// Return the new material
		return material;

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