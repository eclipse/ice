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

import org.eclipse.ice.analysistool.IData;

import java.util.ArrayList;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * A convience class that holds a IData for the java collection on the
 * LWRComponent. This is an intermediary class designed to hold the list of
 * LWRData for the same types of features. The getFeature() operation on LWRData
 * should return the same value as the getName() operation on this class.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author s4h
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class FeatureSet {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * List of IData associated with this feature set.
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
	 * The name of the feature.
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
	 * The constructor. The passed value must be a valid feature set, otherwise
	 * it will set the feature name to null and not allow the addition of any
	 * IData.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param feature
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public FeatureSet(String feature) {
		// begin-user-code

		// If the feature is not null and is not empty, set as the name
		if (feature != null && !feature.trim().isEmpty()) {
			this.name = feature;
		}

		// Setup the IData arrayList
		this.iData = new ArrayList<IData>();

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the name of the feature.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getName() {
		// begin-user-code

		return name;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the IDatas associated with the FeatureSet.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The returned IData list.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<IData> getIData() {
		// begin-user-code

		// If the name is null, then return a cloned copy of the array so that
		// data can not be externally added/deleted.
		if (this.name == null) {
			return (ArrayList<IData>) this.iData.clone();
		}

		// Otherwise, return iData.
		return this.iData;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Adds IData to the list within the feature set. The name of the feature
	 * must match the name set on the FeatureSet, otherwise this operation will
	 * fail. Returns true if operation was successful, false otherwise.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param iData
	 *            <p>
	 *            The data associated with the feature set.
	 *            </p>
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean addIData(IData iData) {
		// begin-user-code

		// If the iData is not null and the feature name is equal to the iData's
		// feature, add the piece to the IData.
		if (iData != null && iData.getFeature().equals(this.name)) {
			this.iData.add(iData);

			// Operation was successful
			return true;
		}

		// Failed
		return false;

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
	 *         The hash of the object.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int hashCode() {
		// begin-user-code

		// Local Declarations
		int hash = 31;

		// Calculate hash
		hash += 31 * this.name.hashCode();
		hash += 31 * this.iData.hashCode();

		// return the hash
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
	public void copy(FeatureSet otherObject) {
		// begin-user-code

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

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Deep copies and returns a newly instantiated object.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The newly instantiated copied object.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Object clone() {
		// begin-user-code

		// Local Declarations
		FeatureSet set = new FeatureSet(null);

		// Copy contents
		set.copy(this);

		// Return newly instantiated object
		return set;

		// end-user-code
	}
}