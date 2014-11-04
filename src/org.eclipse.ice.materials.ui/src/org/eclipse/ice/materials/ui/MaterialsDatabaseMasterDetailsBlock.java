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

import org.eclipse.ice.materials.IMaterialsDatabase;
import org.eclipse.ice.materials.Material;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
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
