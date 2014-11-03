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
 * A convenience class that holds IData for the Java Collection on the
 * SFRComponent. This is an intermediary class designed to hold the list of
 * SFRData for the same types of features. The getFeature() operation on SFRData
 * should return the same value as the getName() operation on this class.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author w5q
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class FeatureSet {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * List of IData associated to the FeatureSet.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ArrayList<IData> iData;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Name of the feature.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private String name;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Parameterized constructor specifying the feature type. The passed value
	 * must be a valid feature set, otherwise it will set the feature name to
	 * null and not allow the addition of any IData.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param feature
	 *            The name of the feature. If this value is null or empty, data
	 *            cannot be added to the set.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public FeatureSet(String feature) {
		// begin-user-code

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
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the name of the feature as a string.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return Returns the name of the feature set as a string. This may be null
	 *         if the name provided during construction was invalid.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getName() {
		// begin-user-code

		// Return the FeatureSet's name.
		return name;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns all IData associated to the FeatureSet.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return Returns an ArrayList of IData associated with the FeatureSet. If
	 *         the feature name is invalid, this list will not be modifiable.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<IData> getData() {
		// begin-user-code

		// By default, return the data stored in this FeatureSet.
		ArrayList<IData> data = iData;

		// If the name was invalid, return a cloned copy so that data cannot be
		// added externally.
		if (name == null) {
			data = (ArrayList<IData>) iData.clone();
		}

		return data;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Adds IData to the list within the feature set. The name of the feature
	 * must match the name set on the FeatureSet, otherwise this operation will
	 * fail.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param iData
	 *            The IData instance to add to the FeatureSet.
	 * @return Returns true if operation was successful, false otherwise.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean addIData(IData iData) {
		// begin-user-code

		// By default, we have not added the data to the List.
		boolean success = false;

		// If the argument is not null, and the feature name matches, add it to
		// the List.
		if (iData != null && iData.getFeature().equals(name)) {
			success = this.iData.add(iData);
		}
		// Return whether or not the data was successfully added to the List.
		return success;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Compares the contents of the objects and returns true if they are
	 * identical, otherwise returns false.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param otherObject
	 *            The other object to compare against.
	 * @return Returns true if the two objects are equal, otherwise false.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Override
	public boolean equals(Object otherObject) {
		// begin-user-code

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
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the hashcode of the object.
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

		// Add all variable hash codes to the hash.
		if (name != null) {
			hash += 31 * name.hashCode();
		}
		hash += 31 * iData.hashCode();

		return hash;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Deep copies the contents of the object from another object.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param otherObject
	 *            The other object to copy the contents of.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void copy(FeatureSet otherObject) {
		// begin-user-code

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
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Deep copies and returns a newly instantiated object.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return The newly instantiated cloned object.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Object clone() {
		// begin-user-code

		// Initialize a new object.
		FeatureSet object = new FeatureSet(name);

		// Copy the contents from this one.
		object.copy(this);

		// Return the newly instantiated object.
		return object;
		// end-user-code
	}
}