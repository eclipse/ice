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
import org.eclipse.ice.analysistool.IDataProvider;

import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.ArrayList;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * An implementation of the IDataProvider. This class is used to store State
 * point data, usually for material decompositions or powers, that can be used
 * to store and display changes in value overtime across different features.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author s4h
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class LWRDataProvider implements IDataProvider {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * A TreeMap implementation of IData and features. Keep in mind that there
	 * can be multiple IData for the same feature.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private TreeMap<Double, ArrayList<FeatureSet>> dataTree;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The current time step. Can not be less than 0, and must be strictly less
	 * than the number of TimeSteps. Defaults to 0.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private double time;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * A description of the source of information for this provider and its
	 * data.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private String sourceInfo;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The time unit.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private String timeUnit;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The constructor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public LWRDataProvider() {
		// begin-user-code
		// Setup TreeMap
		this.dataTree = new TreeMap<Double, ArrayList<FeatureSet>>();

		// Setup Source
		this.sourceInfo = "No Source Available";

		// Setup time
		this.time = 0;
		this.timeUnit = "seconds";
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the sourceInfo. Can not be null or the empty string. Strings passed
	 * will be trimmed before being set.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param sourceInfo
	 *            <p>
	 *            The sourceInfo to set.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setSourceInfo(String sourceInfo) {
		// begin-user-code
		if (sourceInfo != null && !sourceInfo.trim().isEmpty()) {
			this.sourceInfo = sourceInfo.trim();
		}

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Adds a IData piece, keyed on the feature and timeStep, to the dataTree.
	 * If the feature exists in the tree, it will append to the end of the list.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param data
	 *            <p>
	 *            The data to add.
	 *            </p>
	 * @param time
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void addData(LWRData data, double time) {
		// begin-user-code

		// Local Declarations
		ArrayList<FeatureSet> featureSetList;
		FeatureSet set;
		boolean featureExistsFlag = false;

		// Return if the passed parameters are incorrect
		if (data == null || time < 0) {
			return;
		}

		featureSetList = this.dataTree.get(time);

		// If the timestep does not exist in the list, add it to the list
		if (featureSetList == null) {
			featureSetList = new ArrayList<FeatureSet>();
			set = new FeatureSet(data.getFeature());
			set.addIData(data);
			featureSetList.add(set);
			this.dataTree.put(time, featureSetList);

		}

		// Otherwise, the timestep exists and needs to append to the current
		// running list
		else {

			// Append to the current FeatureSet if it exists
			for (int i = 0; i < featureSetList.size(); i++) {
				// IF the featureset is the same name as the data, add it
				if (featureSetList.get(i).getName().equals(data.getFeature())) {
					featureSetList.get(i).addIData(data);
					featureExistsFlag = true;
					break;
				}
			}

			// If the featureSet does not exist, then add it
			if (!featureExistsFlag) {
				// If it does not, add it
				set = new FeatureSet(data.getFeature());
				set.addIData(data);

				// Add it to the list of sets
				featureSetList.add(set);
			}

		}

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Removes the feature and all associated IData from the dataTree at all
	 * time steps. If a user wishes to remove a single piece of IData from the
	 * tree, then use the appropriate getData operation on that feature and
	 * manipulate the data that way.
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
	public void removeAllDataFromFeature(String feature) {
		// begin-user-code

		// If feature is null, return
		if (feature == null) {
			return;
		}
		// Get the iterator
		Iterator<Double> iter;
		iter = this.dataTree.keySet().iterator();

		// Iterate over the list of all timesteps and remove all the features
		while (iter.hasNext()) {
			ArrayList<FeatureSet> list = this.dataTree.get(iter.next());

			// If the FeatureSet with the feature name exists, remove it
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getName().equals(feature)) {
					list.remove(i);
					break; // Break from for loop, otherwise really bad things
							// will happen
				}
			}
		}

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
	public void copy(LWRDataProvider otherObject) {
		// begin-user-code

		// Local Declarations
		Iterator<Double> iter;

		// If null, return
		if (otherObject == null) {
			return;
		}
		// Copy dataTree
		this.dataTree.clear();

		// Setup the iterator
		iter = otherObject.dataTree.keySet().iterator();

		// Currently, this will only shallow copy the ArrayList on value. IData
		// will have to implement copy and clone routines for it to be a deep
		// copy
		while (iter.hasNext()) {
			Double key = iter.next();
			this.dataTree.put(key, otherObject.dataTree.get(key));
		}

		// Copy Time info
		this.time = otherObject.time;
		this.sourceInfo = otherObject.sourceInfo;
		this.timeUnit = otherObject.timeUnit;

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
		LWRDataProvider component = new LWRDataProvider();

		// Copy the component
		component.copy(this);

		// Return the component
		return component;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the time units.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param timeUnit
	 *            <p>
	 *            The time unit to be set.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setTimeUnits(String timeUnit) {
		// begin-user-code

		if (timeUnit != null && !timeUnit.trim().isEmpty()) {
			this.timeUnit = timeUnit;
		}

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Equality check. Returns true if equals, false otherwise.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param otherObject
	 *            <p>
	 *            Object to equate.
	 *            </p>
	 * @return <p>
	 *         True if equal, false otherwise.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean equals(Object otherObject) {
		// begin-user-code

		// Local declarations
		LWRDataProvider component;
		boolean retVal = false;

		// If they are equal to the same object, return true
		if (this == otherObject) {
			return true;
		}
		// If this object is an instance of the LWRDataProvider, cast it.
		// Make sure it is also not null
		if (otherObject != null && otherObject instanceof LWRDataProvider) {
			component = (LWRDataProvider) otherObject;

			// Check values
			retVal = (this.dataTree.equals(component.dataTree)
					&& this.time == component.time
					&& this.sourceInfo.equals(component.sourceInfo) && this.timeUnit
					.equals(component.timeUnit));

		}

		// Return retVal
		return retVal;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The hashcode.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         the hash.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int hashCode() {
		// begin-user-code

		int hash = 31;

		// Calculate IDataProvider info
		hash += 31 * this.dataTree.hashCode();
		hash += 31 * this.time;
		hash += 31 * this.sourceInfo.hashCode();
		hash += 31 * this.timeUnit.hashCode();

		// return hash
		return hash;

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IDataProvider#getFeatureList()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<String> getFeatureList() {
		// begin-user-code

		// Local Declarations
		ArrayList<String> featureList = new ArrayList<String>();
		HashMap<String, Integer> map = new HashMap<String, Integer>();

		// Get the iterator
		Iterator<Double> iter;
		iter = this.dataTree.keySet().iterator();

		// Iterate over the list of all timesteps get one unique feature
		while (iter.hasNext()) {
			ArrayList<FeatureSet> list = this.dataTree.get(iter.next());

			// If the map does not have the feature name, add it
			for (int i = 0; i < list.size(); i++) {
				if (map.get(list.get(i).getName()) == null) {
					map.put(list.get(i).getName(), 1);
				}
			}
		}

		// Append to featureList
		for (String name : map.keySet()) {
			featureList.add(name);
		}

		// Return the featureList
		return featureList;

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IDataProvider#getNumberOfTimeSteps()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int getNumberOfTimeSteps() {
		// begin-user-code

		return this.dataTree.size();
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IDataProvider#setTime(double step)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setTime(double step) {
		// begin-user-code
		if (step >= 0.0) {
			this.time = step;
		}

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IDataProvider#getDataAtCurrentTime(String feature)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<IData> getDataAtCurrentTime(String feature) {
		// begin-user-code

		// If feature is null, return
		if (feature == null) {
			return new ArrayList<IData>();
		}
		// Locate the list
		ArrayList<FeatureSet> list = this.dataTree.get(this.time);

		if (list != null) {
			// If the list exists, return the iData
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getName().equals(feature)) {

					// Return the reference to the list of iData
					return list.get(i).getIData();
				}
			}
		}

		return new ArrayList<IData>();

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IDataProvider#getSourceInfo()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getSourceInfo() {
		// begin-user-code

		return this.sourceInfo;

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IDataProvider#getFeaturesAtCurrentTime()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<String> getFeaturesAtCurrentTime() {
		// begin-user-code

		// Local Declarations
		ArrayList<String> features = new ArrayList<String>();
		ArrayList<FeatureSet> list = null;

		// Get the list at the time
		list = this.dataTree.get(this.time);

		// If the time does not exist, return empty
		if (list == null) {
			return features;
		}
		// Get the features
		for (int i = 0; i < list.size(); i++) {
			features.add(list.get(i).getName());
		}

		return features;

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IDataProvider#getTimes()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<Double> getTimes() {
		// begin-user-code

		// Local Declarations
		ArrayList<Double> times = new ArrayList<Double>();

		// Get the iterator
		Iterator<Double> iter = this.dataTree.keySet().iterator();

		// Add the times to the arraylist
		while (iter.hasNext()) {
			times.add(iter.next());
		}

		// Return the list of times
		return times;

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IDataProvider#getTimeStep(double time)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int getTimeStep(double time) {
		// begin-user-code

		// Get the iterator
		Iterator<Double> iter = this.dataTree.keySet().iterator();
		int counter = 0;

		// Iterate the list
		while (iter.hasNext()) {

			// If the time is there, return the index
			if (iter.next().equals(time)) {
				return counter;
			}
			counter++;

		}

		// Time not found!
		return -1;

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IDataProvider#getTimeUnits()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getTimeUnits() {
		// begin-user-code

		return this.timeUnit;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the current time step.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The current time step.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public double getCurrentTime() {
		// begin-user-code

		return this.time;
		// end-user-code
	}
}