/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
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

/**
 * This interface is used to get keys for objects that will be added to a
 * {@link StateBroker}.
 * 
 * @author Jordan H. Deyton
 * 
 */
public interface IStateBrokerHandler {

	/**
	 * Get the object's key for use with a {@link StateBroker}.
	 * 
	 * @param object
	 *            The object that will be added to a StateBroker.
	 * @return A String key for the object, or null if the object is not
	 *         supported.
	 */
	public String getKey(Object object);

	/**
	 * Adds the object to the specified {@link StateBroker}.
	 * 
	 * @param value
	 *            The value to add to the StateBroker.
	 * @param parent
	 *            The parent of the value object.
	 * @param broker
	 *            The StateBroker that will store the value. If null, no value
	 *            is added.
	 * @return True if the value was successfully updated, false otherwise.
	 */
	public boolean addValue(Object value, Object parent, StateBroker broker);

	/**
	 * Sets the DataSource (Input or Reference) for which data is being added to
	 * the tree. The default is Input.
	 * 
	 * @param dataSource
	 *            The DataSource for the data being added to the broker (Input
	 *            or Reference).
	 */
	public void setDataSource(DataSource dataSource);
}
