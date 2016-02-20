/*******************************************************************************
 * Copyright (c) 2015- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jordan Deyton
 *******************************************************************************/
package org.eclipse.eavp.viz.service.connections.preferences.test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.eavp.viz.service.connections.preferences.IKeyChangeListener;
import org.eclipse.eavp.viz.service.connections.preferences.IKeyManager;

/**
 * This class provides a simple {@link IKeyManager} based on a defined list of
 * keys specified in the constructor. Keys can be taken and released using the
 * methods in this class.
 * 
 * @author Jordan Deyton
 *
 */
public class SimpleDiscreteKeyManager implements IKeyManager {

	/**
	 * The set of available keys. The contents are mutually exclusive with the
	 * set of taken keys.
	 */
	private final Set<String> availableKeys = new HashSet<String>();
	/**
	 * The set of taken keys. The contents are mutually exclusive with the set
	 * of available keys.
	 */
	private final Set<String> takenKeys = new HashSet<String>();

	/**
	 * A list of key change listeners that will be notified when a key is taken
	 * or released (or changed).
	 */
	final List<IKeyChangeListener> listeners = new ArrayList<IKeyChangeListener>();
	
	/**
	 * Creates a simple key manager with a pre-defined list of allowed keys. All
	 * specified keys are available by default.
	 * 
	 * @param keys
	 *            The list of allowed keys. Null is allowed, but duplicate
	 *            values will be ignored.
	 */
	public SimpleDiscreteKeyManager(String... keys) {
		for (String key : keys) {
			availableKeys.add(key);
		}
	}

	/**
	 * Takes all specified keys if they are available and valid. Invalid keys
	 * are ignored. Taken keys will not be listed as available after this method
	 * completes.
	 * 
	 * @param keys
	 *            The list of keys to take.
	 */
	public void takeKeys(String... keys) {
		for (String key : keys) {
			if (availableKeys.remove(key)) {
				takenKeys.add(key);
				for (IKeyChangeListener listener : listeners) {
					listener.keyChanged(null, key);
				}
			}
		}
	}

	/**
	 * Releases all specified keys, making them available if they are valid.
	 * Invalid keys are ignored.
	 * 
	 * @param keys
	 *            The list of keys to release.
	 */
	public void releaseKeys(String... keys) {
		for (String key : keys) {
			if (takenKeys.remove(key)) {
				availableKeys.add(key);
				for (IKeyChangeListener listener : listeners) {
					listener.keyChanged(key, null);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.connections.IKeyManager#keyAvailable(java
	 * .lang.String)
	 */
	@Override
	public boolean keyAvailable(String key) {
		return availableKeys.contains(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.connections.IKeyManager#getAvailableKeys()
	 */
	@Override
	public List<String> getAvailableKeys() {
		return new ArrayList<String>(availableKeys);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.connections.IKeyManager#getNextKey()
	 */
	@Override
	public String getNextKey() throws IllegalStateException {
		if (availableKeys.isEmpty()) {
			throw new IllegalStateException("No more keys left.");
		} else {
			return availableKeys.iterator().next();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.connections.IKeyManager#addKeyChangeListener
	 * (org.eclipse.eavp.viz.service.connections.IKeyChangeListener)
	 */
	@Override
	public void addKeyChangeListener(IKeyChangeListener listener) {
		listeners.add(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.connections.IKeyManager#removeKeyChangeListener
	 * (org.eclipse.eavp.viz.service.connections.IKeyChangeListener)
	 */
	@Override
	public void removeKeyChangeListener(IKeyChangeListener listener) {
		listeners.remove(listener);
	}

}
