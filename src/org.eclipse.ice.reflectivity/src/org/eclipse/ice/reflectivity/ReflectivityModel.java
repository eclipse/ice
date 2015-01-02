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

import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.datastructures.ICEObject.ListComponent;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.datastructures.form.Material;
import org.eclipse.ice.datastructures.form.ResourceComponent;
import org.eclipse.ice.datastructures.form.TableComponent;
import org.eclipse.ice.datastructures.resource.VizResource;
import org.eclipse.ice.item.Item;

/**
 * This classes calculates the reflectivity profile of a set of materials
 * layered on top of each other. It... <add more after you figure out the
 * calculations>
 * 
 * @author Jay Jay Billings, Alex McCaskey
 */
@XmlRootElement(name = "ReflectivityModel")
public class ReflectivityModel extends Item {

	/**
	 * The constructor.
	 */
	public ReflectivityModel() {
		// begin-user-code
		this(null);
		// end-user-code
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
		ByteArrayInputStream stream = new ByteArrayInputStream(" ".getBytes());

		// Create the Form
		form = new Form();
		TableComponent table = new TableComponent();
		table.setId(1);
		table.setName("Reflectivity Input Data");
		table.setDescription("");

		// Create the list that will contain all of the material information
		ListComponent<Material> matList = new ListComponent<Material>();
		matList.setId(1);
		matList.setName("Reflectivity Input Data");
		matList.setDescription("Reflectivity Input Data");
		matList.add(new Material());
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

	/**
	 * This operation is used to setup the name and description of the model as
	 * well as register its builder.
	 */
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

}
