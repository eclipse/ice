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

import org.eclipse.ice.datastructures.form.AllowedValueType;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.IEntryContentProvider;

/**
 * A {@code KeyEntry} is essentially a basic {@link Entry} with a single caveat:
 * Its value and allowed values are managed by a {@link IKeyManager}. If a key
 * is not allowed, then the {@code KeyEntry} will not set its value to that key.
 * <p>
 * Multiple {@code KeyEntry}s can and should share a {@code IKeyManager} and
 * {@link KeyEntryContentProvider}.
 * </p>
 * 
 * @author Jordan Deyton
 *
 */
public class KeyEntry extends Entry {

	/**
	 * The content provider for this {@code Entry}. It must be hooked up to an
	 * {@link IKeyManager}.
	 */
	private KeyEntryContentProvider contentProvider;

	/**
	 * An error message for invalid keys in the case where there is no set list
	 * of allowed keys.
	 */
	private static final String undefinedErrMsg = "'${incorrectValue}' is an unacceptable value. It must be a unique string.";

	/**
	 * The default constructor.
	 * 
	 * @param contentProvider
	 *            The {@code Entry}'s content provider.
	 * @param manager
	 *            The manager for the keys stored in this and other
	 *            {@code KeyEntry}s.
	 */
	public KeyEntry(KeyEntryContentProvider contentProvider) {
		super(contentProvider);

		// Store a reference to the content provider if applicable. Otherwise,
		// we should throw an exception as the KeyManager is required.
		this.contentProvider = contentProvider;

		// Throw an NPE if the content provider is null. A KeyEntry requires a
		// valid content provider!
		if (contentProvider == null) {
			throw new NullPointerException("KeyEntry error: "
					+ "Content provider cannot be null.");
		}

		return;
	}

	/**
	 * The copy constructor. When used, both {@code KeyEntry}s will use the
	 * exact same {@link #keyManager} (but valid or unique keys).
	 * <p>
	 * <b>Note:</b> This method should not be used. It is implemented so that a
	 * template {@code KeyEntry} can be copied when used in
	 * {@code ConnectionManager} {@code TableComponent}s.
	 * </p>
	 * 
	 * @param otherEntry
	 *            The other {@code KeyEntry} to copy.
	 */
	private KeyEntry(KeyEntry entry) {
		// Perform the default construction, using the specified content
		// provider if possible.
		this(entry != null ? entry.contentProvider : null);

		if (entry != null) {
			// Copy the super class' variables.
			super.copy(entry);

			// Copy this class' variables.
			// Nothing to copy, as the content provider was already set by the
			// default constructor.
		}

		return;
	}

	/**
	 * Overrides the parent method to enforce the rule that content providers
	 * *must* be {@link KeyEntryContentProvider}s.
	 */
	@Override
	public void setContentProvider(IEntryContentProvider contentProvider) {
		if (contentProvider instanceof KeyEntryContentProvider) {
			setContentProvider((KeyEntryContentProvider) contentProvider);
		}
	}

	/**
	 * Sets the content provider. Resets the value to the default.
	 * 
	 * @param contentProvider
	 *            The new {@code KeyEntryContentProvider}. If null, nothing is
	 *            done.
	 */
	public void setContentProvider(KeyEntryContentProvider contentProvider) {
		// Update the references to the KeyEntryContentProvider (including the
		// super class) and reset the value to default.
		if (contentProvider != null) {
			this.contentProvider = contentProvider;
			super.setContentProvider(contentProvider);
			value = null;
		}
		return;
	}

	/**
	 * If the specified value is an allowed key for the {@link #keyManager},
	 * then this {@code KeyEntry} will assume that key within the key manager.
	 * Otherwise, this method will return false and set an appropriate error
	 * message.
	 */
	@Override
	public boolean setValue(String newValue) {
		boolean returnCode = false;

		AllowedValueType valueType = iEntryContentProvider
				.getAllowedValueType();

		// For an undefined set of keys, we need to check if the specified key
		// is valid.
		if (valueType == AllowedValueType.Undefined) {
			// Update the key value if we can.
			if (contentProvider.keyAvailable(newValue)) {
				value = newValue;
				returnCode = true;

				changeState = true;
				errorMessage = null;
				notifyListeners();
			} else {
				String error = undefinedErrMsg;
				error = error.replace("${incorrectValue}", newValue);
			}
		}
		// Otherwise, for a discrete set of keys, we can rely on the default
		// behavior.
		else { // AllowedValueType.Discrete
			returnCode = super.setValue(newValue);
		}

		return returnCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.datastructures.form.Entry#clone()
	 */
	@Override
	public Object clone() {
		return new KeyEntry(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.datastructures.form.Entry#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		boolean equals = false;

		// If the other object is equal (as an Entry) and is a KeyEntry, cast it
		// to a KeyEntry and compare the variables managed by the KeyEntry.
		if (super.equals(object) && object instanceof KeyEntry) {
			// Compare all class variables.
			// Since the content provider is the same reference as the one used
			// by the parent class, we don't need to check it again.
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
		hash += 31 * contentProvider.hashCode();

		return hash;
	}
}
