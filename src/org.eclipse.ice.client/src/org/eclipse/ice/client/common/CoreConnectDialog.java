/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
 *   Claire Saunders, Matthew Wang, Anna Wojtowicz
 *******************************************************************************/
package org.eclipse.ice.client.common;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

// CoreConnectDialog inherits from JFace Dialog and defines a 
// user Login prompt to gather hostname and port information
public class CoreConnectDialog extends Dialog {

	private static final int RESET_ID = IDialogConstants.NO_TO_ALL_ID + 1;

	// Hostname and port text fields
	private Text hostnameField;
	private Text portField;

	// User input
	private String port;
	private String hostname;

	// The Constructor
	protected CoreConnectDialog(Shell parentShell) {
		super(parentShell);
	}

	// Constructs the SWT GUI
	@Override
	protected Control createDialogArea(Composite parent) {
		// Create a composite to position widgets
		Composite comp = (Composite) super.createDialogArea(parent);

		// Give the composite 2 columns
		GridLayout layout = (GridLayout) comp.getLayout();
		layout.numColumns = 2;

		// Set text to describe the task

		// Set text to ask for hostname
		Label hostnameLabel = new Label(comp, SWT.RIGHT);
		hostnameLabel.setText("Hostname: ");

		// Create a new text box to input hostname
		hostnameField = new Text(comp, SWT.SINGLE);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		hostnameField.setLayoutData(data);

		// Set text to ask for port
		Label portLabel = new Label(comp, SWT.RIGHT);
		portLabel.setText("Port: ");

		// Create a new text box to input port
		portField = new Text(comp, SWT.SINGLE | SWT.SINGLE);
		data = new GridData(GridData.FILL_HORIZONTAL);
		portField.setLayoutData(data);

		return comp;
	}

	// Create buttons on the button bar, other than the default OK and Cancel
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);
		// Create Reset All button that resets text boxes to empty
		createButton(parent, RESET_ID, "Reset All", false);
	}

	// Called when button is pressed. Performs action corresponding to
	// specific button
	@Override
	protected void buttonPressed(int buttonId) {
		// If button is Reset All, set the text box text to "" (empty)
		if (buttonId == RESET_ID) {
			hostnameField.setText("");
			portField.setText("");
		} else {
			// If not Reset All, get the text field's inputs by calling
			// setHostAndPort()
			this.setHostAndPort(portField.getText(), hostnameField.getText());
			// Allow Dialog to handle what the button means
			super.buttonPressed(buttonId);
		}
	}

	// Return the user input hostname
	public String getHostname() {
		return hostname;
	}

	// Return the user input port
	public String getPort() {
		return port;
	}

	// Extract the login information and save it for retrieval
	private void setHostAndPort(String p, String u) {
		port = p;
		hostname = u;
		return;
	}
}
