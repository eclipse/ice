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

import java.util.Collections;
import java.util.List;

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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;

/**
 * This class provides the main page for the {@link NewItemWizard}. It includes
 * a <code>ListViewer</code> of available <code>Item</code> types.
 *
 * @author Jay Jay Billings, Jordan
 *
 */
public class NewItemWizardPage extends WizardPage {

	/**
	 * The name of the Item that was selected in the list.
	 */
	private String selectedItemType = "";

	/**
	 * The parent composite of the wizard
	 */
	private Composite parentComposite;

	/**
	 * The default constructor.
	 *
	 * @param pageName
	 *            The name of the page.
	 */
	public NewItemWizardPage(String pageName) {
		super(pageName);

		setTitle("Item Selector");
		setDescription("Selection wizard for choosing an Item to create");
	}

	/**
	 * This operation returns the selected Item name.
	 *
	 * @return the selected Item name
	 */
	public String getSelectedItem() {
		return selectedItemType;
	}

	/**
	 * This operation returns true if an Item has been selected.
	 *
	 * @return True if an Item has been selected, false otherwise.
	 */
	private boolean checkSelection() {
		return !selectedItemType.isEmpty();
	}

	/**
	 * This operation creates the view that shows the list of Items that can be
	 * created by the user.
	 */
	@Override
	public void createControl(Composite parent) {

		// Set the parent reference
		parentComposite = parent;

		// Get the client
		IClient client = ClientHolder.getClient();

		// Create the composite for file selection pieces
		Composite itemSelectionComposite = new Composite(parentComposite,
				SWT.NONE);
		// Set its layout
		GridLayout layout = new GridLayout(1, true);
		itemSelectionComposite.setLayout(layout);
		// Set its layout data
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		itemSelectionComposite.setLayoutData(data);

		// Only create the wizard if the client is available
		if (client != null) {
			// Draw the list of Items and present the selection wizard.
			drawWizard(itemSelectionComposite, client);
		} else {
			// Otherwise throw an error
			MessageBox errorMessage = new MessageBox(parent.getShell(), ERROR);
			errorMessage.setMessage("The ICE Client is not available. "
					+ "Please file a bug report.");
			errorMessage.open();
		}

		// Set the control
		setControl(itemSelectionComposite);
		// Disable the finished condition to start
		setPageComplete(false);

		return;

	}

	/**
	 * This operation draws the wizard on the screen.
	 *
	 * @param itemSelectionComposite
	 *            The composite that should hold the contents of the wizard
	 * @param client
	 *            The ICE Client
	 */
	private void drawWizard(Composite itemSelectionComposite, IClient client) {

		// Get the list of available Items from the client
		final List<String> itemTypeList = client.getAvailableItemTypes();
		// Sort the list so that items are displayed lexographically.
		Collections.sort(itemTypeList);

		// Create the item selection label
		Label itemLabel = new Label(itemSelectionComposite, SWT.NONE);
		itemLabel.setText("Please select an Item type.");
		// Add the list for selecting an Item
		ListViewer itemListViewer = new ListViewer(itemSelectionComposite,
				SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		// Set the layout data so that it fills the space
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		itemListViewer.getControl().setLayoutData(data);
		// Add the label provider for the list viewer
		itemListViewer.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				return (String) element;
			}
		});
		// Add the content provider for the list viewer
		itemListViewer.setContentProvider(new IStructuredContentProvider() {

			@Override
			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
				// Nothing to do
			}

			@Override
			public void dispose() {
				// Nothing to do
			}

			@Override
			public Object[] getElements(Object inputElement) {
				return ((List<?>) inputElement).toArray();
			}
		});
		// Create the selection listener
		itemListViewer
				.addSelectionChangedListener(new ISelectionChangedListener() {
					@Override
					public void selectionChanged(SelectionChangedEvent event) {
						// Get and store the selection
						String selection = (String) ((IStructuredSelection) event
								.getSelection()).getFirstElement();
						if (selection != null) {
							selectedItemType = selection;
						}
						// Validate the file and Item selection to enable
						// the finish button
						NewItemWizardPage.this
								.setPageComplete(checkSelection());
					}
				});
		// Set the input to the list from the client
		itemListViewer.setInput(itemTypeList);

		// Add a double-click listener to the ListViewer. If the file was
		// already selected, then a double-click can advance or finish the
		// wizard.
		itemListViewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
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
								.getNextPage(NewItemWizardPage.this);
						// If it exists, move to it.
						if (nextPage != null) {
							container.showPage(nextPage);
						}
					}
				}

				return;
			}
		});

	}

}