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
package org.eclipse.ice.datastructures.form.mesh;

import org.eclipse.ice.datastructures.ICEObject.ICEJAXBHandler;
import org.eclipse.ice.datastructures.ICEObject.ICEObject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This class provides a boundary condition for edges.
 * </p>
 * <p>
 * Currently, a single edge can associate with at most two boundary conditions:
 * one for each polygon it connects. This means that BoundaryConditions should
 * be stored in Polygons rather than an Edge instance, which may be shared
 * between two polygons.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Jordan H. Deyton, w5q
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
@XmlRootElement(name = "BoundaryCondition")
@XmlAccessorType(XmlAccessType.FIELD)
public class BoundaryCondition extends ICEObject {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The type of this BoundaryCondition.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlAttribute
	private BoundaryConditionType type;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The parameters for the BoundaryCondition. This list should have exactly 5
	 * values.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlAttribute
	private ArrayList<Float> values;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The nullary constructor. Sets all values to their defaults (the type is
	 * None, and the values are all 0).
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public BoundaryCondition() {
		// begin-user-code

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
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Constructs a BoundaryCondition with the specified BoundaryConditionType.
	 * Every other feature of the BoundaryCondition is set to its default value.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param type
	 *            The initial BoundaryConditionType.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public BoundaryCondition(BoundaryConditionType type) {
		// begin-user-code

		// Call the nullary constructor to initialize defaults.
		this();

		// Set the type if it is valid.
		if (type != null) {
			this.type = type;
		}
		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Gets the type of the BoundaryCondition.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return A BoundaryConditionType.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public BoundaryConditionType getType() {
		// begin-user-code
		return type;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the type of the BoundaryCondition.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param type
	 *            The new BoundaryConditionType.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setType(BoundaryConditionType type) {
		// begin-user-code
		if (type != null && type != this.type) {
			this.type = type;

			// Notify listeners of the change.
			notifyListeners();
		}
		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Gets the parameter values for this BoundaryCondition.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return An ArrayList of 5 floating point numbers.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<Float> getValues() {
		// begin-user-code
		return new ArrayList<Float>(values);
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the parameter values for the BoundaryCondition. The input must be an
	 * ArrayList of exactly 5 Floats.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param values
	 *            The new parameter values for the BoundaryCondition.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setValues(ArrayList<Float> values) {
		// begin-user-code

		// The list must not be null, must contain 5 elements, and cannot
		// contain a null Float.
		if (values != null && values.size() == 5 && !values.contains(null)) {
			// Create a copy of the list.
			this.values = new ArrayList<Float>(values);

			// Notify listeners of the change.
			notifyListeners();
		}

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns the hash value of the BoundaryCondition.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return The hash of the Object.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Override
	public int hashCode() {
		// begin-user-code

		// Get initial hash code
		int hash = super.hashCode();

		// Hash the local values.
		hash = 31 * hash + type.hashCode();
		hash = 31 * hash + values.hashCode();

		return hash;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation is used to check equality between this BoundaryCondition
	 * and another. It returns true if they are equal and false if they are not.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param otherObject
	 *            The other Object that should be compared with this one.
	 * @return True if the Objects are equal, false otherwise.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Override
	public boolean equals(Object otherObject) {
		// begin-user-code

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
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation copies the contents of a BoundaryCondition into the
	 * current object using a deep copy.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param condition
	 *            The Object from which the values should be copied.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void copy(BoundaryCondition condition) {
		// begin-user-code

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
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns a clone of the BoundaryCondition using a deep
	 * copy.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return The new clone.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Override
	public Object clone() {
		// begin-user-code

		// Create a new BoundaryCondition.
		BoundaryCondition condition = new BoundaryCondition();

		// Deep copy the values from this instance to the new one.
		condition.copy(this);

		return condition;
		// end-user-code
	}

}