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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.eavp.viz.service.connections.IVizConnectionManager;
import org.eclipse.eavp.viz.service.datastructures.VizEntry;
import org.eclipse.eavp.viz.service.preferences.AbstractVizPreferencePage;
import org.eclipse.eavp.viz.service.preferences.CustomScopedPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.osgi.service.prefs.BackingStoreException;

/**
 * This class provides a preference page for configuring multiple viz
 * connections. It currently contains a single table with an add and remove
 * button for adding or removing connections. Connections can be updated by
 * manipulating cells in the table.
 * 
 * @author Jordan Deyton
 *
 */
public abstract class VizConnectionPreferencePage extends AbstractVizPreferencePage {

	/**
	 * The {@code ConnectionTable} used by this preference page. It is
	 * represented by a {@link TableComponentComposite} on the page.
	 */
	private ConnectionTable table;

	/**
	 * The default constructor.
	 */
	public VizConnectionPreferencePage() {
		super(GRID);
	}

	/**
	 * Creates the table of connection preferences. This method may be
	 * overridden to provide a custom table with more properties.
	 * 
	 * @return A table of connections.
	 */
	protected ConnectionTable createConnectionTable() {
		return new ConnectionTable();
	}

	/**
	 * Overrides the default behavior to provide a connection table below the
	 * default connection preference {@code FieldEditor}s.
	 */
	@Override
	protected Control createContents(Composite parent) {
		// Create the default layout initially. This also gives us the return
		// value.
		Control control = super.createContents(parent);

		// Get the created parent Composite for all FieldEditors. We need its
		// layout to make sure the connection table spans all horizontal space.
		Composite container = getFieldEditorParent();
		GridLayout gridLayout = (GridLayout) container.getLayout();

		// If there are no field editors, the number of columns is 0, which
		// prevents the table from appearing. We need to update the number of
		// columns in this case.
		if (gridLayout.numColumns == 0) {
			gridLayout.numColumns = 1;
		}

		// Create a ConnectionComposite to show all of the cached connection
		// preferences.
		TableComponentComposite connections = new TableComponentComposite(container, SWT.NONE);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true, gridLayout.numColumns, 1);
		connections.setLayoutData(gridData);

		// Set the custom Composite's TableComponent to fill the table.
		connections.setTableComponent(table);

		return control;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
	 */
	@Override
	protected void createFieldEditors() {
		// No field editors yet, just the table of connections.
	}

	/**
	 * Gets the delimiter used when saving/loading connection preferences.
	 * 
	 * @return The delimiter for serializing connection preferences.
	 */
	protected String getConnectionPreferenceDelimiter() {
		return IVizConnectionManager.DEFAULT_CONNECTION_PREFERENCE_DELIMITER;
	}

	/**
	 * Gets the ID of the {@link IEclipsePreferences} node in the store.
	 * Connections will be stored under this node.
	 * 
	 * @return The ID of the connection preferences node.
	 */
	protected abstract String getConnectionsPreferenceNodeId();

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.preferences.AbstractVizPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	@Override
	public void init(IWorkbench workbench) {
		/*
		 * This method is called every time the page is loaded.
		 */

		// Perform the required basic initialization.
		super.init(workbench);

		// Load the current preferences into the table.
		table = createConnectionTable();
		loadPreferences(table);

		return;
	}

	/**
	 * Loads the specified {@link ConnectionTable} based on the current
	 * preferences.
	 * 
	 * @param table
	 *            The table to load the current preferences into.
	 */
	private void loadPreferences(ConnectionTable table) {
		// Get the preference node for connection preferences.
		CustomScopedPreferenceStore store = (CustomScopedPreferenceStore) getPreferenceStore();
		IEclipsePreferences node = store.getNode(getConnectionsPreferenceNodeId());

		// Load the current preferences into the table.
		String[] existingKeys;
		try {
			existingKeys = node.keys();
			for (String key : existingKeys) {
				int index = table.addRow();
				List<VizEntry> row = table.getRow(index);

				// Update the key/name in the table.
				row.get(0).setValue(key);
				// Update the other properties.
				String[] preferences = unserializeConnectionPreferences(node.get(key, null));
				for (int i = 0; i < preferences.length; i++) {
					row.get(i + 1).setValue(preferences[i]);
				}
			}
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}

		return;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#performOk()
	 */
	@Override
	public boolean performOk() {
		/*
		 * This method is called every time OK or Apply is clicked.
		 */
		boolean ok = super.performOk();

		// Apply the preferences from the table.
		savePreferences(table);

		return ok;
	}

	/**
	 * Saves the preferences from the specified {@link ConnectionTable}.
	 * 
	 * @param table
	 *            The table containing the new connection preferences to store.
	 */
	private void savePreferences(ConnectionTable table) {
		// Get the preference node for connection preferences.
		CustomScopedPreferenceStore store = (CustomScopedPreferenceStore) getPreferenceStore();
		IEclipsePreferences node = store.getNode(getConnectionsPreferenceNodeId());

		// Get a set of connection names from the table. At the end of this
		// operation, only the connections in the table will be in the
		// preferences.
		Set<String> updated = new HashSet<String>(table.getConnectionNames());
		List<VizEntry> row;

		// Update old connections in the preferences.
		String[] existingNames;
		try {
			existingNames = node.keys();
			for (String name : existingNames) {
				row = table.getConnection(name);
				// If the connection name has a row in the table, it will need
				// to be updated in the preferences.
				if (row != null) {
					node.put(name, serializeConnectionPreferences(row));
					updated.remove(name);
				}
				// Otherwise, the connection was removed from the table and
				// should be removed from the preferences.
				else {
					node.remove(name);
				}
			}
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}

		// Add all new connections to the preferences.
		for (String name : updated) {
			row = table.getConnection(name);
			node.put(name, serializeConnectionPreferences(row));
		}

		return;
	}

	/**
	 * Serializes the specified connection preferences into a string that can be
	 * stored in the preferences.
	 * 
	 * @param connection
	 *            The table row for the connection.
	 * @return The serialized connection preferences.
	 */
	protected String serializeConnectionPreferences(List<VizEntry> connection) {
		String preferences = "";
		String delimiter = getConnectionPreferenceDelimiter();

		if (connection.size() >= 2) {
			// Add the first preference.
			preferences += connection.get(1).getValue();
			// Add all remaining preferences.
			for (int i = 2; i < connection.size(); i++) {
				preferences += delimiter + connection.get(i).getValue();			
			}
		}
		
		return preferences;
	}

	/**
	 * Converts the serialized connection preferences into an array of string
	 * values that can be used to update a row in the connection table.
	 * 
	 * @param preferences
	 *            The serialized connection preferences.
	 * @return An array of all preferences for the row.
	 */
	protected String[] unserializeConnectionPreferences(String preferences) {
		return preferences.split(getConnectionPreferenceDelimiter(), -1);
	}

}
