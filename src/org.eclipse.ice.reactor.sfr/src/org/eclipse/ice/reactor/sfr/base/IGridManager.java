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

import java.util.List;

/**
 * <!-- begin-UML-doc --> Classes that implement this interface should keep
 * track the locations of each Component added. It should offer fast look-up
 * capabilities based on both the location and the name of a component. <!--
 * end-UML-doc -->
 * 
 * @author Jordan H. Deyton
 * 
 */
public interface IGridManager {

	/**
	 * <!-- begin-UML-doc --> Gets the name of the Component in the specified
	 * grid location. <!-- end-UML-doc -->
	 * 
	 * @param location
	 *            The index of the location in the grid being managed.
	 * @return A Component name or <code>null</code> if no name is found.
	 */
	public String getComponentName(int location);

	/**
	 * <!-- begin-UML-doc --> Gets all the locations occupied by a Component in
	 * the grid. <!-- end-UML-doc -->
	 * 
	 * @param name
	 *            The name of the Component to search for.
	 * @return A List of location indexes or an empty List if the Component is
	 *         invalid.
	 */
	public List<Integer> getComponentLocations(String name);

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
	public boolean addComponent(String name, int location);

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
	public boolean removeComponent(int location);

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
	public boolean removeComponent(String name);
}
