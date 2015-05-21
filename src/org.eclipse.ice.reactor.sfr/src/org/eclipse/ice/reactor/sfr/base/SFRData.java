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
 * <p>
 * A class that implements the IData interface; provides setters for the
 * particular sets of IData associated with this class.
 * </p>
 * 
 * @author Anna Wojtowicz
 */
public class SFRData implements IData {
	/**
	 * <p>
	 * Value of the SFRData.
	 * </p>
	 * 
	 */
	private double value;
	/**
	 * <p>
	 * Uncertainty of the SFRData value.
	 * </p>
	 * 
	 */
	private double uncertainty;
	/**
	 * <p>
	 * Units of the SFRData value.
	 * </p>
	 * 
	 */
	private String units;
	/**
	 * <p>
	 * Feature the SFRData value describes.
	 * </p>
	 * 
	 */
	private String feature;
	/**
	 * <p>
	 * Representation of the SFRData's physical position in the reactor as an
	 * array of (x, y, z) coordinates.
	 * </p>
	 * 
	 */
	private ArrayList<Double> position = null;

	/**
	 * <p>
	 * Nullary constructor.
	 * </p>
	 * 
	 */
	public SFRData() {
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
	}

	/**
	 * <!-- begin-UML-doc --> Parameterized constructor with feature specified.
	 * 
	 * @param feature
	 *            String representation of the feature.
	 */
	public SFRData(String feature) {

		// Perform the standard initialization.
		this();

		// Set the feature name (the method should check the value).
		setFeature(feature);

		return;
	}

	/**
	 * <p>
	 * Sets the value of the data.
	 * </p>
	 * 
	 * @param value
	 *            The value of the data.
	 */
	public void setValue(double value) {

		this.value = value;

		return;
	}

	/**
	 * <p>
	 * Sets the uncertainty of the data.
	 * </p>
	 * 
	 * @param uncertainty
	 *            The uncertainty of the data.
	 */
	public void setUncertainty(double uncertainty) {

		this.uncertainty = uncertainty;

		return;
	}

	/**
	 * <p>
	 * Sets the units of the data.
	 * </p>
	 * 
	 * @param units
	 *            The units of the data.
	 */
	public void setUnits(String units) {

		// Only accept non-null, non-empty Strings.
		if (units != null && !units.trim().isEmpty()) {
			this.units = units.trim();
		}

		return;

	}

	/**
	 * <p>
	 * Sets the feature type of the data.
	 * </p>
	 * 
	 * @param feature
	 *            The feature of the data.
	 */
	public void setFeature(String feature) {

		// Only accept non-null, non-empty Strings.
		if (feature != null && !feature.trim().isEmpty()) {
			this.feature = feature.trim();
		}

		return;
	}

	/**
	 * <p>
	 * Sets the position of the SFRData object. Represented in (x, y, z)
	 * coordinates.
	 * </p>
	 * 
	 * @param position
	 *            The position of the data, represented in (x, y, z)
	 *            coordinates.
	 */
	public void setPosition(ArrayList<Double> position) {

		// Change the position variable only if the incoming list of doubles is
		// of the proper size.
		int size = this.position.size();
		if (position != null && position.size() == size) {
			for (int i = 0; i < size; i++) {
				this.position.set(i, position.get(i));
			}
		}

		return;
	}

	/**
	 * <!-- begin-UML-doc --> Returns the position of the SFRData. <!--
	 * end-UML-doc -->
	 * 
	 * @return An ArrayList of doubles representing the position in (x, y, z)
	 *         coordinates.
	 * @see IData#getPosition()
	 */
	public ArrayList<Double> getPosition() {
		return position;
	}

	/**
	 * <!-- begin-UML-doc --> Returns the value of the SFRData. <!-- end-UML-doc
	 * -->
	 * 
	 * @return The value of the SFRData
	 * @see IData#getValue()
	 */
	public double getValue() {
		return value;
	}

	/**
	 * <!-- begin-UML-doc --> Returns the uncertainty of the SFRData. <!--
	 * end-UML-doc -->
	 * 
	 * @return The uncertainty of the SFRData.
	 * @see IData#getUncertainty()
	 */
	public double getUncertainty() {
		return uncertainty;
	}

	/**
	 * <!-- begin-UML-doc --> Returns the units of the SFRData. <!-- end-UML-doc
	 * -->
	 * 
	 * @return The units of the SFRData.
	 * @see IData#getUnits()
	 */
	public String getUnits() {
		return units;
	}

	/**
	 * <!-- begin-UML-doc --> Returns the feature of the SFRData. <!--
	 * end-UML-doc -->
	 * 
	 * @return The feature of the SFRData.
	 * @see IData#getFeature()
	 */
	public String getFeature() {
		return feature;
	}

	/**
	 * <p>
	 * Overrides the equals operation to check the attributes on this object
	 * with another object of the same type. Returns true if the objects are
	 * equal. False otherwise.
	 * </p>
	 * 
	 * @param otherObject
	 *            The object to be compared.
	 * @return True if otherObject is equal. False otherwise.
	 */
	public boolean equals(Object otherObject) {

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
	}

	/**
	 * <p>
	 * Returns the hashCode of the object.
	 * </p>
	 * 
	 * @return The hash of the object.
	 */
	public int hashCode() {

		// Static hash at 31.
		int hash = 31;

		// Add local hashes.
		hash += 31 * position.hashCode();
		hash += 31 * value;
		hash += 31 * uncertainty;
		hash += 31 * units.hashCode();
		hash += 31 * feature.hashCode();

		return hash;
	}

	/**
	 * <p>
	 * Deep copies the contents of the object.
	 * </p>
	 * 
	 * @param data
	 *            The object to be copied from.
	 */
	public void copy(SFRData data) {

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
	}

	/**
	 * <p>
	 * Deep copies and returns a newly instantiated object.
	 * </p>
	 * 
	 * @return The newly instantiated copied object.
	 */
	public Object clone() {

		// Initialize a new object.
		SFRData object = new SFRData();

		// Copy the contents from this one.
		object.copy(this);

		// Return the newly instantiated object.
		return object;
	}
}