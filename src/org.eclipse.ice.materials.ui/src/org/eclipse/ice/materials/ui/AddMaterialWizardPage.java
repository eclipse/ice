/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings, Kasper Gammeltoft
 *******************************************************************************/
package org.eclipse.ice.materials.ui;

import org.eclipse.ice.client.widgets.ElementSourceDialog;
import org.eclipse.ice.client.widgets.ListComponentNattable;
import org.eclipse.ice.datastructures.ICEObject.IElementSource;
import org.eclipse.ice.datastructures.ICEObject.ListComponent;
import org.eclipse.ice.datastructures.form.Material;
import org.eclipse.ice.datastructures.form.MaterialStack;
import org.eclipse.ice.materials.IMaterialsDatabase;
import org.eclipse.ice.materials.MaterialStackWritableTableFormat;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * This class provides all of the necessary UI for defining a new material and a
 * configuring its stoichiometry.
 * 
 * It was developed using the SWT WindowBuilder.
 * 
 * @author Jay Jay Billings, Kasper Gammeltoft
 * 
 */
public class AddMaterialWizardPage extends WizardPage {

	/**
	 * The table that displays the stoichiometric information for the new
	 * material.
	 */
	private ListComponentNattable stoichiometryTable;

	/**
	 * The list to hold the stoichiometric data for the table
	 */
	private ListComponent<MaterialStack> list;

	/**
	 * The source for choosing materials to comprise this new one.
	 */
	private IElementSource source;

	/**
	 * The shell for opening the element source dialog for material selection
	 * while building the stoichiometry
	 */
	private Shell shell;

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
	 * Sets the element source for choosing new materials for the stoichiometry
	 * of this one.
	 * 
	 * @param elementSource
	 *            The source of the materials
	 */
	public void setSource(IElementSource elementSource) {
		source = elementSource;
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
	 * Gets the material created by the fields on this page. Calculates the new
	 * variables for the material from its stoichiometric information.
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
		double density = Double.parseDouble(densityText.getText());
		material.setProperty(Material.DENSITY, density);

		// Add the stoichiometric components as components of the new material.
		for (MaterialStack stack : list) {
			material.addComponent(stack);
		}

		// Updates the material properties for the new material.
		material.updateProperties();

		// Just return the new material. 
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

		// Composite that holds the name text field and labels
		Composite nameComposite = new Composite(nameAndDensityComposite,
				SWT.NONE);
		GridData nameCompositeGridData = new GridData(SWT.FILL, SWT.FILL, true,
				false, 1, 1);
		nameCompositeGridData.widthHint = 1;
		nameCompositeGridData.minimumWidth = 1;
		nameComposite.setLayoutData(nameCompositeGridData);
		nameComposite.setLayout(new GridLayout(2, false));

		// Name label
		Label nameLabel = new Label(nameComposite, SWT.NONE);
		nameLabel.setText("Name:");

		// Name text field
		nameText = new Text(nameComposite, SWT.BORDER);
		nameText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1,
				1));

		// Composite to hold the density text field and labels
		Composite densityComposite = new Composite(nameAndDensityComposite,
				SWT.NONE);
		densityComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false, 1, 1));
		densityComposite.setLayout(new GridLayout(3, false));

		// Density label
		Label densityLabel = new Label(densityComposite, SWT.NONE);
		densityLabel.setText("Density:");

		// Density text field
		densityText = new Text(densityComposite, SWT.BORDER);
		densityText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		// Label for the density units (currently using g/cm3)
		Label densityUnitsLabel = new Label(densityComposite, SWT.NONE);
		densityUnitsLabel.setText("g/cm3");

		// Add a modify listener to update the buttons in the wizard if the text
		// fields change.
		ModifyListener listener = new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent arg0) {
				// Updates the buttons for the wizard (cancel and finish)
				getWizard().getContainer().updateButtons();
			}

		};

		// Add the listener
		nameText.addModifyListener(listener);
		densityText.addModifyListener(listener);

		// Create the composite to hold the stoichiometry buttons and table
		Composite stoichiometryComposite = new Composite(container, SWT.NONE);
		stoichiometryComposite.setLayout(new GridLayout(2, false));
		stoichiometryComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				true, true, 1, 1));

		// Create the composite to hold the table
		Composite stoichiometryTableComposite = new Composite(
				stoichiometryComposite, SWT.NONE);
		stoichiometryTableComposite.setLayout(new GridLayout(1, false));
		stoichiometryTableComposite.setLayoutData(new GridData(SWT.FILL,
				SWT.FILL, true, true, 1, 1));

		// Instanciate the list to give to the NatTable. We want to use material
		// stacks because that is what we are using to build our new materials.
		list = new ListComponent<MaterialStack>();
		list.setTableFormat(new MaterialStackWritableTableFormat());

		// Create the table
		stoichiometryTable = new ListComponentNattable(
				stoichiometryTableComposite, list, true);

		// Create the composite to hold the buttons (add and delete)
		Composite stoichiometryButtonComposite = new Composite(
				stoichiometryComposite, SWT.NONE);
		stoichiometryButtonComposite.setLayout(new GridLayout(1, false));
		stoichiometryButtonComposite.setLayoutData(new GridData(SWT.RIGHT,
				SWT.FILL, false, true, 1, 1));

		// Add the add button
		Button addMaterialButton = new Button(stoichiometryButtonComposite,
				SWT.PUSH);
		addMaterialButton.setText("Add");
		addMaterialButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// We want to pick from the existing materials database
				ElementSourceDialog dialog = new ElementSourceDialog(shell,
						source);
				if (dialog.open() == Window.OK) {
					// Add the material
					Material matToAdd = (Material) dialog.getSelection();
					// Get the right material reference from the database
					IMaterialsDatabase database = (IMaterialsDatabase)source;
					for(Material mat : database.getMaterials()){
						if(matToAdd.equals(mat)){
							matToAdd = mat;
							break;
						}
					}
					
					// Add the new material stack
					list.add(new MaterialStack(matToAdd, 1));
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {

			}

		});

		// Add the delete button
		Button deleteMaterialButton = new Button(stoichiometryButtonComposite,
				SWT.PUSH);
		deleteMaterialButton.setText("Delete");
		deleteMaterialButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				ListComponent<MaterialStack> selected = stoichiometryTable
						.getSelectedObjects();
				if (selected.size() > 0) {
					// Remove all selected materials from the list
					for (MaterialStack stack : selected) {
						list.remove(stack);
					}
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {

			}

		});

		// Set the control to the base container
		setControl(container);
	}
}
