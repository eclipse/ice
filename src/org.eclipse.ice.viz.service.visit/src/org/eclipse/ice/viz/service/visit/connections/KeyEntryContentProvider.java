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
import org.eclipse.ice.datastructures.form.IEntryContentProvider;

/**
 * This class provides an {@link IEntryContentProvider} geared toward
 * {@link KeyEntry}s whose allowed keys are managed by a {@link IKeyManager}.
 * <p>
 * The purpose of this class is to interface some {@code IKeyManager}
 * implementation with a {@code KeyEntry}. The {@code KeyEntry} does not need to
 * manage allowed values or default values. Likewise, the {@code IKeyManager}
 * does not need to worry about how {@code Entry}s work.
 * </p>
 * 
 * @author Jordan Deyton
 *
 */
public class KeyEntryContentProvider extends BasicEntryContentProvider {

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
	 */
	public KeyEntryContentProvider(IKeyManager manager) {
		keyManager = manager;

		// Determine whether the key manager supports any string key or a
		// pre-defined set of keys. Then set the allowed value type.
		AllowedValueType type;

		// If the key manager has a list of keys, then the type is discrete.
		if (!keyManager.getAvailableKeys().isEmpty()) {
			type = AllowedValueType.Discrete;
		} else {
			// If the list is empty and requesting the next key throws an
			// exception, then the type is discrete. In this case, the
			// KeyManager is just out of keys. Otherwise, the keys are not
			// restricted.
			try {
				keyManager.getNextKey();
				type = AllowedValueType.Undefined;
			} catch (IllegalStateException e) {
				type = AllowedValueType.Discrete;
			}
		}
		super.setAllowedValueType(type);

		return;
	}

	/**
	 * Does nothing.
	 */
	@Override
	public void setDefaultValue(String defaultValue) {
		// Do nothing. The default value cannot be specified.
	}

	/**
	 * Returns the next available key from the {@link #keyManager}.
	 */
	@Override
	public String getDefaultValue() {
		return keyManager.getNextKey();
	}

	/**
	 * Does nothing.
	 */
	@Override
	public void setAllowedValueType(AllowedValueType allowedValueType) {
		// Do nothing. The allowed value type is based on the key manager.
	}

	/**
	 * Does nothing.
	 */
	@Override
	public void setAllowedValues(ArrayList<String> allowedValues) {
		// Do nothing. The allowed values are based on the available keys from
		// the key manager.
	}

	/**
	 * Returns the available keys from the {@link #keyManager}.
	 */
	@Override
	public ArrayList<String> getAllowedValues() {
		return (ArrayList<String>) keyManager.getAvailableKeys();
	}

}
