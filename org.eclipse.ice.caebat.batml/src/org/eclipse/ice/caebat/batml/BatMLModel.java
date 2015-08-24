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
 * <p>
 * The BatMLModel extends the Item to provide a model generator for the CAEBAT
 * BatML input files. It uses an EMFComponent to map the BatML schema to an
 * Eclipse Modeling Framework Ecore model, which is then translated to an ICE
 * TreeComposite to be visualized and editted by the user.
 * </p>
 *
 * @author Alex McCaskey
 */
@XmlRootElement(name = "BatMLModel")
public class BatMLModel extends Item {

	/**
	 * <p>
	 * Reference to the main BatML schema file.
	 * </p>
	 */
	private static File xsdFile;

	/**
	 * <p>
	 * Reference to the EMFComponent that takes the XML Schema file and maps it
	 * to an Ecore model.
	 * </p>
	 */
	private static EMFComponent emfComp;

	/**
	 * <p>
	 * Flag to indicate whether this BatML Item has been initialized.
	 * </p>
	 */
	static boolean initialized = false;

	/**
	 * <p>
	 * The constructor.
	 * </p>
	 *
	 */
	public BatMLModel() {
		this(null);
	}

	/**
	 * <p>
	 * The constructor with a project space in which files should be
	 * manipulated.
	 * </p>
	 *
	 * @param projectSpace
	 *            <p>
	 *            The Eclipse project where files should be stored and from
	 *            which they should be retrieved.
	 *            </p>
	 */
	public BatMLModel(IProject projectSpace) {

		// Call super
		super(projectSpace);

	}

	/**
	 * <p>
	 * This method sets up the BatMLModel Item's Form reference, specifically,
	 * it searches for the correct XML schema and creates an EMFComponent and
	 * adds it to the Form.
	 * </p>
	 *
	 *
	 */
	@Override
	protected void setupForm() {

		// Create the Form
		form = new Form();
		form.setName("BatML Model Builder");

		// It could be the case that we've already created the EMFComponent,
		// if so just skip this creation stuff
		if (!initialized) {
			if (project != null && project.isAccessible()) {

				// Refresh the project space
				try {
					project.refreshLocal(IResource.DEPTH_INFINITE, null);
				} catch (CoreException e) {
					logger.error(getClass().getName() + " Exception!",e);
				}

				// Get the batml folder and the correct XML schema
				IFolder batmlFolder = project.getFolder("batml");
				IFile xsdIFile = batmlFolder.getFile("electrical.xsd");

				// If valid, create the Java file instance needed by
				// the EMFComponent
				if (xsdIFile.exists()) {
					try {
						xsdFile = EFS.getStore(xsdIFile.getLocationURI())
								.toLocalFile(0, new NullProgressMonitor());
					} catch (CoreException e) {
						logger.error(getClass().getName() + " Exception!",e);
					}

					// Create the EMFComponent
					if (xsdFile != null) {
						emfComp = new EMFComponent(xsdFile);
						emfComp.setName("BatML Model Editor");
						emfComp.setId(1);
						initialized = true;
					}

				} else {
					System.err
							.println("BatML Model Error. Cannot find requisite "
									+ "electrical.xsd schema file. No BatML tree will be constructed.");
				}
			}
		}

		// If we've been successful, add it to the Form.
		if (emfComp != null) {
			form.addComponent(emfComp);
		}

		return;
	}

	/**
	 * <p>
	 * This operation is used to setup the name and description of the model.
	 * </p>
	 *
	 */
	@Override
	protected void setupItemInfo() {

		// Local Declarations
		String desc = "This item builds models based on a BatteryML schema.";

		// Describe the Item
		setName("BatML Model Builder");
		setDescription(desc);
		itemType = ItemType.Model;

		// Setup the action list. Remove key-value pair support.
		// allowedActions.remove(taggedExportActionString);
		allowedActions.add("Write to XML");

		return;
	}

	/**
	 * <p>
	 * </p>
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
	 * <p>
	 * </p>
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
				if (emfComp.save(EFS.getStore(iFile.getLocationURI())
						.toLocalFile(0, new NullProgressMonitor()))) {
					retStatus = FormStatus.Processed;
				} else {
					retStatus = FormStatus.InfoError;
				}
			} catch (CoreException e) {
				logger.error(getClass().getName() + " Exception!",e);
				retStatus = FormStatus.InfoError;
			}
		}

		return retStatus;
	}

	/**
	 * <p>
	 * </p>
	 *
	 * @param input
	 *            The name of the input input file, including the file extension
	 */
	@Override
	public void loadInput(String input) {

		// Local Declarations
		// Local Declarations
		EMFComponent emfComp = (EMFComponent) form.getComponent(1);
		String path = project.getFolder("batml").getLocation().toOSString()
				+ System.getProperty("file.separator") + input;
		if (emfComp != null) {
			if (!emfComp.load(new File(path))) {
				System.err
						.println("Invalid file passed to SassenaCoherentModel.loadInput");
			}
		}
		return;
	}
}
