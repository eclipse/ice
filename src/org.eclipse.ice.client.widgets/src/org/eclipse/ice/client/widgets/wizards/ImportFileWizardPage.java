/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.client.widgets.wizards;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * This class provides the main page for the {@link ImportFileWizard}. It
 * includes a <code>Label</code>, a read-only <code>Text</code>, and a browse
 * <code>Button</code> that lets the user select one or more files for import
 * into their ICE workspace.
 * 
 * @author Jay Jay Billings, Jordan
 * 
 */
public class ImportFileWizardPage extends WizardPage {

	/**
	 * The files selected in the <code>FileDialog</code> launched by the browse
	 * button.
	 */
	protected final List<String> selectedFileNames;

	/**
	 * The filter path for the selected files.
	 */
	private String filterPath;

	/**
	 * The style bit used for the browse button's <code>FileDialog</code>.
	 */
	protected int fileDialogStyle;

	/**
	 * The <code>Composite</code> that contains all of the widgets on the page.
	 */
	protected Composite wizardPageComposite;

	/**
	 * The default constructor.
	 * 
	 * @param pageName
	 *            The name of the page.
	 */
	public ImportFileWizardPage(String pageName) {
		super(pageName);

		setTitle("ICE File Import Wizard");
		setDescription("Import an input file into ICE.");

		// The default behavior is to allow multiple files to be selected.
		fileDialogStyle = SWT.MULTI;

		filterPath = "";
		selectedFileNames = new ArrayList<String>();

		return;
	}

	/**
	 * Gets the names of all selected files.
	 * 
	 * @return A list containing the selected file names.
	 */
	public List<String> getSelectedFiles() {
		return new ArrayList<String>(selectedFileNames);
	}

	/**
	 * This operation returns the filter path for the selected file (the path to
	 * the directory that contains the selected files).
	 * 
	 * @return The filter path.
	 */
	public String getFilterPath() {
		return filterPath;
	}

	/**
	 * This operation returns true if one or more files have been selected.
	 * 
	 * @return True if a file has been selected, false otherwise.
	 */
	protected boolean checkSelection() {
		return !selectedFileNames.isEmpty() && !filterPath.isEmpty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets
	 * .Composite)
	 */
	@Override
	public void createControl(Composite parent) {

		if (parent != null) {

			GridData gridData;

			wizardPageComposite = new Composite(parent, SWT.NONE);
			wizardPageComposite.setLayout(new GridLayout(2, false));

			// Create the text bar label
			Label fileLabel = new Label(wizardPageComposite, SWT.NONE);
			fileLabel.setText("Selected File:");
			gridData = new GridData(SWT.LEFT, SWT.END, false, false);
			gridData.horizontalSpan = 2;
			fileLabel.setLayoutData(gridData);

			// Create the Text.
			final Text fileText = new Text(wizardPageComposite, SWT.NONE
					| SWT.BORDER);
			fileText.setEditable(false);
			fileText.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true,
					true));

			// Create the browse button.
			Button browseButton = new Button(wizardPageComposite, SWT.NONE);
			browseButton.setLayoutData(new GridData(SWT.RIGHT, SWT.BEGINNING,
					false, true));
			browseButton.setText("Browse...");
			// Add a listener to open a file dialog when the button is clicked
			final Shell shell = parent.getShell();
			browseButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					// Create the JFace FileDialog
					FileDialog fileDialog = new FileDialog(shell,
							fileDialogStyle);
					fileDialog.setText("Select a file to import into ICE");
					fileDialog.open();

					// Store the selected file names.
					selectedFileNames.clear();
					for (String fileName : fileDialog.getFileNames()) {
						selectedFileNames.add(fileName);
					}

					// Get the filter path and store it
					filterPath = fileDialog.getFilterPath();

					// Update the file text if something has been selected
					int numFiles = selectedFileNames.size();
					// Print out the full path for a single selected file.
					if (numFiles == 1) {
						fileText.setText(filterPath
								+ System.getProperty("file.separator")
								+ selectedFileNames.get(0));
					}
					// Print out a list of the selected files if multiple files
					// are selected.
					else if (numFiles > 1) {
						String text = "\"" + selectedFileNames.get(0) + "\"";
						for (int i = 1; i < selectedFileNames.size(); i++) {
							text += ", \"" + selectedFileNames.get(i) + "\"";
						}
						fileText.setText(text);
					}

					// Set the finish button if both the item and file are
					// selected
					setPageComplete(checkSelection());

					return;
				}
			});

			// Set the control. This is required of JFace Wizards.
			setControl(wizardPageComposite);
			// Disable the finished condition to start.
			setPageComplete(false);
		}

		return;
	}
}
