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
package org.eclipse.ice.reflectivity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.datastructures.ICEObject.ListComponent;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.datastructures.form.Material;
import org.eclipse.ice.datastructures.form.ResourceComponent;
import org.eclipse.ice.datastructures.resource.VizResource;
import org.eclipse.ice.item.model.Model;
import org.eclipse.ice.materials.IMaterialsDatabase;
import org.eclipse.ice.materials.MaterialWritableTableFormat;

/**
 * This classes calculates the reflectivity profile of a set of materials
 * layered on top of each other. It... <add more after you figure out the
 * calculations>
 * 
 * @author Jay Jay Billings, Alex McCaskey
 */
@XmlRootElement(name = "ReflectivityModel")
public class ReflectivityModel extends Model {

	/**
	 * The constructor.
	 */
	public ReflectivityModel() {
		this(null);
	}

	/**
	 * The constructor with a project space in which files should be handled.
	 * 
	 * @param projectSpace
	 *            The Eclipse project where files should be stored and from
	 *            which they should be retrieved.
	 */
	public ReflectivityModel(IProject projectSpace) {
		// Call super
		super(projectSpace);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.item.Item#process(java.lang.String)
	 */
	@Override
	public FormStatus process(String actionName) {
		// begin-user-code
		return super.process(actionName);
		// end-user-code
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.item.Item#setupForm()
	 */
	@Override
	protected void setupForm() {
		// begin-user-code

		// Create an empty stream for the output files

		// FIXME! Simple data entered now for testing
		String line1 = "#features,t, p_x, p_y\n";
		String line2 = "#units,t,p_x,p_y\n";
		String line3 = "1.0,1.0,1.0\n";
		String line4 = "2.0,4.0,8.0\n";
		String line5 = "3.0,9.0,27.0\n";
		String allLines = line1 + line2 + line3 + line4 + line5;

		ByteArrayInputStream stream = new ByteArrayInputStream(
				allLines.getBytes());

		// Let the parent setup the Form
		super.setupForm();

		// Add a data component for the number of rough layers and the input file
		
		// FIXME!
		
		// Configure a list of property names for the materials
		ArrayList<String> names = new ArrayList<String>();
		names.add("Material ID");
		names.add("Thickness (A)");
		names.add("Roughness (A)");
		names.add("Scattering Length Density (A^-2)");
		names.add("Mu_abs (A^-2)");
		names.add("Mu_inc (A^-1)");
		// Create the writable format to be used by the list
		MaterialWritableTableFormat format = new MaterialWritableTableFormat(names);
		
		// Create the list that will contain all of the material information
		ListComponent<Material> matList = new ListComponent<Material>();
		matList.setId(1);
		matList.setName("Reflectivity Input Data");
		matList.setDescription("Reflectivity Input Data");
		matList.add(new Material());
		matList.setTableFormat(format);
		// Make sure to put it in the form!
		form.addComponent(matList);

		if (project != null) {
			// FIXME! ID is always 1 at this point!
			String basename = "reflectivityModel_" + getId() + "_";
			// Create the output file for the reflectivity data
			IFile reflectivityFile = project.getFile(basename + "rfd.csv");
			// Create the output file for the scattering density data
			IFile scatteringFile = project.getFile(basename + "scdens.csv");
			try {
				// Reflectivity first
				if (reflectivityFile.exists()) {
					reflectivityFile.delete(true, null);
				}
				reflectivityFile.create(stream, true, null);
				// Then the scattering file
				if (scatteringFile.exists()) {
					scatteringFile.delete(true, null);
				}
				stream.reset();
				scatteringFile.create(stream, true, null);

				// Create the VizResource to hold the reflectivity data
				VizResource reflectivitySource = new VizResource(
						reflectivityFile.getLocation().toFile());
				reflectivitySource.setName("Reflectivity Data File");
				reflectivitySource.setId(1);
				reflectivitySource
						.setDescription("Data from reflectivity calculation");

				// Create the VizResource to hold the scatDensity data
				VizResource scatDensitySource = new VizResource(scatteringFile
						.getLocation().toFile());
				scatDensitySource.setName("Scattering Density Data File");
				scatDensitySource.setId(2);
				scatDensitySource.setDescription("Data from Stattering "
						+ "Density calculation");

				// Create a component to hold the output
				ResourceComponent resources = new ResourceComponent();
				resources.setName("Results");
				resources.setDescription("Results and Output");
				resources.setId(2);
				resources.addResource(reflectivitySource);
				resources.addResource(scatDensitySource);
				form.addComponent(resources);
			} catch (CoreException | IOException e) {
				// Complain
				System.err.println("ReflectivityModel Error: "
						+ "Problem creating reflectivity files!");
				e.printStackTrace();
			}
		}

		return;
		// end-user-code
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.item.Item#setupItemInfo()
	 */
	@Override
	protected void setupItemInfo() {
		// begin-user-code

		// Local Declarations
		String desc = "This item builds models for " + "Reflectivity.";

		// Describe the Item
		setName(ReflectivityModelBuilder.name);
		setItemBuilderName(ReflectivityModelBuilder.name);
		setDescription(desc);
		itemType = ReflectivityModelBuilder.type;

		return;
		// end-user-code
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.item.Item#setupFormWithServices()
	 */
	@Override
	public void setupFormWithServices() {

		// If the materials database is available, register it as the element
		// provider for the list component of materials on the Form.
		IMaterialsDatabase database = getMaterialsDatabase();
		if (database != null) {
			// Grab the component
			ListComponent<Material> matList = (ListComponent<Material>) form.getComponent(1);
			// Set the database as an element source
			matList.setElementSource(database);
		}

		return;
	}
	
}
