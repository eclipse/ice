/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.reactor;

import org.eclipse.ice.analysistool.IData;

import java.util.ArrayList;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * A class that implements the IData interface. It provides setters for the
 * particular sets of IData associated with this class along with some basic
 * equality and copying routines for convenience.
 * </p>
 * <p>
 * </p>
 * <p>
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author s4h
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class LWRData implements IData {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The representation of the x, y, z coordinate.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ArrayList<Double> position;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The value.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private double value;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The uncertainty value.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private double uncertainty;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The representation of the type of "Unit" represented by the value
	 * (Meters, velocity, etc).
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private String units;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The feature (unique name) of this object.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private String feature;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The constructor. Sets up the default values for the LWRData.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public LWRData() {
		// begin-user-code
		// Setup the default values for the listed attributes
		this.feature = "Feature 1";

		// Setup position with 3 default values
		this.position = new ArrayList<Double>();
		this.position.add(0.0);
		this.position.add(0.0);
		this.position.add(0.0);

		this.uncertainty = 0.0;
		this.units = "seconds";
		this.value = 0.0;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * A parameterized Constructor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param feature
	 *            <p>
	 *            The name of the feature
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public LWRData(String feature) {
		// begin-user-code
		// Call nullary constructor
		this();

		// Set the feature
		this.setFeature(feature);
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the position of the LWRData. The passed parameter can not be null
	 * and must be equal to three dimensions (x, y, z coordinate plane and in
	 * that order for less than 3 dimensions). If working in less than 3
	 * dimensions, the offset values should be set to 0.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param position
	 *            <p>
	 *            The position
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setPosition(ArrayList<Double> position) {
		// begin-user-code

		// If the position is not 3, return
		if (position.size() == 3) {
			// Add all uniquely
			this.position.clear();
			// Iterate over the list and add each position
			for (int i = 0; i < position.size(); i++) {
				this.position.add(position.get(i));
			}
		}

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the value.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param value
	 *            <p>
	 *            The value.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setValue(double value) {
		// begin-user-code

		this.value = value;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the uncertainty.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param uncertainty
	 *            <p>
	 *            The uncertainty.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setUncertainty(double uncertainty) {
		// begin-user-code

		this.uncertainty = uncertainty;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the units. Can not be null or the empty string. Strings are trimmed
	 * accordingly upon being set.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param units
	 *            <p>
	 *            The units.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setUnits(String units) {
		// begin-user-code

		// If the String is not null and it is not empty string (when trimmed)
		// set accordingly
		if (units != null && !units.trim().isEmpty()) {
			this.units = units.trim();
		}

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the feature. Can not set to null or the empty string. Strings are
	 * trimmed accordingly upon being set.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param feature
	 *            <p>
	 *            The feature.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setFeature(String feature) {
		// begin-user-code
		// If the String is not null and it is not empty string (when trimmed)
		// set accordingly
		if (feature != null && !feature.trim().isEmpty()) {
			this.feature = feature.trim();
		}

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @param otherObject
	 *            <p>
	 *            The object to be compared.
	 *            </p>
	 * @return <p>
	 *         True if otherObject is equal. False otherwise.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean equals(Object otherObject) {
		// begin-user-code

		// Local Declarations
		LWRData data;
		boolean retVal = false;

		// If they are equal to the same object, return true
		if (this == otherObject) {
			return true;
		}
		// If this object is an instance of the LWRData, cast it.
		// Make sure it is also not null
		if (otherObject != null && otherObject instanceof LWRData) {
			data = (LWRData) otherObject;

			// Check values
			retVal = (this.position.equals(data.position)
					&& this.value == data.value
					&& this.uncertainty == data.uncertainty
					&& this.units.equals(data.units) && this.feature
					.equals(data.feature));

		}

		// Return retVal
		return retVal;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the hashCode of the object.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The hash.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int hashCode() {
		// begin-user-code

		// Local Declarations
		int hash = 31;

		// Calculate the hashCode for the following attributes
		hash += 31 * this.position.hashCode();
		hash += 31 * this.value;
		hash += 31 * this.uncertainty;
		hash += 31 * this.units.hashCode();
		hash += 31 * this.feature.hashCode();

		// Return the hash
		return hash;

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
	 *            <p>
	 *            The object to be copied.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void copy(LWRData otherObject) {
		// begin-user-code

		// If object is null, return
		if (otherObject == null) {
			return;
		}
		// Copy contents
		this.position.clear();
		// Deep copy position
		for (int i = 0; i < otherObject.position.size(); i++) {
			this.position.add(new Double(otherObject.position.get(i)
					.doubleValue()));
		}
		this.feature = otherObject.feature;
		this.uncertainty = otherObject.uncertainty;
		this.units = otherObject.units;
		this.value = otherObject.value;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Deep copies and returns a newly instantiated object.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Object clone() {
		// begin-user-code

		// Local Declarations
		LWRData data = new LWRData();

		// Copy contents
		data.copy(this);

		// Return the newly instantiated object
		return data;

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IData#getPosition()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<Double> getPosition() {
		// begin-user-code

		return this.position;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IData#getValue()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public double getValue() {
		// begin-user-code

		return this.value;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IData#getUncertainty()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public double getUncertainty() {
		// begin-user-code

		return this.uncertainty;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IData#getUnits()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getUnits() {
		// begin-user-code

		return this.units;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IData#getFeature()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getFeature() {
		// begin-user-code

		return this.feature;
		// end-user-code
	}
}