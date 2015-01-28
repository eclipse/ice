/*******************************************************************************
 * Copyright (c) 2015- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jordan Deyton
 *******************************************************************************/
package org.eclipse.ice.viz.service.visit.connections;

import java.util.ArrayList;

import org.eclipse.ice.datastructures.form.AllowedValueType;
import org.eclipse.ice.datastructures.form.BasicEntryContentProvider;

/**
 * This class provides a basic content provider for {@link PortEntry}s. Some
 * notes about its behavior:
 * <ul>
 * <li>To set the range of allowed ports, use {@link #setRange(int, int)}.</li>
 * <li>To set the default port value, use {@link #setDefaultValue(int)}.</li>
 * <li>The {@code AllowedValueType} is strictly {@code Continuous}.</li>
 * </ul>
 * 
 * @author Jordan Deyton
 *
 */
public class PortEntryContentProvider extends BasicEntryContentProvider {

	/**
	 * The minimum allowed port. Anything below this is simply invalid!
	 */
	public static final int MIN_PORT = 1;
	/**
	 * The preferred minimum port. Port numbers below this are typically
	 * reserved.
	 */
	public static final int MIN_PORT_PREFERRED = 1024;
	/**
	 * The maximum allowed port. Anything above this is simply an invalid port
	 * number with current technology.
	 */
	public static final int MAX_PORT = 65535;

	/**
	 * The default constructor.
	 */
	public PortEntryContentProvider() {
		// PortEntries are strictly continuous between some min and max value.
		super.setAllowedValueType(AllowedValueType.Continuous);
		// Set the default min and max port.
		setRange(MIN_PORT_PREFERRED, MAX_PORT);
	}

	/**
	 * Does nothing. {@link PortEntry}s are strictly of the continuous type.
	 */
	@Override
	public void setAllowedValueType(AllowedValueType allowedValueType) {
		// Do nothing.
	};

	/**
	 * Does nothing. Use {@link #setRange(int, int)} instead.
	 */
	@Override
	public void setAllowedValues(ArrayList<String> allowedValues) {
		// Do nothing.
	};

	/**
	 * Sets the range of allowed ports. This range may exclude the default port
	 * as specified by {@link #setDefaultValue(String)}.
	 * 
	 * @param min
	 *            The minimum port. Must be less than or equal to max and
	 *            between {@link #MIN_PORT} and {@link #MAX_PORT}.
	 * @param max
	 *            The maximum port. Must be greater than or equal to min and
	 *            between {@link #MIN_PORT} and {@link #MAX_PORT}.
	 */
	public boolean setRange(int min, int max) {
		boolean changed = false;
		if (min <= max && min >= MIN_PORT && max <= MAX_PORT) {
			ArrayList<String> allowedValues = new ArrayList<String>(2);
			allowedValues.add(Integer.toString(min));
			allowedValues.add(Integer.toString(max));
			super.setAllowedValues(allowedValues);
			changed = true;
		}
		return changed;
	}

	/**
	 * Sets the default port to the specified value. This is a convenience
	 * method that redirects to {@link #setDefaultValue(String)}.
	 * 
	 * @param defaultValue
	 *            The default port as an integer.
	 */
	public void setDefaultValue(int defaultValue) {
		setDefaultValue(Integer.toString(defaultValue));
	}

	/**
	 * Sets the default port for managed {@link PortEntry}s. The specified value
	 * may be outside the range as specified via {@link #setRange(int, int)}.
	 */
	@Override
	public void setDefaultValue(String defaultValue) {
		// Only set the default value if it is a valid port number within the
		// statically defined range. This does NOT check that the default value
		// is within the range specified by setRange(int,int).
		try {
			int defaultPort = Integer.valueOf(defaultValue);
			if (defaultPort >= MIN_PORT && defaultPort <= MAX_PORT) {
				super.setDefaultValue(defaultValue);
			}
		} catch (NumberFormatException e) {
			// Don't set it.
		}
		return;
	}
}
