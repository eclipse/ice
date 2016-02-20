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

import java.util.List;

import org.eclipse.eavp.viz.service.datastructures.IVizEntryContentProvider;
import org.eclipse.eavp.viz.service.datastructures.VizEntry;

/**
 * This class provides an {@link Entry} that is a "continuous" {@code Entry}
 * with a twist: instead of the allowed values' being a range between two
 * doubles, they are a range between two integers. The range is specified by the
 * {@link PortEntryContentProvider}.
 * 
 * @author Jordan Deyton
 *
 */
public class PortEntry extends VizEntry {

	/**
	 * The copy constructor.
	 * 
	 * @param entry
	 *            The other {@code PortEntry} to copy.
	 */
	public PortEntry(PortEntry entry) {
		// If the specified entry is not null, we can copy it.
		if (entry != null) {
			// Copy the super class' variables.
			super.copy(entry);

			// Copy this class' variables.
			// Nothing to do.
		}
		// Otherwise, we must set up the default entry.
		else {
			setContentProvider(new PortEntryContentProvider());
		}
		return;
	}

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

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.datastructures.VizEntry#clone()
	 */
	@Override
	public Object clone() {
		return new PortEntry(this);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.datastructures.VizEntry#equals(java.lang.Object)
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
		else if (object != null && object instanceof PortEntry) {
			// Check all of the super class variables.
			equals = super.equals(object);

			// Compare all class variables.
			// Nothing to do.
		}

		return equals;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.datastructures.VizEntry#hashCode()
	 */
	@Override
	public int hashCode() {
		// Get the default hash code.
		int hash = super.hashCode();

		// Add class variable hash codes here:
		// Nothing to add. The port value is stored as a regular Entry value.

		// To avoid an equivalent hash code from a regular Entry copied from
		// this one (which should not be the case), add a static value to the
		// hash code.
		hash += 31 * 1;

		return hash;
	}

	/**
	 * Overrides the parent method to enforce the rule that content providers
	 * *must* be {@link PortEntryContentProvider}s.
	 */
	@Override
	public void setContentProvider(IVizEntryContentProvider contentProvider) {
		if (contentProvider instanceof PortEntryContentProvider) {
			setContentProvider((PortEntryContentProvider) contentProvider);
		}
	}

	/**
	 * Sets the content provider. Resets the value to the default.
	 * 
	 * @param contentProvider
	 *            The new {@code PortEntryContentProvider}. If null, nothing is
	 *            done.
	 */
	public void setContentProvider(PortEntryContentProvider contentProvider) {
		// Update the references to the PortEntryContentProvider (including the
		// super class) and reset the value to default.
		if (contentProvider != null) {
			super.setContentProvider(contentProvider);
			value = null;
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
}
