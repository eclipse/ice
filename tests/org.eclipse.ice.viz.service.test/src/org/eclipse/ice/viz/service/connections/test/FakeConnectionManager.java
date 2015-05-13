/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Jordan Deyton - Initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.ice.viz.service.connections.test;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ice.viz.service.connections.ConnectionManager;
import org.eclipse.ice.viz.service.connections.ConnectionTable;
import org.eclipse.ice.viz.service.connections.IConnectionAdapter;
import org.eclipse.ice.viz.service.preferences.CustomScopedPreferenceStore;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * This class provides a {@link ConnectionManager} for {@link FakeConnection}s.
 * It is to be used only for testing the base {@code ConnectionManager} class.
 * 
 * @author Jordan Deyton
 *
 */
public class FakeConnectionManager extends ConnectionManager<FakeConnection> {

	/**
	 * A list of all created connection adapters. <b>Do not modify this list
	 * when testing!</b>
	 */
	public List<FakeConnectionAdapter> adapters;

	/**
	 * A reference to the associated preference page's {@link IPreferenceStore}.
	 * If this has been determined previously, then it should be returned in
	 * {@link #getPreferenceStore()}.
	 */
	private CustomScopedPreferenceStore preferenceStore = null;

	/**
	 * The default constructor.
	 */
	public FakeConnectionManager() {
		if (adapters == null) {
			adapters = new ArrayList<FakeConnectionAdapter>();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.viz.service.connections.ConnectionManager#getPreferenceStore
	 * ()
	 */
	@Override
	protected CustomScopedPreferenceStore getPreferenceStore() {
		if (preferenceStore == null) {
			// Get the PreferenceStore for the bundle.
			preferenceStore = new CustomScopedPreferenceStore(getClass());
		}
		return preferenceStore;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.connections.ConnectionManager#
	 * createConnectionTable()
	 */
	@Override
	protected ConnectionTable createConnectionTable() {
		ConnectionTable table = new ConnectionTable();
		return table;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.connections.ConnectionManager#
	 * createConnectionAdapter()
	 */
	@Override
	protected IConnectionAdapter<FakeConnection> createConnectionAdapter() {
		FakeConnectionAdapter adapter = new FakeConnectionAdapter();
		if (adapters == null) {
			adapters = new ArrayList<FakeConnectionAdapter>();
		}
		adapters.add(adapter);
		return adapter;
	}

}