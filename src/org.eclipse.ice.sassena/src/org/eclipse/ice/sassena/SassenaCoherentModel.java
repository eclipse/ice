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
package org.eclipse.ice.sassena;

import java.io.File;

import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.datastructures.form.emf.EMFComponent;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemType;

/**
 * The SassenaCoherentModel extends the Item to provide a model generator for the 
 * Sassena Coherent input files. It uses an EMFComponent to map the Sassena schema to an
 * Eclipse Modeling Framework Ecore model, which is then translated to an ICE
 * TreeComposite to be visualized and edited by the user.
 * 
 * @author Alex McCaskey
 */
@XmlRootElement(name = "SassenaCoherentModel")
public class SassenaCoherentModel extends Item {

	/**
	 * Reference to the main Sassena schema file.
	 */
	private static File xsdFile;

	/**
	 * The constructor.
	 * 
	 */
	public SassenaCoherentModel() {
		this(null);
	}

	/**
	 * The constructor with a project space in which files should be
	 * manipulated.
	 * 
	 * @param projectSpace
	 *            The Eclipse project where files should be stored and from
	 *            which they should be retrieved.
	 */
	public SassenaCoherentModel(IProject projectSpace) {

		// Call super
		super(projectSpace);

	}

	/**
	 * This method sets up the SassenaCoherentModel Item's Form reference, specifically,
	 * it searches for the correct XML schema and creates an EMFComponent and
	 * adds it to the Form.
	 * 
	 */
	@Override
	protected void setupForm() {

		// Create the Form
		EMFComponent emfComp = null;
		form = new Form();
		form.setName("Sassena Coherent Model Builder");

		// It could be the case that we've already created the EMFComponent,
		// if so just skip this creation stuff
		// if (!initialized) {
		if (project != null && project.isAccessible()) {

			// Refresh the project space
			try {
				project.refreshLocal(IResource.DEPTH_INFINITE, null);
			} catch (CoreException e1) {
				e1.printStackTrace();
			}

			// Get the sassena folder and the correct XML schema
			IFolder sassenaFolder = project.getFolder("sassena");
			IFile xsdIFile = sassenaFolder.getFile("sassenaCoh.xsd");

			// If valid, create the Java file instance needed by
			// the EMFComponent
			if (xsdIFile.exists()) {
				try {
					xsdFile = EFS.getStore(xsdIFile.getLocationURI())
							.toLocalFile(0, new NullProgressMonitor());
				} catch (CoreException e) {
					e.printStackTrace();
				}

				// Create the EMFComponent
				if (xsdFile != null) {
					emfComp = new EMFComponent(xsdFile);
					emfComp.setName("Sassena Coherent Model Editor");
					emfComp.setId(1);
				}

			} else {
				System.err
						.println("Sassena Model Error. Cannot find requisite "
								+ "sassenaCoh.xsd schema file. No Sassena Coherent tree will be constructed.");
			}
		}
		// }

		// If we've been successful, add it to the Form.
		if (emfComp != null) {
			form.addComponent(emfComp);
		}

		return;
	}

	/**
	 * This operation is used to setup the name and description of the model.
	 * 
	 */
	@Override
	protected void setupItemInfo() {

		// Local Declarations
		String desc = "This item builds models based on a Sassena Coherent schema.";

		// Describe the Item
		setName("Sassena Coherent Model Builder");
		setDescription(desc);
		itemType = ItemType.Model;

		// Setup the action list. Remove key-value pair support.
		// allowedActions.remove(taggedExportActionString);
		allowedActions.add("Write to XML");

		return;
	}

	/**
	 * This method overrides Item.process to provide a process 
	 * action that writes the EMFComponent to an XML file. 
	 */
	@Override
	public FormStatus process(String actionName) {

		// Local Declarations
		FormStatus retStatus = FormStatus.InfoError;
		EMFComponent emfComp = (EMFComponent) form.getComponent(1);

		// Make sure we've got the correct action
		if (emfComp != null && actionName.equals("Write to XML")) {
			// Get the file name
			String fileName = xsdFile.getName().replaceAll(".xsd", ".xml");

			// Create the IFile reference
			IFile iFile = project.getFile(fileName);
			try {
				// Save the File
				if (emfComp.save(EFS.getStore(iFile.getLocationURI())
						.toLocalFile(0, new NullProgressMonitor()))) {
					retStatus = FormStatus.Processed;
				} else {
					retStatus = FormStatus.InfoError;
				}
			} catch (CoreException e) {
				e.printStackTrace();
				retStatus = FormStatus.InfoError;
			}
		}

		return retStatus;
	}

	/**
	 * This method loads an existing XML file as an EMFComponent.
	 *  
	 * @param input
	 *            The name of the input input file, including the file extension
	 */
	@Override
	public void loadInput(String input) {

		// Local Declarations
		EMFComponent emfComp = (EMFComponent) form.getComponent(1);
		String path = project.getFolder("sassena").getLocation().toOSString()
				+ System.getProperty("file.separator") + input;
		if (emfComp != null) {
			if (!emfComp.load(new File(path))) {
				System.err.println("Invalid file passed to SassenaCoherentModel.loadInput");
			}
		}

		return;
	}

}
