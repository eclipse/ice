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

package org.eclipse.eavp.viz.service.styles;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.eavp.viz.service.ISeriesStyle;

/**
 * An abstract implementation of a series style object to be subclassed. It
 * provides the basic architecture for mapping the properties of the style. It
 * is recommended to use this class with a modified constructor to set the
 * standard property key set to be used for different default cases. The methods
 * declared in the {@link ISeriesStyle} that are implemented here are not meant
 * to be overridden. From this, other classes do not have the rights to change
 * the keys for the properties declared. All of the properties that should be
 * available are to be set in the properties map in some constructor of the
 * style. For the user's benefit, it is recommended to use public static final
 * string variables to declare the keys for your properties.
 * 
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
		// Put whatever properties you want here! This class should not have
		// any, as we do not know what kind of style we are defining yet
	}

	/**
	 * Copies the properties from the specified style to this style
	 * 
	 * @param other
	 *            The other style to copy
	 */
	public void copy(AbstractSeriesStyle other) {
		this.properties = other.properties;
	}

	@Override
	public boolean equals(Object other) {
		// Local declarations
		boolean isEqual = false;
		// Only compare if the other object is not null and an instance of this
		// class
		if (other != null && other instanceof AbstractSeriesStyle) {
			// If these are the same reference, must be equal
			if (other == this) {
				isEqual = true;
				// Otherwise, compare the properties of each style
			} else {
				AbstractSeriesStyle otherStyle = (AbstractSeriesStyle) other;
				isEqual = properties.equals(otherStyle.properties);
			}
		}
		// Return the equality between the two styles
		return isEqual;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.ISeriesStyle#getAllPropertyTypes()
	 */
	@Override
	public Set<String> getAllPropertyTypes() {
		return properties.keySet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.ISeriesStyle#getProperty(java.lang.String)
	 */
	@Override
	public Object getProperty(String propertyType) {

		return properties.get(propertyType);
	}

	@Override
	public int hashCode() {
		// Local declarations
		int hash = 8;
		// Compute the hash from the properties
		hash = 31 * hash + properties.hashCode();
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.ISeriesStyle#setProperty(java.lang.String,
	 * java.lang.Object)
	 */
	@Override
	public boolean setProperty(String propertyType, Object value) {
		// Make sure that the caller is not creating a new property and that the
		// new value for the property is of the allowed type.
		boolean propertyExists = properties.containsKey(propertyType);
		boolean valueIsGood = false;
		// If the property exists then see if the value is of the right type
		if (propertyExists) {
			// If the current value is null, then any value is good. Otherwise,
			// see if the new value is an instance of the current value
			valueIsGood = properties.get(propertyType) == null || value == null
					|| (value.getClass()
							.isInstance(properties.get(propertyType)));

			// Set the property
			if (valueIsGood) {
				properties.put(propertyType, value);
			}
		}
		// Return true if both the property type and new value were acceptable,
		// as only then is the property set.
		return propertyExists && valueIsGood;
	}

}
