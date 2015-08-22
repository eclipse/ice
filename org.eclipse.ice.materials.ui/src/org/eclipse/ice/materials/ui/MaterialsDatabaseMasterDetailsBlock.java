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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.ice.datastructures.form.Material;
import org.eclipse.ice.datastructures.form.MaterialStack;
import org.eclipse.ice.materials.IMaterialsDatabase;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.MasterDetailsBlock;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
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
	 * The list that holds the materials database information.
	 */
	ArrayList<Material> materials;

	/**
	 * The managed form for the block.
	 */
	IManagedForm mForm;

	/**
	 * The tree viewer for displaying the materials
	 */
	TreeViewer treeViewer;

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
		Section section = toolkit.createSection(parent, ExpandableComposite.TITLE_BAR
				| Section.DESCRIPTION | ExpandableComposite.EXPANDED);
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

		// Create a composite to hold the tree viewer and the filter text
		Composite treeComp = new Composite(sectionClient, SWT.NONE);
		treeComp.setLayout(new GridLayout(1, false));
		treeComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,
				1));

		// Add filter to the Dialog to filter the table results
		final Text filter = new Text(treeComp, SWT.BORDER | SWT.SEARCH);
		filter.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		// Create the tree viewer that shows the contents of the database
		treeViewer = new TreeViewer(treeComp);
		treeViewer.setContentProvider(new MaterialsDatabaseContentProvider());
		treeViewer.setLabelProvider(new MaterialsDatabaseLabelProvider());

		// Create a sorted final list from the database for pulling the database
		// information
		materials = new ArrayList<Material>();
		materials.addAll(materialsDatabase.getMaterials());

		// Sorts the list according to the material compareTo operator
		Collections.sort(materials);

		// Create a copy of the master list for the table to display.
		ArrayList<Object> editableCopy = new ArrayList<Object>();
		for (int i = 0; i < materials.size(); i++) {
			editableCopy.add(materials.get(i));
		}

		// Set the treeviewer input
		treeViewer.setInput(editableCopy);

		// Add a modify listener to filter the table as the user types in the
		// filter.
		filter.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent arg0) {
				List<Material> listFromTree = (List<Material>) treeViewer
						.getInput();
				// Get the filter text
				String filterText = filter.getText().toLowerCase();

				// Checks to see if this is a search for a specific
				// isotope or a element (in which case all isotopes should be
				// shown through the filter).
				boolean useElementName = !((filterText.length() > 0) && (Character
						.isDigit(filterText.charAt(0))));

				// Iterate over the list and pick the items to keep from the
				// filter text.
				int numRemoved = 0;
				for (int i = 0; i < materials.size(); i++) {

					Material mat = materials.get(i);

					String matName = "";
					if (useElementName) {
						matName = mat.getElementalName();
					} else {
						matName = mat.getName();
					}
					// Finally, if the material fits the filter, make sure it is
					// in the list. Otherwise, take it out of the list.
					if (matName.toLowerCase().startsWith(filterText)) {
						// make sure material is in list
						if (!listFromTree.contains(mat)) {
							listFromTree.add(i - numRemoved, mat);
						}

					} else {

						// remove materials that do not fit the search criteria.
						if (listFromTree.contains(mat)) {
							listFromTree.remove(mat);
						}
						numRemoved++;
					}
				}
				// Refresh the tree viewer so that it is repainted
				treeViewer.refresh();
			}
		});

		// Lay out the list
		treeViewer.getTree().setLayout(new GridLayout(1, true));

		// Sets the gridData to grab the available space, but to have only the
		// treeview have the scrolling. This allows for the master tree to
		// scroll without moving the details page out of the viewport.
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		data.widthHint = sectionClient.getClientArea().width;
		data.heightHint = sectionClient.getClientArea().height;
		treeViewer.getTree().setLayoutData(data);

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

		// Add a listener to the add button to open the Add Material Wizard
		addMaterialButton.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// Create a wizard dialog to hold the AddMaterialWizard that
				// will be
				// used to create new materials.
				IWorkbenchWindow window = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow();
				AddMaterialWizard addMaterialWizard = new AddMaterialWizard(
						window, materialsDatabase);
				addMaterialWizard.setWindowTitle("Create a new material");
				WizardDialog addMaterialDialog = new WizardDialog(window
						.getShell(), addMaterialWizard);

				// Get the new material to add
				if (addMaterialDialog.open() == Window.OK) {
					Material newMaterial = addMaterialWizard.getMaterial();

					
					//materials.getReadWriteLock().writeLock().lock();
					try{
						materials.add(newMaterial);
						Collections.sort(materials);
					} catch(Exception ex){
						ex.printStackTrace();
						//materials.getReadWriteLock().writeLock().unlock();
					}
					
					// Add to database
					materialsDatabase.addMaterial(newMaterial);
					
					// Add to tree's list
					ArrayList<Material> listFromTree = (ArrayList<Material>) treeViewer
							.getInput();
					listFromTree.add(newMaterial);
					Collections.sort(listFromTree);
					treeViewer.refresh();
				}

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// Nothing TODO
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
		String msg = "Are you sure you want to delete this material(s)?";
		String[] labels = { "OK", "Cancel" };
		final MessageDialog deletionDialog = new MessageDialog(
				parent.getShell(), title, null, msg, MessageDialog.WARNING,
				labels, 0);
		SelectionListener deletionListener = new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int index = deletionDialog.open();
				// If the user presses OK
				if (index == 0) {
					// Get the currently selected materials
					IStructuredSelection selection = (IStructuredSelection) treeViewer
							.getSelection();
					Iterator it = selection.iterator();

					// Get the model from the treeViewer
					List<Material> listFromTree = (List<Material>) treeViewer
							.getInput();

					// Remove each selected material
					while (it.hasNext()) {
						Material toDelete = (Material) it.next();
						// Remove the material from the user's database
						materialsDatabase.deleteMaterial(toDelete);

						//materials.getReadWriteLock().writeLock().lock();
						try {
							// Remove from the master materials list
							materials.remove(toDelete);
						} catch(Exception ex) {
							ex.printStackTrace();
							//materials.getReadWriteLock().writeLock().unlock();
						}
						// Remove the material from the tree viewer
						listFromTree.remove(toDelete);
					}

					// Update the treeViwer so that it repaints and shows the
					// changes on screen.
					treeViewer.refresh();
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// nothing TODO
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
				if (index == 0) {
					materialsDatabase.restoreDefaults();
					// Create a sorted final list from the database for pulling
					// the database information
					List newList = materialsDatabase.getMaterials();
					//materials.getReadWriteLock().writeLock().lock();
					try {
						materials.clear();
						materials.addAll(newList);
						// Sorts the list according to the material compareTo
						// operator
						Collections.sort(materials);
					} catch(Exception ex) {
						ex.printStackTrace();
						//materials.getReadWriteLock().writeLock().unlock();
					}

					// Get the model from the treeViewer
					List listFromTree = (List<Object>) treeViewer.getInput();
					// Refresh the list from the reset materials database
					listFromTree.clear();
					for (int i = 0; i < materials.size(); i++) {
						listFromTree.add(materials.get(i));
					}

					// Update the treeViwer so that it repaints and shows the
					// changes on screen.
					treeViewer.refresh();
				}
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
		MaterialDetailsPage detailsPage = new MaterialDetailsPage(materialsDatabase);
		MaterialDetailsPage stackDetailsPage = new MaterialDetailsPage(materialsDatabase);
		detailsPart.registerPage(Material.class, detailsPage);
		detailsPart.registerPage(MaterialStack.class, stackDetailsPage);

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
