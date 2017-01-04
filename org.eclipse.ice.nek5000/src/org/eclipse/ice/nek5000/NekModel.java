/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.nek5000;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.eavp.viz.modeling.factory.IControllerProviderFactory;
import org.eclipse.eavp.viz.service.IVizService;
import org.eclipse.eavp.viz.service.IVizServiceFactory;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemType;
import org.eclipse.ice.nek5000.internal.VizServiceFactoryHolder;
import org.eclipse.january.form.Component;
import org.eclipse.january.form.DataComponent;
import org.eclipse.january.form.DiscreteEntry;
import org.eclipse.january.form.Form;
import org.eclipse.january.form.FormStatus;
import org.eclipse.january.form.IEntry;
import org.eclipse.january.form.MeshComponent;

/**
 * This class extends the Item to create a modeler for Nek5000 input files. It
 * uses existing Nek5000 examples to seed the Form.
 *
 * @author Jay Jay Billings and Anna Wojtowicz
 *
 */
@XmlRootElement(name = "NekModel")
public class NekModel extends Item {

	/**
	 * The NekReader used to read Nek example REA files.
	 */
	private NekReader reader;

	/**
	 * The NekWriter used to write Nek REA files.
	 */
	private NekWriter writer;

	/**
	 * The name of the Nek5000 example problem currently loaded into the Form.
	 */
	protected String exampleName; // Default for now

	/**
	 * The identification number of the data component that holds the template
	 * entry
	 */
	private int selectorComponentId;

	/**
	 * The action name for reading a Nek5000 input file.
	 */
	public static final String nekReadActionString = "Read Nek5000 Input";

	/**
	 * The action name for writing a Nek5000 input file.
	 */
	public static final String nekWriteActionString = "Write Nek5000 Input";

	/**
	 * A flag signaling that the construction of the item has finished.
	 */
	private boolean constructionFinished = false;

	/**
	 * The factory containing the visualization services.
	 */
	private IVizServiceFactory factory;

	/**
	 * The nullary constructor. This should only be used for testing.
	 */
	public NekModel() {
		this(null);

		reader = new NekReader();
		constructionFinished = true;

		return;
	}

	/**
	 * The default constructor.
	 *
	 * @param projectSpace
	 *            The Eclipse IProject that stores data related to this Item.
	 */
	public NekModel(IProject projectSpace) {
		super(projectSpace);

		reader = new NekReader();
		constructionFinished = true;
		setupForm();

		return;
	}

	/**
	 * Constructor for specifying the factory to be used to produce data objects
	 * from the input in the read files.
	 *
	 * @param projectSpace
	 *            The Eclipse IProject that stores data related to this Item.
	 * @param factory
	 *            The factory which will create views and controllers for meshes
	 *            read from the files.
	 */
	public NekModel(IProject projectSpace, IControllerProviderFactory factory) {
		super(projectSpace);

		reader = new NekReader();
		reader.setControllerFactory(factory);
		constructionFinished = true;
		setupForm();

		return;
	}

	/**
	 * This operation returns the list of reafiles in the
	 * "Nek5000_Model_Builder" folder of the project space, or null if the
	 * folder does not exist or there are no files.
	 *
	 * @return
	 */
	private ArrayList<String> getProjectFiles() {

		// Local Declarations
		ArrayList<String> files = new ArrayList<String>();

		// Add files from the project space to the Form
		if (project != null) {
			try {
				// Get the Nek folder
				IFolder nekFolder = getPreferencesDirectory();
				project.refreshLocal(IResource.DEPTH_INFINITE, null);
				// If it exists, get any existing problem files out of it
				if (nekFolder.exists()) {
					// Get any resources that exist
					IResource[] resources = nekFolder.members();
					// Add them to the list of input files
					for (IResource resource : resources) {
						// Dump the name of this file if debugging is enabled
						logger.debug("NekModel Message: " + "Found file "
								+ resource.getName() + ".");
						// See if the resource is a file with a .rea extension.
						if (resource.getType() == IResource.FILE
								&& resource.getProjectRelativePath()
										.lastSegment().contains(".rea")) {
							files.add(resource.getName());
						}
					}
				} else {
					// Otherwise, return a null list so that the caller knows
					// there are no resources.
					return null;
				}
			} catch (CoreException e) {
				logger.error(getClass().getName() + " Exception!", e);
			}
		}

		return files;
	}

