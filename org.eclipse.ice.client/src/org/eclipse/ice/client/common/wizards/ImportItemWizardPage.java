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
package org.eclipse.ice.client.common.wizards;

import java.util.ArrayList;
import java.util.Collections;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
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
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides the main wizard page for importing files into ICE for
 * specific {@code Item}s. The primary difference between the
 * {@link ImportFileWizardPage} is that this class additionally provides a
 * {@link ListViewer} that displays the available {@code Item} types. This page
 * also does not allow multiple files to be selected.
 * 
 * @author Jay Jay Billings, Jordan
 * 
 */
public class ImportItemWizardPage extends ImportFileWizardPage {

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory.getLogger(ImportItemWizardPage.class);

	/**
	 * The name of the {@code Item} that was selected from the list of available
	 * {@code Item}s.
	 */
	private String selectedItemType;

	/**
	 * Reference to the name of the project to 
	 * use in this Import wizard
	 */
	private String selectedProject;

	private Combo projectCombo;
	
	/**
	 * The default constructor.
	 * 
	 * @param pageName
	 *            The name of the page.
	 */
	public ImportItemWizardPage(String pageName, String currentProject) {
		super(pageName);

		setTitle("ICE Item Import Wizard");
		setDescription("Import an input file into an ICE item.");

		// Override the default behavior to allow only one file to be selected
		// at a time.
		fileDialogStyle = SWT.SINGLE;

		selectedItemType = "";

		selectedProject = currentProject;

		return;
	}

	/**
	 * Gets the name of the selected file.
	 * 
	 * @return The name of the file selected via the browse button.
	 */
	public String getSelectedFile() {
		return (selectedFileNames.isEmpty() ? null : selectedFileNames.get(0));
	}

	/**
	 * Gets the type of {@code Item} that was selected from the list of
	 * available {@code Item}s.
	 * 
	 * @return The name of the selected type of item, for instance a specific
	 *         model builder or launcher.
	 */
	public String getSelectedItem() {
		return selectedItemType;
	}

	/**
	 * Return the name of the Project specified by the user
	 * 
	 * @return
	 */
	public String getSelectedProject() {
		selectedProject = projectCombo.getItems()[projectCombo.getSelectionIndex()];
		return selectedProject;
	}

	/**
	 * Override the default behavior to check that the {@link #selectedItemType}
	 * is set.
	 */
	@Override
	protected boolean checkSelection() {
		return super.checkSelection() && !selectedItemType.isEmpty();
	}

	/**
	 * Override the default behavior to add a <code>ListViewer</code> with the
	 * available item types.
	 */
	@Override
	public void createControl(Composite parent) {

		// Get the client.
		IClient client = null;
		try {
			client = IClient.getClient();
		} catch (CoreException e) {
			logger.error("Error getting IClient instance", e);
		}

		if (client != null) {
			// Get the list of available Items from the client
			final ArrayList<String> itemTypes = client.getAvailableItemTypes();
			// Sort the list so that items are displayed lexographically.
			Collections.sort(itemTypes);

			// Create the Text and browse Button.
			super.createControl(parent);

			if (parent != null) {
				GridData gridData;

				// Create a label for the Project Combo
				Label label = new Label(wizardPageComposite, SWT.NONE);
				label.setText("Please select the Project where this Item should be created");
				
				// Create the Combo
				projectCombo = new Combo(wizardPageComposite, SWT.READ_ONLY);
				gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
				gridData.horizontalSpan = 2;
				projectCombo.setLayoutData(gridData);
				
				// Get a list of all Project Names
				ArrayList<String> projectNames = new ArrayList<String>();
				for (IProject p : ResourcesPlugin.getWorkspace().getRoot().getProjects()) {
					projectNames.add(p.getName());
				}
				
				// Set the possible 
				projectCombo.setItems(projectNames.toArray(new String[projectNames.size()]));
				if (selectedProject != null) {
					projectCombo.select(projectNames.indexOf(selectedProject));
				} else {
					projectCombo.select(0);
				}

				// Create a label above the list.
				label = new Label(wizardPageComposite, SWT.NONE);
				label.setText("Please select an Item that this file represents");
				gridData = new GridData(SWT.LEFT, SWT.END, false, false);
				gridData.horizontalSpan = 2;
				label.setLayoutData(gridData);

				// Create the list.
				ListViewer itemList = new ListViewer(wizardPageComposite,
						SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
				gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
				gridData.horizontalSpan = 2;
				itemList.getControl().setLayoutData(gridData);

				// Add the label provider for the list viewer
				itemList.setLabelProvider(new LabelProvider() {
					@Override
					public String getText(Object element) {
						return (String) element;
					}
				});

				// Add the content provider for the list viewer
				itemList.setContentProvider(new IStructuredContentProvider() {
					@Override
					public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
						// Nothing to do
					}

					@Override
					public void dispose() {
						// Nothing to do
					}

					@Override
					public Object[] getElements(Object inputElement) {
						return ((ArrayList<?>) inputElement).toArray();
					}
				});

				// Create the selection listener
				itemList.addSelectionChangedListener(new ISelectionChangedListener() {
					@Override
					public void selectionChanged(SelectionChangedEvent event) {
						// Get and store the selection
						String selection = (String) ((IStructuredSelection) event.getSelection()).getFirstElement();
						if (selection != null) {
							selectedItemType = selection;
						}
						// Validate the file and Item selection to enable
						// the finish button
						ImportItemWizardPage.this.setPageComplete(checkSelection());
					}
				});
				// Set the input to the list from the client
				itemList.setInput(itemTypes);

				// Add a double-click listener to the ListViewer. If the file
				// was already selected, then a double-click can advance or
				// finish the wizard.
				itemList.addDoubleClickListener(new IDoubleClickListener() {
					@Override
					public void doubleClick(DoubleClickEvent event) {

						// If the page is complete, we can try to finish the
						// wizard.
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
							// Otherwise, we can try to advance to the next page
							// if one exists.
							else {
								// Get the next page.
								IWizardPage nextPage = wizard.getNextPage(ImportItemWizardPage.this);
								// If it exists, move to it.
								if (nextPage != null) {
									container.showPage(nextPage);
								}
							}
						}

						return;
					}
				});

				// Force the parent Composite to repaint.
				parent.layout();
			}

		} else {
			MessageBox errorMessage = new MessageBox(parent.getShell(), ERROR);
			errorMessage.setMessage("The ICE Client is not available. " + "Please file a bug report.");
		}

		return;
	}
}
