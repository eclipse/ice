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

import java.awt.event.ActionListener;
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
	 * The process action name for calculating the reflectivity.
	 */
	private final String processActionName = "Calculate Reflectivity";

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
		
		// Local Declarations
		FormStatus retVal;
		
		if (actionName.equals(processActionName)) {
			
			// Convert the material table to slabs
			
			// Calculate the reflectivity
			ReflectivityCalculator calculator = new ReflectivityCalculator();
			//calculator.getReflectivityProfile(slabs, numRough, deltaQ0, deltaQ1ByQ, wavelength, waveVector, getRQ4);
			
			// Write the files
			
			retVal = FormStatus.InfoError;
		} else {
			retVal = super.process(actionName);
		}
		
		return retVal;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.item.Item#setupForm()
	 */
	@Override
	protected void setupForm() {

		// FIXME! Simple data entered now for testing
		String line1 = "#features,t, p_x, p_y\n";
		String line2 = "#units,t,p_x,p_y\n";
		String line3 = "1.0,1.0,1.0\n";
		String line4 = "2.0,4.0,8.0\n";
		String line5 = "3.0,9.0,27.0\n";
		String allLines = line1 + line2 + line3 + line4 + line5;

		// Create an empty stream for the output files
		ByteArrayInputStream stream = new ByteArrayInputStream(
				allLines.getBytes());

		// Let the parent setup the Form
		super.setupForm();

		// FIXME! - Add a data component for the number of rough layers and the
		// input
		// file

		// Configure a list of property names for the materials
		ArrayList<String> names = new ArrayList<String>();
		names.add("Material ID");
		names.add("Thickness (A)");
		names.add("Roughness (A)");
		names.add("Scattering Length Density (A^-2)");
		names.add("Mu_abs (A^-2)");
		names.add("Mu_inc (A^-1)");
		// Create the writable format to be used by the list
		MaterialWritableTableFormat format = new MaterialWritableTableFormat(
				names);

		// Create the list that will contain all of the material information
		ListComponent<Material> matList = new ListComponent<Material>();
		matList.setId(1);
		matList.setName("Reflectivity Input Data");
		matList.setDescription("Reflectivity Input Data");
		matList.setTableFormat(format);

		// Create a default list of materials for now. This is TEMPORARY, I
		// imagine.
		fillMaterialList(matList);

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

		// Put the action name in the form so that the reflectivity can be
		// calculated.
		allowedActions.add(0, processActionName);

		return;
	}

	/**
	 * This operation fills the material list with a default set of materials so
	 * that the Item is immediately valid and can be processed.
	 * 
	 * @param matList
	 *            the list of Materials that represents the system. One material
	 *            per layer.
	 */
	private void fillMaterialList(ListComponent<Material> matList) {

		// Local Declarations
		Material material = null;

		// Create the slabs that define the system, starting with air
		Slab air = new Slab();
		air.thickness = 200.0;
		matList.add(convertSlabToMaterial(air, "Air", 1));

		// NiOx
		Slab niOx = new Slab();
		niOx.scatteringLength = (0.00000686 + 0.00000715) / 2.0;
		niOx.trueAbsLength = 2.27931868269305E-09;
		niOx.incAbsLength = 4.74626235093697E-09;
		niOx.thickness = 22.0;
		niOx.interfaceWidth = 4.0 * 2.35;
		matList.add(convertSlabToMaterial(niOx, "NiOx", 1));

		// Ni
		Slab ni = new Slab();
		ni.scatteringLength = 9.31e-6;
		ni.trueAbsLength = 2.27931868269305E-09;
		ni.incAbsLength = 4.74626235093697E-09;
		ni.thickness = 551.0;
		ni.interfaceWidth = 4.3 * 2.35;
		matList.add(convertSlabToMaterial(ni, "Ni", 1));

		// SiNiOx
		Slab siNiOx = new Slab();
		siNiOx.scatteringLength = (0.00000554 + 0.00000585) / 2.0;
		siNiOx.trueAbsLength = 2.27931868269305E-09;
		siNiOx.incAbsLength = 4.74626235093697E-09;
		siNiOx.thickness = 42.0;
		siNiOx.interfaceWidth = 7.0 * 2.35;
		matList.add(convertSlabToMaterial(siNiOx, "SiNiOx", 1));

		// SiOx
		Slab si = new Slab();
		si.scatteringLength = 2.070e-6;
		si.trueAbsLength = 4.74981478870069E-11;
		si.incAbsLength = 1.99769988072137E-12;
		si.thickness = 100.0;
		si.interfaceWidth = 17.5;
		matList.add(convertSlabToMaterial(si, "Si", 1));

		return;
	}

	/**
	 * This operation create a Material based on a Slab
	 * 
	 * @param slab
	 *            the slab
	 * @param name
	 *            the name of the material
	 * @param id
	 *            the material id
	 * @return the new material created from the slab
	 */
	private Material convertSlabToMaterial(Slab slab, String name, int id) {
		// Just add each property of the slab to the list of properties of the
		// material.
		Material material = new Material();
		material.setName(name);
		material.setProperty("Material ID", id);
		material.setProperty("Thickness (A)", slab.thickness);
		material.setProperty("Roughness (A)", slab.interfaceWidth);
		material.setProperty("Scattering Length Density (A^-2)",
				slab.scatteringLength);
		material.setProperty("Mu_abs (A^-2)", slab.trueAbsLength);
		material.setProperty("Mu_inc (A^-1)", slab.incAbsLength);
		return material;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.item.Item#setupItemInfo()
	 */
	@Override
	protected void setupItemInfo() {

		// Local Declarations
		String desc = "This item builds models for " + "Reflectivity.";

		// Describe the Item
		setName(ReflectivityModelBuilder.name);
		setItemBuilderName(ReflectivityModelBuilder.name);
		setDescription(desc);
		itemType = ReflectivityModelBuilder.type;

		return;
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
			ListComponent<Material> matList = (ListComponent<Material>) form
					.getComponent(1);
			// Set the database as an element source
			matList.setElementSource(database);
		}

		return;
	}

}
