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

import org.eclipse.ice.viz.service.AbstractVizPreferencePage;
import org.eclipse.ice.viz.service.CustomScopedPreferenceStore;
import org.eclipse.ice.viz.service.preferences.TableComponentComposite;
import org.eclipse.ice.viz.service.preferences.TableComponentPreferenceAdapter;
import org.eclipse.ice.viz.service.visit.connections.ConnectionManager;
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
 * @author Jay Jay Billings, Jordan Deyton
 * 
 */
public class VisItPreferencePage extends AbstractVizPreferencePage {

	/**
	 * The {@code ConnectionManager} used by this preference page. It is
	 * represented by a {@link ConnectionComposite} on the page.
	 */
	private final ConnectionManager connectionManager = new ConnectionManager();

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

		ConnectionPreference p;

		// Add the host name and port for testing remote connections.
		p = ConnectionPreference.Host;
		addField(new StringFieldEditor(p.toString(), p.getName(),
				getFieldEditorParent()));
		p = ConnectionPreference.HostPort;
		addField(new StringFieldEditor(p.toString(), p.getName(),
				getFieldEditorParent()));

		// For now, we only expose the VisIt path since we do not have remote
		// connections working properly.
		p = ConnectionPreference.VisItPath;
		addField(new StringFieldEditor(p.toString(), p.getName(),
				getFieldEditorParent()));

		// TODO Using the following code as an example, add a password column to
		// the ConnectionManager.
		// StringFieldEditor passwordField = new StringFieldEditor("password",
		// "Password", getFieldEditorParent()) {
		// @Override
		// protected void doFillIntoGrid(Composite parent, int numColumns) {
		// super.doFillIntoGrid(parent, numColumns);
		//
		// getTextControl().setEchoChar('*');
		// }
		//
		// @Override
		// protected void doStore() {
		// ISecurePreferences preferences = SecurePreferencesFactory
		// .getDefault();
		// ISecurePreferences node = preferences
		// .node("visit.connection.credentials");
		// try {
		// String user = "bambam";
		// String password = getTextControl().getText();
		//
		// System.out.println(user + " - " + password);
		//
		// node.put("user", user, true);
		// node.put("password", password, true);
		// } catch (StorageException e) {
		// e.printStackTrace();
		// }
		// // Default behavior...
		// // getPreferenceStore().setValue(getPreferenceName(),
		// // textField.getText());
		// }
		//
		// @Override
		// protected void doLoad() {
		// Text textField = getTextControl();
		// if (textField != null) {
		// ISecurePreferences preferences = SecurePreferencesFactory
		// .getDefault();
		// if (preferences.nodeExists("visit.connection.credentials")) {
		// ISecurePreferences node = preferences
		// .node("visit.connection.credentials");
		// try {
		// String user = node.get("user", "");
		// String password = node.get("password", "");
		//
		// System.out.println(user + " - " + password);
		//
		// textField.setText(password);
		// oldValue = password;
		// } catch (StorageException e) {
		// e.printStackTrace();
		// }
		// }
		// }
		// // Default behavior...
		// // Text textField = getTextControl();
		// // if (textField != null) {
		// // String value =
		// // getPreferenceStore().getString(getPreferenceName());
		// // textField.setText(value);
		// // oldValue = value;
		// // }
		// }
		// };
		//
		// addField(passwordField);

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

		// TODO Remove this after we have multiple connections and a way to
		// notify the VizService that a particular connection has changed.
		VisItVizService.getInstance().preferencesChanged();

		return ok;
	}

}