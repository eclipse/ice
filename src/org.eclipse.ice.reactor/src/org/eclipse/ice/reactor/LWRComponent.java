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

import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.ICEObject.IUpdateableListener;
import org.eclipse.ice.datastructures.componentVisitor.IReactorComponent;
import org.eclipse.ice.analysistool.IData;
import org.eclipse.ice.analysistool.IDataProvider;
import org.eclipse.ice.io.hdf.HdfReaderFactory;
import org.eclipse.ice.io.hdf.HdfWriterFactory;
import org.eclipse.ice.io.hdf.IHdfWriteable;
import org.eclipse.ice.io.hdf.IHdfReadable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.eclipse.ice.datastructures.componentVisitor.IComponentVisitor;

import ncsa.hdf.object.Dataset;
import ncsa.hdf.object.Datatype;
import ncsa.hdf.object.HObject;
import ncsa.hdf.object.h5.H5CompoundDS;
import ncsa.hdf.object.h5.H5Datatype;
import ncsa.hdf.object.h5.H5Group;
import ncsa.hdf.object.h5.H5File;

/**
 * <!-- begin-UML-doc -->
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
 * <!-- end-UML-doc -->
 * 
 * @author s4h
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class LWRComponent implements IReactorComponent, IDataProvider,
		IHdfWriteable, IHdfReadable {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * An ArrayList of ICE IComponentListeners.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlTransient
	private ArrayList<IUpdateableListener> listeners;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Classifies a LWRComponentType. This should only be set on the lowest
	 * level constructor AND should represent the lowest level object.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlTransient
	protected HDF5LWRTagType HDF5LWRTag;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Name of this LWRComponent.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlTransient
	protected String name;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * A TreeMap implementation of IData and features. Keep in mind that there
	 * can be multiple IData for the same feature.
	 * </p>
	 * <!-- end-UML-doc -->
	 */
	@XmlTransient
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
	@XmlTransient
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
	@XmlTransient
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
	@XmlTransient
	private String timeUnit;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The id of this LWRComponent. Can not be less than zero.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlTransient
	protected int id;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The description of this LWRComponent.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlTransient
	protected String description;

	// Names for groups
	@XmlTransient
	private String dataH5GroupName = "State Point Data";
	@XmlTransient
	private String timeStepNamePrefix = "TimeStep: ";

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The nullary Constructor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public LWRComponent() {
		// begin-user-code

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

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * A parameterized Constructor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param name
	 *            <p>
	 *            Name of this LWRComponent
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public LWRComponent(String name) {
		// begin-user-code
		// Call this constructor
		this();

		// Set name appropriately
		this.setName(name);

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation notifies the listeners of the LWRComponent that its data
	 * state has changed.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected void notifyListeners() {
		// begin-user-code

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
			// Notify listeners
			this.notifyListeners();
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

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Identifiable#setId(int id)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setId(int id) {
		// begin-user-code

		// If the id is less 0, then do not set it
		if (id >= 0) {
			this.id = id;

			// Notify listeners
			this.notifyListeners();
		}

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Identifiable#getDescription()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getDescription() {
		// begin-user-code

		return this.description;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Identifiable#getId()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int getId() {
		// begin-user-code

		return this.id;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Identifiable#setName(String name)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setName(String name) {
		// begin-user-code

		// If the name is not null, and if you trim it, it is not the emptry
		// string
		if (name != null && !name.trim().isEmpty()) {
			// Trim it!
			this.name = name.trim();

			// Notify listeners
			this.notifyListeners();
		}

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Identifiable#getName()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getName() {
		// begin-user-code

		return this.name;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Identifiable#setDescription(String description)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setDescription(String description) {
		// begin-user-code

		// If the name is not null, and if you trim it, it is not the emptry
		// string
		if (description != null && !description.trim().isEmpty()) {
			// Trim it!
			this.description = description.trim();

			// Notify listeners
			this.notifyListeners();
		}

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Identifiable#equals(Object otherObject)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean equals(Object otherObject) {
		// begin-user-code

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
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Identifiable#hashCode()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int hashCode() {
		// begin-user-code
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
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IUpdateable#update(String updatedKey, String newValue)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void update(String updatedKey, String newValue) {
		// begin-user-code

		// This does nothing at this time

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IUpdateable#register(IUpdateableListener listener)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void register(IUpdateableListener listener) {
		// begin-user-code

		// If the listener is not null, add it
		if (listener != null) {
			this.listeners.add(listener);
		}

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Component#accept(IComponentVisitor visitor)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void accept(IComponentVisitor visitor) {
		// begin-user-code

		if (visitor != null) {
			visitor.visit(this);
		}
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IReactorComponent#toString()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String toString() {
		// begin-user-code
		return this.name;
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
			this.notifyListeners();
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
	 * (non-Javadoc)
	 * 
	 * @see IHdfWriteable#createGroup(H5File h5File, H5Group parentH5Group)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public H5Group createGroup(H5File h5File, H5Group parentH5Group) {
		// begin-user-code

		// Create the group for this component
		H5Group h5Group = HdfWriterFactory.createH5Group(h5File, name,
				parentH5Group);

		// Return the group
		return h5Group;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IHdfWriteable#getWriteableChildren()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<IHdfWriteable> getWriteableChildren() {
		// begin-user-code
		return null;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IHdfWriteable#writeAttributes(H5File h5File, H5Group h5Group)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean writeAttributes(H5File h5File, H5Group h5Group) {
		// begin-user-code

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
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IHdfWriteable#writeDatasets(H5File h5File, H5Group h5Group)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean writeDatasets(H5File h5File, H5Group h5Group) {
		// begin-user-code

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
					e.printStackTrace();
					return false;
				}

			}

			// Iterate the counter
			counter++;
		}

		// Everything passed, return true!
		return true;

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IHdfReadable#readChild(IHdfReadable iHdfReadable)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean readChild(IHdfReadable iHdfReadable) {
		// begin-user-code
		return true;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IHdfReadable#readAttributes(H5Group h5Group)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean readAttributes(H5Group h5Group) {
		// begin-user-code

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

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IHdfReadable#readDatasets(H5Group h5Group)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean readDatasets(H5Group h5Group) {
		// begin-user-code

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
						e.printStackTrace();
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

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the LWRComponentType.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The LWRComponentType.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public HDF5LWRTagType getHDF5LWRTag() {
		// begin-user-code

		return this.HDF5LWRTag;
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
	public void copy(LWRComponent otherObject) {
		// begin-user-code

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
		LWRComponent component = new LWRComponent();

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
	 * This operation accepts an ILWRComponentVisitor that can be visit the
	 * LWRComponent to ascertain its type and perform various type-specific
	 * operations.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param visitor
	 *            <p>
	 *            The visitor
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void accept(ILWRComponentVisitor visitor) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	@Override
	public void unregister(IUpdateableListener listener) {
		// TODO Auto-generated method stub

	}

}