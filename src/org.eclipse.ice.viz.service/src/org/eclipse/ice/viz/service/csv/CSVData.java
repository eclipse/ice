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
package org.eclipse.ice.viz.service.csv;

import java.util.ArrayList;

import org.eclipse.ice.analysistool.IData;
import org.eclipse.ice.datastructures.ICEObject.ICEObject;

/**
 * 
 * This class is the implementation of IData and inherits from NiCEObject for
 * the PlotViewer tool.
 * 
 * @author Matthew Wang, Claire Saunders
 * 
 */

public class CSVData extends ICEObject implements IData {

	/**
	 * The position of the value, typically 3 dimensional
	 */
	private ArrayList<Double> position;

	/**
	 * The value of the data
	 */
	private double value;

	/**
	 * The associated uncertainty of the data
	 */
	private double uncertainty;

	/**
	 * The units of the data
	 */
	private String units;

	/**
	 * The feature
	 */
	private String feature;

	/**
	 * Tolerance for comparing doubles
	 */
	private double tol;

	/**
	 * Constructor takes in feature and value
	 * 
	 * @param feature
	 * @param value
	 */
	public CSVData(String feature, Double value) {
		this.feature = feature;
		this.value = value;
		this.position = new ArrayList<Double>();
		this.units = null;
		this.setUncertainty(0.0);
		this.tol = Math.pow(10, -15);
	}

	/**
	 * Constructor takes in feature, sets value to zero
	 * 
	 * @param newFeature
	 */
	public CSVData(String newFeature) {
		this.feature = newFeature;
		this.setValue(0.0);
		this.position = new ArrayList<Double>();
		this.units = null;
		this.setUncertainty(0.0);
		this.tol = Math.pow(10, -15);
	}

	/**
	 * Method to check if the current object is equal to the object passed in
	 */
	@Override
	public boolean equals(Object otherCSVData) {

		// Check if they are the same references in memory
		if (this == otherCSVData) {
			return true;
		}

		// Check that the object is not null, and that it is a Form
		if (otherCSVData == null || !(otherCSVData instanceof CSVData)) {
			return false;
		}

		// Check that these objects have the same NiCEObject data
		if (!super.equals(otherCSVData)) {
			return false;
		}

		// Other object must be a CSVData, so cast it
		CSVData castedCSVData = (CSVData) otherCSVData;
		// Check that their values are equal
		if (Math.abs(this.value - castedCSVData.getValue()) > tol) {
			return false;
		}

		// Check that their uncertainties are equal
		if (Math.abs(this.uncertainty - castedCSVData.getUncertainty()) > tol) {
			return false;
		}

		// Check that their features are equal
		if (!this.feature.equals(castedCSVData.getFeature())) {
			return false;
		}

		ArrayList<Double> castedCSVDataPosition = castedCSVData.getPosition();
		// Check that their positions ArrayList sizes are equal
		if (this.position.size() == castedCSVDataPosition.size()) {
			int size = this.position.size();
			// Check each position for equality
			for (int i = 0; i < size; i++) {
				if (Math.abs(this.position.get(i)
						- castedCSVDataPosition.get(i)) > tol) {
					return false;
				}
			}
		} else {
			return false;
		}

		// If it made it here, both CSVData are equal
		return true;
	}

	/**
	 * Compute and return the hash code for instances of this object
	 */
	@Override
	public int hashCode() {

		// Call ICEObject#hashCode()
		int hash = super.hashCode();

		// Compute the hash code from this object's data
		hash = 31 * hash + (null == position ? 0 : position.hashCode());
		hash = 31 * hash
				+ (null == (Double) value ? 0 : ((Double) value).hashCode());
		hash = 31
				* hash
				+ (null == (Double) uncertainty ? 0 : ((Double) uncertainty)
						.hashCode());
		hash = 31 * hash + (null == units ? 0 : units.hashCode());
		hash = 31 * hash + (null == feature ? 0 : feature.hashCode());
		hash = 31 * hash
				+ (null == (Double) tol ? 0 : ((Double) tol).hashCode());

		// Return the computed hash code
		return hash;
	}

	/**
	 * This operation performs a deep copy of the attributes of another CSVData
	 * into the current CSVData.
	 * 
	 * @param otherCSVData
	 */
	public void copy(CSVData otherCSVData) {

		// Return if otherForm is null
		if (otherCSVData == null) {
			return;
		}
		// Copy contents into super and current object
		super.copy((ICEObject) otherCSVData);

		// Copy the feature
		this.feature = otherCSVData.getFeature();

		// Copy the value
		this.value = otherCSVData.getValue();

		// Copy the uncertainty
		this.uncertainty = otherCSVData.getUncertainty();

		// Deep copy of the position
		for (Double pos : otherCSVData.getPosition()) {
			this.position.add(pos);
		}
	}

	/**
	 * This operation creates a deep copy of the CSVData object and returns it
	 */
	@Override
	public CSVData clone() throws NullPointerException {

		// Instantiate the newCSVData object

		// Check that the input for the clone is valid. If invalid, throw a
		// NullPointerException
		if (feature != null && !Double.isNaN(value)) {
			CSVData newCSVData = new CSVData(this.feature, this.value);
			// Use copy to perform a deep copy
			newCSVData.copy(this);
			return newCSVData;
		} else {
			throw new NullPointerException();
		}
	}

	/**
	 * Mutators
	 */

	/**
	 * Sets the position of this data
	 * 
	 * @param position
	 */
	public void setPosition(ArrayList<Double> position) {
		this.position = position;
	}

	/**
	 * Sets the value of this data
	 * 
	 * @param value
	 */
	public void setValue(double value) {
		this.value = value;
	}

	/**
	 * Sets the uncertainty value of this data. Any uncertainty set as -1 means
	 * there is no associated uncertainty for the data
	 * 
	 * @param uncertainty
	 */
	public void setUncertainty(double uncertainty) {
		if (uncertainty >= 0.0) {
			this.uncertainty = uncertainty;
		}
	}

	/**
	 * Sets the units of this data
	 * 
	 * @param units
	 */
	public void setUnits(String units) {
		this.units = units;
	}

	/**
	 * Sets the feature of this data
	 * 
	 * @param feature
	 */
	public void setFeature(String feature) {
		this.feature = feature;
	}

	/**
	 * Adds a position to the position ArrayList
	 * 
	 * @param pos
	 */
	public void addPosition(Double pos) {
		this.position.add(pos);
	}

	/**
	 * Accessors
	 */

	/**
	 * Get the position
	 */
	@Override
	public ArrayList<Double> getPosition() {
		return position;
	}

	/**
	 * Get the value
	 */
	@Override
	public double getValue() {
		return value;
	}

	/**
	 * Get the uncertainty
	 */
	@Override
	public double getUncertainty() {
		return uncertainty;
	}

	/**
	 * Gett the units
	 */
	@Override
	public String getUnits() {
		return units;
	}

	/**
	 * Get the feature
	 */
	@Override
	public String getFeature() {
		return feature;
	}

	/**
	 * toString function to print out the data of the object
	 */
	@Override
	public String toString() {
		String outputString = "";
		outputString += "Position: " + this.position.toString() + "\n";
		outputString += "Feature: " + this.feature + "\n";
		outputString += "Units: " + this.units + "\n";
		outputString += "Value: " + this.value + "\n";
		outputString += "Uncertainty: " + this.uncertainty + "\n";
		return outputString;
	}
}
