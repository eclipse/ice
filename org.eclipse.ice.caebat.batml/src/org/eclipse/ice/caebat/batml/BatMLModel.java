/*******************************************************************************
 * Copyright (c) 2014, 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Alex McCaskey (UT-Battelle, LLC.) - initial API and implementation and/or
 *      initial documentation
 *    Jay Jay Billings (UT-Battelle, LLC.) - fixed class author tag
 *    Jordan Deyton (UT-Battelle, LLC.) - UML doc cleanup
 *******************************************************************************/
package org.eclipse.ice.caebat.batml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.january.form.emf.EMFComponent;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemType;
import org.eclipse.january.form.Form;
import org.eclipse.january.form.FormStatus;

/**
 * The BatMLModel extends the Item to provide a model generator for the CAEBAT
 * BatML input files. It uses an EMFComponent to map the BatML schema to an
 * Eclipse Modeling Framework Ecore model, which is then translated to an ICE
 * TreeComposite to be visualized and editted by the user.
 *
 * @author Alex McCaskey, Andrew Bennett
 */
@XmlRootElement(name = "BatMLModel")
public class BatMLModel extends Item {

	/**
	 * References to the main BatML schema file and xml file to be loaded, respectively.
	 */
	private static File xsdFile, xmlFile;

	/**
	 * Reference to the EMFComponent that takes the XML Schema file and maps it
	 * to an Ecore model.
	 */
	private static EMFComponent emfComp;

