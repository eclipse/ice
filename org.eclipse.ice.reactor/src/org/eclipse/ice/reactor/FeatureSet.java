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
package org.eclipse.ice.reactor;

import java.util.ArrayList;

import org.eclipse.ice.analysistool.IData;

/**
 * <p>
 * A convience class that holds a IData for the java collection on the
 * LWRComponent. This is an intermediary class designed to hold the list of
 * LWRData for the same types of features. The getFeature() operation on LWRData
 * should return the same value as the getName() operation on this class.
 * </p>
 * 
 * @author Scott Forest Hull II
 */
public class FeatureSet {
	/**
	 * <p>
	 * List of IData associated with this feature set.
	 * </p>
	 * 
	 */
	private ArrayList<IData> iData;
	/**
	 * <p>
	 * The name of the feature.
	 * </p>
	 * 
	 */
	private String name;

	/**
	 * <p>
	 * The constructor. The passed value must be a valid feature set, otherwise
	 * it will set the feature name to null and not allow the addition of any
	 * IData.
	 * </p>
	 * 
	 * @param feature
	 */
	public FeatureSet(String feature) {

		// If the feature is not null and is not empty, set as the name
		if (feature != null && !feature.trim().isEmpty()) {
			this.name = feature;
		}

		// Setup the IData arrayList
		this.iData = new ArrayList<IData>();

	}

	/**
	 * <p>
	 * Returns the name of the feature whose data is contained by this set.
	 * </p>
	 * 
	 * @return The name of the feature.
	 */
	public String getName() {

		return name;

	}

	/**
	 * <p>
	 * Returns the IDatas associated with the FeatureSet.
	 * </p>
	 * 
	 * @return <p>
	 *         The returned IData list.
	 *         </p>
	 */
	public ArrayList<IData> getIData() {

		// If the name is null, then return a cloned copy of the array so that
		// data can not be externally added/deleted.
		if (this.name == null) {
			return new ArrayList<IData>(iData);
		}

		// Otherwise, return iData.
		return this.iData;

	}

	/**
	 * <p>
	 * Adds IData to the list within the feature set. The name of the feature
	 * must match the name set on the FeatureSet, otherwise this operation will
	 * fail. Returns true if operation was successful, false otherwise.
	 * </p>
	 * 
	 * @param iData
	 *            <p>
	 *            The data associated with the feature set.
	 *            </p>
	 * @return True if the data was added, false otherwise.
	 */
	public boolean addIData(IData iData) {

		// If the iData is not null and the feature name is equal to the iData's
		// feature, add the piece to the IData.
		if (iData != null && iData.getFeature().equals(this.name)) {
			this.iData.add(iData);

			// Operation was successful
			return true;
		}

		// Failed
		return false;

	}

	/**
	 * <p>
	 * Overrides the equals operation to check the attributes on this object
	 * with another object of the same type. Returns true if the objects are
	 * equal. False otherwise.
	 * </p>
	 * 
	 * @param otherObject
	 *            <p>
	 *            The object to be compared.
	 *            </p>
	 * @return <p>
	 *         True if otherObject is equal. False otherwise.
	 *         </p>
	 */
	@Override
	public boolean equals(Object otherObject) {

		// Local declarations
		FeatureSet featureSet;
		boolean retVal = false;

		// If they are equal to the same object, return true
		if (this == otherObject) {
			return true;
		}

		// If this object is an instance of the FeatureSet, cast it.
		// Make sure it is also not null
		if (otherObject != null && otherObject instanceof FeatureSet) {
			featureSet = (FeatureSet) otherObject;

			// Check values
			retVal = (this.name.equals(featureSet.name) && this.iData
					.equals(featureSet.iData));

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
	 *         The hash of the object.
	 *         </p>
	 */
	@Override
	public int hashCode() {

		// Local Declarations
		int hash = 31;

		// Calculate hash
		hash += 31 * this.name.hashCode();
		hash += 31 * this.iData.hashCode();

		// return the hash
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
	public void copy(FeatureSet otherObject) {

		// If null, return
		if (otherObject == null) {
			return;
		}

		// Setup the name and iData
		this.name = otherObject.name;
		this.iData.clear();

		// Deep copy iData
		for (int i = 0; i < otherObject.iData.size(); i++) {
			this.iData.add(otherObject.iData.get(i));
		}

	}

	/**
	 * <p>
	 * Deep copies and returns a newly instantiated object.
	 * </p>
	 * 
	 * @return <p>
	 *         The newly instantiated copied object.
	 *         </p>
	 */
	@Override
	public Object clone() {

		// Local Declarations
		FeatureSet set = new FeatureSet(null);

		// Copy contents
		set.copy(this);

		// Return newly instantiated object
		return set;

	}
}