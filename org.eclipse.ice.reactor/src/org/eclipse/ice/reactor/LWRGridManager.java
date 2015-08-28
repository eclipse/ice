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
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.ice.datastructures.ICEObject.Component;

/**
 * The LWRGridManager class manages LWRComponents and their GridLocations on a
 * Cartesian grid with an equal number of rows and columns. This class
 * implements the ICE IGridManager interface.
 * <p>
 * This class also allows a "pass through" for LWRDataProviders, which are used
 * to store state point data. This is a preferred method for storing data over
 * time instead of using LWRComponent's IDataProvider directly. Please see
 * GridLocation for more details on the usage of this delegation class.
 * </p>
 *
 * @author Scott Forest Hull II
 */
public class LWRGridManager extends LWRComponent implements IGridManager {
	/**
	 *
	 */
	private TreeMap<GridLocation, String> lWRComponents;

	/**
	 * The size of the rows and columns.
	 */
	private int size;

	/**
	 * A grid table suffix for reading the dataset.
	 */
	protected String hdf5GridTableSuffix = "'s Grid Table";
	// Names for groups
	private String dataH5GroupName = "Positions";
	private String timeStepNamePrefix = "TimeStep: ";

	private String headTableString = " headTable";
	private String dataTableString = " dataTable";

	/**
	 * The Constructor.
	 *
	 * @param size
	 *            The maximum number of rows or columns.
	 */
	public LWRGridManager(int size) {

		// Setup LWRComponent Attributes
		this.name = "LWRGridManager 1";
		this.description = "LWRGridManager 1's Description";
		this.id = 1;

		// Setup defaults for the LWRGridManager
		lWRComponents = new TreeMap<GridLocation, String>();
		this.size = 1;

		// Setup size if it is at least 1 or greater. Otherwise use defaults
		if (size > 0) {
			this.size = size;
		}

		// Setup the HDF5LWRTagType to correct type
		this.HDF5LWRTag = HDF5LWRTagType.LWRGRIDMANAGER;

	}

	/**
	 * Returns the maximum number of rows or columns.
	 *
	 * @return Returns the maximum number of rows or columns.
	 */
	public int getSize() {

		return this.size;
	}

	/**
	 * Overrides the equals operation to check the attributes on this object
	 * with another object of the same type. Returns true if the objects are
	 * equal. False otherwise.
	 *
	 * @param otherObject
	 *            The object to be compared.
	 * @return True if otherObject is equal. False otherwise.
	 */
	@Override
	public boolean equals(Object otherObject) {

		// Local Declarations
		LWRGridManager manager;
		boolean retVal = false;

		// Make sure the object is not null and is an instance of this object
		if (otherObject != null && otherObject instanceof LWRGridManager) {
			manager = (LWRGridManager) otherObject;

			// If they are equal to the same heap, return true
			if (this == otherObject) {
				return true;
			}
			// Check values
			retVal = (super.equals(otherObject)
					&& this.lWRComponents.equals(manager.lWRComponents)
					&& this.size == manager.size);

			// If the size is not equal, return false
			if (this.lWRComponents.size() != manager.lWRComponents.size()) {
				return false;
			}

			// Check the map for comparisons
			for (Map.Entry<GridLocation, String> entry : this.lWRComponents
					.entrySet()) {

				// Grab the location
				GridLocation location = entry.getKey();
				boolean objectFound = false;

				// Iterate over the manager's list to see if the object exists.
				// If it does, mark it as true
				for (Map.Entry<GridLocation, String> managerEntry : manager.lWRComponents
						.entrySet()) {
					if (managerEntry.getKey().equals(location) && managerEntry
							.getValue().equals(entry.getValue())) {
						objectFound = true;
						break;
					}
				}
				// If the object was not found, then return false.
				if (!objectFound) {
					return false;
				}
			}

		}

		// Return retVal
		return retVal;
	}

	/**
	 * Returns the hashCode of the object.
	 *
	 * @return The hash of the object.
	 */
	@Override
	public int hashCode() {

		// Local Declarations
		int hash = super.hashCode();

		// Compute hash of attributes
		hash += 31 * this.lWRComponents.hashCode();
		hash += 31 * this.size;

		// Return the hash
		return hash;

	}

