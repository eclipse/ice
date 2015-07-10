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
package org.eclipse.ice.viz.service.paraview.proxy;

import java.util.Set;

/**
 * This class provides a simple wrapper for managing properties for an
 * {@link IParaViewProxy}.
 * 
 * @author Jordan Deyton
 *
 */
public interface IProxyProperty {
	
	/**
	 * Gets the name of the property. This should be a user-friendly name.
	 * 
	 * @return The property name.
	 */
	public String getName();

	/**
	 * Gets the current value associated with the property.
	 * 
	 * @return The property's value.
	 */
	public String getValue();

	/**
	 * Gets the set of allowed values for the property.
	 * 
	 * @return The property's allowed values.
	 */
	public Set<String> getAllowedValues();

	/**
	 * Sets the current value associated with the property, if possible. This
	 * method may be a long operation as the property may need to update a
	 * remote process.
	 * 
	 * @param value
	 *            The new value.
	 * @return True if the value was updated to a <i>new</i> value, false
	 *         otherwise.
	 * @throws NullPointerException
	 *             If the specified value is {@code null} and {@code null} is
	 *             not an allowed value.
	 * @throws IllegalArgumentException
	 *             If the specified value is not allowed.
	 * @throws UnsupportedOperationException
	 *             If the property is read only.
	 */
	public boolean setValue(String value) throws NullPointerException,
			IllegalArgumentException, UnsupportedOperationException;
}
