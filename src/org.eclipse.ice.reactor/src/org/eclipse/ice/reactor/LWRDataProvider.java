/*******************************************************************************
 * Copyright (c) 2013, 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Scott Forest Hull II - Initial API and implementation and/or initial documentation
 *   Jordan Deyton - bug 474744
 *******************************************************************************/
package org.eclipse.ice.reactor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.eclipse.ice.analysistool.IData;
import org.eclipse.ice.analysistool.IDataProvider;

/**
 * An implementation of the IDataProvider. This class is used to store State
 * point data, usually for material decompositions or powers, that can be used
 * to store and display changes in value overtime across different features.
 * 
 * @author Scott Forest Hull II
 */
public class LWRDataProvider implements IDataProvider {
	/**
	 * A TreeMap implementation of IData and features. Keep in mind that there
	 * can be multiple IData for the same feature.
	 */
	private TreeMap<Double, ArrayList<FeatureSet>> dataTree;
	/**
	 * The current time step. Can not be less than 0, and must be strictly less
	 * than the number of TimeSteps. Defaults to 0.
	 */
	private double time;
	/**
	 * A description of the source of information for this provider and its
	 * data.
	 */
	private String sourceInfo;
	/**
	 * The time unit.
	 */
	private String timeUnit;

	/**
	 * The constructor.
	 */
	public LWRDataProvider() {
		// Setup TreeMap
		this.dataTree = new TreeMap<Double, ArrayList<FeatureSet>>();

		// Setup Source
		this.sourceInfo = "No Source Available";

		// Setup time
		this.time = 0;
		this.timeUnit = "seconds";
	}

	/**
	 * Sets the sourceInfo. Can not be null or the empty string. Strings passed
	 * will be trimmed before being set.
	 * 
	 * @param sourceInfo
	 *            The sourceInfo to set.
	 */
	public void setSourceInfo(String sourceInfo) {
		if (sourceInfo != null && !sourceInfo.trim().isEmpty()) {
			this.sourceInfo = sourceInfo.trim();
		}
	}

	/**
	 * Adds a IData piece, keyed on the feature and timeStep, to the dataTree.
	 * If the feature exists in the tree, it will append to the end of the list.
	 * 
	 * @param data
	 *            The data to add.
	 * @param time
	 */
	public void addData(LWRData data, double time) {

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

		return;
	}

	/**
	 * Removes the feature and all associated IData from the dataTree at all
	 * time steps. If a user wishes to remove a single piece of IData from the
	 * tree, then use the appropriate getData operation on that feature and
	 * manipulate the data that way.
	 * 
	 * @param feature
	 *            The feature.
	 */
	public void removeAllDataFromFeature(String feature) {

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

		return;
	}

	/**
	 * Deep copies the contents of the object.
	 * 
	 * @param otherObject
	 *            The object to be copied.
	 */
	public void copy(LWRDataProvider otherObject) {

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

		return;
	}

	/**
	 * Deep copies and returns a newly instantiated object.
	 * 
	 * @return The newly instantiated copied object.
	 */
	@Override
	public Object clone() {

		// Local Declarations
		LWRDataProvider component = new LWRDataProvider();

		// Copy the component
		component.copy(this);

		// Return the component
		return component;
	}

	/**
	 * Sets the time units.
	 * 
	 * @param timeUnit
	 *            The time unit to be set.
	 */
	public void setTimeUnits(String timeUnit) {
		if (timeUnit != null && !timeUnit.trim().isEmpty()) {
			this.timeUnit = timeUnit;
		}
	}

	/**
	 * Equality check. Returns true if equals, false otherwise.
	 * 
	 * @param otherObject
	 *            Object to equate.
	 * @return True if equal, false otherwise.
	 */
	@Override
	public boolean equals(Object otherObject) {

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
			retVal = this.time == component.time
					&& this.sourceInfo.equals(component.sourceInfo)
					&& this.timeUnit.equals(component.timeUnit)
					&& this.dataTree.size() == component.dataTree.size();

			// There is no intrinsic order of the FeatureSet ordering in the
			// data tree's lists. Thus, for each list in both components, we
			// need to manually check the correct feature sets against each
			// other based on their names.
			if (retVal) {

				Iterator<Entry<Double, ArrayList<FeatureSet>>> iter;
				Iterator<Entry<Double, ArrayList<FeatureSet>>> otherIter;
				Iterator<FeatureSet> setIter;
				Entry<Double, ArrayList<FeatureSet>> entry;
				Entry<Double, ArrayList<FeatureSet>> otherEntry;

				// Iterate over each entry in the data tree.
				iter = dataTree.entrySet().iterator();
				otherIter = component.dataTree.entrySet().iterator();
				while (retVal && iter.hasNext()) {
					entry = iter.next();
					otherEntry = otherIter.next();

					// Compare the times and the feature set array sizes.
					// FIXME double comparison is bad...
					retVal = entry.getKey().equals(otherEntry.getKey()) && entry
							.getValue().size() == otherEntry.getValue().size();
					// If the times and list sizes match, we need to compare the
					// contents of the lists.
					if (retVal) {
						// For each feature set at this time in this data tree,
						// find a corresponding feature set (based on the
						// feature's name) and compare.
						setIter = entry.getValue().iterator();
						while (retVal && setIter.hasNext()) {
							FeatureSet set = setIter.next();
							FeatureSet otherSet = null;
							// Find the corresponding feature set with the same
							// feature name.
							String feature = set.getName();
							for (FeatureSet s : otherEntry.getValue()) {
								if (feature.equals(s.getName())) {
									otherSet = s;
									break;
								}
							}
							// Compare the two feature sets. If one was not
							// found, this returns false.
							retVal = set.equals(otherSet);
						}
					}
				}
			}

		}

		// Return retVal
		return retVal;
	}

	/**
	 * The hashcode.
	 * 
	 * @return the hash.
	 */
	@Override
	public int hashCode() {

		int hash = 31;

		// Calculate IDataProvider info
		hash += 31 * this.dataTree.hashCode();
		hash += 31 * this.time;
		hash += 31 * this.sourceInfo.hashCode();
		hash += 31 * this.timeUnit.hashCode();

		// return hash
		return hash;
	}

	/*
	 * Implements a method from IDataProvider.
	 */
	@Override
	public ArrayList<String> getFeatureList() {

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
	}

	/*
	 * Implements a method from IDataProvider.
	 */
	@Override
	public int getNumberOfTimeSteps() {
		return this.dataTree.size();
	}

	/*
	 * Implements a method from IDataProvider.
	 */
	@Override
	public void setTime(double step) {
		if (step >= 0.0) {
			this.time = step;
		}
	}

	/*
	 * Implements a method from IDataProvider.
	 */
	@Override
	public ArrayList<IData> getDataAtCurrentTime(String feature) {

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
	}

	/*
	 * Implements a method from IDataProvider.
	 */
	@Override
	public String getSourceInfo() {
		return this.sourceInfo;
	}

	/*
	 * Implements a method from IDataProvider.
	 */
	@Override
	public ArrayList<String> getFeaturesAtCurrentTime() {

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
	}

	/*
	 * Implements a method from IDataProvider.
	 */
	@Override
	public ArrayList<Double> getTimes() {

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
	}

	/*
	 * Implements a method from IDataProvider.
	 */
	@Override
	public int getTimeStep(double time) {

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
	}

	/*
	 * Implements a method from IDataProvider.
	 */
	@Override
	public String getTimeUnits() {
		return this.timeUnit;
	}

	/**
	 * Returns the current time step.
	 * 
	 * @return The current time step.
	 */
	public double getCurrentTime() {
		return this.time;
	}
}