	/**
	 * Deep copies the contents of the object.
	 *
	 * @param otherObject
	 *            The object to be copied.
	 */
	public void copy(LWRGridManager otherObject) {

		// Local Declarations
		Iterator<GridLocation> iter;
		GridLocation location;

		// If the otherObject is null, return
		if (otherObject == null) {
			return;
		}
		// Copy contents- super
		super.copy(otherObject);

		// Copy contents

		this.size = otherObject.size;

		// Perform a deep copy of the tree
		this.lWRComponents.clear();

		// Get the iterator
		iter = otherObject.lWRComponents.keySet().iterator();

		// Iterate over the list, deep copy the lWRComponents and values
		while (iter.hasNext()) {
			location = iter.next();
			this.lWRComponents.put((GridLocation) location.clone(),
					otherObject.lWRComponents.get(location));
		}

	}

	/**
	 * Deep copies and returns a newly instantiated object.
	 *
	 * @return The newly instantiated copied object.
	 */
	@Override
	public Object clone() {

		// Local Declarations
		LWRGridManager manager = new LWRGridManager(0);

		// Copy the contents of this manager
		manager.copy(this);

		// Return the newly instantiated object
		return manager;

	}

	/**
	 * (non-Javadoc)
	 *
	 * @see IGridManager#getComponentName(GridLocation location)
	 */
	@Override
	public String getComponentName(GridLocation location) {

		// If the location is not null, return the component
		if (location != null) {
			return this.lWRComponents.get(location);
		}
		return null;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see IGridManager#addComponent(Component component, GridLocation
	 *      location)
	 */
	@Override
	public void addComponent(Component component, GridLocation location) {

		// If the passed args are not null and if the locations are valid, add
		// to the grid
		// Also, if the location does already exist, do not add component
		if (component != null && location != null
				&& location.getRow() < this.size
				&& location.getColumn() < this.size && location.getRow() >= 0
				&& location.getColumn() >= 0
				&& !this.lWRComponents.containsKey(location)) {
			this.lWRComponents.put(location, component.getName());
		}

	}

	/**
	 * (non-Javadoc)
	 *
	 * @see IGridManager#removeComponent(GridLocation location)
	 */
	@Override
	public void removeComponent(GridLocation location) {

		// If the location is not null, remove location
		if (location != null) {
			this.lWRComponents.remove(location);
		}

	}

	/**
	 * (non-Javadoc)
	 *
	 * @see IGridManager#removeComponent(Component component)
	 */
	@Override
	public void removeComponent(Component component) {

		// If the component is not null, remove the associated component
		if (component != null) {

			// Iterate over a map
			for (Map.Entry<GridLocation, String> entry : this.lWRComponents
					.entrySet()) {
				String lComponent = entry.getValue();

				// If the components are equal, remove the entry
				if (lComponent.equals(component.getName())) {
					this.lWRComponents.remove(entry.getKey());
					return;
				}
			}

		}

	}

	/**
	 * 
	 * Returns the data provider at the grid location or null if it does not
	 * exist.
	 * 
	 *
	 * @param location
	 *            The grid location.
	 * @return The provider at that location
	 * 
	 */
	public LWRDataProvider getDataProviderAtLocation(GridLocation location) {

		if (location == null) {
			return null;
		}
		// Iterate over a map
		for (Map.Entry<GridLocation, String> entry : this.lWRComponents
				.entrySet()) {

			// If the key exists, return
			if (location.getRow() == entry.getKey().getRow()
					&& location.getColumn() == entry.getKey().getColumn()) {
				return entry.getKey().getLWRDataProvider();
			}
		}

		// Not found!
		return null;

	}

	/**
	 * Returns the list of grid locations at the given name. If none are found,
	 * returns an empty list.
	 *
	 * @param name
	 *            The name
	 * @return The locations
	 * 
	 */
	public ArrayList<GridLocation> getGridLocationsAtName(String name) {

		// Local Declarations
		ArrayList<GridLocation> locations = new ArrayList<GridLocation>();

		if (name == null) {
			return locations;
		}
		// Iterate over a map
		for (Map.Entry<GridLocation, String> entry : this.lWRComponents
				.entrySet()) {

			// If the key exists, return
			if (name.equals(entry.getValue())) {
				locations.add((GridLocation) entry.getKey().clone());
			}
		}

		// Return list
		return locations;

	}

}