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
package org.eclipse.ice.viz.service.paraview.connections.preferences;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.viz.service.connections.preferences.ConnectionTable;
import org.eclipse.ice.viz.service.connections.preferences.TableComponentComposite;
import org.eclipse.ice.viz.service.paraview.ParaViewVizService;
import org.eclipse.ice.viz.service.preferences.AbstractVizPreferencePage;
import org.eclipse.ice.viz.service.preferences.CustomScopedPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.osgi.service.prefs.BackingStoreException;

/**
 * This class provides a preference page for the ParaView visualization service.
 * 
 * @author Jordan Deyton
 *
 */
public class ParaViewConnectionPreferencePage extends AbstractVizPreferencePage {

	/**
	 * The {@code ConnectionTable} used by this preference page. It is
	 * represented by a {@link TableComponentComposite} on the page.
	 */
	private final ConnectionTable table = new ConnectionTable();

	/**
	 * The default constructor.
	 */
	public ParaViewConnectionPreferencePage() {
		super(GRID);
	}

	/*
	 * Implements an abstract method from FieldEditorPreferencePage.
	 */
	@Override
	protected void createFieldEditors() {
		// No field editors yet, just the table of connections.
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
	 * Overrides a method from AbstractVizPreferencePage.
	 */
	@Override
	public void init(IWorkbench workbench) {
		// Perform the required basic initialization.
		super.init(workbench);

		// Replace the default title.
		setDescription("ParaView Visualization Preferences");

		// Load the current preferences into the table.
		loadPreferences(table);

		return;
	}

	/*
	 * Overrides a method from FieldEditorPreferencePage.
	 */
	@Override
	public boolean performOk() {
		boolean ok = super.performOk();

		// Apply the preferences from the table.
		savePreferences(table);

		return ok;
	}

	/**
	 * Gets the delimiter used when saving/loading connection preferences.
	 * 
	 * @return The delimiter for serializing connection preferences.
	 */
	private String getDelimiter() {
		// TODO Get this from the manager or the viz service somehow.
		return ",";
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
		IEclipsePreferences node = store.getNode(ParaViewVizService.CONNECTIONS_NODE_ID);

		// Load the current preferences into the table.
		String[] existingKeys;
		try {
			existingKeys = node.keys();
			for (String key : existingKeys) {
				int index = table.addRow();
				List<Entry> row = table.getRow(index);

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

	/**
	 * Saves the preferences from the specified {@link ConnectionTable}.
	 * 
	 * @param table
	 *            The table containing the new connection preferences to store.
	 */
	private void savePreferences(ConnectionTable table) {
		// Get the preference node for connection preferences.
		CustomScopedPreferenceStore store = (CustomScopedPreferenceStore) getPreferenceStore();
		IEclipsePreferences node = store.getNode(ParaViewVizService.CONNECTIONS_NODE_ID);

		// Get a set of connection names from the table. At the end of this
		// operation, only the connections in the table will be in the
		// preferences.
		Set<String> updated = new HashSet<String>(table.getConnectionNames());
		List<Entry> row;

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
	private String serializeConnectionPreferences(List<Entry> connection) {
		String preferences = "";
		String delimiter = getDelimiter();

		// Add the host, port, and path.
		preferences += connection.get(1).getValue();
		preferences += delimiter + connection.get(2).getValue();
		preferences += delimiter + connection.get(3).getValue();

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
	private String[] unserializeConnectionPreferences(String preferences) {
		return preferences.split(getDelimiter(), -1);
	}

}
