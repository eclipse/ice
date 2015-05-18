/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.reactor;

import org.eclipse.ice.datastructures.ICEObject.Component;

/**
 * <p>
 * An interface for managing Components on a grid.
 * </p>
 * 
 * @author Scott Forest Hull II
 */
public interface IGridManager {
	/**
	 * <p>
	 * Returns the Component at the provided GridLocation or null if one does
	 * not exist at the provided location.
	 * </p>
	 * 
	 * @param location
	 *            <p>
	 *            A GridLocation.
	 *            </p>
	 * @return <p>
	 *         A Component object to return.
	 *         </p>
	 */
	public String getComponentName(GridLocation location);

	/**
	 * <p>
	 * Adds a Component and its GridLocation to this GridManager. If a Component
	 * already exists at that location, then this operation does nothing.
	 * </p>
	 * 
	 * @param component
	 *            <p>
	 *            A Component object to add.
	 *            </p>
	 * @param location
	 *            <p>
	 *            A GridLocation.
	 *            </p>
	 */
	public void addComponent(Component component, GridLocation location);

	/**
	 * <p>
	 * Removes the Component at the provided GridLocation from this GridManager.
	 * </p>
	 * 
	 * @param location
	 *            <p>
	 *            A GridLocation.
	 *            </p>
	 */
	public void removeComponent(GridLocation location);

	/**
	 * <p>
	 * Removes the provided Component from this GridManager.
	 * </p>
	 * 
	 * @param component
	 *            <p>
	 *            A Component object to remove.
	 *            </p>
	 */
	public void removeComponent(Component component);
}