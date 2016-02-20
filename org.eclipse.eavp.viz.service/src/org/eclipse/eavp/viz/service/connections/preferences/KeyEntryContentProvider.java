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
 * This class provides an {@link IEntryContentProvider} geared toward
 * {@link KeyEntry}s whose allowed keys are managed by a {@link IKeyManager}.
 * <p>
 * The purpose of this class is to interface some {@code IKeyManager}
 * implementation with a {@code KeyEntry}. The {@code KeyEntry} does not need to
 * manage allowed values or default values. Likewise, the {@code IKeyManager}
 * does not need to worry about how {@code Entry}s work.
 * </p>
 * <p>
 * Notifications sent from the key manager will automatically update the default
 * and allowed values for this content provider.
 * </p>
 * 
 * @author Jordan Deyton
 *
 */
public class KeyEntryContentProvider extends BasicVizEntryContentProvider
		implements IKeyChangeListener {

	/**
	 * This manages all allowed keys for associated {@link KeyEntry}s.
	 */
	private final IKeyManager keyManager;

	/**
	 * The default constructor.
	 * 
	 * @param manager
	 *            This manages all allowed keys for associated {@link KeyEntry}
	 *            s.
	 * 
	 * @throws NullPointerException
	 *             An NPE is thrown if the specified key manager is null, as a
	 *             valid key manager is required.
	 */
	public KeyEntryContentProvider(IKeyManager keyManager)
			throws NullPointerException {
		this.keyManager = keyManager;

		// Check for a null key manager.
		if (keyManager == null) {
			throw new NullPointerException("KeyEntryContentProvider error: "
					+ "Cannot use null key manager.");
		}

		// Determine whether the key manager supports any string key or a
		// pre-defined set of keys. Then set the allowed value type.
		VizAllowedValueType type;

		// If the key manager has a list of keys, then the type is discrete.
		if (!keyManager.getAvailableKeys().isEmpty()) {
			type = VizAllowedValueType.Discrete;
			super.setDefaultValue(keyManager.getNextKey());
		} else {
			// If the list is empty and requesting the next key throws an
			// exception, then the type is discrete. In this case, the
			// KeyManager is just out of keys. Otherwise, the keys are not
			// restricted.
			try {
				String defaultKey = keyManager.getNextKey();
				// Set the default key to the next available one. This will need
				// to be updated when the next available key is taken.
				type = VizAllowedValueType.Undefined;
				super.setDefaultValue(defaultKey);
			} catch (IllegalStateException e) {
				type = VizAllowedValueType.Discrete;
				super.setDefaultValue("");
			}
		}
		super.setAllowedValueType(type);

		// Set the default allowed values. This will need to be updated when one
		// of the key manager's keys is taken or released.
		super.setAllowedValues((ArrayList<String>) keyManager
				.getAvailableKeys());

		// Register with the key manager as a key change listener so we can
		// update the default value and allowed values when the keys change.
		keyManager.addKeyChangeListener(this);

		return;
	}

	/**
	 * A copy constructor.
	 * <p>
	 * Note that the key manager is not re-created for the copy.
	 * </p>
	 * 
	 * @param otherProvider
	 *            The other KeyEntryContentProvider to copy.
	 * @throws NullPointerException
	 *             An NPE is thrown if the specified content provider is null,
	 *             as its valid key manager is required.
	 */
	public KeyEntryContentProvider(KeyEntryContentProvider provider)
			throws NullPointerException {
		// Perform the default construction, using the specified provider's key
		// manager if possible. This is OK since the same key manager is shared.
		this(provider != null ? provider.keyManager : null);

		// If the specified content provider is not null, we can copyt it.
		if (provider != null) {
			// Copy the super class' variables.
			super.copy(provider);

			// Copy this class' variables.
			// Nothing to copy, as the key manager was already set by the
			// default constructor.
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
		return new KeyEntryContentProvider(this);
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
		else if (object != null && object instanceof KeyEntryContentProvider) {
			// Check all of the super class variables.
			equals = super.equals(object);

			// Compare all class variables.
			KeyEntryContentProvider otherKeyProvider = (KeyEntryContentProvider) object;
			equals &= keyManager.equals(otherKeyProvider.keyManager);
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
		hash += 31 * keyManager.hashCode();

		return hash;
	}

	/**
	 * Gets whether the specified key is available for this content provider.
	 * <p>
	 * This provides an alternative to checking {@link #getAllowedValues()} when
	 * the key set is unlimited or "undefined" (in which case the other method
	 * returns an empty list).
	 * </p>
	 * 
	 * @param key
	 *            The key to check for availability.
	 * @return True if the specified key is available, false otherwise.
	 */
	public boolean keyAvailable(String key) {
		return keyManager.keyAvailable(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.connections.IKeyChangeListener#keyChanged
	 * (java.lang.String, java.lang.String)
	 */
	@Override
	public void keyChanged(String oldKey, String newKey) {
		// Update the allowed values to those that are now available. This only
		// needs to be done for discrete key managers, as this list is always
		// empty for undefined key managers.
		if (getAllowedValueType() == VizAllowedValueType.Discrete) {
			List<String> availableKeys = keyManager.getAvailableKeys();
			super.setAllowedValues((ArrayList<String>) availableKeys);
			// We can't get the next available key if there are none left. In
			// that case, set the default value to the empty string.
			if (availableKeys.isEmpty()) {
				super.setDefaultValue("");
			}
			// If there is a key available, set it as the default key.
			else {
				super.setDefaultValue(keyManager.getNextKey());
			}
		}
		// For "undefined" key managers, get the next available key.
		else {
			super.setDefaultValue(keyManager.getNextKey());
		}
		return;
	}

	/**
	 * Overrides the parent behavior to do nothing. The allowed values are
	 * determined by the associated {@link #keyManager}.
	 * 
	 * @param allowedValues
	 */
	@Override
	public void setAllowedValues(ArrayList<String> allowedValues) {
		// Do nothing.
	}

	/**
	 * Overrides the parent behavior to do nothing. The allowed value type is
	 * determined by the associated {@link #keyManager}.
	 * 
	 * @param allowedValueType
	 */
	@Override
	public void setAllowedValueType(VizAllowedValueType allowedValueType) {
		// Do nothing.
	}

	/**
	 * Overrides the parent behavior to do nothing. The default is determined by
	 * the associated {@link #keyManager}.
	 * 
	 * @param defaultValue
	 */
	@Override
	public void setDefaultValue(String defaultValue) {
		// Do nothing.
	}
}
