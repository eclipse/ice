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
package org.eclipse.ice.client.rcp.actions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class ChooseWorkspaceDialog extends TitleAreaDialog {

	// Preference Keys needed
	private static final String key_workspaceRootDir = "WSRoot";
	private static final String key_rememberWS = "WSRemember";
	private static final String key_lastUsedWorkspaces = "WSLastUsed";

	// Preference Store
	private static Preferences prefs = Preferences
			.userNodeForPackage(ChooseWorkspaceDialog.class);

	// Dialog Messages
	private static final String message = "Your workspace is where project settings and important files will be stored.";
	private static final String info = "Please select a directory that will be your workspace.";
	private static final String error = "You must set a directory.";

	// Widgets needed
	private Combo wsPath;
	private Button rememberButton;
	private Composite comp;

	// List of previous workspaces
	private List<String> lastUsed;

	// Eventual User response, the desired workspace
	private String workspaceDirectoryPath;
	private String reason;

	// The number of last selected workspaces in the Combo box
	private static final int MaxHistory = 5;

	// Boolean to indicate whether we are setting an initial workspace
	// or switching from a current one.
	private boolean switchWorkspace;

	// The Constructor
	public ChooseWorkspaceDialog(boolean switchWS, Image image) {
		super(Display.getDefault().getActiveShell());
		this.switchWorkspace = switchWS;
		if (image != null) {
			this.setTitleImage(image);
		}
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		// Set different shell titles depending on whether we are
		// creating a new workspace or switching from a current one
		if (switchWorkspace) {
			newShell.setText("Switch Workspace");
		} else {
			newShell.setText("Workspace Selection");
		}
	}

	// Returns users selection in the "Remember Workspace" check box
	public static boolean isRemembered() {
		return prefs.getBoolean(key_rememberWS, false);
	}

	public void setRemembered(boolean value) {
		prefs.putBoolean(key_rememberWS, value);
		return;
	}

	// Returns the most recent set workspace directory
	public static String getLastWorkspace() {
		return prefs.get(key_lastUsedWorkspaces, null);
	}

	@Override
	// Creates the SWT widgets needed to get user information
	protected Control createDialogArea(Composite parent) {
		// Set the title and message of this TitleAreaDialog
		this.setTitle("Pick a Workspace");
		this.setMessage(message);

		// Create the underlying composite
		comp = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		// Give it 3 columns and allow the widgets to fill available horizontal
		// space
		layout.numColumns = 3;
		GridData data = new GridData(GridData.FILL_HORIZONTAL
				| GridData.GRAB_HORIZONTAL | GridData.FILL_VERTICAL
				| GridData.VERTICAL_ALIGN_END);
		// Set the composites layout and layout data
		comp.setLayout(layout);
		comp.setLayoutData(data);

		// Create a workspace label
		CLabel wsLabel = new CLabel(comp, SWT.NONE);
		wsLabel.setText("Workspace Root Path");

		// Create the workspace combo box, it lists the selected workspace
		// and any previous workspaces
		wsPath = new Combo(comp, SWT.BORDER);
		// Get what default directory should be in the box upon opening
		String wsRoot = prefs.get(key_workspaceRootDir, "");
		if (wsRoot == null || wsRoot.length() == 0) {
			wsRoot = getWorkspaceSuggestion();
		}
		wsPath.setText(wsRoot == null ? "" : wsRoot);
		wsPath.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
				| GridData.GRAB_HORIZONTAL));

		// Create the browse button as a SWT.Push variety
		Button browseButton = new Button(comp, SWT.PUSH);
		browseButton.setText("Browse");
		// Add an event listener that displays a Directory Dialog prompt
		browseButton.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				// Create the DirectoryDialog and set its text and info
				DirectoryDialog dialog = new DirectoryDialog(new Shell(Display
						.getCurrent()));
				dialog.setText("Select Workspace Directory");
				dialog.setMessage(info);
				dialog.setFilterPath(wsPath.getText());
				// Open the dialog and get the user's selection
				String userSelection = dialog.open();
				// Check the selections validity
				if (userSelection == null && wsPath.getText().length() == 0) {
					setMessage(error, IMessageProvider.ERROR);
				} else {
					// If valid set the selection in the combo box
					setMessage(message);
					wsPath.setText(userSelection);
				}
			}
		});

		// Create a remember workspace check button
		rememberButton = new Button(comp, SWT.CHECK);
		rememberButton.setText("Remember Workspace");
		rememberButton.setSelection(prefs.getBoolean(key_rememberWS, false));

		// Populate the list of previous workspace selections for the combo box
		String lastUsedWS = prefs.get(key_lastUsedWorkspaces, "");
		lastUsed = new ArrayList<String>();
		if (lastUsed != null) {
			String[] all = lastUsedWS.split("#");
			for (String str : all) {
				lastUsed.add(str);
			}
		}
		// Add the gathered last workspaces and add them to the combo box
		for (String last : lastUsed) {
			wsPath.add(last);
		}

		return comp;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		
		// Local Declarations
		String okLabel = "Ok", cancelLabel = "Cancel";
		
		// Create OK and Cancel buttons for the widget
		createButton(parent, IDialogConstants.OK_ID, okLabel,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				cancelLabel, false);
	}

	@Override
	// This method is called when the OK button is pressed
	protected void okPressed() {
		// Get the users selected directory
		String str = wsPath.getText();

		// Check the directory's validity
		if (str.length() == 0) {
			setMessage(error, IMessageProvider.ERROR);
			return;
		}

		// Make sure the selected workspace exists and is valid
		if (!checkWorkspaceExists(str)) {
			setMessage(error, IMessageProvider.ERROR);
			Status status = new Status(IStatus.ERROR, "gov.ice.iceclient", 0,
					reason, null);
			// Display the dialog
			ErrorDialog.openError(
					Display.getCurrent().getActiveShell(),
					"Workspace Selection Error",
					"Error creating the workspace directory "
							+ this.wsPath.getText() + ".", status);
			return;
		}

		// Remove the selection from the lastUsed String array
		// If the array does not contain the directory then add it to the array
		lastUsed.remove(str);
		if (!lastUsed.contains(str)) {
			lastUsed.add(0, str);
		}

		// If size of array is greater than the maximum number of used
		// workspaces we want to show, delete the oldest one.
		if (lastUsed.size() > MaxHistory) {
			List<String> remove = new ArrayList<String>();
			for (int i = 5; i < lastUsed.size(); i++) {
				remove.add(lastUsed.get(i));
			}
			lastUsed.removeAll(remove);
		}

		// Create a string concatenation of all the last used workspaces
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < lastUsed.size(); i++) {
			buf.append(lastUsed.get(i));
			if (i != lastUsed.size() - 1) {
				buf.append("#");
			}
		}

		// Save the remember and last workspace preferences
		prefs.putBoolean(key_rememberWS, rememberButton.getSelection());
		prefs.put(key_lastUsedWorkspaces, buf.toString());

		// Save the valid workspace directory path
		workspaceDirectoryPath = str;

		// Set the workspace directory preference
		prefs.put(key_workspaceRootDir, str);

		// Call the super classes okPressed method
		super.okPressed();

		return;
	}

	// Make sure the workspace exists. If it doesn't, create it.
	private boolean checkWorkspaceExists(String directory) {
		// Create the directory
		File file = new File(directory);
		// Check that it exists and we have permission to write to it
		if (!file.exists()) {
			// create it
			boolean created = file.mkdirs();
			if (!created) {
				reason = "Invalid user file permissions.";
				return false;
			}
		}
		return true;
	}

	@Override
	// Allow the dialog to be resizeable
	protected boolean isResizable() {
		return true;
	}

	// Getter for the selected workspace directory
	public String getWorkspacePath() {
		return workspaceDirectoryPath;
	}

	// Provide a workspace suggestion
	private String getWorkspaceSuggestion() {
		StringBuffer buf = new StringBuffer();

		String home = System.getProperty("user.home");
		if (home == null) {
			home = File.separator + "temp";
		}

		buf.append(home);
		buf.append(File.separator);
		buf.append("ICE");

		return buf.toString();
	}
}
