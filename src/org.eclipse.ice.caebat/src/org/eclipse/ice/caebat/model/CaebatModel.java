/*******************************************************************************
 * Copyright (c) 2013, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.caebat.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ice.datastructures.form.AllowedValueType;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.datastructures.form.MasterDetailsComponent;
import org.eclipse.ice.datastructures.form.TableComponent;
import org.eclipse.ice.datastructures.form.TimeDataComponent;
import org.eclipse.ice.datastructures.updateableComposite.Component;
import org.eclipse.ice.io.ips.IPSReader;
import org.eclipse.ice.io.ips.IPSWriter;
import org.eclipse.ice.item.Item;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This class is the model representation of the CAEBAT model. It inherits from
 * the Item Class. It will get template files from ICEFiles/Caebat_Model that can 
 * be used to launch simulations from the Caebat Launcher Item.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author s4h, bzq
 */
@XmlRootElement(name = "CaebatModel")
public class CaebatModel extends Item {

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * A custom tag for ini files operation. Set in the constructor.
	 * </p>
	 * <!-- end-UML-doc -->
	 */
	private String customTaggedExportString = "Export to Caebat INI format";

	// The name of the example chosen
	protected String exampleName; // Default for now

	/**
	 * A nullary constructor that delegates to the project constructor.
	 */
	public CaebatModel() {
		this(null);
		return;
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The constructor for the CaebatModel. Calls the constructor for Item by
	 * passing the IProject. It should call setupForm() in the super
	 * constructor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param project
	 *            <p>
	 *            The passed IProject for the workspace.
	 *            </p>
	 */
	public CaebatModel(IProject project) {

		// begin-user-code

		// Setup the form and everything
		super(project);
		return;
		// end-user-code

	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation overrides the Item.setupForm() operation.
	 * </p>
	 * <!-- end-UML-doc -->
	 */
	public void setupForm() {
		// begin-user-code

		// This method will create a new Form and add all the dataComponents to
		// the form. These dataComponents will be accessed later in
		// loadDataComponents.

		form = new Form();
		ArrayList<String> problemFiles = null;
		String separator = System.getProperty("file.separator");
		// Setup Item information
		setName("Caebat Model");
		setDescription("This model creates input for CAEBAT.");

		loadInput(null);

		// Add an action to the list to allow for the INI exports
		customTaggedExportString = "Export to Caebat INI format";
		allowedActions.add(0, customTaggedExportString);

		// ----- Finish setting up the Form so that it can be immediately
		// launched

		return;
		// end-user-code

	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Overrides the reviewEntries operation. This will still call
	 * super.reviewEntries, but will handle the dependencies after all other dep
	 * handing is finished.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return the status of the form
	 */
	protected FormStatus reviewEntries(Form preparedForm) {

		// begin-user-code
		FormStatus retStatus = FormStatus.ReadyToProcess;
		Component dataComp = null;
		
		// Grab the data component from the Form and only proceed if it exists
		ArrayList<Component> components = preparedForm.getComponents();
		dataComp = components.get(0);

		// Make sure the form has some data
		if (dataComp == null || !"Time Loop Data".equals(dataComp.getName())) {
			retStatus = FormStatus.InfoError;
		}
		return retStatus;
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Overrides item's process by adding a customTaggedExportString (ini).
	 * Still utilizes Item's process functionality for all other calls.
	 * </p>
	 * <!-- end-UML-doc -->
	 */
	public FormStatus process(String actionName) {
		// begin-user-code
		FormStatus retStatus;

		// If it is the custom operation, call this here.
		if (this.customTaggedExportString.equals(actionName)) {

			// Get the file from the project space to create the output
			String filename = getName().replaceAll("\\s+", "_") + "_" + getId()
					+ ".conf";
			IFile outputFile = project.getFile(filename);
			// Get the file path
			URI outputFilePath = null;
			try {
				outputFilePath = new URI(outputFile.getLocation().toOSString());
			} catch (URISyntaxException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}

			// Get the data from the form
			ArrayList<Component> components = form.getComponents();

			if (components.size() > 0 && components.get(0).getName() == "Caebat Input Problems") {
				components = new ArrayList<Component>(components.subList(1,
						components.size()));
			}

			if (components.size() > 3) {

				// create a new IPSWriter with the output file
				IPSWriter writer = new IPSWriter();
				try {
					writer.write(form, outputFilePath);
					// Refresh the project space
					project.refreshLocal(IResource.DEPTH_ONE, null);
				} catch (CoreException e) {
					// Complain
					System.err.println("CaebatModel Message: "
							+ "Failed to refresh the project space.");
					e.printStackTrace();
				}
				// ensure that the new file is all good
				// return a success
				retStatus = FormStatus.Processed;
			} else {
				System.err.println("Not enough components to write new file!");
				retStatus = FormStatus.InfoError;
			}

		}

		// Otherwise let item deal with the process
		else {
			System.out.println("Not enough components to write new file!");
			retStatus = super.process(actionName);
		}

		return retStatus;
		// end-user-code

	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation loads the given example into the Form.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param name
	 *            The path name of the example file name to load.
	 */
	public void loadInput(String name) {

		// Give a default value if nothing has been specified
		BufferedReader in = null;
		URI uri = null;
		if (name == null) {
			try {
				uri = new URI("platform:/plugin/org.eclipse.ice.caebat/data/case_6.conf");
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		// Load the imported file
		} else {
			String separator = System.getProperty("file.separator");
			String userDir = System.getProperty("user.home") + separator
					+ "ICEFiles" + separator + "default";
			try {
				uri = new URI("file:" + userDir + separator + name);
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// Load the components from the file
		IPSReader reader = new IPSReader();
		Form newForm = null;
		newForm = reader.read(uri);
		
		ArrayList<Component> components = newForm.getComponents();
		ArrayList<Component> existingComponents = form.getComponents();
		
		// Update the components by copying the new ones
		if (components != null && components.size() == 4) {

			// Replace the old components
			if (existingComponents.size() == 4) {
				((DataComponent) existingComponents.get(0))
						.copy((DataComponent) components.get(0));
				((TableComponent) existingComponents.get(1))
						.copy((TableComponent) components.get(1));
				((TableComponent) existingComponents.get(2))
						.copy((TableComponent) components.get(2));
				((MasterDetailsComponent) existingComponents.get(3))
						.copy((MasterDetailsComponent) components.get(3));
			} else {
				// Add the new components
				form.addComponent((DataComponent) components.get(0));
				form.addComponent((TableComponent) components.get(1));
				form.addComponent((TableComponent) components.get(2));
				form.addComponent((MasterDetailsComponent) components.get(3));	
			}


		} else {
			System.out.println("Caebat Model Message: Could not read in "
					+ "a valid case for processing.");
		}
		
	}
}
