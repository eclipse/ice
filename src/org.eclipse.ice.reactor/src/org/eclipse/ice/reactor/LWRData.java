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
 * <p>
 * A class that implements the IData interface. It provides setters for the
 * particular sets of IData associated with this class along with some basic
 * equality and copying routines for convenience.
 * </p>
 * <p>
 * </p>
 * <p>
 * </p>
 * 
 * @author s4h
 */
public class LWRData implements IData {
	/**
	 * <p>
	 * The representation of the x, y, z coordinate.
	 * </p>
	 * 
	 */
	private ArrayList<Double> position;
	/**
	 * <p>
	 * The value.
	 * </p>
	 * 
	 */
	private double value;
	/**
	 * <p>
	 * The uncertainty value.
	 * </p>
	 * 
	 */
	private double uncertainty;
	/**
	 * <p>
	 * The representation of the type of "Unit" represented by the value
	 * (Meters, velocity, etc).
	 * </p>
	 * 
	 */
	private String units;
	/**
	 * <p>
	 * The feature (unique name) of this object.
	 * </p>
	 * 
	 */
	private String feature;

	/**
	 * <p>
	 * The constructor. Sets up the default values for the LWRData.
	 * </p>
	 * 
	 */
	public LWRData() {
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
	}

	/**
	 * <p>
	 * A parameterized Constructor.
	 * </p>
	 * 
	 * @param feature
	 *            <p>
	 *            The name of the feature
	 *            </p>
	 */
	public LWRData(String feature) {
		// Call nullary constructor
		this();

		// Set the feature
		this.setFeature(feature);
	}

	/**
	 * <p>
	 * Sets the position of the LWRData. The passed parameter can not be null
	 * and must be equal to three dimensions (x, y, z coordinate plane and in
	 * that order for less than 3 dimensions). If working in less than 3
	 * dimensions, the offset values should be set to 0.
	 * </p>
	 * 
	 * @param position
	 *            <p>
	 *            The position
	 *            </p>
	 */
	public void setPosition(ArrayList<Double> position) {

		// If the position is not 3, return
		if (position.size() == 3) {
			// Add all uniquely
			this.position.clear();
			// Iterate over the list and add each position
			for (int i = 0; i < position.size(); i++) {
				this.position.add(position.get(i));
			}
		}

	}

	/**
	 * <p>
	 * Sets the value.
	 * </p>
	 * 
	 * @param value
	 *            <p>
	 *            The value.
	 *            </p>
	 */
	public void setValue(double value) {

		this.value = value;

	}

	/**
	 * <p>
	 * Sets the uncertainty.
	 * </p>
	 * 
	 * @param uncertainty
	 *            <p>
	 *            The uncertainty.
	 *            </p>
	 */
	public void setUncertainty(double uncertainty) {

		this.uncertainty = uncertainty;

	}

	/**
	 * <p>
	 * Sets the units. Can not be null or the empty string. Strings are trimmed
	 * accordingly upon being set.
	 * </p>
	 * 
	 * @param units
	 *            <p>
	 *            The units.
	 *            </p>
	 */
	public void setUnits(String units) {

		// If the String is not null and it is not empty string (when trimmed)
		// set accordingly
		if (units != null && !units.trim().isEmpty()) {
			this.units = units.trim();
		}

	}

	/**
	 * <p>
	 * Sets the feature. Can not set to null or the empty string. Strings are
	 * trimmed accordingly upon being set.
	 * </p>
	 * 
	 * @param feature
	 *            <p>
	 *            The feature.
	 *            </p>
	 */
	public void setFeature(String feature) {
		// If the String is not null and it is not empty string (when trimmed)
		// set accordingly
		if (feature != null && !feature.trim().isEmpty()) {
			this.feature = feature.trim();
		}

	}

	/**
	 * 
	 * @param otherObject
	 *            <p>
	 *            The object to be compared.
	 *            </p>
	 * @return <p>
	 *         True if otherObject is equal. False otherwise.
	 *         </p>
	 */
	public boolean equals(Object otherObject) {

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

	}

	/**
	 * <p>
	 * Returns the hashCode of the object.
	 * </p>
	 * 
	 * @return <p>
	 *         The hash.
	 *         </p>
	 */
	public int hashCode() {

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

	}

	/**
	 * <p>
	 * Deep copies the contents of the object.
	 * </p>
	 * 
	 * @param otherObject
	 *            <p>
	 *            The object to be copied.
	 *            </p>
	 */
	public void copy(LWRData otherObject) {

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

	}

	/**
	 * <p>
	 * Deep copies and returns a newly instantiated object.
	 * </p>
	 * 
	 * @return
	 */
	public Object clone() {

		// Local Declarations
		LWRData data = new LWRData();

		// Copy contents
		data.copy(this);

		// Return the newly instantiated object
		return data;

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IData#getPosition()
	 */
	public ArrayList<Double> getPosition() {

		return this.position;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IData#getValue()
	 */
	public double getValue() {

		return this.value;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IData#getUncertainty()
	 */
	public double getUncertainty() {

		return this.uncertainty;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IData#getUnits()
	 */
	public String getUnits() {

		return this.units;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IData#getFeature()
	 */
	public String getFeature() {

		return this.feature;
	}
}