	/**
	 * This operation returns a data component that contains the list of
	 * reafiles that may be selected by a user from the Nek5000_Model_Builder
	 * directory. This component will be empty if there are no files and contain
	 * an Entry that says "No problems available."
	 *
	 * @param files
	 *            The set of Nek reafiles available to be loaded
	 * @return the data component
	 */
	private DataComponent createSelectorComponent(
			final ArrayList<String> files) {

		// Local Declaration
		DataComponent filesComp = new DataComponent();
		IEntry filesEntry = null;
		final String noFilesValue = "No examples available.";
		String entryName = "Available examples";
		String entryDesc = "The example problem file that will be loaded.";
		selectorComponentId = 1;

		// Setup the data component
		filesComp.setName("Nek5000 Example Problems");
		filesComp.setDescription("The following is a list of Nek5000 example "
				+ "problems available to modify in ICE.");
		// // Get the last component id in the form
		// // Get the id based on the last component in the Form
		filesComp.setId(selectorComponentId);

		// Configure the values of the file Entry
		if (files != null && !(files.isEmpty())) {
			// Setup the Entry with the list of files
			filesEntry = new DiscreteEntry();
			filesEntry.setAllowedValues(files);
			filesEntry.setDefaultValue(files.get(0));
			filesEntry.setValue(files.get(0));
		} else {
			// Setup the Entry with only value to describe that there are no
			// examples.
			filesEntry = new DiscreteEntry();
			filesEntry.setAllowedValues(Arrays.asList(noFilesValue));
			filesEntry.setDefaultValue(noFilesValue);
			filesEntry.setValue(noFilesValue);
		}

		// Setup the file Entry's descriptive information
		filesEntry.setName(entryName);
		filesEntry.setDescription(entryDesc);
		filesEntry.setId(1);

		// Add the Entry to the Component
		filesComp.addEntry(filesEntry);

		return filesComp;

	}

	/**
	 * <p>
	 * This operation overrides Item.reviewEntries() to review the selected
	 * example problem and load a different one, if required.
	 * </p>
	 *
	 * @param preparedForm
	 *            The Form to review
	 * @return The status
	 *
	 */
	@Override
	protected FormStatus reviewEntries(Form preparedForm) {

		// Local Declarations
		FormStatus retStatus = FormStatus.ReadyToProcess;
		DataComponent dataComp = null;
		IEntry problemEntry = null;
		String problemName = null;
		String separator = System.getProperty("file.separator");

		// Grab the data component from the Form and only proceed if it exists
		dataComp = (DataComponent) preparedForm
				.getComponent(selectorComponentId);
		if (dataComp != null
				&& "Nek5000 Example Problems".equals(dataComp.getName())
				&& selectorComponentId == 1) {

			// Get the entry that defines which problem to use an get the name
			// of the template
			problemEntry = dataComp.retrieveAllEntries().get(0);
			problemName = problemEntry.getValue();

			// See if the problem is different than the current one and re-load
			// it if needed
			if (!problemName.equals(exampleName)) {

				IFolder nekFolder = getPreferencesDirectory();
				String problemPathName = nekFolder.getLocation().toOSString()
						+ separator + problemName;
				try {
					// Load new problem and set new example name
					loadExample(problemPathName);
					logger.info("NekModel Message: Loading example: "
							+ problemName);
					exampleName = problemName;
				} catch (FileNotFoundException e) {
					logger.debug("NekModel Message: " + "Could not find file "
							+ problemName);
					logger.error(getClass().getName() + " Exception!", e);
				} catch (IOException e) {
					logger.debug("NekModel Message: " + "Could not read file "
							+ problemName);
					logger.error(getClass().getName() + " Exception!", e);
				}
			}
		} else {
			retStatus = FormStatus.InfoError;
		}
		return retStatus;

	}

	/**
	 * This operation loads the given example into the Form.
	 *
	 * @param name
	 *            The path name of the example file name to load.
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private void loadExample(String name)
			throws FileNotFoundException, IOException {

		// Get the factory
		if (factory == null) {
			factory = VizServiceFactoryHolder.getFactory();
		}

		// TODO Provide a way for the user to select which mesh editor service
		// is desired
		// Set the reader's ControllerProviderFactory
		IVizService service = factory.get("ICE JavaFX Mesh Editor");
		reader.setControllerFactory(service.getControllerProviderFactory());

		// Load the components from the file
		File file = new File(name);
		ArrayList<Component> components = reader.loadREAFile(file);
		ArrayList<Component> existingComponents = form.getComponents();

		// Update the components by copying the new ones
		if (components != null && existingComponents.size() > 1) {
			// First component should be the example selector, leave it be
			// After that, according to the NekReader spec, the first four
			// components are DataComponents
			((DataComponent) existingComponents.get(1))
					.copy((DataComponent) components.get(0));
			((DataComponent) existingComponents.get(2))
					.copy((DataComponent) components.get(1));
			((DataComponent) existingComponents.get(3))
					.copy((DataComponent) components.get(2));
			((DataComponent) existingComponents.get(4))
					.copy((DataComponent) components.get(3));
			// Then two MeshComponents
			((MeshComponent) existingComponents.get(5))
					.copy((MeshComponent) components.get(4));
			((MeshComponent) existingComponents.get(6))
					.copy((MeshComponent) components.get(5));
			// Then the rest are DataComponents
			((DataComponent) existingComponents.get(7))
					.copy((DataComponent) components.get(6));
			((DataComponent) existingComponents.get(8))
					.copy((DataComponent) components.get(7));
			((DataComponent) existingComponents.get(9))
					.copy((DataComponent) components.get(8));
			((DataComponent) existingComponents.get(10))
					.copy((DataComponent) components.get(9));
			((DataComponent) existingComponents.get(11))
					.copy((DataComponent) components.get(10));
			((DataComponent) existingComponents.get(12))
					.copy((DataComponent) components.get(11));
			((DataComponent) existingComponents.get(13))
					.copy((DataComponent) components.get(12));
		} else if (components != null) {
			// Add the components since the form is empty (aside from the
			// example selector)
			for (int i = 0; i < components.size(); i++) {
				form.addComponent(components.get(i));
			}
		} else {
			// Complain
			System.err.println("NekModel Message: "
					+ "No components found in form " + name + ".");
		}

		return;
	}

	/**
	 * This example writes the set of components to an REA file.
	 *
	 * @param components
	 */
	private void writeREAFile() {

		// Get the file from the project space
		String filename = getName().replaceAll("\\s+", "_") + "_" + getId()
				+ ".rea";
		IFile outputFile = project.getFile(filename);
		// Get the file path
		String outputFilePath = outputFile.getLocation().toString();

		// Print some debugging information
		logger.debug("NekModel Message: " + "Writing Nek input file to "
				+ outputFilePath + ".");

		// Setup the writer
		writer = new NekWriter();
		// Create the file
		File reaFile = new File(outputFilePath);
		try {
			reaFile.createNewFile();
			writer.writeReaFile(form.getComponents(), reaFile,
					reader.getLastProperties());
			// Refresh the project space
			project.refreshLocal(IResource.DEPTH_ONE, null);
		} catch (IOException e) {
			// Complain
			System.err.println(
					"NekModel Message: " + "Failed to write the file.");
			logger.error(getClass().getName() + " Exception!", e);
		} catch (CoreException e) {
			// Complain
			System.err.println("NekModel Message: "
					+ "Failed to refresh the project space.");
			logger.error(getClass().getName() + " Exception!", e);
		}

		return;
	}

