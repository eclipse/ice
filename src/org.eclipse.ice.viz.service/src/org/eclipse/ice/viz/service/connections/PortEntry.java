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
package org.eclipse.ice.viz.service.connections;

import java.util.List;

import org.eclipse.ice.viz.service.datastructures.VizEntry;

/**
 * This class provides an {@link VizEntry} that is a "continuous" {@code Entry}
 * with a twist: instead of the allowed values' being a range between two
 * doubles, they are a range between two integers. The range is specified by the
 * {@link PortEntryContentProvider}.
 * 
 * @author Jordan Deyton
 *
 */
public class PortEntry extends VizEntry {

	/**
	 * The default constructor.
	 * 
	 * @param contentProvider
	 *            The {@code PortEntry}'s content provider. If null, a default
	 *            content provider is used.
	 */
	public PortEntry(PortEntryContentProvider contentProvider) {
		// If the provided content provider is null, create a default one.
		super(contentProvider != null ? contentProvider
				: new PortEntryContentProvider());
	}

	/**
	 * The copy constructor.
	 * <p>
	 * <b>Note:</b> This method should not be used. It is implemented so that a
	 * template {@code KeyEntry} can be copied when used in
	 * {@code ConnectionManager} {@code TableComponent}s.
	 * </p>
	 * 
	 * @param otherEntry
	 *            The other {@code KeyEntry} to copy.
	 */
	private PortEntry(PortEntry otherEntry) {
		// Perform the typical Entry copy method.
		super.copy(otherEntry);

		// Copy the PortEntry-specific features.
		if (otherEntry != null) {

			// Share the custom content provider!
			iEntryContentProvider = otherEntry.iEntryContentProvider;
		} else {
			// We should throw an exception.
			throw new NullPointerException("PortEntry error: "
					+ "Null copy constructor argument.");
		}

		return;
	}

	/**
	 * Checks the new value before attempting to set it. The value must be an
	 * integer that lies within the port range (inclusive) defined by its
	 * associated {@link PortEntryContentProvider}.
	 */
	@Override
	public boolean setValue(String newValue) {
		boolean returnCode = false;

		// Determine the lower and upper bound on port numbers.
		List<String> allowedValues = iEntryContentProvider.getAllowedValues();
		int lowerBound = Integer.valueOf(allowedValues.get(0));
		int upperBound = Integer.valueOf(allowedValues.get(1));

		// Try to get the new port number.
		try {
			int newValueInt = Integer.valueOf(newValue);
			if (newValueInt >= lowerBound && newValueInt <= upperBound) {
				value = newValue;
				returnCode = true;
			}
		} catch (NumberFormatException e) {
			// This is not an integer!
		}

		// If the value changed, notify listeners.
		if (returnCode) {
			changeState = true;
			errorMessage = null;
			notifyListeners();
		}
		// Otherwise, update the error message.
		else {
			String error = continuousErrMsg;
			error = error.replace("${incorrectValue}",
					newValue != null ? newValue : "null");
			error = error.replace("${lowerBound}", allowedValues.get(0));
			error = error.replace("${upperBound}", allowedValues.get(1));
			errorMessage = error;
		}

		return returnCode;
	}

	/**
	 * Overrides the default clone operation on {@code Entry}.
	 * <p>
	 * <b>Note:</b> This method should not be used. It is implemented so that a
	 * template {@code KeyEntry} can be copied when used in
	 * {@code ConnectionManager} {@code TableComponent}s.
	 * </p>
	 */
	@Override
	public Object clone() {
		return new PortEntry(this);
	}
}
