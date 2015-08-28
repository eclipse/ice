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

import java.util.ArrayList;

import org.eclipse.ice.analysistool.IData;

/**
 * <p>
 * A convenience class that holds IData for the Java Collection on the
 * SFRComponent. This is an intermediary class designed to hold the list of
 * SFRData for the same types of features. The getFeature() operation on SFRData
 * should return the same value as the getName() operation on this class.
 * </p>
 * 
 * @author Anna Wojtowicz
 */
public class FeatureSet {
	/**
	 * <p>
	 * List of IData associated to the FeatureSet.
	 * </p>
	 * 
	 */
	private ArrayList<IData> iData;

	/**
	 * <p>
	 * Name of the feature.
	 * </p>
	 * 
	 */
	private String name;

	/**
	 * <p>
	 * Parameterized constructor specifying the feature type. The passed value
	 * must be a valid feature set, otherwise it will set the feature name to
	 * null and not allow the addition of any IData.
	 * </p>
	 * 
	 * @param feature
	 *            The name of the feature. If this value is null or empty, data
	 *            cannot be added to the set.
	 */
	public FeatureSet(String feature) {

		// When the FeatureSet gets an invalid feature name, don't add any data
		// to it.
		if (feature == null || "".equals(feature.trim())) {
			System.err
					.println("FeatureSet error: Invalid feature name String provided in constructor!");
			name = null;
		} else {
			name = feature.trim();
		}

		// Initialize the List of IData.
		iData = new ArrayList<IData>();

		return;
	}

	/**
	 * <p>
	 * Returns the name of the feature as a string.
	 * </p>
	 * 
	 * @return Returns the name of the feature set as a string. This may be null
	 *         if the name provided during construction was invalid.
	 */
	public String getName() {

		// Return the FeatureSet's name.
		return name;
	}

	/**
	 * <p>
	 * Returns all IData associated to the FeatureSet.
	 * </p>
	 * 
	 * @return Returns an ArrayList of IData associated with the FeatureSet. If
	 *         the feature name is invalid, this list will not be modifiable.
	 */
	public ArrayList<IData> getData() {

		// By default, return the data stored in this FeatureSet.
		ArrayList<IData> data = iData;

		// If the name was invalid, return a cloned copy so that data cannot be
		// added externally.
		if (name == null) {
			data = (ArrayList<IData>) iData.clone();
		}

		return data;
	}

	/**
	 * <p>
	 * Adds IData to the list within the feature set. The name of the feature
	 * must match the name set on the FeatureSet, otherwise this operation will
	 * fail.
	 * </p>
	 * 
	 * @param iData
	 *            The IData instance to add to the FeatureSet.
	 * @return Returns true if operation was successful, false otherwise.
	 */
	public boolean addIData(IData iData) {

		// By default, we have not added the data to the List.
		boolean success = false;

		// If the argument is not null, and the feature name matches, add it to
		// the List.
		if (iData != null && iData.getFeature().equals(name)) {
			success = this.iData.add(iData);
		}
		// Return whether or not the data was successfully added to the List.
		return success;
	}

	/**
	 * <p>
	 * Compares the contents of the objects and returns true if they are
	 * identical, otherwise returns false.
	 * </p>
	 * 
	 * @param otherObject
	 *            The other object to compare against.
	 * @return Returns true if the two objects are equal, otherwise false.
	 */
	@Override
	public boolean equals(Object otherObject) {

		// By default, the objects are not equivalent.
		boolean equals = false;

		// Check the reference.
		if (this == otherObject) {
			equals = true;
		}
		// Check the information stored in the other object.
		else if (otherObject != null && otherObject instanceof FeatureSet) {

			// We can now cast the other object.
			FeatureSet featureSet = (FeatureSet) otherObject;

			// Compare the values between the two objects.
			if (name != null) {
				equals = (name.equals(featureSet.name) && iData
						.equals(featureSet.iData));
			} else {
				equals = (featureSet.name == null && iData
						.equals(featureSet.iData));
			}
		}

		return equals;
	}

	/**
	 * <p>
	 * Returns the hashcode of the object.
	 * </p>
	 * 
	 * @return The hash of the object.
	 */
	@Override
	public int hashCode() {

		// Static hash at 31.
		int hash = 31;

		// Add all variable hash codes to the hash.
		if (name != null) {
			hash += 31 * name.hashCode();
		}
		hash += 31 * iData.hashCode();

		return hash;
	}

	/**
	 * <p>
	 * Deep copies the contents of the object from another object.
	 * </p>
	 * 
	 * @param otherObject
	 *            The other object to copy the contents of.
	 */
	public void copy(FeatureSet otherObject) {

		// Check the parameters.
		if (otherObject == null) {
			return;
		}
		// Copy the name.
		name = otherObject.name;

		// Copy the contents of the iData List.
		iData.clear();
		for (IData data : otherObject.iData) {
			iData.add(data);
		}
		return;
	}

	/**
	 * <p>
	 * Deep copies and returns a newly instantiated object.
	 * </p>
	 * 
	 * @return The newly instantiated cloned object.
	 */
	@Override
	public Object clone() {

		// Initialize a new object.
		FeatureSet object = new FeatureSet(name);

		// Copy the contents from this one.
		object.copy(this);

		// Return the newly instantiated object.
		return object;
	}
}