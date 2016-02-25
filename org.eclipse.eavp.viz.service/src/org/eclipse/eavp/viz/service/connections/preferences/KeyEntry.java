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

import org.eclipse.eavp.viz.service.datastructures.IVizEntryContentProvider;
import org.eclipse.eavp.viz.service.datastructures.VizAllowedValueType;
import org.eclipse.eavp.viz.service.datastructures.VizEntry;


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
public class KeyEntry extends VizEntry {

	/**
	 * An error message for invalid keys in the case where there is no set list
	 * of allowed keys.
	 */
	private static final String undefinedErrMsg = "'${incorrectValue}' is an unacceptable value. It must be a unique string.";

	/**
	 * The content provider for this {@code Entry}. It must be hooked up to an
	 * {@link IKeyManager}.
	 */
	private KeyEntryContentProvider contentProvider;

	/**
	 * The copy constructor. When used, both {@code KeyEntry}s will use the
	 * exact same {@link #keyManager} (but valid or unique keys).
	 * 
	 * @param entry
	 *            The other {@code KeyEntry} to copy.
	 * @throws NullPointerException
	 *             An NPE is thrown if the provided entry to copy is null, as a
	 *             valid {@code KeyEntryContentProvider} cannot be acquired from
	 *             a null entry.
	 */
	public KeyEntry(KeyEntry entry) throws NullPointerException {
		// If the specified entry is not null, we can copy it.
		if (entry != null) {
			// Copy the super class' variables.
			super.copy(entry);

			// Copy this class' variables.
			// The super class clones the content provider, so we just need to
			// set the cast reference to it.
			contentProvider = (KeyEntryContentProvider) iEntryContentProvider;
		}
		// Otherwise, we must throw an exception as this will be in an invalid
		// state (no KeyEntryContentProvider).
		else {
			throw new NullPointerException("KeyEntry error: "
					+ "Cannot copy null KeyEntry.");
		}
		return;
	}

	/**
	 * The default constructor.
	 * 
	 * @param contentProvider
	 *            The {@code Entry}'s content provider.
	 * @param manager
	 *            The manager for the keys stored in this and other
	 *            {@code KeyEntry}s.
	 * @throws NullPointerException
	 *             An NPE is thrown if the provided content provider is null, as
	 *             a valid {@code KeyEntryContentProvider} is required.
	 */
	public KeyEntry(KeyEntryContentProvider contentProvider)
			throws NullPointerException {
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

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.datastructures.VizEntry#clone()
	 */
	@Override
	public Object clone() {
		return new KeyEntry(this);
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
		else if (object != null && object instanceof KeyEntry) {
			// Check all of the super class variables.
			equals = super.equals(object);

			// Compare all class variables.
			// Nothing to do (the content provider was already compared in the
			// super method).
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
		hash += 31 * contentProvider.hashCode();

		return hash;
	}

	/**
	 * Overrides the parent method to enforce the rule that content providers
	 * *must* be {@link KeyEntryContentProvider}s.
	 */
	@Override
	public void setContentProvider(IVizEntryContentProvider contentProvider) {
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

		VizAllowedValueType valueType = contentProvider.getAllowedValueType();

		// If the new value is the same, do nothing.
		if (value == newValue || (value != null && value.equals(newValue))) {
			returnCode = true;

			changeState = false;
			errorMessage = null;
		}
		// For an undefined set of keys, we need to check if the specified key
		// is valid.
		else if (valueType == VizAllowedValueType.Undefined) {
			// If the new value is valid, change it.
			if (contentProvider.keyAvailable(newValue)) {
				value = newValue;
				returnCode = true;

				changeState = true;
				errorMessage = null;
				notifyListeners();
			}
			// If the new value is invalid, set up the error message.
			else {
				String error = undefinedErrMsg;
				changeState = false;
				errorMessage = error.replace("${incorrectValue}",
						newValue != null ? newValue : "null");
			}
		}
		// Otherwise, for a discrete set of keys, we can rely on the default
		// behavior.
		else { // AllowedValueType.Discrete
			returnCode = super.setValue(newValue);
		}

		return returnCode;
	}
}
