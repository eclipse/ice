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

package org.eclipse.ice.viz.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Kasper Gammeltoft
 *
 */
public abstract class AbstractSeriesStyle implements ISeriesStyle {

	/**
	 * The properties mapping for the series style.
	 */
	protected Map<String, Object> properties;

	/**
	 * The constructor. Initializes the properties map.
	 */
	public AbstractSeriesStyle() {
		properties = new HashMap<String, Object>();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.viz.service.ISeriesStyle#getProperty(java.lang.String)
	 */
	@Override
	public Object getProperty(String propertyType) {

		return properties.get(propertyType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.viz.service.ISeriesStyle#setProperty(java.lang.String,
	 * java.lang.Object)
	 */
	@Override
	public boolean setProperty(String propertyType, Object value) {
		boolean retVal = properties.containsKey(propertyType);
		if (retVal) {
			properties.put(propertyType, value);
		}
		return retVal;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.ISeriesStyle#getAllPropertyTypes()
	 */
	@Override
	public Set<String> getAllPropertyTypes() {
		return properties.keySet();
	}

}
