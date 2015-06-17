/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings
 *******************************************************************************/
package org.eclipse.ice.materials.ui;

import java.util.List;

import org.eclipse.ice.datastructures.form.Material;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Table;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * This class provides all of the necessary UI for defining a new material and a
 * configuring its stoichiometry.
 * 
 * It was developed using the SWT WindowBuilder.
 * 
 * @author Jay Jay Billings
 * 
 */
public class AddMaterialWizardPage extends WizardPage {

	/**
	 * The table that holds the stoichiometric information for the new material.
	 */
	private Table stoichiometryTable;

	/**
	 * The text widget that holds the name for the new material.
	 */
	private Text nameText;

	/**
	 * The text widget that holds the density for the new material.
	 */
	private Text densityText;

	/**
	 * The constructor
	 * 
	 * @param pageName
	 *            the page name
	 * @wbp.parser.constructor
	 */
	public AddMaterialWizardPage(String pageName) {
		super(pageName);
	}

	/**
	 * An alternative constructor for creating the wizard page
	 * 
	 * @param pageName
	 *            the name of the page
	 * @param title
	 *            the title to show in the title bar
	 * @param titleImage
	 *            an image to go along with the title
	 */
	public AddMaterialWizardPage(String pageName, String title,
			ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}

	/**
	 * Returns if the material page's necessary fields are complete.
	 * 
	 * @return Returns true if the name field and the density field have valid
	 *         values.
	 */
	@Override
	public boolean isPageComplete() {
		boolean hasName = nameText.getText().length() > 0;
		boolean density;
		try {
			Double d = Double.parseDouble(densityText.getText());
			density = true;
		} catch (Exception e) {
			density = false;
		}
		return hasName && density;
	}

	/**
	 * Gets the material created by the fields on this page.
	 * 
	 * @return A new material with the set name, density and stoichiometry
	 *         denoted on the page.
	 */
	public Material getMaterial() {
		// Creates the new material
		Material material = new Material();
		// Set the name
		material.setName(nameText.getText());
		// Set the density
		material.setProperty("Dens (g/cm3)",
				Double.parseDouble(densityText.getText()));

		/**
		 * // Set the material's components from the stoichiometery
		 * List<Material> components = (List<Material>) this.stoichiometryTable
		 * .getData(); // See if there are components to add
		 * if(!components.isEmpty()){ for (Material m : components) {
		 * material.addComponent(m); } }
		 */
		return material;
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

		setMessage("Provide a name, density and "
				+ "stoichiometry for the new material.");

		// FIXME! - Add more documentation to this operation once the layout and
		// actions are finalized. ~JJB 20141230 11:44

		// Create the base container for the wizard
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout(1, true));

		// Add the composite to hold the name and density blocks
		Composite nameAndDensityComposite = new Composite(container, SWT.None);
		nameAndDensityComposite.setLayout(new GridLayout(2, true));
		nameAndDensityComposite.setLayoutData(new GridData(SWT.FILL,
				SWT.CENTER, true, false, 1, 1));

		Composite nameComposite = new Composite(nameAndDensityComposite,
				SWT.NONE);
		GridData nameCompositeGridData = new GridData(SWT.FILL, SWT.FILL, true,
				false, 1, 1);
		nameCompositeGridData.widthHint = 1;
		nameCompositeGridData.minimumWidth = 1;
		nameComposite.setLayoutData(nameCompositeGridData);
		nameComposite.setLayout(new GridLayout(2, false));

		Label nameLabel = new Label(nameComposite, SWT.NONE);
		nameLabel.setText("Name:");

		nameText = new Text(nameComposite, SWT.BORDER);
		nameText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1,
				1));

		Composite densityComposite = new Composite(nameAndDensityComposite,
				SWT.NONE);
		densityComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false, 1, 1));
		densityComposite.setLayout(new GridLayout(3, false));

		Label densityLabel = new Label(densityComposite, SWT.NONE);
		densityLabel.setText("Density:");

		densityText = new Text(densityComposite, SWT.BORDER);
		densityText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		Label densityUnitsLabel = new Label(densityComposite, SWT.NONE);
		densityUnitsLabel.setText("g/cm3");

		// Add a modify listener to update the buttons in the wizard if the text
		// fields change.
		ModifyListener listener = new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent arg0) {
				getWizard().getContainer().updateButtons();
			}

		};

		// Add the listener
		nameText.addModifyListener(listener);
		densityText.addModifyListener(listener);

		Composite stoichiometryComposite = new Composite(container, SWT.NONE);
		stoichiometryComposite.setLayout(new GridLayout(2, false));
		stoichiometryComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				true, true, 1, 1));

		Composite stoichiometryTableComposite = new Composite(
				stoichiometryComposite, SWT.NONE);
		stoichiometryTableComposite.setLayout(new GridLayout(1, false));
		stoichiometryTableComposite.setLayoutData(new GridData(SWT.FILL,
				SWT.FILL, true, true, 1, 1));

		TableViewer stoichiometryTableViewer = new TableViewer(
				stoichiometryTableComposite, SWT.BORDER | SWT.FULL_SELECTION);
		stoichiometryTable = stoichiometryTableViewer.getTable();
		stoichiometryTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));

		Composite stoichiometryButtonComposite = new Composite(
				stoichiometryComposite, SWT.NONE);
		stoichiometryButtonComposite.setLayout(new GridLayout(1, false));
		stoichiometryButtonComposite.setLayoutData(new GridData(SWT.RIGHT,
				SWT.FILL, false, true, 1, 1));

		Button addMaterialButton = new Button(stoichiometryButtonComposite,
				SWT.PUSH);
		addMaterialButton.setText("Add");

		Button deleteMaterialButton = new Button(stoichiometryButtonComposite,
				SWT.PUSH);
		deleteMaterialButton.setText("Delete");

		// Set the control to the base container
		setControl(container);
	}
}
