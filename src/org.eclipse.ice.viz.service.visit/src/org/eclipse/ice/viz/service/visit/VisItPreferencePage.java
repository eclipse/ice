/*******************************************************************************
 * Copyright (c) 2014- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings, Jordan Deyton
 *******************************************************************************/
package org.eclipse.ice.viz.service.visit;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.ice.viz.service.AbstractVizPreferencePage;
import org.eclipse.ice.viz.service.connections.ConnectionTable;
import org.eclipse.ice.viz.service.connections.IKeyChangeListener;
import org.eclipse.ice.viz.service.connections.visit.VisItConnectionTable;
import org.eclipse.ice.viz.service.preferences.CustomScopedPreferenceStore;
import org.eclipse.ice.viz.service.preferences.DynamicComboFieldEditor;
import org.eclipse.ice.viz.service.preferences.TableComponentComposite;
import org.eclipse.ice.viz.service.preferences.TableComponentPreferenceAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;

/**
 * This class provides a preferences page for the VisIt visualization service.
 * 
 * @author Jay Jay Billings, Jordan Deyton
 * 
 */
public class VisItPreferencePage extends AbstractVizPreferencePage implements
		IKeyChangeListener {

	/**
	 * The {@code ConnectionManager} used by this preference page. It is
	 * represented by a {@link ConnectionComposite} on the page.
	 */
	private final ConnectionTable connectionManager = new VisItConnectionTable();

	/**
	 * This set contains removed keys for pre-existing connections (those that
	 * are in the preference store when the page loads).
	 * <p>
	 * <b>Note:</b> Removed keys should be processed before changed keys, as it
	 * is acceptable for a key to be removed and later change an existing key to
	 * that removed key.
	 * </p>
	 */
	private final Set<String> removedKeys = new HashSet<String>();
	/**
	 * This map contains changed keys for pre-existing connections (those that
	 * are in the preference store when the page loads) whose keys have been
	 * changed. A map entry's key is the new key, while its value is the
	 * original key at page load.
	 */
	private final Map<String, String> newToOld = new HashMap<String, String>();
	/**
	 * This set strictly contains new keys that do not currently exist in the
	 * preference store.
	 */
	private final Set<String> addedKeys = new HashSet<String>();

	/**
	 * The default constructor.
	 */
	public VisItPreferencePage() {
		super(GRID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	@Override
	public void init(IWorkbench workbench) {
		setDescription("VisIt 2.8.2 Connection Preferences");

		// Read the table of connections stored in the preferences into the
		// ConnectionManager.
		TableComponentPreferenceAdapter adapter = new TableComponentPreferenceAdapter();
		adapter.toTableComponent(
				(CustomScopedPreferenceStore) getPreferenceStore(),
				connectionManager);

		// Sync the key changes from the ConnectionManager and register for key
		// change events.
		resetKeyChangeInfo();
		connectionManager.addKeyChangeListener(this);

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors
	 * ()
	 */
	@Override
	protected void createFieldEditors() {
		Composite parent = getFieldEditorParent();

		// Create a new DynamicComboFieldEditor for the default connection. The
		// default connection should only be selected from the list of
		// connections from the connection table.
		final DynamicComboFieldEditor defaultConnection;
		defaultConnection = new DynamicComboFieldEditor("defaultConnection",
				"Default Connection", parent,
				connectionManager.getConnectionNames());
		addField(defaultConnection);

		// Add a key change listener so that we can refresh the values in the
		// default connection field editor when necessary.
		connectionManager.addKeyChangeListener(new IKeyChangeListener() {
			@Override
			public void keyChanged(String oldKey, String newKey) {
				// Get the current value for the default connection.
				String value = defaultConnection.getValue();
				// Update the default connection Combo's allowed values.
				List<String> names = connectionManager.getConnectionNames();
				defaultConnection.setAllowedValues(names);
				// If the selected connection's name was changed, make sure it
				// is still the default connection.
				if (oldKey != null && newKey != null && oldKey.equals(value)) {
					defaultConnection.setValue(newKey);
				}
				return;
			}
		});

		return;
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
		TableComponentComposite connections = new TableComponentComposite(
				container, SWT.NONE);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true,
				gridLayout.numColumns, 1);
		connections.setLayoutData(gridData);

		// Set the custom Composite's TableComponent to fill the table.
		connections.setTableComponent(connectionManager);

		return control;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#performOk()
	 */
	@Override
	public boolean performOk() {
		boolean ok = super.performOk();

		// Write the table of connections stored in the ConnectionManager into
		// the preference store.
		TableComponentPreferenceAdapter adapter = new TableComponentPreferenceAdapter();
		adapter.toPreferences(connectionManager,
				(CustomScopedPreferenceStore) getPreferenceStore());

		// Notify the viz service that the preferences have been updated.
		VisItVizService.getInstance().preferencesChanged(newToOld, addedKeys,
				removedKeys);
		// Clear the key change info now that the viz service has been notified
		// of all changes.
		resetKeyChangeInfo();

		return ok;
	}

	/**
	 * Resets the information about changed connection keys.
	 */
	private void resetKeyChangeInfo() {

		// Clear all of the key change information and put all current keys into
		// the new-to-old-key map.
		newToOld.clear();
		addedKeys.clear();
		removedKeys.clear();

		for (String key : connectionManager.getConnectionNames()) {
			newToOld.put(key, key);
		}

		return;
	}

	// TODO Test this method.
	/**
	 * This method keeps track of added, removed, and changed keys. It keeps
	 * track of the respective collections to make updating the viz service's
	 * known connections easier.
	 */
	@Override
	public void keyChanged(String oldKey, String newKey) {

		// If a key was changed...
		if (oldKey != null && newKey != null) {
			// A pre-existing key was changed...
			if (newToOld.containsKey(oldKey)) {
				newToOld.put(newKey, newToOld.remove(oldKey));
			}
			// A new key was changed...
			else if (addedKeys.contains(oldKey)) {
				addedKeys.remove(oldKey);
				addedKeys.add(newKey);
			}
		}
		// If a key was removed...
		else if (oldKey != null) {
			// A pre-existing key was removed...
			if (newToOld.containsKey(oldKey)) {
				removedKeys.add(newToOld.remove(oldKey));
			}
			// A new key was removed...
			else if (addedKeys.contains(oldKey)) {
				addedKeys.remove(oldKey);
			}
		}
		// If a key was added...
		else if (newKey != null) {
			// A pre-existing key was added...
			if (removedKeys.contains(newKey)) {
				removedKeys.remove(newKey);
				newToOld.put(newKey, newKey);
			}
			// A new key was added...
			else if (!addedKeys.contains(newKey)) {
				addedKeys.add(newKey);
			}
		}
		return;
	}

}