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

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * This class extends the GridManager to manage IDataProviders for each location
 * in the grid. Because the client class already has a GridManager to maintain
 * locations, this class removes the burden of managing the data providers when
 * that functionality is so closely tied to managing the grid locations.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class GridDataManager extends GridManager {

	// A map of IDataProviders (typically SFRComponents) keyed on the locations.
	// Updates to the super class' location sshould also be reflected in this.
	private Map<Integer, SFRComponent> dataProviders;

	// FIXME - For now, we need to use SFRComponents so that data *can be added*
	// to the IDataProvider. The LWR model currently has an LWRDataProvider,
	// which is essentially an LWRComponent without the name, description, etc.

	/**
	 * The default constructor.
	 * 
	 * @param size
	 *            The size of the Grid.
	 */
	public GridDataManager(int size) {
		super(size);

		// Initialize the Map of IDataProviders.
		dataProviders = new TreeMap<Integer, SFRComponent>();

		return;
	}

	/**
	 * Gets an IDataProvider for a specified location. If no component inhabits
	 * the location, then the return value will be null.
	 * 
	 * @param location
	 *            The location to fetch the IDataProvider.
	 * @return An IDataProvider (typically an SFRComponent) or null if the
	 *         location is unset.
	 */
	public SFRComponent getDataProvider(int location) {
		return dataProviders.get(location);
	}

	/**
	 * Overrides the super class' behavior to also initialize an IDataProvider
	 * for the location.
	 */
	@Override
	public boolean addComponent(String name, int location) {

		// By default, we did not succeed in adding the Component.
		boolean success = super.addComponent(name, location);

		// If possible, add a new IDataProvider to the location.
		if (success) {
			dataProviders.put(location, new SFRComponent());
		}
		return success;
	}

	/**
	 * Overrides the super class' behavior to also remove the IDataProvider from
	 * the location.
	 */
	@Override
	public boolean removeComponent(int location) {

		// By default, we did not succeed in removing the Component location.
		boolean success = super.removeComponent(location);

		// If possible, remove the associated IDataProvider from the map.
		if (success) {
			dataProviders.remove(location);
		}
		return success;
	}

	/**
	 * Overrides the super class' behavior to also remove the IDataProvider from
	 * any location inhabited by the component.
	 */
	@Override
	public boolean removeComponent(String name) {

		// Get the locations for this component.
		List<Integer> locations = super.getComponentLocations(name);

		// By default, we did not succeed in removing the Component.
		boolean success = super.removeComponent(name);

		// If possible, remove all of the associated IDataProviders from the
		// map.
		if (success) {
			for (int location : locations) {
				dataProviders.remove(location);
			}
		}
		return success;
	}

	/**
	 * <!-- begin-UML-doc --> Compares the contents of objects and returns true
	 * if they are identical, otherwise returns false. <!-- end-UML-doc -->
	 * 
	 * @param otherObject
	 *            The object to compare against.
	 * @return True if otherObject is equal. False otherwise.
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
		else if (otherObject != null && otherObject instanceof GridDataManager) {

			// We can now cast the other object.
			GridDataManager manager = (GridDataManager) otherObject;

			// Compare the values between the two objects.
			equals = (super.equals(manager) && dataProviders
					.equals(manager.dataProviders));
		}

		return equals;
	}

	/**
	 * <p>
	 * Returns the hashCode of the object.
	 * </p>
	 * 
	 * @return The hash of the object.
	 */
	@Override
	public int hashCode() {

		// Static hash at 31.
		int hash = super.hashCode();

		// Add local hashes.
		hash += 31 * dataProviders.hashCode();

		return hash;
	}

	/**
	 * <p>
	 * Deep copies the contents of the object from another object.
	 * </p>
	 * 
	 * @param otherObject
	 *            The other object to copy the contents of.
	 */
	public void copy(GridDataManager otherObject) {

		// Check the parameters.
		if (otherObject == null) {
			return;
		}
		super.copy(otherObject);

		// Copy the Map.
		dataProviders.clear();
		dataProviders.putAll(otherObject.dataProviders);

		return;
	}

	/**
	 * <p>
	 * Deep copies and returns a newly instantiated object.
	 * </p>
	 * 
	 * @return The newly instantiated cloned object.
	 */
	@Override
	public Object clone() {

		// Initialize a new GridManager.
		GridDataManager manager = new GridDataManager(1);

		// Copy the contents from this one.
		manager.copy(this);

		// Return the newly instantiated object.
		return manager;
	}
}
