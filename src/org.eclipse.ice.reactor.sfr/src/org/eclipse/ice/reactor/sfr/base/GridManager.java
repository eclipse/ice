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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * <!-- begin-UML-doc --> A GridManager manages the locations of ICE Components
 * in a set of possible locations.
 * <p>
 * Classes that employ this GridManager are expected to translate between their
 * own geometric coordinate system and 0-based indexes used within this class.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class GridManager implements IGridManager {

	/**
	 * <!-- begin-UML-doc --> The size of the list of possible locations. This
	 * is considered the maximum index plus 1. <!-- end-UML-doc -->
	 */
	private int size;

	/**
	 * <!-- begin-UML-doc --> A Map of Components keyed on their names. Each
	 * entry contains a Set of locations for quickly looking up all the
	 * locations for a particular Component. <!-- end-UML-doc -->
	 */
	private Map<String, Set<Integer>> components;

	/**
	 * <!-- begin-UML-doc --> A Map of locations keyed on their index. Each
	 * entry contains the name of the Component at that location. If the
	 * location is not in the Map, the value should be null. <!-- end-UML-doc
	 * -->
	 */
	private Map<Integer, String> locations;

	/**
	 * <!-- begin-UML-doc --> The default constructor. <!-- end-UML-doc -->
	 * 
	 * @param size
	 *            The maximum index supported plus 1.
	 */
	public GridManager(int size) {

		this.size = (size > 0 ? size : Integer.MAX_VALUE);

		components = new HashMap<String, Set<Integer>>();

		locations = new TreeMap<Integer, String>();

		return;
	}

	/**
	 * <!-- begin-UML-doc --> Gets the name of the Component in the specified
	 * grid location. <!-- end-UML-doc -->
	 * 
	 * @param location
	 *            The index of the location in the grid being managed.
	 * @return A Component name or <code>null</code> if no name is found.
	 */
	public String getComponentName(int location) {

		String name = null;

		if (location >= 0 && location < size) {
			name = locations.get(location);
		}
		return name;
	}

	/**
	 * <!-- begin-UML-doc --> Gets all the locations occupied by a Component in
	 * the grid. <!-- end-UML-doc -->
	 * 
	 * @param name
	 *            The name of the Component to search for.
	 * @return A List of location indexes or an empty List if the Component is
	 *         invalid.
	 */
	public List<Integer> getComponentLocations(String name) {

		List<Integer> locations = new ArrayList<Integer>();

		if (name != null) {
			Set<Integer> indexes = components.get(name);

			if (indexes != null) {
				for (int index : indexes) {
					locations.add(index);
				}
			}

		}

		return locations;
	}

	/**
	 * <!-- begin-UML-doc --> Adds a Component to the specified location in the
	 * grid. If the parameters are valid and the Component did not already
	 * occupy that location, this will return true. <!-- end-UML-doc -->
	 * 
	 * @param name
	 *            The name of the Component to add to the grid.
	 * @param location
	 *            The location index in which to put the Component.
	 * @return True if the component was added to the location, false otherwise.
	 */
	public boolean addComponent(String name, int location) {

		// By default, we did not succeed in adding the Component.
		boolean success = false;

		if (name != null && location >= 0 && location < size) {

			// Get the Set of indexes this Component occupies.
			Set<Integer> indexes = components.get(name);
			if (indexes == null) {
				indexes = new HashSet<Integer>();
				components.put(name, indexes);
			}

			// Add this location to that Set. It will return true if the
			// location did not previously exist in the Set.
			if (indexes.add(location)) {

				// Update the Map of locations.
				String oldName = locations.put(location, name);

				// If there was a previous Component in that location, update
				// its
				// Set of locations.
				if (oldName != null && !oldName.equals(name)) {
					indexes = components.get(oldName);
					indexes.remove(location);

					// If it no longer resides in any locations, remove it from
					// the
					// Map of Components.
					if (indexes.isEmpty()) {
						components.remove(oldName);
					}
				}

				// We have successfully added the Component to the location.
				success = true;
			}
		}

		return success;
	}

	/**
	 * <!-- begin-UML-doc --> Dissociates the Component at a specified location
	 * with that location. If the location has a corresponding Component, this
	 * will return true. <!-- end-UML-doc -->
	 * 
	 * @param location
	 *            The index of the Component to remove.
	 * @return True if a component was successfully removed from the location,
	 *         false otherwise.
	 */
	public boolean removeComponent(int location) {

		// By default, we did not succeed in removing the Component location.
		boolean success = false;

		if (location >= 0 && location < size) {
			String name = locations.remove(location);

			// If there was a previous Component in that location, update its
			// Set of locations.
			if (name != null) {
				Set<Integer> indexes = components.get(name);

				// Remove the specified location from the Component's list of
				// locations. This operation will return true if the location
				// was valid.
				success = indexes.remove(location);

				// If it no longer resides in any locations, remove it from the
				// Map of Components.
				if (indexes.isEmpty()) {
					components.remove(name);
				}
			}
		}

		return success;
	}

	/**
	 * <!-- begin-UML-doc --> Dissociates the Component with all locations that
	 * it currently occupies. If the Component had corresponding locations, this
	 * will return true. <!-- end-UML-doc -->
	 * 
	 * @param name
	 *            The name of the Component to remove from the GridManager.
	 * @return True if the component was successfully removed from the
	 *         IGridManager. False otherwise.
	 */
	public boolean removeComponent(String name) {

		// By default, we did not succeed in removing the Component.
		boolean success = false;

		if (name != null) {

			Set<Integer> indexes = components.remove(name);
			if (indexes != null) {
				for (int index : indexes) {
					locations.remove(index);
				}

				// If we found a valid set of indexes, we have removed the
				// Component successfully.
				success = true;
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
	public boolean equals(Object otherObject) {

		// By default, the objects are not equivalent.
		boolean equals = false;

		// Check the reference.
		if (this == otherObject) {
			equals = true;
		}
		// Check the information stored in the other object.
		else if (otherObject != null && otherObject instanceof GridManager) {

			// We can now cast the other object.
			GridManager manager = (GridManager) otherObject;

			// Compare the values between the two objects.
			equals = (size == manager.size
					&& components.equals(manager.components) && locations
					.equals(manager.locations));
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
	public int hashCode() {

		// Static hash at 31.
		int hash = 31;

		// Add local hashes.
		hash += 31 * size;
		hash += 31 * components.hashCode();
		hash += 31 * locations.hashCode();

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
	public void copy(GridManager otherObject) {

		// Check the parameters.
		if (otherObject == null) {
			return;
		}

		// Copy the size.
		size = otherObject.size;

		// Copy the two Maps.
		components.clear();
		components.putAll(otherObject.components);
		locations.clear();
		locations.putAll(otherObject.locations);

		return;
	}

	/**
	 * <p>
	 * Deep copies and returns a newly instantiated object.
	 * </p>
	 * 
	 * @return The newly instantiated cloned object.
	 */
	public Object clone() {

		// Initialize a new GridManager.
		GridManager manager = new GridManager(size);

		// Copy the contents from this one.
		manager.copy(this);

		// Return the newly instantiated object.
		return manager;
	}

}
