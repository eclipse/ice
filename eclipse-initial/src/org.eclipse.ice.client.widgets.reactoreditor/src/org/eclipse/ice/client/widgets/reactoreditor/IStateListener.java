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
package org.eclipse.ice.client.widgets.reactoreditor;

/**
 * A class implementing this interface should register with a StateBroker as a
 * listener. The methods in this class are, of course, required for such a
 * class.
 * 
 * @author djg
 * 
 */
public interface IStateListener {

	/**
	 * Receive an update notification from the StateBroker.
	 * 
	 * @param key
	 *            The key of the changed value.
	 * @param value
	 *            The new value.
	 */
	public void update(String key, Object value);

	/**
	 * Sets the broker that will send updates to this listener.
	 * 
	 * @param broker
	 *            The new StateBroker to register with and listen to.
	 */
	public void setBroker(StateBroker broker);

	/**
	 * Register all keys the listener would like to hear about.
	 */
	public void registerKeys();

	/**
	 * Unregister all keys for which the listener wants updates. This will be
	 * necessary when this listener is being disposed.
	 */
	public void unregisterKeys();
}
