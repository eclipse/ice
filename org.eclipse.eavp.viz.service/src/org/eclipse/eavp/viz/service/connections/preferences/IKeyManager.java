/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Jordan Deyton - Initial API and implementation and/or initial documentation
 *   
 *******************************************************************************/
package org.eclipse.eavp.viz.service.connections.preferences;

import java.util.List;

/**
 * An {@code IKeyManager} manages a set of available keys. This set may be
 * either a bounded or unbounded set of string keys.
 * 
 * @author Jordan Deyton
 *
 */
public interface IKeyManager {

	/**
	 * Adds a new {@link IKeyChangeListener} to listen for key change events in
	 * the manager.
	 * 
	 * @param listener
	 *            The new listener. The same listener should not be added twice.
	 */
	public void addKeyChangeListener(IKeyChangeListener listener);

	/**
	 * Gets the list of available keys.
	 * 
	 * @return A list containing the available keys. If there are none left or
	 *         there is no limited set of keys, this list will be empty.
	 */
	public List<String> getAvailableKeys();

	/**
	 * Returns the next available key, usually based on some default prefix,
	 * e.g., "key1", "key2".
	 * 
	 * @return The next available key.
	 * @throws IllegalStateException
	 *             An exception is thrown if there is a pre-defined list of keys
	 *             and there are no more keys available.
	 */
	public String getNextKey() throws IllegalStateException;

	/**
	 * Determines whether the specified key is available.
	 * 
	 * @param key
	 *            The key to test.
	 * @return True if the key is available, false otherwise.
	 */
	public boolean keyAvailable(String key);

	/**
	 * Removes an existing {@link IKeyChangeListener} from the key manager.
	 * 
	 * @param listener
	 *            The old listener. This removes the first matching occurrence
	 *            of the listener from the manager.
	 */
	public void removeKeyChangeListener(IKeyChangeListener listener);
}
