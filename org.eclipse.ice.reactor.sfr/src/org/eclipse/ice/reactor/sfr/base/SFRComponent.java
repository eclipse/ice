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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.eclipse.ice.analysistool.IData;
import org.eclipse.ice.analysistool.IDataProvider;
import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.ICEObject.IUpdateable;
import org.eclipse.ice.datastructures.ICEObject.IUpdateableListener;
import org.eclipse.ice.datastructures.ICEObject.Identifiable;
import org.eclipse.ice.datastructures.componentVisitor.IComponentVisitor;
import org.eclipse.ice.datastructures.componentVisitor.IReactorComponent;

/**
 * <p >
 * The SFRComponent class represents all reactor components that can be stored
 * in a SFRComposite. This class should be treated as an extension of the Java
 * Object class where identifying pieces of information are stored on this
 * object. This class also implements the IDataProvider interface in order to
 * provide state point data capabilities for individual, unique instances of
 * this extended class.
 * </p>
 * <p >
 * This class implements the ICE Component interface.
 * </p>
 * 
 * @author Anna Wojtowicz
 */
public class SFRComponent implements IReactorComponent, IDataProvider {

	/**
	 * <p>
	 * Name of the SFRComponent.
	 * </p>
	 * 
	 */
	private String name;
	/**
	 * <p>
	 * Description of the SFRComponent.
	 * </p>
	 * 
	 */
	private String description;
	/**
	 * <p>
	 * ID of the SFRComponent; cannot be less than zero.
	 * </p>
	 * 
	 */
	private int id;
	/**
	 * <p>
	 * A description of the source of information for this provider and its
	 * data.
	 * </p>
	 * 
	 */
	private String sourceInfo;
	/**
	 * <p>
	 * The current time step. Can not be less than 0, and must be strictly less
	 * than the number of TimeSteps. Defaults to 0.
	 * </p>
	 * 
	 */
	private double time;
	/**
	 * <p>
	 * The time unit.
	 * </p>
	 * 
	 */
	private String timeUnits;
	/**
	 * <p>
	 * A TreeMap implementation of IData and features. Keep in mind that there
	 * can be multiple IData for the same feature.
	 * </p>
	 * 
	 */
	private TreeMap<Double, HashMap<String, FeatureSet>> dataTree;

	/**
	 * <p>
	 * An ArrayList of ICE IComponentListeners that should be notified when the
	 * Component has changed.
	 * </p>
	 * 
	 */
	private List<IUpdateableListener> listeners;

	/**
	 * <p>
	 * Nullary constructor.
	 * </p>
	 * 
	 */
	public SFRComponent() {

		// Initialize the default name, description, and ID.
		name = "Component 1";
		description = "Component 1's Description";
		id = 1;

		// Initialize the default source information.
		sourceInfo = "No Source Information";

		// Initialize the default time information.
		time = 0.0;
		timeUnits = "seconds";

		// Initialize the tree used to store IData.
		dataTree = new TreeMap<Double, HashMap<String, FeatureSet>>();

		// Initialize the List of listeners.
		listeners = new ArrayList<IUpdateableListener>();

		return;
	}

	/**
	 * <p>
	 * Parameterized constructor with name specified.
	 * </p>
	 * 
	 * @param name
	 *            Name of the SFRComponent. Cannot be an empty string or null.
	 */
	public SFRComponent(String name) {

		// Call the nullary constructor to set all the defaults.
		this();

		// Set the name to the specified String if it is not null and not empty.
		setName(name);

		return;
	}

	/**
	 * <p>
	 * This operation notifies the listeners of the Component that its data
	 * state has changed.
	 * </p>
	 * 
	 */
	public void notifyListeners() {

		// Stop if we have no listeners.
		if (listeners.isEmpty()) {
			return;
		}
		// Notify the listeners through a separate thread.
		Thread notifierThread = new Thread() {
			@Override
			public void run() {

				// Loop over the listeners and update them.
				for (int i = 0; i < listeners.size(); i++) {
					listeners.get(i).update(SFRComponent.this);
				}
				// FIXME - The iterator-based loop below throws
				// ConcurrentModificationExceptions. Although the List itself is
				// not modified, the listeners in it may be.
				// for (IUpdateableListener listener : listeners)
				// listener.update(SFRComponent.this);

				return;
			}
		};

		// Start the thread to send out the notifications.
		notifierThread.start();

		return;
	}