	/**
	 * The constructor.
	 */
	public BatMLModel() {
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
	public BatMLModel(IProject projectSpace) {
		super(projectSpace);
	}

	/**
	 * This method sets up the BatMLModel Item's Form reference, specifically,
	 * it searches for the correct XML schema and creates an EMFComponent and
	 * adds it to the Form.
	 */
	@Override
	protected void setupForm() {

		form = new Form();
		
		// It could be the case that we've already created the EMFComponent,
		// if so just skip this creation stuff
		if (project != null && project.isAccessible()) {

			// Refresh the project space
			try {
				project.refreshLocal(IResource.DEPTH_INFINITE, null);
			} catch (CoreException e) {
				logger.error(getClass().getName() + " Exception!", e);
			}

			// Load in the default information
			loadInput(null);
		}
	}

	/**
	 * This operation is used to setup the name and description of the model.
	 */
	@Override
	protected void setupItemInfo() {
		String desc = "This item builds models based on a BatteryML schema.";
		setName("BatML Model");
		setDescription(desc);
		itemType = ItemType.Model;
		allowedActions.add(0, "Write to XML");
	}

	/**
	 * Makes sure that the form is in an acceptable state
	 * 
	 * @param preparedForm
	 *            The form prepared for review.
	 * @return The Form's status if the review was successful or not.
	 */
	@Override
	protected FormStatus reviewEntries(Form preparedForm) {
		return super.reviewEntries(preparedForm);
	}

	/**
	 * Used to export the form's information into an output file.
	 * 
	 * @param actionName
	 * 			A string description of the process that should happen
	 */
	@Override
	public FormStatus process(String actionName) {

		// Local Declarations
		FormStatus retStatus = FormStatus.InfoError;

		// Make sure we've got the correct action
		if (actionName.equals("Write to XML")) {
			// Get the file name
			String fileName = xsdFile.getName().replaceAll(".xsd", ".xml");

			// Create the IFile reference
			IFile iFile = project.getFile(fileName);
			try {
				// Save the File
				if (emfComp.save(EFS.getStore(iFile.getLocationURI()).toLocalFile(0, new NullProgressMonitor()))) {
					retStatus = FormStatus.Processed;
				} else {
					retStatus = FormStatus.InfoError;
				}
			} catch (CoreException e) {
				logger.error(getClass().getName() + " Exception!", e);
				retStatus = FormStatus.InfoError;
			}
		}

		return retStatus;
	}

	/**
	 * Loads a file into the form.  If null input is given a default form will
	 * be populated by transferring files from the plugin itself.
	 * 
	 * @param input
	 *            The name of the input input file, including the file extension
	 */
	@Override
	public void loadInput(String input) {

		IFile xsdIFile, xmlIFile;
		File temp = null;
		
		// If this is our first time loading up the form we will probably need to import the schema files
		if (form.getNumberOfComponents() == 0) {

			String[] schemas = { "BuildingBlockDB.xsd", "MaterialDB.xsd", "PackDB.xsd", "UnitsML-v1.0-csd03.xsd",
					"matml31.xsd", "CellDB.xsd", "ModelDB.xsd", "PartDB.xsd", "common_basic_data_types.xsd",
					"CellSandwichDB.xsd", "ModuleDB.xsd", "SimulationDB.xsd", "electrical.xml", "DeviceDB.xsd",
					"NamedParameters.xsd", "UnitsDB.xsd", "electrical.xsd" };

	
			// Create a filepath for the default file
			String defaultFilePath = project.getLocation().toOSString() + System.getProperty("file.separator")
					+ "batml";
			temp = new File(defaultFilePath);
			if (!temp.exists()) {
				temp.mkdir();
			}
					
			// Load the schema files into the workspace
			for (String schemaFile : schemas) {
				try {
					// Create a temporary location to load the default file
					temp = new File(defaultFilePath + System.getProperty("file.separator") + schemaFile);
					if (!temp.exists()) {
						temp.createNewFile();
	
						// Pull the default file from inside the plugin
						URI uri = new URI("platform:/plugin/org.eclipse.ice.caebat.batml/data/" + schemaFile);
						InputStream reader = uri.toURL().openStream();
						FileOutputStream outStream = new FileOutputStream(temp);
	
						// Write out the default file from the plugin to the temp location
						int fileByte;
						while ((fileByte = reader.read()) != -1) {
							outStream.write(fileByte);
						}
						outStream.close();
						reader.close();
					}
				} catch (URISyntaxException e) {
					logger.error(getClass().getName() + " Exception!", e);
					logger.error("[BatML Model] ERROR: Could not load the default BatML schema data!");
				} catch (MalformedURLException e) {
					logger.error(getClass().getName() + " Exception!", e);
					logger.error("[BatML Model] ERROR: Could not load the default BatML schema data!");
				} catch (IOException e) {
					logger.error(getClass().getName() + " Exception!", e);
					logger.error("[BatML Model] ERROR: Could not load the default BatML schema data!");
				}
			}
			
			// Make sure that ICE can find the files to load
			try {
				project.refreshLocal(IResource.DEPTH_INFINITE, null);
			} catch (CoreException e) {
				logger.error("[BatML Model] ERROR: Could not refresh project workspace!");
			}
		}
		// Load up either a default file, or the newly imported one.
		if (input == null) {
			xsdIFile = project.getFolder("batml").getFile("electrical.xsd");
			xmlIFile = project.getFolder("batml").getFile("electrical.xml");
		} else {
			xsdIFile = project.getFolder("batml").getFile("electrical.xsd");
			xmlIFile = project.getFile(input);
		}

		// Get the handle to the files on the local file system
		xsdFile = xsdIFile.getRawLocation().makeAbsolute().toFile();
		xmlFile = xmlIFile.getRawLocation().makeAbsolute().toFile();
		
		// Create the EMFComponent
		if (xsdFile != null) {
			emfComp = new EMFComponent();
			emfComp.load(xsdFile, xmlFile);
			emfComp.getEMFTreeComposite().setName("BatML");
			emfComp.setName("BatML Model Editor");
			emfComp.setId(1);
		} else {
			emfComp = new EMFComponent();
			emfComp.setName("BatML Model Editor");
			emfComp.setDescription("Could not find BatML input for model creation!");
			emfComp.setId(1);
		}

		form.addComponent(emfComp);
	}
}
