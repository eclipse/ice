/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings
 *******************************************************************************/
package org.eclipse.ice.viz.service.visit;

import org.eclipse.ice.viz.service.AbstractVizPreferencePage;
import org.eclipse.ice.viz.service.visit.preferences.Connection;
import org.eclipse.ice.viz.service.visit.preferences.ConnectionManager;
import org.eclipse.ice.viz.service.visit.preferences.ConnectionPreferenceAdapter;
import org.eclipse.ice.viz.service.visit.preferences.TableComponentComposite;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;

/**
 * This class provides a preferences page for the VisIt visualization service.
 * 
 * @author Jay Jay Billings
 * 
 */
public class VisItPreferencePage extends AbstractVizPreferencePage {

	/**
	 * The {@code ConnectionManager} used by this preference page. It is
	 * represented by a {@link ConnectionComposite} on the page.
	 */
	private ConnectionManager connectionManager;

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

		// Create a ConnectionManager and load it from the preference store.
		connectionManager = new ConnectionManager();
		ConnectionPreferenceAdapter adapter = new ConnectionPreferenceAdapter();
		adapter.toConnectionManager(getPreferenceStore(), connectionManager);
		
		// FIXME Temporarily add a basic connection.
		connectionManager.clear();
		connectionManager.addRow();
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

		// TODO These preferences will each need to be custom tailored.
		for (ConnectionPreference p : ConnectionPreference.values()) {
			addField(new StringFieldEditor(p.toString(), p.getName(),
					getFieldEditorParent()));
		}

		return;
	}

	@Override
	protected Control createContents(Composite parent) {
		// Create the default layout initially. This also gives us the return
		// value.
		Control control = super.createContents(parent);

		Composite container = getFieldEditorParent();
		GridLayout gridLayout = (GridLayout) container.getLayout();
		
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
}