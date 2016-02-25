/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Kasper Gammeltoft
 *******************************************************************************/

package org.eclipse.eavp.viz.service;

import java.util.Set;

/**
 * Defines the layout of how a series can be styled to have it be formatted by
 * the PlotEditor that it eventually is plotted with.
 * 
 * @author Kasper Gammeltoft
 *
 */
public interface ISeriesStyle {

	/**
	 * Gets a list of all of the different object properties available.
	 * 
	 * @return Set<String> A list of all of the property types available.
	 *         Returns an empty set if there are no properties for the given
	 *         series style.
	 */
	public Set<String> getAllPropertyTypes();

	/**
	 * Gets the property value for the specified property type. Note that it is
	 * up to the implementation to show which property types are available.
	 * 
	 * @param propertyType
	 *            The name of the property to fetch.
	 * @return Object The value of the specified property, or null if no
	 *         property by the specified name was found.
	 */
	public Object getProperty(String propertyType);

	/**
	 * Sets the property value for the specified property name. Note that it is
	 * up to the implementation to show which property types are available.
	 * 
	 * @param propertyType
	 *            The name of the property to set.
	 * @param value
	 *            The new value of the property to set.
	 * @return boolean Returns true if the property existed and the value was
	 *         successfully set, false if otherwise.
	 */
	public boolean setProperty(String propertyType, Object value);
}
