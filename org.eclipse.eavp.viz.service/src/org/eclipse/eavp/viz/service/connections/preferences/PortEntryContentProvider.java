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
package org.eclipse.eavp.viz.service.connections.preferences;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.eavp.viz.service.datastructures.BasicVizEntryContentProvider;
import org.eclipse.eavp.viz.service.datastructures.VizAllowedValueType;

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
public class PortEntryContentProvider extends BasicVizEntryContentProvider {

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
		super.setAllowedValueType(VizAllowedValueType.Continuous);

		// Set the default port and range. We call the super
		// setDefaultValue(...) method so that the default value is invalid.
		// Subsequently setting the range will update the default value to the
		// default min port value.
		super.setDefaultValue(Integer.toString(-1));
		setRange(MIN_PORT_PREFERRED, MAX_PORT);
	}

	/**
	 * A copy constructor.
	 * 
	 * @param provider
	 *            The other PortEntryContentProvider to copy.
	 */
	public PortEntryContentProvider(PortEntryContentProvider provider) {
		// Use the default setup initially. This is reasonable since the default
		// construction doesn't create any heavy-weight objects.
		this();

		// If the specified content provider is not null, we can copy it.
		if (provider != null) {
			// Copy the super class' variables.
			super.copy(provider);

			// Copy this class' variables.
			// Nothing to copy.
		}
		// Otherwise, the default settings have already been set.
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.datastructures.form.BasicEntryContentProvider#clone()
	 */
	@Override
	public Object clone() {
		return new PortEntryContentProvider(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.datastructures.ICEObject.ICEObject#equals(java.lang.Object
	 * )
	 */
	@Override
	public boolean equals(Object object) {
		boolean equals = false;

		// If the references match, we know it is equivalent.
		if (object == this) {
			equals = true;
		}
		// Otherwise, if the type of the object is correct, we need to perform a
		// full equivalence check.
		else if (object != null && object instanceof PortEntryContentProvider) {
			// Check all of the super class variables.
			equals = super.equals(object);

			// Compare all class variables.
			// Nothing to do.
		}

		return equals;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.datastructures.form.BasicEntryContentProvider#hashCode()
	 */
	@Override
	public int hashCode() {
		// Get the default hash code.
		int hash = super.hashCode();

		// Add local variable hash codes here:
		// Nothing to add. The port range is stored as allowed values.

		// To avoid an equivalent hashcode from a regular content provider
		// copied from this one (which should not be the case), add a static
		// value to the hash code.
		hash += 31 * 1;

		return hash;
	}

	/**
	 * If possible, converts the new allowed value list into the min and max
	 * ports, then redirects to {@link #setRange(int, int)}. Note that the
	 * incoming list is also rejected if it does not have exactly two elements.
	 */
	@Override
	public void setAllowedValues(ArrayList<String> allowedValues) {
		// If possible, convert the *two* allowed values into integers and pass
		// them on to the setRange(...) method. The first allowed value is the
		// min, and the second is the max.
		if (allowedValues != null && allowedValues.size() == 2) {
			try {
				int min = Integer.parseInt(allowedValues.get(0));
				int max = Integer.parseInt(allowedValues.get(1));
				setRange(min, max);
			} catch (NumberFormatException e) {
				// The allowed values could not be parsed. Do nothing.
			}
		}
		return;
	}

	/**
	 * Does nothing. {@link PortEntry}s are strictly of the continuous type.
	 */
	@Override
	public void setAllowedValueType(VizAllowedValueType allowedValueType) {
		// Do nothing.
	}

	/**
	 * Sets the default port to the specified value. This is a convenience
	 * method that redirects to {@link #setDefaultValue(String)}.
	 * 
	 * @param defaultValue
	 *            The default port as an integer.
	 */
	public void setDefaultValue(int defaultValue) {
		// Get the current min and max port. (Parsing here shouldn't fail.)
		List<String> allowedValues = getAllowedValues();
		int min = Integer.parseInt(allowedValues.get(0));
		int max = Integer.parseInt(allowedValues.get(1));

		// If the new value is between the min and max (inclusive), set it by
		// diverting to the super class' operation.
		if (defaultValue >= min && defaultValue <= max) {
			super.setDefaultValue(Integer.toString(defaultValue));
		}

		return;
	}

	/**
	 * Sets the default port for managed {@link PortEntry}s. The specified value
	 * may be outside the range as specified via {@link #setRange(int, int)}.
	 */
	@Override
	public void setDefaultValue(String defaultValue) {
		// If possible, convert the new default value into an integer and pass
		// it on to the setDefaultValue(int) method.
		try {
			setDefaultValue(Integer.parseInt(defaultValue));
		} catch (NumberFormatException e) {
			// The number string could not be parsed. Do nothing.
		}
		return;
	}

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
	 * @return True if the value changed, false otherwise.
	 */
	public boolean setRange(int min, int max) {
		boolean changed = false;
		if (min <= max && min >= MIN_PORT && max <= MAX_PORT) {
			ArrayList<String> allowedValues = new ArrayList<String>(2);
			allowedValues.add(Integer.toString(min));
			allowedValues.add(Integer.toString(max));

			// Continue with the default setAllowedValues(...) procedure.
			super.setAllowedValues(allowedValues);

			// If necessary, clamp the default port to the allowed port range.
			int defaultPort = Integer.parseInt(getDefaultValue());
			if (defaultPort < min) {
				setDefaultValue(min);
			} else if (defaultPort > max) {
				setDefaultValue(max);
			}

			changed = true;
		}
		return changed;
	}
}