	/**
	 * This operation sets the information that identifies this Item.
	 */
	@Override
	protected void setupItemInfo() {

		// Local declarations
		String description = "This item builds Nek5000 input models "
				+ "for simulating fluid dynamics in sodium-cooled fast "
				+ "reactors.";

		// Set the model defaults
		setName(NekModelBuilder.name);
		setDescription(description);
		setItemBuilderName(NekModelBuilder.name);
		itemType = ItemType.Model;

		// Set the default example name
		exampleName = "conj_ht.rea";

		// Setup the action list. Remove key-value pair support.
		allowedActions.remove(taggedExportActionString);
		// Add Nek5000 reader action string
		allowedActions.add(nekReadActionString);
		// Add Nek5000 writer action string
		allowedActions.add(nekWriteActionString);

		return;
	}

	/**
	 * This operation sets up the Form that contains the Nek5000 information.
	 */
	@Override
	protected void setupForm() {

		// Create the Form
		if (form == null) {
			form = new Form();
		}
		ArrayList<String> problemFiles = null;
		String separator = System.getProperty("file.separator");

		// Get the Nek .rea file from the project
		if (project != null && project.isAccessible()) {

			// Get the Nek5000 folder
			IFolder nekFolder = getPreferencesDirectory();

			if (constructionFinished) {
				// Get the files from it if it exists
				if (nekFolder.exists()) {
					try {

						// Grab the list of problem files in the Nek directory
						problemFiles = getProjectFiles();

						// Create the DataComponent that selects which problem
						// to load
						form.addComponent(
								createSelectorComponent(problemFiles));

						// If the list of problem files is valid
						if (problemFiles != null && !(problemFiles.isEmpty())) {

							// Push the work onto the loader
							loadExample(nekFolder.getLocation().toOSString()
									+ separator + problemFiles.get(0));
						}

					} catch (FileNotFoundException e) {
						// Complain
						System.err.println("NekModel Message: "
								+ "Unable to find REA file.");
						logger.error(getClass().getName() + " Exception!", e);
					} catch (IOException e) {
						// Complain
						System.err.println("NekModel Message: "
								+ "Unable to load REA file.");
						logger.error(getClass().getName() + " Exception!", e);
					}
				}
			}

		}

		return;
	}

	/**
	 * This operation writes the Nek5000 file to disk.
	 */
	@Override
	public FormStatus process(String actionName) {

		// Local Declarations
		FormStatus retStatus = FormStatus.Unacceptable;

		// We need the project space for this, so check it
		if (project != null) {
			// Only process the request if the Item is enabled and the correct
			// action was selected.
			if (isEnabled() && actionName.equals(nekWriteActionString)) {
				// Write the file
				writeREAFile();
				// Set the status
				retStatus = FormStatus.Processed;
			} else {
				// Otherwise pass it up
				retStatus = super.process(actionName);
			}
		}

		// Reset the status and return. It should only be updated if the Item is
		// enabled.
		if (isEnabled()) {
			status = retStatus;
			return status;
		} else {
			return retStatus;
		}
	}

	/**
	 * Setter method for the factory which will produce views and controllers
	 * for the objects read from the Nek file.
	 * 
	 * @param factory
	 *            The reader's new factory
	 */
	public void setControllerFactory(IControllerProviderFactory factory) {
		reader.setControllerFactory(factory);
	}

}
