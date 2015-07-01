/*******************************************************************************
 * Copyright (c) 2011, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.item;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.ice.datastructures.ICEObject.IUpdateable;

/**
 * <p>
 * The Registry class holds all of the information relevant to the Item, which
 * is accessible as a key-value pair. Entries may be registered with the
 * Registry and it will call their update method when the value of a key is
 * initially set or changed.
 * </p>
 * 
 * @author Jay Jay Billings
 */
public class Registry {

	/**
	 * <p>
	 * The set of IUpdateable objects that are registered for updates.
	 * </p>
	 * 
	 */
	private ArrayList<IUpdateable> updateables;

	/**
	 * The map for mapping keys to things that should be updated.
	 */
	private HashMap<String, ArrayList<IUpdateable>> keysAndComponents;

	/**
	 * The map for storing keys and values
	 */
	private HashMap<String, String> keysAndValues;

	/**
	 * <p>
	 * The constructor.
	 * </p>
	 */
	public Registry() {
		keysAndValues = new HashMap<String, String>();
		keysAndComponents = new HashMap<String, ArrayList<IUpdateable>>();
	}

	/**
	 * <p>
	 * The register operation registers a class that implements IUpdateable
	 * against a key in the Registry. If the value of the key is set or changed,
	 * the Registry will call the update operation of the IUpdateable interface
	 * when the Registry.dispatch() is called. If the key is not in the
	 * Registry, it will be added as part of the registration.
	 * </p>
	 * 
	 * @param registrant
	 *            <p>
	 *            The Entry that depends on the value of the key and should be
	 *            notified of changes.
	 *            </p>
	 * @param key
	 *            <p>
	 *            The key for which the Entry should be notified of changes to
	 *            its value.
	 *            </p>
	 * @return <p>
	 *         True if the registration is successful, false otherwise.
	 *         </p>
	 */
	public boolean register(IUpdateable registrant, String key) {

		// Local Declarations
		boolean retVal = false;

		// Check the maps to see if they contain the key
		if (keysAndComponents.containsKey(key)
				&& keysAndValues.containsKey(key)) {
			// Just push the registrant onto the list, set the return value
			retVal = keysAndComponents.get(key).add(registrant);
		} else {
			// Create a a dummy list for the Entries
			ArrayList<IUpdateable> dummyList = new ArrayList<IUpdateable>();
			dummyList.add(registrant);
			// Put the key and the list into the map
			keysAndComponents.put(key, dummyList);
			keysAndValues.put(key, null);
			// Set the return value by checking for the keys
			retVal = keysAndComponents.containsKey(key)
					|| keysAndValues.containsKey(key);
		}

		return retVal;
	}

	/**
	 * <p>
	 * The dispatch operation directs the Registry to call the update operation
	 * on all of the Entries that are registered against keys with updated
	 * values.
	 * </p>
	 */
	public void dispatch() {

		// Loop over all the keys and update the ones that need it.
		for (String aKey : keysAndValues.keySet()) {
			// Debug info
			// Only do the update for keys that have registrants
			if (keysAndComponents.containsKey(aKey)) {
				for (IUpdateable registrant : keysAndComponents.get(aKey)) {
					registrant.update(aKey, keysAndValues.get(aKey));
					// Debug information
				}
			}
		}

		// This is debug information only - print the registered keys and the
		// hashcodes

		return;
	}

	/**
	 * <p>
	 * The setValue operations sets the value for a certain key.
	 * </p>
	 * 
	 * @param key
	 *            <p>
	 *            The key whose value should be updated.
	 *            </p>
	 * @param value
	 *            <p>
	 *            The new value.
	 *            </p>
	 * @return <p>
	 *         True if the value is set, false otherwise.
	 *         </p>
	 */
	public boolean setValue(String key, String value) {
		boolean retVal = false;

		// Set the value against the key
		keysAndValues.put(key, value);
		// Set the return value by making sure it actually made it into the map
		retVal = keysAndValues.containsKey(key);

		return retVal;
	}

	/**
	 * <p>
	 * The getValue operation checks the Registry for a key and returns its
	 * value. It returns NULL if the key does not have a value or if the key
	 * does not exist in the Registry.
	 * </p>
	 * 
	 * @param key
	 *            <p>
	 *            The key for which the value should be returned.
	 *            </p>
	 * @return <p>
	 *         The value of the key. NULL if the value is unset or key does not
	 *         exist.
	 *         </p>
	 */
	public String getValue(String key) {
		return keysAndValues.get(key);
	}

	/**
	 * <p>
	 * The updateValue operation updates the value for the given key in the
	 * Registry. It does not add the key to the Registry, so if the key is not
	 * already there this operation will do nothing. It returns true if it is
	 * able to update the value and false if it can not find the key in the
	 * Registry or for some other problem.
	 * </p>
	 * 
	 * @param key
	 *            <p>
	 *            The key that should be updated.
	 *            </p>
	 * @param value
	 *            <p>
	 *            The new value.
	 *            </p>
	 * @return <p>
	 *         True if it is can update the value and false if not
	 *         </p>
	 */
	public boolean updateValue(String key, String value) {

		// Local Declarations
		boolean retVal = false;

		// Update the value if it is in the map
		if (keysAndValues.containsKey(key)) {
			keysAndValues.put(key, value);
			retVal = true;
		}

		return retVal;
	}

	/**
	 * <p>
	 * This operation checks to see if the Registry contains a particular key.
	 * </p>
	 * 
	 * @param keyToCheck
	 *            <p>
	 *            The key to be checked.
	 *            </p>
	 * @return <p>
	 *         True if the key exists in the registry, false otherwise.
	 *         </p>
	 */
	public boolean containsKey(String keyToCheck) {
		return keysAndValues.containsKey(keyToCheck);
	}
}