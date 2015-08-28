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
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

import javax.xml.bind.annotation.XmlTransient;

import org.eclipse.ice.analysistool.IData;
import org.eclipse.ice.analysistool.IDataProvider;
import org.eclipse.ice.datastructures.ICEObject.IUpdateableListener;
import org.eclipse.ice.datastructures.componentVisitor.IComponentVisitor;
import org.eclipse.ice.datastructures.componentVisitor.IReactorComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * The LWRComponent class represents all reactor components that can be stored
 * in a LWRComposite. This class should be treated as an extension of the Java
 * Object class where identifying pieces of information are stored on this
 * object. This class also implements the IDataProvider interface in order to
 * provide state point data capabilities for individual, unique instances of
 * this extended class. A key note to understand about this implementation for
 * IDataProvider is that for individual changes on a particular position, say a
 * Rod at a at a certain coordinate within an assembly changes it's material
 * density, then this data should be stored at a higher level within the
 * LWRDataProvider on the Assembly. The IDataProvider implementation here is
 * designed specifically for showing changes across all named positions of the
 * same type. If ,for example, the data changed across the same assembly across
 * all of the same rod types, then the statepoint data should be stored on this
 * class.
 * </p>
 * <p>
 * This class implements the ICE Component interface.
 * </p>
 *
 * @author Scott Forest Hull II
 */
public class LWRComponent implements IReactorComponent, IDataProvider {

	/**
	 * Logger for handling event messages and other information.
	 */
	protected final Logger logger;

	/**
	 * <p>
	 * An ArrayList of ICE IComponentListeners.
	 * </p>
	 *
	 */
	@XmlTransient
	private ArrayList<IUpdateableListener> listeners;
	/**
	 * <p>
	 * Classifies a LWRComponentType. This should only be set on the lowest
	 * level constructor AND should represent the lowest level object.
	 * </p>
	 *
	 */
	@XmlTransient
	protected HDF5LWRTagType HDF5LWRTag;
	/**
	 * <p>
	 * Name of this LWRComponent.
	 * </p>
	 *
	 */
	@XmlTransient
	protected String name;
	/**
	 * <p>
	 * A TreeMap implementation of IData and features. Keep in mind that there
	 * can be multiple IData for the same feature.
	 * </p>
	 */
	@XmlTransient
	private TreeMap<Double, ArrayList<FeatureSet>> dataTree;
	/**
	 * <p>
	 * The current time step. Can not be less than 0, and must be strictly less
	 * than the number of TimeSteps. Defaults to 0.
	 * </p>
	 *
	 */
	@XmlTransient
	private double time;
	/**
	 * <p>
	 * A description of the source of information for this provider and its
	 * data.
	 * </p>
	 *
	 */
	@XmlTransient
	private String sourceInfo;
	/**
	 * <p>
	 * The time unit.
	 * </p>
	 *
	 */
	@XmlTransient
	private String timeUnit;
	/**
	 * <p>
	 * The id of this LWRComponent. Can not be less than zero.
	 * </p>
	 *
	 */
	@XmlTransient
	protected int id;
	/**
	 * <p>
	 * The description of this LWRComponent.
	 * </p>
	 *
	 */
	@XmlTransient
	protected String description;

	// Names for groups
	@XmlTransient
	private String dataH5GroupName = "State Point Data";
	@XmlTransient
	private String timeStepNamePrefix = "TimeStep: ";

	/**
	 * The nullary Constructor.
	 */
	public LWRComponent() {

		// Create the logger
		logger = LoggerFactory.getLogger(getClass());

		// Setup default values
		this.name = "Component 1";
		this.id = 0;
		this.description = "Component 1's Description";

		// Setup listeners
		this.listeners = new ArrayList<IUpdateableListener>();

		// Setup TreeMap
		this.dataTree = new TreeMap<Double, ArrayList<FeatureSet>>();

		// Setup Source
		this.sourceInfo = "No Source Available";

		// Setup time
		this.time = 0;
		this.timeUnit = "seconds";

		// Setup the HDF5LWRTagType to correct type
		this.HDF5LWRTag = HDF5LWRTagType.LWRCOMPONENT;

	}

	/**
	 * A parameterized Constructor.
	 *
	 * @param name
	 *            Name of this LWRComponent
	 */
	public LWRComponent(String name) {
		// Call this constructor
		this();

		// Set name appropriately
		this.setName(name);
	}

