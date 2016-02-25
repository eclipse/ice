/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
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
package org.eclipse.eavp.viz.service.datastructures.VizObject;

import java.util.ArrayList;

import org.eclipse.eavp.viz.service.datastructures.VizIdentifiable;

/**
 * <p>
 * The IVizUpdateable interface provides a single update operation that may be used
 * by implementers to receive an update based on a key-value pair. This is used
 * by any class for receiving or posting updates.
 * </p>
 * 
 * @author Jay Jay Billings
 */

public interface IVizUpdateable{
	/**
	 * 
	 */
	ArrayList<IVizUpdateableListener> iComponentListener = null;

	/**
	 * <p>
	 * This operation notifies a class that has implemented IUpdateable that the
	 * value associated with the particular key has been updated.
	 * </p>
	 * 
	 * @param updatedKey
	 *            <p>
	 *            A unique key that describes the value that to be updated.
	 *            </p>
	 * @param newValue
	 *            <p>
	 *            The updated value of the key.
	 *            </p>
	 */
	public void update(String updatedKey, String newValue);

	/**
	 * <p>
	 * This operation registers a listener that realizes the IUpdateableListener
	 * interface with the IUpdateable so that it can receive notifications of
	 * changes to the IUpdateable if they are published.
	 * </p>
	 * 
	 * @param listener
	 *            <p>
	 *            The new listener that should be notified when the the
	 *            Component's state changes.
	 *            </p>
	 */
	public void register(IVizUpdateableListener listener);

	/**
	 * <p>
	 * This operation unregisters a listener that realizes the
	 * IUpdateableListener interface with the IUpdateable so that it will no
	 * longer receive notifications of changes to the IUpdateable if they are
	 * published.
	 * </p>
	 * 
	 * @param listener
	 *            <p>
	 *            The listener that should no longer receive updates.
	 *            </p>
	 */
	public void unregister(IVizUpdateableListener listener);
}

