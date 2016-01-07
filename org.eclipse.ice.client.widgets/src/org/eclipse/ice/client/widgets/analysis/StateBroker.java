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
package org.eclipse.ice.client.widgets.analysis;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A StateBroker instance manages a HashMap of key/value pairs and a registry
 * (also a HashMap) of IStateListeners who want to be notified when the value of
 * a certain key is changed.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class StateBroker {
	/**
	 * The values stored in the HashMap.
	 */
	private Map<String, Object> values;
	/**
	 * The registry of IStateListeners. Keyed on the same keys as the values
	 * HashMap.
	 */
	private Map<String, HashSet<IStateListener>> registry;

	/**
	 * The constructor. This initializes the HashMaps used for maintaining the
	 * keys, values, and registry.
	 */
	public StateBroker() {
		values = new HashMap<String, Object>();
		registry = new HashMap<String, HashSet<IStateListener>>();

		return;
	}

	/**
	 * Get the value associated with the provided key.
	 * 
	 * @param key
	 *            A string.
	 * @return Returns an Object or null if the Object equals null or none
	 *         exists.
	 */
	public Object getValue(String key) {
		return values.get(key);
	}

	/**
	 * Puts the value associated with the provided key in the HashMap. This will
	 * notify registered IStateListeners if the value has been changed.
	 * 
	 * @param key
	 *            A string.
	 * @param value
	 *            The new value.
	 * @return Returns the old value associated with the key.
	 */
	public Object putValue(String key, Object value) {
		// We need to check for null values and for equivalence of values.
		Object oldValue;

		// We need to make sure the key exists first. This is necessary to
		// discern whether the current value does not exist or it is null.
		// (HashMap.put() returns null in both cases)
		if (!values.containsKey(key)) {
			oldValue = values.put(key, value);
			notify(key);
		} else {
			// If the key does exist, insert the new value and receive its
			// current value.
			oldValue = values.put(key, value);
			// Compare values to see if the value has actually changed.
			if (oldValue != value) {
				notify(key);
			}
		}
		// We need to return the old value.
		return oldValue;
	}

	/**
	 * Copies only the values from the specified StateBroker to this one.
	 * 
	 * @param broker
	 *            The StateBroker whose key-value pairs should be copied.
	 */
	public void copyValues(StateBroker broker) {

		// Make sure the source broker is valid.
		if (broker != null) {
			// Clear all key-value pairs.
			values.clear();

			// Add each key-value pair from the other broker to this one. We
			// should notify listeners when we add these pairs.
			for (Entry<String, Object> entry : broker.values.entrySet()) {
				putValue(entry.getKey(), entry.getValue());
			}
		}

		return;
	}

	/**
	 * Registers an IStateListener to be notified if the value associated with
	 * the key has changed.
	 * 
	 * @param key
	 *            A string. The value associated with this is important to the
	 *            IStateListener.
	 * @param listener
	 *            The IStateListener that wants to be notified of changes to the
	 *            key's value.
	 * @return Returns the current value Object associated with the key.
	 */
	public Object register(String key, IStateListener listener) {
		// Get the set of listeners listening to this key.
		HashSet<IStateListener> listeners = registry.get(key);

		// If necessary, create the set of listeners.
		if (listeners == null) {
			listeners = new HashSet<IStateListener>();
			registry.put(key, listeners);
		}

		// Add the registering listener to the set.
		listeners.add(listener);

		// Return the current value for the key.
		return values.get(key);
	}

	/**
	 * Unregisters an IStateListener so that it no longer receives notification
	 * should a key's value change.
	 * 
	 * @param key
	 *            A string, the value of which is no longer important to the
	 *            listener.
	 * @param listener
	 *            The IStateListener that wants to get out of notifications.
	 */
	public void unregister(String key, IStateListener listener) {
		// Get the set of listeners listening to this key.
		HashSet<IStateListener> listeners = registry.get(key);

		// Remove this listener.
		if (listeners != null) {
			listeners.remove(listener);
		}

		return;
	}

	/**
	 * Notifies all IStateListeners registered for a particular key.
	 * 
	 * @param key
	 *            A string. The value associated with it should have changed.
	 */
	private void notify(String key) {
		// Get the set of listeners listening to this key.
		HashSet<IStateListener> listeners = registry.get(key);

		// For each of the listeners, send them the key and the new value.
		if (listeners != null) {
			Object value = values.get(key);
			for (IStateListener listener : listeners) {
				listener.update(key, value);
			}
		}

		return;
	}

	/**
	 * Resets all of the keys prefixed by the provided String. This is used to
	 * reset the values for all keys for a particular datasource.
	 * 
	 * @param datasource
	 *            The data source, e.g., Input, Reference, Comparison.
	 */
	public void resetSource(String datasource) {

		// Loop over the keys in the Map. If the key matches the data source,
		// (all keys are of the form "Input-some-key"), reset it.
		for (Entry<String, Object> entry : values.entrySet()) {
			if (entry.getKey().startsWith(datasource)) {
				entry.setValue(null);
				notify(entry.getKey());
			}
		}

		return;
	}
}