	/**
	 * Returns the name of the SFRComponent as a String.
	 * 
	 * @return The name of the SFRComponent.
	 */
	@Override
	public String getName() {

		// Return the name.
		return name;
	}

	/**
	 * <p>
	 * Returns the description of the SFRComponent as a String.
	 * </p>
	 * 
	 * @return The description of the SFRComponent.
	 * @see Identifiable#getDescription()
	 */
	@Override
	public String getDescription() {

		// Return the description.
		return description;
	}

	/**
	 * <p>
	 * Returns the ID of the SFRComponent as an int.
	 * </p>
	 * 
	 * @see Identifiable#getId()
	 * @return The ID of the SFRComponent.
	 */
	@Override
	public int getId() {

		// Return the ID.
		return id;
	}

	/**
	 * <p>
	 * Sets the source information.
	 * </p>
	 * 
	 * @param sourceInfo
	 *            The source information of the SFRComponent. Cannot be an empty
	 *            string or null.
	 */
	public void setSourceInfo(String sourceInfo) {

		// Only update the sourceInfo if the String is not null and not empty.
		if (sourceInfo != null && !sourceInfo.trim().isEmpty()) {
			this.sourceInfo = sourceInfo.trim();

			// Notify listeners of the change.
			notifyListeners();
		}

		return;
	}

	/**
	 * <p>
	 * Returns the source information as a string.
	 * </p>
	 * 
	 * @return The source information of the SFRComponent.
	 * @see IDataProvider#getSourceInfo()
	 */
	@Override
	public String getSourceInfo() {

		// Return the source information String.
		return sourceInfo;
	}

	/**
	 * <p>
	 * Returns the current time as a double.
	 * </p>
	 * 
	 * @return The current time.
	 */
	public double getCurrentTime() {

		// Return the current time.
		return time;
	}

	/**
	 * <p>
	 * Sets the time units.
	 * </p>
	 * 
	 * @param timeUnits
	 *            The time units. Cannot be an empty string or null.
	 */
	public void setTimeUnits(String timeUnits) {

		// Only set the time units if the String is not null and not empty.
		if (timeUnits != null && !timeUnits.trim().isEmpty()) {
			this.timeUnits = timeUnits.trim();

			// FIXME - This call is not in the equivalent LWR method.
			// Notify listeners of the change.
			notifyListeners();
		}

		return;
	}

