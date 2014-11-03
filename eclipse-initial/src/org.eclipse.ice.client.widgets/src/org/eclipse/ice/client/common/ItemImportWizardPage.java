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
package org.eclipse.ice.client.common;

import java.util.ArrayList;

import org.eclipse.ice.client.common.internal.ClientHolder;
import org.eclipse.ice.iclient.IClient;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;

/**
 * This class is a wizard page that creates the necessary widgets to properly
 * guide a user through the Item import process.
 * 
 * @author Jay Jay Billings
 * 
 */
public class ItemImportWizardPage extends WizardPage {

	/**
	 * The name of the Item that was selected in the list.
	 */
	private String selectedItemName = "";

	/**
	 * The name of the file that has been selected.
	 */
	private String selectedFileName = "";

	/**
	 * The filter path for the selected file.
	 */
	private String filterPath = "";

	/**
	 * The parent composite of the wizard
	 */
	private Composite parentComposite;

	/**
	 * The constructor
	 * 
	 * @param pageName
	 */
	protected ItemImportWizardPage(String pageName) {
		super(pageName);

		setTitle("ICE Item Import Wizard");
		setDescription("Import an input file into an ICE item.");
	}

	/**
	 * This operation returns the selected Item name.
	 * 
	 * @return the selected Item name
	 */
	public String getSelectedItem() {
		return selectedItemName;
	}

	/**
	 * This operation returns the selected file name.
	 * 
	 * @return the selected file name
	 */
	public String getSelectedFile() {
		return selectedFileName;
	}

	/**
	 * This operation returns the filter path for the selected file.
	 * 
	 * @return The filter path
	 */
	public String getFilterPath() {
		return filterPath;
	}

	/**
	 * This operation returns true if a file and an Item have been selected.
	 * 
	 * @return True if a file and an Item have been selected, false otherwise.
	 */
	private boolean checkSelections() {
		return (!selectedItemName.isEmpty())
				&& (!selectedFileName.isEmpty() && (!filterPath.isEmpty()));
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

		// Set the parent reference
		parentComposite = parent;

		// Get the client
		IClient client = ClientHolder.getClient();

		// Only create the wizard if the client is available
		if (client != null) {
			// Create the composite for file selection pieces
			Composite fileSelectionComposite = new Composite(parentComposite,
					SWT.NONE);
			// Set its layout
			GridLayout layout = new GridLayout(1, true);
			fileSelectionComposite.setLayout(layout);
			// Set its layout data
			GridData data = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
			fileSelectionComposite.setLayoutData(data);

			// Create the text bar label
			Label fileLabel = new Label(fileSelectionComposite, SWT.NONE);
			fileLabel.setText("Selected File:");

			// Create the interior composite for the text bar and buttons
			Composite textAndBrowseComposite = new Composite(
					fileSelectionComposite, SWT.FILL);
			// Set its layout
			textAndBrowseComposite.setLayout(new GridLayout(2, false));
			// Set its layout data
			data = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
			textAndBrowseComposite.setLayoutData(data);

			// Create the text bar that will show the file name
			final Text fileText = new Text(textAndBrowseComposite, SWT.NONE
					| SWT.BORDER);
			fileText.setEditable(false);
			data = new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1);
			fileText.setLayoutData(data);

			// Create the file browse button
			Button browseButton = new Button(textAndBrowseComposite, SWT.NONE);
			data = new GridData(SWT.RIGHT, SWT.CENTER, false, true, 1, 1);
			browseButton.setLayoutData(data);
			browseButton.setText("Browse");
			// Add a listener to open a file dialog when the button is clicked
			browseButton.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					// Create the JFace FileDialog
					FileDialog fileDialog = new FileDialog(parentComposite
							.getShell(), SWT.SINGLE);
					fileDialog.setText("Select a file to import into ICE");
					fileDialog.open();

					// Get the filename and store it
					selectedFileName = fileDialog.getFileName();
					// Get the filter path and store it
					filterPath = fileDialog.getFilterPath();

					// Update the file text if something has been selected
					if (!selectedFileName.isEmpty() && !filterPath.isEmpty()) {
						fileText.setText(filterPath
								+ System.getProperty("file.separator")
								+ selectedFileName);
					}

					// Set the finish button if both the item and file are
					// selected
					ItemImportWizardPage.this
							.setPageComplete(checkSelections());

					return;
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					// Nothing to do
				}
			});

			// Get the list of available Items from the client
			final ArrayList<String> itemTypeList = client
					.getAvailableItemTypes();
			// Create the item selection label
			Label itemLabel = new Label(fileSelectionComposite, SWT.NONE);
			itemLabel.setText("Please select an item "
					+ "that this file represents");
			// Add the list for selecting an Item
			ListViewer itemListViewer = new ListViewer(fileSelectionComposite,
					SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
			// Set the layout data so that it fills the space
			data = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
			itemListViewer.getControl().setLayoutData(data);
			// Add the label provider for the list viewer
			itemListViewer.setLabelProvider(new LabelProvider() {
				public String getText(Object element) {
					return (String) element;
				}
			});
			// Add the content provider for the list viewer
			itemListViewer.setContentProvider(new IStructuredContentProvider() {

				public void inputChanged(Viewer viewer, Object oldInput,
						Object newInput) {
					// Nothing to do
				}

				public void dispose() {
					// Nothing to do
				}

				public Object[] getElements(Object inputElement) {
					return ((ArrayList<?>) inputElement).toArray();
				}
			});
			// Create the selection listener
			itemListViewer
					.addSelectionChangedListener(new ISelectionChangedListener() {
						public void selectionChanged(SelectionChangedEvent event) {
							// Get and store the selection
							String selection = (String) ((IStructuredSelection) event
									.getSelection()).getFirstElement();
							if (selection != null) {
								selectedItemName = selection;
							}
							// Validate the file and Item selection to enable
							// the finish button
							ItemImportWizardPage.this
									.setPageComplete(checkSelections());
						}
					});
			// Set the input to the list from the client
			itemListViewer.setInput(itemTypeList);

			// Add a double-click listener to the ListViewer. If the file was
			// already selected, then a double-click can advance or finish the
			// wizard.
			itemListViewer.addDoubleClickListener(new IDoubleClickListener() {
				public void doubleClick(DoubleClickEvent event) {

					// If the page is complete, we can try to finish the wizard.
					if (isPageComplete()) {
						// Get the wizard and its container.
						IWizard wizard = getWizard();
						IWizardContainer container = wizard.getContainer();
						// If the container is a WizardDialog, we can try to
						// finish the wizard and close the dialog.
						if (container instanceof WizardDialog) {
							if (wizard.performFinish()) {
								((WizardDialog) container).close();
							}
						}
						// Otherwise, we can try to advance to the next page if
						// one exists.
						else {
							// Get the next page.
							IWizardPage nextPage = wizard
									.getNextPage(ItemImportWizardPage.this);
							// If it exists, move to it.
							if (nextPage != null) {
								container.showPage(nextPage);
							}
						}
					}

					return;
				}
			});

			// Set the control
			setControl(fileSelectionComposite);
			// Disable the finished condition to start
			setPageComplete(false);

			// Otherwise throw an error
		} else {
			MessageBox errorMessage = new MessageBox(parent.getShell(), ERROR);
			errorMessage.setMessage("The ICE Client is not available. "
					+ "Please file a bug report.");
		}

		return;
	}

}
