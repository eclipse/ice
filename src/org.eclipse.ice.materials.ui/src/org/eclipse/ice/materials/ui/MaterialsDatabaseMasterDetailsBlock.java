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
package org.eclipse.ice.materials.ui;

import org.eclipse.ice.datastructures.form.Material;
import org.eclipse.ice.materials.IMaterialsDatabase;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.MasterDetailsBlock;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

/**
 * This class is a MasterDetailsBlock that creates a list of materials in the
 * Master block. It delegate the display of the material properties to the
 * Details block.
 * 
 * @author Jay Jay Billings
 * 
 */
public class MaterialsDatabaseMasterDetailsBlock extends MasterDetailsBlock {

	/**
	 * The IMaterialsDatabase that is rendered by this block.
	 */
	IMaterialsDatabase materialsDatabase;

	/**
	 * The managed form for the block.
	 */
	IManagedForm mForm;

	/**
	 * The constructor
	 * 
	 * @param database
	 *            The materials database that should be rendered and
	 *            manipulated.
	 */
	public MaterialsDatabaseMasterDetailsBlock(IMaterialsDatabase database) {
		// Store the database
		materialsDatabase = database;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.forms.MasterDetailsBlock#createMasterPart(org.eclipse.
	 * ui.forms.IManagedForm, org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createMasterPart(IManagedForm managedForm, Composite parent) {

		// Put in an overall section to hold the master block. It is used to
		// notify the workbench of selection events from the try and to look
		// nice.
		mForm = managedForm;
		FormToolkit toolkit = mForm.getToolkit();
		Section section = toolkit.createSection(parent, Section.TITLE_BAR
				| Section.DESCRIPTION | Section.EXPANDED);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		section.setLayoutData(gridData);
		section.setLayout(new GridLayout(1, true));
		section.setText("Materials Tree");
		section.setDescription("Select a material to edit");

		// Create the area in which the block will be rendered - the
		// "section client"
		Composite sectionClient = toolkit.createComposite(section);
		// Configure the layout to be greedy.
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginWidth = 2;
		layout.marginHeight = 2;
		// Set the layout and the layout data
		sectionClient.setLayout(layout);
		sectionClient.setLayoutData(new GridData(GridData.FILL_BOTH));
		// Finally tell the section about its client
		section.setClient(sectionClient);

		// Create the SectionPart that holds the section and is registered with
		// the managed form to publish events.
		final SectionPart sectionPart = new SectionPart(section);
		mForm.addPart(sectionPart);

		// Create the tree viewer that shows the contents of the database
		TreeViewer treeViewer = new TreeViewer(sectionClient);
		treeViewer.setContentProvider(new MaterialsDatabaseContentProvider());
		treeViewer.setLabelProvider(new MaterialsDatabaseLabelProvider());

		// Set the input and layout information on the treeViewer
		treeViewer.setInput(materialsDatabase.getMaterials());
		treeViewer.getTree().setLayout(new GridLayout(1, true));
		treeViewer.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));

		// Add a listener to notify the managed form when a selection is made so
		// that its details can be presented.
		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				mForm.fireSelectionChanged(sectionPart, event.getSelection());
			}
		});

		// Add a composite for holding the Add and Delete buttons for adding
		// or removing materials
		Composite buttonComposite = new Composite(sectionClient, SWT.NONE);
		buttonComposite.setLayout(new GridLayout(1, false));
		buttonComposite.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false,
				true, 1, 1));

		// Create the Add button
		Button addMaterialButton = new Button(buttonComposite, SWT.PUSH);
		addMaterialButton.setText("Add");
		// Create a wizard dialog to hold the AddMaterialWizard that will be used to create new materials.
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		AddMaterialWizard addMaterialWizard = new AddMaterialWizard(window);
		addMaterialWizard.setWindowTitle("Create a new material");
		final WizardDialog addMaterialDialog = new WizardDialog(window.getShell(), addMaterialWizard);
		// Add a listener to the add button to open the Add Material Wizard
		addMaterialButton.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// Just pop open the dialog
				addMaterialDialog.create();
				addMaterialDialog.open();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// Just pop open the dialog
				addMaterialDialog.create();
				addMaterialDialog.open();				
			}
		});

		// Create the Delete button
		Button deleteMaterialButton = new Button(buttonComposite, SWT.PUSH);
		deleteMaterialButton.setText("Delete");
		// Create a listener that will throw up a warning message before
		// performing the actual deletion from the database.The message is just
		// a simple JFace message dialog that is opened when either button
		// is pressed.
		String title = "Confirm Deletion";
		String msg = "Are you sure you want to delete this material?";
		String[] labels = { "OK", "Cancel" };
		final MessageDialog deletionDialog = new MessageDialog(
				parent.getShell(), title, null, msg, MessageDialog.WARNING,
				labels, 0);
		SelectionListener deletionListener = new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int index = deletionDialog.open();
				// Do the deletion - NOT YET IMPLEMENTED!
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				int index = deletionDialog.open();
				// Do the deletion - NOT YET IMPLEMENTED!
			}
		};
		deleteMaterialButton.addSelectionListener(deletionListener);

		// Create the Restore Defaults button
		Button restoreMaterialButton = new Button(buttonComposite, SWT.PUSH);
		restoreMaterialButton.setText("Reset");
		restoreMaterialButton.setToolTipText("Restore the database to "
				+ "its initial state.");
		// Create a listener that will throw up a warning message before
		// performing the actual deletion from the database.The message is just
		// a simple JFace message dialog that is opened when either button
		// is pressed.
		title = "Confirm Restoration to Initial State";
		msg = "Are you sure you want restore the "
				+ "database to its initial configuration? "
				+ "This will erase ALL of your changes "
				+ "and updates, and it cannot be undone.";
		final MessageDialog restoreDialog = new MessageDialog(
				parent.getShell(), title, null, msg, MessageDialog.WARNING,
				labels, 0);
		SelectionListener restoreListener = new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int index = restoreDialog.open();
				// Do the restore - NOT YET IMPLEMENTED!
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				int index = restoreDialog.open();
				// Do the restore - NOT YET IMPLEMENTED!
			}
		};
		restoreMaterialButton.addSelectionListener(restoreListener);

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.forms.MasterDetailsBlock#registerPages(org.eclipse.ui.
	 * forms.DetailsPart)
	 */
	@Override
	protected void registerPages(DetailsPart detailsPart) {
		detailsPart.registerPage(Material.class, new MaterialDetailsPage(
				materialsDatabase));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.forms.MasterDetailsBlock#createToolBarActions(org.eclipse
	 * .ui.forms.IManagedForm)
	 */
	@Override
	protected void createToolBarActions(IManagedForm managedForm) {
		// TODO Auto-generated method stub

	}

}