	/**
	 * <p>
	 * Adds a IData piece, keyed on the feature and time, to the dataTree. If
	 * the feature exists in the tree, it will append to the end of the list.
	 * </p>
	 * 
	 * @param data
	 *            The piece of data to be added to the dataTree. Cannot be null.
	 * @param time
	 *            The time at which the data will be added to the dataTree. If
	 *            the time does not exist, it will be created. Time cannot be
	 *            negative.
	 */
	public void addData(SFRData data, double time) {

		// Check the parameters.
		if (data == null || time < 0) {
			return;
		}
		// Get the name of the feature in the data.
		String feature = data.getFeature();

		// Get the list of FeatureSets for the time in the data tree.
		HashMap<String, FeatureSet> featureSetMap = dataTree.get(time);

		// If the timestep is not in the map, add a new entry in the data tree.
		if (featureSetMap == null) {
			// Initialize a new list of FeatureSets and add it to the tree.
			featureSetMap = new HashMap<String, FeatureSet>();
			dataTree.put(time, featureSetMap);
		}

		// At this point, we have a FeatureSet Map that is in the data tree. Try
		// to get the FeatureSet for the feature name.
		FeatureSet featureSet = featureSetMap.get(feature);

		// If no existing set matches the feature name, create a new set.
		if (featureSet == null) {
			// Create a new FeatureSet and add it to the list.
			featureSet = new FeatureSet(feature);
			featureSetMap.put(feature, featureSet);
		}

		// We have either found or created a FeatureSet. Add the data to it and
		// notify listeners if the data has changed.
		if (featureSet.addIData(data)) {
			notifyListeners();
		}
		return;
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
	 *            The name of the feature. Must be a valid feature, or not data
	 *            will be removed.
	 */
	public void removeDataFromFeature(String feature) {

		// Check the parameters.
		if (feature == null) {
			return;
		}
		// Whether or not we have removed data.
		boolean updated = false;

		// For each list of FeatureSets, remove the feature's data.
		for (HashMap<String, FeatureSet> featureSetMap : dataTree.values()) {

			// Remove any FeatureSet with the feature name. If the Map returns a
			// non-null value, then we have removed something.
			if (featureSetMap.remove(feature) != null) {
				updated = true;
			}
		}

		// If we have removed data, notify listeners of this change.
		if (updated) {
			notifyListeners();
		}
		return;
	}

	/**
	 * <!-- begin-UML-doc --> Sets the ID of the SFRComponent. <!-- end-UML-doc
	 * -->
	 * 
	 * @param id
	 *            The ID of the SFRComponent. Must be non-negative.
	 * @see Identifiable#setId(int id)
	 */
	@Override
	public void setId(int id) {

		// Only allow non-negative IDs.
		if (id >= 0) {
			this.id = id;

			// Notify listeners of the change.
			notifyListeners();
		}

		return;
	}

	/**
	 * <!-- begin-UML-doc --> Sets the name of the SFRComponent. <!--
	 * end-UML-doc -->
	 * 
	 * @param name
	 *            The name of the SFRComponent. Cannot be null or an empty
	 *            string.
	 * @see Identifiable#setName(String name)
	 */
	@Override
	public void setName(String name) {

		// Only allow names that are not null and not empty.
		if (name != null && !name.trim().isEmpty()) {
			this.name = name.trim();

			// Notify listeners of the change.
			notifyListeners();
		}

		return;
	}

	/**
	 * <!-- begin-UML-doc --> Sets the description of the SFRComponent. <!--
	 * end-UML-doc -->
	 * 
	 * @param description
	 *            The description of the SFRComponent. Cannot be an empty string
	 *            or null.
	 * @see Identifiable#setDescription(String description)
	 */
	@Override
	public void setDescription(String description) {

		// Only allow descriptions that are not null.
		if (description != null && !description.trim().isEmpty()) {
			this.description = description.trim();

			// Notify listeners of the change.
			notifyListeners();
		}

		return;
	}

	/**
	 * <p>
	 * Compares the contents of objects and returns true if they are identical,
	 * otherwise returns false.
	 * </p>
	 * 
	 * @param otherObject
	 *            The other object to compare against.
	 * @return Returns true if the two objects are equal, otherwise false.
	 * @see Identifiable#equals(Object otherObject)
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
		else if (otherObject != null && otherObject instanceof SFRComponent) {

			// Cast the other object to an SFRComponent.
			SFRComponent component = (SFRComponent) otherObject;

			// Compare all the variables. Save the biggest for last (Java should
			// short-circuit the logical operators as soon as it detects a
			// mismatch).
			equals = (id == component.id && time == component.time
					&& name.equals(component.name)
					&& description.equals(component.description)
					&& timeUnits.equals(component.timeUnits)
					&& sourceInfo.equals(component.sourceInfo) && dataTree
					.equals(component.dataTree));
		}

		return equals;
	}

	/**
	 * <p>
	 * Returns the hashCode of the object.
	 * </p>
	 * 
	 * @return The hash of the object.
	 * @see Identifiable#hashCode()
	 */
	@Override
	public int hashCode() {

		// Static hash at 31.
		int hash = 31;

		// Add local hashes.
		hash += 31 * name.hashCode();
		hash += 31 * description.hashCode();
		hash += 31 * id;
		hash += 31 * sourceInfo.hashCode();
		hash += 31 * time;
		hash += 31 * timeUnits.hashCode();
		hash += 31 * dataTree.hashCode();

		return hash;
	}

	/**
	 * <p>
	 * Deep copies and returns a newly instantiated object.
	 * </p>
	 * 
	 * @return The newly instantiated copied object.
	 */
	@Override
	public Object clone() {

		// Initialize a new object.
		SFRComponent object = new SFRComponent();

		// Copy the contents from this one.
		object.copy(this);

		// Return the newly instantiated object.
		return object;
	}

	/**
	 * <!-- begin-UML-doc --> Deep copies the contents of the SFRComponent. <!--
	 * end-UML-doc -->
	 * 
	 * @param component
	 *            The other SFRComponent to copy the contents of.
	 */
	public void copy(SFRComponent component) {

		// Check the parameters.
		if (component == null) {
			return;
		}
		// Copy the name, description, and ID.
		name = component.name;
		description = component.description;
		id = component.id;

		// Copy the source information.
		sourceInfo = component.sourceInfo;

		// Copy the time information.
		time = component.time;
		timeUnits = component.timeUnits;

		// Copy the data tree used to store IData.
		dataTree = new TreeMap<Double, HashMap<String, FeatureSet>>();

		// FIXME - The corresponding LWR code also does a shallow copy here.
		// Should we really do a deep copy on all of this? Otherwise, the
		// documentation is sort of lying about deep copying.

		// Perform a shallow copy on the data tree.
		for (Entry<Double, HashMap<String, FeatureSet>> entry : component.dataTree
				.entrySet()) {
			dataTree.put(entry.getKey(), entry.getValue());
		}

		// Add the listeners from the other component.
		listeners.addAll(component.listeners);

		// Notify listeners of the changes.
		notifyListeners();

		return;
	}

	/**
	 * <p>
	 * This operation notifies a class that has implemented IUpdateable that the
	 * value associated with the particular key has been updated.
	 * </p>
	 * 
	 * @param updatedKey
	 *            A unique key that describes the value that is to be updated.
	 * @param newValue
	 *            The updated value of the key.
	 * @see IUpdateable#update(String updatedKey, String newValue)
	 */
	@Override
	public void update(String updatedKey, String newValue) {

		// Nothing is required for this method.

	}

	/**
	 * <p>
	 * This operation registers a listener that realizes the IComponentListener
	 * interface with the Component so that it can receive notifications of
	 * changes to the Component, if the Component publishes such information.
	 * </p>
	 * 
	 * @param listener
	 *            The new listener that should be notified when the the
	 *            Component's state changes.
	 * @see IUpdateable#register(IUpdateableListener listener)
	 */
	@Override
	public void register(IUpdateableListener listener) {

		// Only register listeners that are not null.
		if (listener != null) {
			listeners.add(listener);
		}
		return;
	}

	/**
	 * <!-- begin-UML-doc --> Accepts an IComponentVisitor that can visit the
	 * SFRComponent to ascertain its type and perform various type-specific
	 * operations. <!-- end-UML-doc -->
	 * 
	 * @param visitor
	 *            The SFRComponent's visitor.
	 * @see Component#accept(IComponentVisitor visitor)
	 */
	@Override
	public void accept(IComponentVisitor visitor) {

		// Only accept valid visitors.
		if (visitor != null) {
			visitor.visit(this);
		}
		return;
	}

	/**
	 * <!-- begin-UML-doc --> This method calls the ISFRComponentVisitor's visit
	 * operation, passing itself as the argument.<br>
	 * <br>
	 * SFRComponent is not represented with a visit(SFRComponent) operation in
	 * ISFRComponentVisitor, so this method does nothing. However, <b>subclasses
	 * that are represented should override this method</b>. <!-- end-UML-doc
	 * -->
	 * 
	 * @param visitor
	 *            An ISFRComponentVisitor that is visiting this SFRComponent.
	 */
	public void accept(ISFRComponentVisitor visitor) {

		return;
	}

	/**
	 * <!-- begin-UML-doc --> Gets a String representation of the SFRComponent.
	 * 
	 * @return The name of the SFRComponent as a string.
	 * @see IReactorComponent#toString()
	 */
	@Override
	public String toString() {

		// Return the component's name.
		return name;
	}

	/**
	 * <!-- begin-UML-doc --> Returns an ArrayList of strings representing the
	 * names of all features contained in the SFRComponent's dataTree. <!--
	 * end-UML-doc -->
	 * 
	 * @return ArrayList of strings representing all unique features in the
	 *         dataTree.
	 * @see IDataProvider#getFeatureList()
	 */
	@Override
	public ArrayList<String> getFeatureList() {

		// Initialize the list of feature names.
		ArrayList<String> featureList = new ArrayList<String>();

		// Initialize a Set of Strings to hold only unique feature names.
		Set<String> featureNames = new HashSet<String>();

		// Loop over all of the lists of FeatureSets.
		for (HashMap<String, FeatureSet> featureSetMap : dataTree.values()) {

			// Add all of the features (keys) from this key set.
			featureNames.addAll(featureSetMap.keySet());
		}

		// Add each name from the Set to the List.
		featureList.addAll(featureNames);

		// Return the list of feature names.
		return featureList;
	}

	/**
	 * <!-- begin-UML-doc --> Returns the total number of time steps contained
	 * in the SFRComponent's dataTree. <!-- end-UML-doc -->
	 * 
	 * @return Returns the total time steps in the dataTree.
	 * @see IDataProvider#getNumberOfTimeSteps()
	 */
	@Override
	public int getNumberOfTimeSteps() {

		// Return the size of the data tree ( which is keyed on time values).
		return dataTree.size();
	}

	/**
	 * <!-- begin-UML-doc --> Sets the current time. <!-- end-UML-doc -->
	 * 
	 * @param newTime
	 *            The new time to set.
	 * @see IDataProvider#setTime(double step)
	 */
	@Override
	public void setTime(double newTime) {

		// We only allow non-negative times
		if (newTime >= 0.0) {
			time = newTime;

			// Notify listeners of the change.
			notifyListeners();
		}

		return;
	}

	/**
	 * <!-- begin-UML-doc --> Returns all IData corresponding to the specified
	 * features at the current time. Will return an empty list if the feature at
	 * the current time contains no data. <!-- end-UML-doc -->
	 * 
	 * @param feature
	 *            The feature of IData to be returned. Cannot be null.
	 * @return An ArrayList of all data found with the specified feature, at the
	 *         current time.
	 * @see IDataProvider#getDataAtCurrentTime(String feature)
	 */
	@Override
	public ArrayList<IData> getDataAtCurrentTime(String feature) {

		// Don't process anything if the parameter is invalid.
		if (feature != null) {

			// Get the list of FeatureSets at the current time.
			HashMap<String, FeatureSet> featureSetMap = dataTree.get(time);

			// If the time is in the data tree, find the FeatureSet with the
			// requested name.
			if (featureSetMap != null) {
				FeatureSet featureSet = featureSetMap.get(feature);

				// If the FeatureSet exists, get its data.
				if (featureSet != null) {
					return featureSet.getData();
				}
			}
		}

		// No data was found, so return an empty list.
		return new ArrayList<IData>();
	}

	/**
	 * <!-- begin-UML-doc --> Returns an ArrayList of strings representing all
	 * features found in the SFRComponent's dataTree, at the current time. <!--
	 * end-UML-doc -->
	 * 
	 * @return An ArrayList of strings representing all features in the
	 *         dataTree, at the current time.
	 * @see IDataProvider#getFeaturesAtCurrentTime()
	 */
	@Override
	public ArrayList<String> getFeaturesAtCurrentTime() {

		// Initialize the list of features to return.
		ArrayList<String> features = new ArrayList<String>();

		// Get the list of FeatureSets at the current time.
		HashMap<String, FeatureSet> featureSets = dataTree.get(time);

		// If the time is in the data tree, add all of the corresponding feature
		// names to the list.
		if (featureSets != null) {
			features.addAll(featureSets.keySet());
		}
		// Return the list of features.
		return features;
	}

	/**
	 * <!-- begin-UML-doc --> Returns an ArrayList of doubles representing all
	 * times found in the SFRComponent's dataTree. <!-- end-UML-doc -->
	 * 
	 * @return An ArrayList of doubles representing all times in the dataTree.
	 * @see IDataProvider#getTimes()
	 */
	@Override
	public ArrayList<Double> getTimes() {

		// Initialize the list of times.
		ArrayList<Double> times = new ArrayList<Double>();

		// Add each time in the data tree to the list.
		for (double time : dataTree.keySet()) {
			times.add(time);
		}

		// Return the list of times.
		return times;
	}

	/**
	 * <!-- begin-UML-doc --> Returns an integer representing the time step
	 * associated to the specified time. Returns -1 if no time step is found.
	 * 
	 * @param time
	 *            The time.
	 * @return The time step associated to the input time. If no time step is
	 *         found, returns -1.
	 * @see IDataProvider#getTimeStep(double time)
	 */
	@Override
	public int getTimeStep(double time) {

		// Initialize an iterator over the data tree's keys and a counter.
		Iterator<Double> iter = dataTree.keySet().iterator();
		int counter = 0;

		// Loop over the keys in the data tree.
		while (iter.hasNext()) {

			// If the key matches, return the key's index.
			if (iter.next().equals(time)) {
				return counter;
			}
			// Update the index counter.
			counter++;
		}

		// A matching time was not found.
		return -1;
	}

	/**
	 * <!-- begin-UML-doc --> Returns the time units as a string. <!--
	 * end-UML-doc -->
	 * 
	 * @return The time units.
	 * @see IDataProvider#getTimeUnits()
	 */
	@Override
	public String getTimeUnits() {

		// Return the time units String.
		return timeUnits;
	}

	@Override
	public void unregister(IUpdateableListener listener) {
		// TODO Auto-generated method stub

	}

}