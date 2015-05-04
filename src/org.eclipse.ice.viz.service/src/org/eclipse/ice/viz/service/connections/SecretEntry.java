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

import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.IEntryContentProvider;

/**
 * A {@code SecretEntry} is simply an {@link Entry} whose secret flag is set to
 * true.
 * 
 * @author Jordan Deyton
 *
 */
public class SecretEntry extends Entry {

	// If we add any other properties, then we will need to override the clone
	// operation and provide a copy constructor.

	/**
	 * A constructor that will create an {@code Entry} with only a unique ID and
	 * a name. Default values are set:
	 * <ul>
	 * <li>objectDescription = "Entry" + uniqueId</li>
	 * <li>defaultValue = ""</li>
	 * <li>allowedValues = null</li>
	 * <li>allowedValueType = AllowedValueType.Undefined</li>
	 * </ul>
	 * The constructor will call the setup function after setting the default
	 * values. The setup function can be overridden to tailor the properties of
	 * the {@code Entry} or otherwise overload the behavior of the {@code Entry}
	 * .
	 */
	public SecretEntry() {
		super();

		secretFlag = true;
	}

	/**
	 * A constructor that sets the {@code Entry} to the content provider.
	 * 
	 * @param contentProvider
	 *            The content provider for the {@code Entry}.
	 */
	public SecretEntry(IEntryContentProvider contentProvider) {
		super(contentProvider);

		secretFlag = true;
	}

	/**
	 * A copy constructor.
	 * 
	 * @param entry
	 *            The entry whose data will be copied into this one.
	 */
	public SecretEntry(SecretEntry entry) {
		// Perform the default construction first.
		this();

		if (entry != null) {
			// Copy the super class' variables.
			super.copy(entry);

			// Copy this class' variables.
			// Nothing to copy.
		}
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.datastructures.form.Entry#clone()
	 */
	@Override
	public Object clone() {
		return new SecretEntry(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.datastructures.form.Entry#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		boolean equals = false;

		// If the other object is equal (as an Entry) and is a SecretEntry, cast
		// it to a SecretEntry and compare the variables managed by SecretEntry.
		if (super.equals(object) && object instanceof SecretEntry) {
			// Compare all class variables:
			// Nothing to compare.
			equals = true;
		}

		return equals;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.datastructures.form.Entry#hashCode()
	 */
	@Override
	public int hashCode() {
		// Get the default hash code.
		int hash = super.hashCode();

		// Add class variable hash codes here:
		// Nothing to add. The secretness is stored as a flag in the base class.

		// To avoid an equivalent hash code from a regular Entry copied from
		// this one (which should not be the case), add a static value to the
		// hash code.
		hash += 31 * 1;

		return hash;
	}
}
