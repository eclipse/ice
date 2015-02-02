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
package org.eclipse.ice.viz.service.paraview;

import java.util.List;

import org.eclipse.ice.datastructures.ICEObject.IUpdateable;
import org.eclipse.ice.datastructures.ICEObject.IUpdateableListener;
import org.eclipse.ice.viz.service.AbstractVizPreferencePage;
import org.eclipse.ice.viz.service.CustomScopedPreferenceStore;
import org.eclipse.ice.viz.service.connections.ConnectionManager;
import org.eclipse.ice.viz.service.connections.IKeyChangeListener;
import org.eclipse.ice.viz.service.preferences.DynamicComboFieldEditor;
import org.eclipse.ice.viz.service.preferences.TableComponentComposite;
import org.eclipse.ice.viz.service.preferences.TableComponentPreferenceAdapter;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;

/**
 * This class provides a preference page for the ParaView visualization service.
 * 
 * @author Jordan Deyton
 *
 */
public class ParaViewPreferencePage extends AbstractVizPreferencePage {

	/**
	 * The {@code ConnectionManager} used by this preference page. It is
	 * represented by a table on the page.
	 */
	private final ConnectionManager connectionManager = new ConnectionManager();

	/**
	 * The {@code FieldEditor} for the default connection. Its values need to be
	 * updated when the rows in the {@link #connectionManager} change.
	 */
	private DynamicComboFieldEditor defaultConnection = null;

	/**
	 * The default constructor.
	 */
	public ParaViewPreferencePage() {
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
		setDescription("ParaView Connection Preferences");

		// Read the table of connections stored in the preferences into the
		// ConnectionManager.
		TableComponentPreferenceAdapter adapter = new TableComponentPreferenceAdapter();
		adapter.toTableComponent(
				(CustomScopedPreferenceStore) getPreferenceStore(),
				connectionManager);

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

		defaultConnection = new DynamicComboFieldEditor("defaultConnection",
				"Default Connection", parent,
				connectionManager.getConnectionNames());
		addField(defaultConnection);

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

		return ok;
	}

}
