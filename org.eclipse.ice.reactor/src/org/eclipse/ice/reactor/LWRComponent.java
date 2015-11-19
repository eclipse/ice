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
import java.util.Vector;

import javax.xml.bind.annotation.XmlTransient;

import ncsa.hdf.object.Dataset;
import ncsa.hdf.object.Datatype;
import ncsa.hdf.object.HObject;
import ncsa.hdf.object.h5.H5CompoundDS;
import ncsa.hdf.object.h5.H5Datatype;
import ncsa.hdf.object.h5.H5File;
import ncsa.hdf.object.h5.H5Group;

import org.eclipse.ice.analysistool.IData;
import org.eclipse.ice.analysistool.IDataProvider;
import org.eclipse.ice.datastructures.ICEObject.IUpdateableListener;
import org.eclipse.ice.datastructures.componentVisitor.IComponentVisitor;
import org.eclipse.ice.datastructures.componentVisitor.IReactorComponent;
import org.eclipse.ice.io.hdf.HdfReaderFactory;
import org.eclipse.ice.io.hdf.HdfWriterFactory;
import org.eclipse.ice.io.hdf.IHdfReadable;
import org.eclipse.ice.io.hdf.IHdfWriteable;
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
public class LWRComponent implements IReactorComponent, IDataProvider,
		IHdfWriteable, IHdfReadable {

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
	 * (non-Javadoc)
	 * @see org.eclipse.ice.datastructures.ICEObject.Identifiable#setId(int)
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
	 * (non-Javadoc)
	 * @see org.eclipse.ice.datastructures.ICEObject.Identifiable#getDescription()
	 */
	@Override
	public String getDescription() {

		return this.description;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.datastructures.ICEObject.Identifiable#getId()
	 */
	@Override
	public int getId() {

		return this.id;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.datastructures.ICEObject.Identifiable#setName(java.lang.String)
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
	 * (non-Javadoc)
	 * @see org.eclipse.ice.datastructures.ICEObject.Identifiable#getName()
	 */
	@Override
	public String getName() {

		return this.name;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.datastructures.ICEObject.Identifiable#setDescription(java.lang.String)
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
					&& this.HDF5LWRTag.equals(component.HDF5LWRTag) && this.timeUnit
					.equals(component.timeUnit));

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
	 * (non-Javadoc)
	 * @see org.eclipse.ice.datastructures.ICEObject.IUpdateable#update(java.lang.String, java.lang.String)
	 */
	@Override
	public void update(String updatedKey, String newValue) {

		// This does nothing at this time

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.datastructures.ICEObject.IUpdateable#register(org.eclipse.ice.datastructures.ICEObject.IUpdateableListener)
	 */
	@Override
	public void register(IUpdateableListener listener) {
		// If the listener is not null, add it
		if (listener != null) {
			this.listeners.add(listener);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.datastructures.ICEObject.Component#accept(org.eclipse.ice.datastructures.componentVisitor.IComponentVisitor)
	 */
	@Override
	public void accept(IComponentVisitor visitor) {

		if (visitor != null) {
			visitor.visit(this);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.name;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.analysistool.IDataProvider#getFeatureList()
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
	 * (non-Javadoc)
	 * @see org.eclipse.ice.analysistool.IDataProvider#getNumberOfTimeSteps()
	 */
	@Override
	public int getNumberOfTimeSteps() {

		return this.dataTree.size();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.analysistool.IDataProvider#setTime(double)
	 */
	@Override
	public void setTime(double step) {
		if (step >= 0.0) {
			this.time = step;
			this.notifyListeners();
		}

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.analysistool.IDataProvider#getDataAtCurrentTime(java.lang.String)
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
	 * (non-Javadoc)
	 * @see org.eclipse.ice.analysistool.IDataProvider#getSourceInfo()
	 */
	@Override
	public String getSourceInfo() {

		return this.sourceInfo;

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.analysistool.IDataProvider#getFeaturesAtCurrentTime()
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
	 * (non-Javadoc)
	 * @see org.eclipse.ice.analysistool.IDataProvider#getTimes()
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
	 * (non-Javadoc)
	 * @see org.eclipse.ice.analysistool.IDataProvider#getTimeStep(double)
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
	 * (non-Javadoc)
	 * @see org.eclipse.ice.analysistool.IDataProvider#getTimeUnits()
	 */
	@Override
	public String getTimeUnits() {

		return this.timeUnit;

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.io.hdf.IHdfWriteable#createGroup(ncsa.hdf.object.h5.H5File, ncsa.hdf.object.h5.H5Group)
	 */
	@Override
	public H5Group createGroup(H5File h5File, H5Group parentH5Group) {

		// Create the group for this component
		H5Group h5Group = HdfWriterFactory.createH5Group(h5File, name,
				parentH5Group);

		// Return the group
		return h5Group;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.io.hdf.IHdfWriteable#getWriteableChildren()
	 */
	@Override
	public ArrayList<IHdfWriteable> getWriteableChildren() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.io.hdf.IHdfWriteable#writeAttributes(ncsa.hdf.object.h5.H5File, ncsa.hdf.object.h5.H5Group)
	 */
	@Override
	public boolean writeAttributes(H5File h5File, H5Group h5Group) {

		boolean flag = true;

		flag &= HdfWriterFactory.writeStringAttribute(h5File, h5Group,
				"HDF5LWRTag", HDF5LWRTag.toString());
		flag &= HdfWriterFactory.writeStringAttribute(h5File, h5Group, "name",
				name);
		flag &= HdfWriterFactory.writeIntegerAttribute(h5File, h5Group, "id",
				id);
		flag &= HdfWriterFactory.writeStringAttribute(h5File, h5Group,
				"description", description);

		return flag;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.io.hdf.IHdfWriteable#writeDatasets(ncsa.hdf.object.h5.H5File, ncsa.hdf.object.h5.H5Group)
	 */
	@Override
	public boolean writeDatasets(H5File h5File, H5Group h5Group) {

		// If these pieces are null, return
		if (h5File == null || h5Group == null) {
			return false;
		}
		// Local Declarations
		H5Group dataH5Group, timeStepH5Group;
		Iterator<Double> iter;
		int counter = 0;
		String[] featureDSNames = { "value", "uncertainty", "units", "position" };

		// Preliminary work: Create the dataTypes and setup the DS for it's
		// dimensions

		// Each member's type
		Datatype[] memberDatatypes = {
				HdfWriterFactory.createFloatH5Datatype(h5File),
				HdfWriterFactory.createFloatH5Datatype(h5File),
				null, // Will replace later with a specific string datatype.
				HdfWriterFactory.createFloatH5Datatype(h5File),
				HdfWriterFactory.createFloatH5Datatype(h5File),
				HdfWriterFactory.createFloatH5Datatype(h5File), };

		// Create a HDF5Group for data
		dataH5Group = HdfWriterFactory.createH5Group(h5File, dataH5GroupName,
				h5Group);

		// Get the iterator
		iter = this.dataTree.keySet().iterator();

		// Iterate over the dataTree and create timesteps for each key in the
		// tree
		while (iter.hasNext()) {

			// Get the time
			double time = iter.next();

			// Get the list of FeatureSets at that time
			ArrayList<FeatureSet> setList = this.dataTree.get(time);

			// Create a new timeStep group based on the prefix and the timeStep
			timeStepH5Group = HdfWriterFactory.createH5Group(h5File,
					timeStepNamePrefix + "" + counter, dataH5Group);

			// Add attributes to the timeStep group

			// Create attribute: time as Double
			HdfWriterFactory.writeDoubleAttribute(h5File, timeStepH5Group,
					"time", time);

			// Create attribute: units as String
			HdfWriterFactory.writeStringAttribute(h5File, timeStepH5Group,
					"units", this.timeUnit);

			// Create a Compound Dataset for each timeStep to represent the
			// collection of FeatureSets. This contains the list of Feature Sets
			for (int i = 0; i < setList.size(); i++) {

				// Get the FeatureSet
				FeatureSet set = setList.get(i);

				// Setup the size of the String array Dataset
				int maxLength = 0;
				H5Datatype datatype = null;

				// Get the number of IDatas stored on the FeatureSet
				int iDataSize = set.getIData().size();

				// Create the arrays for each dataSet
				double[] value = new double[iDataSize];
				double[] uncertainty = new double[iDataSize];
				String[] units = new String[iDataSize];
				double[] position = new double[iDataSize * 3];

				// Iterate over the IDatas to fill out the arrays listed above
				for (int j = 0; j < set.getIData().size(); j++) {
					// Get the iData at the location
					IData iData = set.getIData().get(j);

					// Copy contents of iData to the array
					value[j] = iData.getValue();
					uncertainty[j] = iData.getUncertainty();
					units[j] = iData.getUnits();
					maxLength = Math.max(units[j].length(), maxLength);

					// Get the position
					ArrayList<Double> pos = iData.getPosition();
					position[3 * j] = pos.get(0);
					position[3 * j + 1] = pos.get(1);
					position[3 * j + 2] = pos.get(2);

				}

				// Setup the 3rd position in the memberList DataTypes for the
				// correct size
				// Create a custom String data type for the value
				try {
					datatype = (H5Datatype) h5File.createDatatype(
							Datatype.CLASS_STRING, maxLength, Datatype.NATIVE,
							Datatype.NATIVE);

					memberDatatypes[2] = datatype;

					// Calculate the dimensions of the length of each dataSet by
					// the number of IDatas in the FeatureSet
					long[] dims = { 1, set.getIData().size() };

					// Create the arrayList of objects to add the data to the
					// list
					ArrayList<Object> data = new ArrayList<Object>();
					data.add(value);
					data.add(uncertainty);
					data.add(units);
					data.add(position);

					// The member's sizes at each point in the dataset
					int[] memberSizes = { 1, 1, 1, 3 };

					// Create the compound dataset
					Dataset dataSet = h5File.createCompoundDS(set.getName(),
							timeStepH5Group, dims, null, null, 0,
							featureDSNames, memberDatatypes, memberSizes, data);
					dataSet.init();
				} catch (Exception e) {
					// Break and return
					logger.error(getClass().getName() + " Exception!",e);
					return false;
				}

			}

			// Iterate the counter
			counter++;
		}

		// Everything passed, return true!
		return true;

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.io.hdf.IHdfReadable#readChild(org.eclipse.ice.io.hdf.IHdfReadable)
	 */
	@Override
	public boolean readChild(IHdfReadable iHdfReadable) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.io.hdf.IHdfReadable#readAttributes(ncsa.hdf.object.h5.H5Group)
	 */
	@Override
	public boolean readAttributes(H5Group h5Group) {

		// Local attributes (so we only call read ONCE)
		// These will clear out by the garbage collector
		String name, description;
		Integer id;

		// Get the information
		name = HdfReaderFactory.readStringAttribute(h5Group, "name");
		description = HdfReaderFactory.readStringAttribute(h5Group,
				"description");
		id = HdfReaderFactory.readIntegerAttribute(h5Group, "id");

		// If any of them are erroneous, return false
		if (name == null || description == null || id == null) {
			return false;
		}
		// Set the primitive data
		this.name = name;
		this.description = description;
		this.id = id.intValue();

		return true;

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.io.hdf.IHdfReadable#readDatasets(ncsa.hdf.object.h5.H5Group)
	 */
	@Override
	public boolean readDatasets(H5Group h5Group) {

		// Local Declarations
		int numOfTimeSteps = 0;
		H5Group dataH5Group, timeStepH5Group;

		// return if null
		if (h5Group == null) {
			return false;
		}
		// Get the dataH5Group
		dataH5Group = HdfReaderFactory.getChildH5Group(h5Group,
				this.dataH5GroupName);

		ArrayList<H5Group> dataH5GroupMembers = HdfReaderFactory
				.getChildH5Groups(dataH5Group);

		// Verify the group exists
		if (dataH5Group != null && !(dataH5GroupMembers.isEmpty())) {

			// Get the number of timeSteps
			numOfTimeSteps = dataH5GroupMembers.size();

			// Iterate over the timeSteps to create the list of features
			for (int i = 0; i < numOfTimeSteps; i++) {

				// Convert the h5Group to a timeStep
				timeStepH5Group = dataH5GroupMembers.get(i);

				// Get the local time attribute and setup the featureSet list
				double time = HdfReaderFactory.readDoubleAttribute(
						timeStepH5Group, "time");
				ArrayList<FeatureSet> list = new ArrayList<FeatureSet>();

				ArrayList<HObject> timeStepH5GroupMembers = HdfReaderFactory
						.getChildH5Members(timeStepH5Group);

				// Get the number of FeatureSets
				int numOfFeatureSets = timeStepH5GroupMembers.size();

				// Iterate over the FeatureSets
				for (int j = 0; j < numOfFeatureSets; j++) {

					// Get the FeatureGroup
					H5CompoundDS featureGroup = (H5CompoundDS) timeStepH5GroupMembers
							.get(j);

					// Get the name of the feature
					String featureName = featureGroup.getName();

					// Create the feature set
					FeatureSet set = new FeatureSet(featureName);

					// Get the object data off the dataset
					Object data;
					try {
						data = featureGroup.getData();
					} catch (Exception e) {
						logger.error(getClass().getName() + " Exception!",e);
						return false;
					}

					// Convert the data to a readable format: Cast it as a
					// Vector, then grab the pieces out of it
					Vector<Object> objects = (Vector<Object>) data;

					// Cast the objects to their subsets
					// Create the arrays for each dataSet
					double[] value = (double[]) objects.get(0);
					double[] uncertainty = (double[]) objects.get(1);
					String[] units = (String[]) objects.get(2);
					double[] pos = (double[]) objects.get(3);

					// Iterate over the values and create the correct LWRData
					for (int k = 0; k < value.length; k++) {

						// Create the data and setup basic attributes
						LWRData lwrdata = new LWRData(featureName);
						lwrdata.setValue(value[k]);
						lwrdata.setUncertainty(uncertainty[k]);
						lwrdata.setUnits(units[k]);

						// Setup position
						ArrayList<Double> position = new ArrayList<Double>();
						position.add(pos[k * 3]);
						position.add(pos[k * 3 + 1]);
						position.add(pos[k * 3 + 2]);

						// Set position
						lwrdata.setPosition(position);

						// Add the lwrdata to the list
						set.addIData(lwrdata);
					}

					// Add the set to the list
					list.add(set);

				}

				// Add the list to the dataTree
				this.dataTree.put(time, list);

			}

		}

		return true;

	}

	/**
	 * <p>
	 * Returns the current time step.
	 * </p>
	 *
	 * @return <p>
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
	 * @return <p>
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
	 * @return <p>
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
	 * (non-Javadoc)
	 * @see org.eclipse.ice.datastructures.ICEObject.IUpdateable#unregister(org.eclipse.ice.datastructures.ICEObject.IUpdateableListener)
	 */
	@Override
	public void unregister(IUpdateableListener listener) {
		// TODO Auto-generated method stub

	}

}