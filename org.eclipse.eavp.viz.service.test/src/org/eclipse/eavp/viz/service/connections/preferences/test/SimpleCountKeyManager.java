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
import java.util.List;

import org.eclipse.eavp.viz.service.connections.preferences.IKeyChangeListener;
import org.eclipse.eavp.viz.service.connections.preferences.IKeyManager;

/**
 * This class provides a very simple unrestricted or "undefined" key manager
 * that generates keys based on an integer counter. When the next key is taken,
 * the counter is incremented and the next available key is updated.
 * 
 * @author Jordan Deyton
 *
 */
public class SimpleCountKeyManager implements IKeyManager {

	/**
	 * The counter used to generate valid keys.
	 */
	private int count = 0;

	/**
	 * A list of key change listeners that will be notified when a key is taken
	 * or released (or changed).
	 */
	final List<IKeyChangeListener> listeners = new ArrayList<IKeyChangeListener>();

	/**
	 * Takes the next available key if it is available.
	 * <p>
	 * Listeners are notified that the key has been taken.
	 * </p>
	 * @return The next available key, {@link #count}, as a string.
	 */
	public String takeKey() {
		String key = Integer.toString(count++);
		for (IKeyChangeListener listener : listeners) {
			listener.keyChanged(null, key);
		}
		return key;
	}
	
	/**
	 * Releases the specified key if it is valid.
	 * <p>
	 * Listeners are notified that the key has been released.
	 * </p>
	 * @param key The key to release.
	 */
	public void releaseKey(String key) {
		int keyInt = Integer.parseInt(key);
		if (keyInt >= 0 && keyInt < count) {
			for (IKeyChangeListener listener : listeners) {
				listener.keyChanged(key, null);
			}
		}
		return;
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
		boolean available = false;

		// The key is only available if it is the next key.
		try {
			int keyInt = Integer.parseInt(key);
			available = (keyInt == count);
		} catch (NumberFormatException e) {
			// Nothing to do if the string is invalid.
		}

		return available;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.connections.IKeyManager#getAvailableKeys()
	 */
	@Override
	public List<String> getAvailableKeys() {
		// Since there is an unbounded number of keys (hypothetically), return
		// an empty list (as discussed in the method documentation).
		return new ArrayList<String>(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.connections.IKeyManager#getNextKey()
	 */
	@Override
	public String getNextKey() throws IllegalStateException {
		return Integer.toString(count);
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