	/**
	 * This operation notifies the listeners of the LWRComponent that its data
	 * state has changed.
	 */
	protected void notifyListeners() {

		// If the listeners are empty, return
		if (this.listeners == null || this.listeners.isEmpty()) {
			return;
		}
		// Local Declarations
		final LWRComponent compHandle = this;

		// Create a thread on which to notify the listeners.
		Thread notifierThread = new Thread() {

			@Override
			public void run() {

				// Loop over all listeners and update them
				for (int i = 0; i < listeners.size(); i++) {
					listeners.get(i).update(compHandle);
				}

				return;
			}
		};

		// Launch the thread and do the notifications
		notifierThread.start();

		return;

	}

	/**
	 * <p>
	 * Sets the sourceInfo. Can not be null or the empty string. Strings passed
	 * will be trimmed before being set.
	 * </p>
	 *
	 * @param sourceInfo
	 *            <p>
	 *            The sourceInfo to set.
	 *            </p>
	 */
	public void setSourceInfo(String sourceInfo) {
		if (sourceInfo != null && !sourceInfo.trim().isEmpty()) {
			this.sourceInfo = sourceInfo.trim();
			// Notify listeners
			this.notifyListeners();
		}

	}

	/**
	 * <p>
	 * Adds a IData piece, keyed on the feature and timeStep, to the dataTree.
	 * If the feature exists in the tree, it will append to the end of the list.
	 * </p>
	 *
	 * @param data
	 *            <p>
	 *            The data to add.
	 *            </p>
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
			this.notifyListeners();

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
					this.notifyListeners();
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
				this.notifyListeners();
			}

		}

	}

	/**
	 * <p>
	 * Removes the feature and all associated IData from the dataTree at all
	 * time steps. If a user wishes to remove a single piece of IData from the
	 * tree, then use the appropriate getData operation on that feature and
	 * manipulate the data that way.
	 * </p>
	 *
	 * @param feature
	 *            <p>
	 *            The feature.
	 *            </p>
	 */
	public void removeAllDataFromFeature(String feature) {

		// Local Declarations
		boolean updated = false;

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
					updated = true;
					break; // Break from for loop, otherwise really bad things
							// will happen
				}
			}
		}

		// Notify listeners if updated
		if (updated) {
			// Notify listeners
			this.notifyListeners();
		}

	}

	/*
	 * Implements method from Identifiable.
	 */
	@Override
	public void setId(int id) {

		// If the id is less 0, then do not set it
		if (id >= 0) {
			this.id = id;

			// Notify listeners
			this.notifyListeners();
		}

	}

	/*
	 * Implements method from Identifiable.
	 */
	@Override
	public String getDescription() {

		return this.description;
	}

	/*
	 * Implements method from Identifiable.
	 */
	@Override
	public int getId() {

		return this.id;
	}

	/*
	 * Implements method from Identifiable.
	 */
	@Override
	public void setName(String name) {

		// If the name is not null, and if you trim it, it is not the emptry
		// string
		if (name != null && !name.trim().isEmpty()) {
			// Trim it!
			this.name = name.trim();

			// Notify listeners
			this.notifyListeners();
		}

	}

	/*
	 * Implements method from Identifiable.
	 */
	@Override
	public String getName() {

		return this.name;
	}

	/*
	 * Implements method from Identifiable.
	 */
	@Override
	public void setDescription(String description) {

		// If the name is not null, and if you trim it, it is not the emptry
		// string
		if (description != null && !description.trim().isEmpty()) {
			// Trim it!
			this.description = description.trim();

			// Notify listeners
			this.notifyListeners();
		}

	}

	/*
	 * Overrides super class method.
	 */
	@Override
	public boolean equals(Object otherObject) {

		// Local declarations
		LWRComponent component;
		boolean retVal = false;

		// If they are equal to the same object, return true
		if (this == otherObject) {
			return true;
		}

		// If this object is an instance of the LWRComponent, cast it.
		// Make sure it is also not null
		if (otherObject != null && otherObject instanceof LWRComponent) {
			component = (LWRComponent) otherObject;

			// Check values
			retVal = (this.id == component.id
					&& this.description.equals(component.description)
					&& this.name.equals(component.name)
					&& this.dataTree.equals(component.dataTree)
					&& this.time == component.time
					&& this.sourceInfo.equals(component.sourceInfo)
					&& this.HDF5LWRTag.equals(component.HDF5LWRTag)
					&& this.timeUnit.equals(component.timeUnit));

		}

		// Return retVal
		return retVal;
	}

	/*
	 * Overrides super class method.
	 */
	@Override
	public int hashCode() {
		// Static hash at 31
		int hash = 31;

		// Calculate hash for name, id, and description
		hash += 31 * this.name.hashCode();
		hash += 31 * this.id;
		hash += 31 * this.description.hashCode();

		// Calculate IDataProvider info
		hash += 31 * this.dataTree.hashCode();
		hash += 31 * this.time;
		hash += 31 * this.sourceInfo.hashCode();
		hash += 31 * this.HDF5LWRTag.hashCode();
		hash += 31 * this.timeUnit.hashCode();

		// Return hash
		return hash;
	}

	/*
	 * Implements method from IUpdateable.
	 */
	@Override
	public void update(String updatedKey, String newValue) {

		// This does nothing at this time

	}

	/*
	 * Implements method from IUpdateable.
	 */
	@Override
	public void register(IUpdateableListener listener) {
		// If the listener is not null, add it
		if (listener != null) {
			this.listeners.add(listener);
		}
	}

	/*
	 * Implements method from Component.
	 */
	@Override
	public void accept(IComponentVisitor visitor) {

		if (visitor != null) {
			visitor.visit(this);
		}
	}

	/*
	 * Implements method from IReactorComponent.
	 */
	@Override
	public String toString() {
		return this.name;
	}

	/*
	 * Implements method from IDataProvider.
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
	 * Implements method from IDataProvider.
	 */
	@Override
	public int getNumberOfTimeSteps() {

		return this.dataTree.size();
	}

	/*
	 * Implements method from IDataProvider.
	 */
	@Override
	public void setTime(double step) {
		if (step >= 0.0) {
			this.time = step;
			this.notifyListeners();
		}

	}

	/*
	 * Implements method from IDataProvider.
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
	 * Implements method from IDataProvider.
	 */
	@Override
	public String getSourceInfo() {

		return this.sourceInfo;

	}

	/*
	 * Implements method from IDataProvider.
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
	 * Implements method from IDataProvider.
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
	 * Implements method from IDataProvider.
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
	 * Implements method from IDataProvider.
	 */
	@Override
	public String getTimeUnits() {

		return this.timeUnit;

	}

	/**
	 * <p>
	 * Returns the current time step.
	 * </p>
	 *
	 * @return
	 * 		<p>
	 *         The current time step.
	 *         </p>
	 */
	public double getCurrentTime() {

		return this.time;
	}

	/**
	 * <p>
	 * Returns the LWRComponentType.
	 * </p>
	 *
	 * @return
	 * 		<p>
	 *         The LWRComponentType.
	 *         </p>
	 */
	public HDF5LWRTagType getHDF5LWRTag() {

		return this.HDF5LWRTag;
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
	public void copy(LWRComponent otherObject) {

		// Local Declarations
		Iterator<Double> iter;

		// If null, return
		if (otherObject == null) {
			return;
		}
		// Copy name, id, description
		this.name = otherObject.name;
		this.id = otherObject.id;
		this.description = otherObject.description;

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

		// Add the listeners to the set
		this.listeners.addAll(otherObject.listeners);

		// Copy type
		this.HDF5LWRTag = otherObject.HDF5LWRTag;

		// Copy Time info
		this.time = otherObject.time;
		this.sourceInfo = otherObject.sourceInfo;
		this.timeUnit = otherObject.timeUnit;

		// Notify the listeners
		notifyListeners();

	}

	/**
	 * <p>
	 * Deep copies and returns a newly instantiated object.
	 * </p>
	 *
	 * @return
	 * 		<p>
	 *         The newly instantiated copied object.
	 *         </p>
	 */
	@Override
	public Object clone() {

		// Local Declarations
		LWRComponent component = new LWRComponent();

		// Copy the component
		component.copy(this);

		// Return the component
		return component;

	}

	/**
	 * <p>
	 * Sets the time units.
	 * </p>
	 *
	 * @param timeUnit
	 *            <p>
	 *            The time unit to be set.
	 *            </p>
	 */
	public void setTimeUnits(String timeUnit) {

		if (timeUnit != null && !timeUnit.trim().isEmpty()) {
			this.timeUnit = timeUnit;
		}

	}

	/**
	 * <p>
	 * This operation accepts an ILWRComponentVisitor that can be visit the
	 * LWRComponent to ascertain its type and perform various type-specific
	 * operations.
	 * </p>
	 *
	 * @param visitor
	 *            <p>
	 *            The visitor
	 *            </p>
	 */
	public void accept(ILWRComponentVisitor visitor) {
		// TODO Auto-generated method stub

	}

	/*
	 * Implements method from IUpdateable.
	 */
	@Override
	public void unregister(IUpdateableListener listener) {
		// TODO Auto-generated method stub

	}

}