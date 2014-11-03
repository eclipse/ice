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
package org.eclipse.ice.reactor.sfr.base;

import org.eclipse.ice.analysistool.IData;
import java.util.ArrayList;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * A class that implements the IData interface; provides setters for the
 * particular sets of IData associated with this class.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author w5q
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class SFRData implements IData {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Value of the SFRData.
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
	 * Uncertainty of the SFRData value.
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
	 * Units of the SFRData value.
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
	 * Feature the SFRData value describes.
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
	 * Representation of the SFRData's physical position in the reactor as an
	 * array of (x, y, z) coordinates.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ArrayList<Double> position = null;

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
	public SFRData() {
		// begin-user-code
		// Initialize the default feature name.
		feature = "Feature 1";

		// Initialize position with 3 default values.
		position = new ArrayList<Double>();
		position.add(0.0);
		position.add(0.0);
		position.add(0.0);

		// Initialize the remaining defaults.
		uncertainty = 0.0;
		units = "seconds";
		value = 0.0;

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc --> Parameterized constructor with feature specified.
	 * <!-- end-UML-doc -->
	 * 
	 * @param feature
	 *            String representation of the feature.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public SFRData(String feature) {
		// begin-user-code

		// Perform the standard initialization.
		this();

		// Set the feature name (the method should check the value).
		setFeature(feature);

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the value of the data.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param value
	 *            The value of the data.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setValue(double value) {
		// begin-user-code

		this.value = value;

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the uncertainty of the data.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param uncertainty
	 *            The uncertainty of the data.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setUncertainty(double uncertainty) {
		// begin-user-code

		this.uncertainty = uncertainty;

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the units of the data.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param units
	 *            The units of the data.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setUnits(String units) {
		// begin-user-code

		// Only accept non-null, non-empty Strings.
		if (units != null && !units.trim().isEmpty()) {
			this.units = units.trim();
		}

		return;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the feature type of the data.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param feature
	 *            The feature of the data.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setFeature(String feature) {
		// begin-user-code

		// Only accept non-null, non-empty Strings.
		if (feature != null && !feature.trim().isEmpty()) {
			this.feature = feature.trim();
		}

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the position of the SFRData object. Represented in (x, y, z)
	 * coordinates.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param position
	 *            The position of the data, represented in (x, y, z)
	 *            coordinates.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setPosition(ArrayList<Double> position) {
		// begin-user-code

		// Change the position variable only if the incoming list of doubles is
		// of the proper size.
		int size = this.position.size();
		if (position != null && position.size() == size) {
			for (int i = 0; i < size; i++) {
				this.position.set(i, position.get(i));
			}
		}

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc --> Returns the position of the SFRData. <!--
	 * end-UML-doc -->
	 * 
	 * @return An ArrayList of doubles representing the position in (x, y, z)
	 *         coordinates.
	 * @see IData#getPosition()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<Double> getPosition() {
		// begin-user-code
		return position;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc --> Returns the value of the SFRData. <!-- end-UML-doc
	 * -->
	 * 
	 * @return The value of the SFRData
	 * @see IData#getValue()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public double getValue() {
		// begin-user-code
		return value;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc --> Returns the uncertainty of the SFRData. <!--
	 * end-UML-doc -->
	 * 
	 * @return The uncertainty of the SFRData.
	 * @see IData#getUncertainty()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public double getUncertainty() {
		// begin-user-code
		return uncertainty;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc --> Returns the units of the SFRData. <!-- end-UML-doc
	 * -->
	 * 
	 * @return The units of the SFRData.
	 * @see IData#getUnits()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getUnits() {
		// begin-user-code
		return units;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc --> Returns the feature of the SFRData. <!--
	 * end-UML-doc -->
	 * 
	 * @return The feature of the SFRData.
	 * @see IData#getFeature()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getFeature() {
		// begin-user-code
		return feature;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Overrides the equals operation to check the attributes on this object
	 * with another object of the same type. Returns true if the objects are
	 * equal. False otherwise.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param otherObject
	 *            The object to be compared.
	 * @return True if otherObject is equal. False otherwise.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean equals(Object otherObject) {
		// begin-user-code

		// By default, the objects are not equivalent.
		boolean equals = false;

		// Check the reference.
		if (this == otherObject) {
			equals = true;
		}
		// Check the information stored in the other object.
		else if (otherObject != null && otherObject instanceof SFRData) {
			// Cast the other object to an SFRComponent.
			SFRData data = (SFRData) otherObject;

			// Compare all the variables. Save the biggest for last (Java should
			// short-circuit the logical operators as soon as it detects a
			// mismatch).
			equals = (position.equals(data.position) && value == data.value
					&& uncertainty == data.uncertainty
					&& units.equals(data.units) && feature.equals(data.feature));
		}

		return equals;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the hashCode of the object.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return The hash of the object.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int hashCode() {
		// begin-user-code

		// Static hash at 31.
		int hash = 31;

		// Add local hashes.
		hash += 31 * position.hashCode();
		hash += 31 * value;
		hash += 31 * uncertainty;
		hash += 31 * units.hashCode();
		hash += 31 * feature.hashCode();

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
	 *            The object to be copied from.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void copy(SFRData data) {
		// begin-user-code

		// Check the parameters.
		if (data == null) {
			return;
		}

		// Deep copy the position data.
		position.clear();
		position.addAll(data.position);

		// Update the other variables.
		feature = data.feature;
		uncertainty = data.uncertainty;
		units = data.units;
		value = data.value;

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
	 * @return The newly instantiated copied object.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Object clone() {
		// begin-user-code

		// Initialize a new object.
		SFRData object = new SFRData();

		// Copy the contents from this one.
		object.copy(this);

		// Return the newly instantiated object.
		return object;
		// end-user-code
	}
}