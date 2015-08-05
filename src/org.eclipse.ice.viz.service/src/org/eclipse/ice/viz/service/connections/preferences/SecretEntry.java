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
package org.eclipse.ice.viz.service.connections.preferences;

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
		// Use the default setup initially. This is reasonable since the default
		// construction doesn't create any heavy-weight objects.
		this();

		// If the specified entry is not null, we can copy it.
		if (entry != null) {
			// Copy the super class' variables.
			super.copy(entry);

			// Copy this class' variables.
			// Nothing to copy.
		}
		// Otherwise, the default settings have already been set.
		return;
	}

	/*
	 * Overrides a method from Entry.
	 */
	@Override
	public Object clone() {
		return new SecretEntry(this);
	}

	/*
	 * Overrides a method from Entry.
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
		else if (object != null && object instanceof SecretEntry) {
			// Check all of the super class variables.
			equals = super.equals(object);

			// Compare all class variables.
			// Nothing to do.
		}

		return equals;
	}

	/*
	 * Overrides a method from Entry.
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
