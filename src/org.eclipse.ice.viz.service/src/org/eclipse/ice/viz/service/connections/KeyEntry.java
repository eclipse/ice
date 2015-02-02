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
	 * The manager for the keys stored in this and other {@code KeyEntry}s.
	 */
	private final IKeyManager keyManager;

	/**
	 * An error message for invalid keys in the case where there is no set list
	 * of allowed keys.
	 */
	protected static final String undefinedErrMsg = "'${incorrectValue}' is an unacceptable value. It must be a unique string.";

	/**
	 * The default constructor.
	 * 
	 * @param contentProvider
	 *            The {@code Entry}'s content provider.
	 * @param manager
	 *            The manager for the keys stored in this and other
	 *            {@code KeyEntry}s.
	 */
	public KeyEntry(KeyEntryContentProvider contentProvider, IKeyManager manager) {
		super(contentProvider);

		// Store a reference to the KeyManager if applicable. Otherwise, we
		// should throw an exception as the KeyManager is required.
		if (manager != null) {
			keyManager = manager;

			// Set the initial value of the Entry.
			setValue(keyManager.getNextKey());
		} else {
			throw new NullPointerException("KeyEntry error: "
					+ "Null KeyManager passed to constructor.");
		}

		return;
	}

	/**
	 * The copy constructor. When used, both {@code KeyEntry}s will use the
	 * exact same {@link #keyManager}.
	 * <p>
	 * <b>Note:</b> This method should not be used. It is implemented so that a
	 * template {@code KeyEntry} can be copied when used in
	 * {@code ConnectionManager} {@code TableComponent}s.
	 * </p>
	 * 
	 * @param otherEntry
	 *            The other {@code KeyEntry} to copy.
	 */
	private KeyEntry(KeyEntry otherEntry) {
		// Perform the typical Entry copy method.
		super.copy(otherEntry);

		// Copy the KeyEntry-specific features.
		if (otherEntry != null) {
			keyManager = otherEntry.keyManager;

			// Set the initial value of the Entry.
			setValue(keyManager.getNextKey());

			// Share the custom content provider!
			iEntryContentProvider = otherEntry.iEntryContentProvider;
		} else {
			// We should throw an exception.
			throw new NullPointerException("KeyEntry error: "
					+ "Null copy constructor argument.");
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

		// Update the key value if we can.
		if (keyManager.keyAvailable(newValue)) {
			value = newValue;
			returnCode = true;

			changeState = true;
			errorMessage = null;
			notifyListeners();
		}
		// Otherwise, handle the error message for a set of allowed keys.
		else if (valueType == AllowedValueType.Discrete) {
			String allowedValues = null;
			for (String allowedValue : iEntryContentProvider.getAllowedValues()) {
				if (allowedValues != null) {
					allowedValues += ", " + allowedValue;
				} else {
					allowedValues = allowedValue;
				}
			}

			String error = discreteErrMsg;
			error = error.replace("${incorrectValue}", newValue);
			error = error.replace(" ${allowedValues}", allowedValues);
			errorMessage = error;
		}
		// Handle the case where there is no set list of keys.
		else {
			String error = undefinedErrMsg;
			error = error.replace("${incorrectValue}", newValue);
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
		return new KeyEntry(this);
	}

}
