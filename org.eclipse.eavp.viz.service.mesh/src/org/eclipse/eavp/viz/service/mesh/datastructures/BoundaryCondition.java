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
package org.eclipse.eavp.viz.service.mesh.datastructures;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.eavp.viz.service.datastructures.VizObject.VizObject;

/**
 * <p>
 * This class provides a boundary condition for edges.
 * </p>
 * <p>
 * Currently, a single edge can associate with at most two boundary conditions:
 * one for each polygon it connects. This means that BoundaryConditions should
 * be stored in Polygons rather than an Edge instance, which may be shared
 * between two polygons.
 * </p>
 * 
 * @author Jordan H. Deyton, Anna Wojtowicz
 */
@XmlRootElement(name = "BoundaryCondition")
@XmlAccessorType(XmlAccessType.FIELD)
public class BoundaryCondition extends VizObject {
	/**
	 * <p>
	 * The type of this BoundaryCondition.
	 * </p>
	 * 
	 */
	@XmlAttribute
	private BoundaryConditionType type;
	/**
	 * <p>
	 * The parameters for the BoundaryCondition. This list should have exactly 5
	 * values.
	 * </p>
	 * 
	 */
	@XmlAttribute
	private ArrayList<Float> values;

	/**
	 * <p>
	 * The nullary constructor. Sets all values to their defaults (the type is
	 * None, and the values are all 0).
	 * </p>
	 * 
	 */
	public BoundaryCondition() {

		// Set the default type and values.
		type = BoundaryConditionType.None;
		values = new ArrayList<Float>();
		for (int i = 0; i < 5; i++) { // Five default values of zero
			values.add(0f);
		}

		// Clear the ICEObject's name and description.
		setName("");
		setDescription("");

		return;
	}

	/**
	 * <p>
	 * Constructs a BoundaryCondition with the specified BoundaryConditionType.
	 * Every other feature of the BoundaryCondition is set to its default value.
	 * </p>
	 * 
	 * @param type
	 *            The initial BoundaryConditionType.
	 */
	public BoundaryCondition(BoundaryConditionType type) {

		// Call the nullary constructor to initialize defaults.
		this();

		// Set the type if it is valid.
		if (type != null) {
			this.type = type;
		}
		return;
	}

	/**
	 * <p>
	 * Gets the type of the BoundaryCondition.
	 * </p>
	 * 
	 * @return A BoundaryConditionType.
	 */
	public BoundaryConditionType getType() {
		return type;
	}

	/**
	 * <p>
	 * Sets the type of the BoundaryCondition.
	 * </p>
	 * 
	 * @param type
	 *            The new BoundaryConditionType.
	 */
	public void setType(BoundaryConditionType type) {
		if (type != null && type != this.type) {
			this.type = type;

			// Notify listeners of the change.
			notifyListeners();
		}
		return;
	}

	/**
	 * <p>
	 * Gets the parameter values for this BoundaryCondition.
	 * </p>
	 * 
	 * @return An ArrayList of 5 floating point numbers.
	 */
	public ArrayList<Float> getValues() {
		return new ArrayList<Float>(values);
	}

	/**
	 * <p>
	 * Sets the parameter values for the BoundaryCondition. The input must be an
	 * ArrayList of exactly 5 Floats.
	 * </p>
	 * 
	 * @param values
	 *            The new parameter values for the BoundaryCondition.
	 */
	public void setValues(ArrayList<Float> values) {

		// The list must not be null, must contain 5 elements, and cannot
		// contain a null Float.
		if (values != null && values.size() == 5 && !values.contains(null)) {
			// Create a copy of the list.
			this.values = new ArrayList<Float>(values);

			// Notify listeners of the change.
			notifyListeners();
		}

		return;
	}

	/**
	 * <p>
	 * This operation returns the hash value of the BoundaryCondition.
	 * </p>
	 * 
	 * @return The hash of the Object.
	 */
	@Override
	public int hashCode() {

		// Get initial hash code
		int hash = super.hashCode();

		// Hash the local values.
		hash = 31 * hash + type.hashCode();
		hash = 31 * hash + values.hashCode();

		return hash;
	}

	/**
	 * <p>
	 * This operation is used to check equality between this BoundaryCondition
	 * and another. It returns true if they are equal and false if they are not.
	 * </p>
	 * 
	 * @param otherObject
	 *            The other Object that should be compared with this one.
	 * @return True if the Objects are equal, false otherwise.
	 */
	@Override
	public boolean equals(Object otherObject) {

		boolean equal = false;

		// Check the reference.
		if (this == otherObject) {
			equal = true;
		}
		// Check that the other object is not null and an instance of the
		// BoundaryCondition.
		else if (otherObject != null
				&& otherObject instanceof BoundaryCondition) {

			// Cast to a BoundaryCondition.
			BoundaryCondition otherCondition = (BoundaryCondition) otherObject;

			// Check the super class' equality and local values.
			equal = (super.equals(otherObject) && type == otherCondition.type && values
					.equals(otherCondition.values));
		}

		return equal;
	}

	/**
	 * <p>
	 * This operation copies the contents of a BoundaryCondition into the
	 * current object using a deep copy.
	 * </p>
	 * 
	 * @param condition
	 *            The Object from which the values should be copied.
	 */
	public void copy(BoundaryCondition condition) {

		// Return if the other object is null.
		if (condition == null) {
			return;
		}
		// Copy the super class data.
		super.copy(condition);

		// Copy the local data.
		type = condition.type;
		values = condition.values;

		// Notify listeners of the change.
		notifyListeners();

		return;
	}

	/**
	 * <p>
	 * This operation returns a clone of the BoundaryCondition using a deep
	 * copy.
	 * </p>
	 * 
	 * @return The new clone.
	 */
	@Override
	public Object clone() {

		// Create a new BoundaryCondition.
		BoundaryCondition condition = new BoundaryCondition();

		// Deep copy the values from this instance to the new one.
		condition.copy(this);

		return condition;
	}

}