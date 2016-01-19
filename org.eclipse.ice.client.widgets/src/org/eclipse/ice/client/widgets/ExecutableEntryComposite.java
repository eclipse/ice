/*******************************************************************************
 * Copyright (c) 2012, 2014, 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Alex McCaskey
 *******************************************************************************/
package org.eclipse.ice.client.widgets;

import java.io.File;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.datastructures.entry.IEntry;
import org.eclipse.ice.iclient.IClient;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.remote.core.IRemoteConnectionHostService;
import org.eclipse.remote.ui.dialogs.RemoteResourceBrowser;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;

/**
 * The ExecutableEntryComposite is an extension of hte FileEntryComposite 
 * that creates a browse button that let's users navigate to an executable on 
 * either the local machine or a remote host.
 * 
 * @author Alex McCaskey
 *
 */
public class ExecutableEntryComposite extends FileEntryComposite {

	/**
	 * The Constructor
	 * @param parent
	 * @param refEntry
	 * @param style
	 */
	public ExecutableEntryComposite(Composite parent, IEntry refEntry, int style) {
		super(parent, refEntry, style);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.client.widgets.FileEntryComposite#render()
	 */
	@Override
	public void render() {

		// Get the number of allowed values
		int numAllowedValues = entry.getAllowedValues().size();

		// If this is a File entry, draw dropdown (if applicable)
		// and browse button
		createLabel();
		if (numAllowedValues > 0) {
			createExecutableDropDown();
		}
		createExecutableBrowser();
		setLayout(setupDropDownLayout(numAllowedValues));
	}

	/**
	 * This method creates a drop down Combo for an Entry with the Executable
	 * AllowedValueType.
	 */
	private void createExecutableDropDown() {
		if (widget == null || widget.isDisposed()) {
			widget = new Combo(this,
					SWT.BORDER | SWT.LEAD | SWT.DROP_DOWN | SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL);
			widget.setFocus();
			widget.setLayoutData(new GridData(400, SWT.DEFAULT));

			List<String> allowedValues = entry.getAllowedValues();
			// Add a selection listener
			((Combo)widget).addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					// Set the value of the Entry
					setEntryValue(((Combo) e.widget).getText());
					// Notify any listeners that the selection has changed
					notifyListeners(SWT.Selection, new Event());
				}
			});

			// Add a traverse listener to validate the entry
			// when the user hits Enter or Tab
			widget.addTraverseListener(new TraverseListener() {
				@Override
				public void keyTraversed(TraverseEvent e) {
					if (e.detail == SWT.TRAVERSE_RETURN || e.detail == SWT.TRAVERSE_TAB_NEXT) {

						// Get the entered text and create a File
						String path = ((Combo)widget).getText();
						File file = new File(path);

						// If this is an actual executable that exists, then
						// add it to the File Entry.
						if (file.exists() && file.isFile()) {
							// Check if its an executable
							if (file.canExecute()) {
								List<String> valueList = entry.getAllowedValues();
								valueList.add(file.toURI().toString());

								// Finish setting the allowed values and default
								// value
								entry.setAllowedValues(valueList);

								// If it is executable just add its absolute
								// path
								setEntryValue(file.toURI().toString());
							} else {
								// If its just a File, import it
								IClient client = null;
								try {
									client = IClient.getClient();
								} catch (CoreException e1) {
									e1.printStackTrace();
								}

								if (client != null) {
									client.importFile(file.toURI());
									// Set the entry's value to the new file
									setEntryValue(file.getName());
								}
							}

							notifyListeners(SWT.Selection, new Event());
						}
					}
				}
			});

			// Determine the current value of the entry.
			String currentValue = entry.getValue();
			for (int i = 0; i < allowedValues.size(); i++) {
				String allowedValue = allowedValues.get(i);
				((Combo)widget).add(allowedValue);
				if (allowedValue.equals(currentValue)) {
					((Combo)widget).select(i);
				}
			}
		} else {
			// If the dropDown hasn't been disposed, check if a new AllowedValue
			// has been added to the Entry
			List<String> allowedValues = entry.getAllowedValues();
			List<String> comboValues = Arrays.asList(((Combo)widget).getItems());

			for (int i = 0; i < allowedValues.size(); i++) {
				String allowedValue = allowedValues.get(i);
				// Add any new AllowedValues to the dropDown
				if (!comboValues.contains(allowedValue)) {
					((Combo)widget).add(allowedValue);
				}
			}
		}
	}

	/**
	 * This method creates a Local/Remote file browser button for an Entry with
	 * the Executable AllowedValueType.
	 *
	 */
	private void createExecutableBrowser() {
		boolean redraw = buttons.isEmpty();

		if (redraw) {
			// Create a new button, set the text
			Button browseButton = new Button(this, SWT.PUSH);
			browseButton.setText("Browse...");

			// RemoteFileWidget widg = new RemoteFileWidget(this, SWT.NONE,
			// SWT.NONE, "TITLE", "/");
			// Add an event listener that displays a Directory Dialog prompt
			browseButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {

					// Create a variable for the Executable Value
					String entryValue = null;

					// Create a MessageDialog to get whether the user
					// wants to use a remote or local executable.
					MessageDialog dialog = new MessageDialog(getShell(), "Local or Remote Application", null,
							"Please specify whether your executable is local or remote.",
							MessageDialog.QUESTION_WITH_CANCEL, new String[] { "Local", "Remote" }, 0);

					// Launch the dialot and get the result
					int result = dialog.open();

					// If the user selected Remote
					if (result == 1) {

						// Create a new Remote browser and set its type to
						// File_Browser
						RemoteResourceBrowser browser = new RemoteResourceBrowser(getShell(), SWT.NONE);
						browser.setTitle("Select a remote executable.");
						browser.setType(RemoteResourceBrowser.FILE_BROWSER);

						// Open and make sure they didn't select Cancel
						if (browser.open() != Window.OK) {
							return;
						}

						// Get the selected Resource
						IFileStore fs = browser.getResource();
						// Get the hostname
						String hostName = browser.getConnection().getService(IRemoteConnectionHostService.class)
								.getHostname();

						// Set up the entry value
						URI uri = fs.toURI();
						entryValue = uri.getScheme() + "://" + hostName + uri.getPath();

					} else {
						// If Local, just open up a file browser
						FileDialog fileDialog = new FileDialog(getShell());
						fileDialog.setText("Select an executable to import into ICE");
						String filePath = fileDialog.open();
						if (filePath != null) {
							// Import the files
							File importedFile = new File(filePath);
							entryValue = importedFile.toURI().toString();
						}
					}

					// If we got a valid entryValue, then let's set it.
					if (entryValue != null && !entryValue.isEmpty()) {
						// Create a new content provider with the new file
						// in the allowed values list
						List<String> valueList = entry.getAllowedValues();
						if (!valueList.contains(entryValue)) {
							valueList.add(entryValue);
						}

						// Finish setting the allowed values and default
						// value
						entry.setAllowedValues(valueList);


						// If it is executable just add its absolute path
						setEntryValue(entryValue);

					}

					// Notify any listeners of the selection event
					notifyListeners(SWT.Selection, new Event());

					return;
				}

			});

			// Add the browse button
			buttons.add(browseButton);
		}
	}

}
