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
package org.eclipse.ice.proteus;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemType;
import org.eclipse.io.ini.INIReader;
import org.eclipse.io.ini.INIWriter;

/**
 * <p>
 * An PROTEUS Item for creating PROTEUS input files. This Item expects to find
 * the input specifications generated from PROTEUS in the ${workspace}/SHARP
 * directory. The specifications can be found at http://projects.eclipse.org/projects/technology.ice in
 * the "files" section or in the ICE repository. The files are in the native ICE
 * form specified by the XML schema (ICESchema.xsd) available from the same
 * source.
 * </p>
 * <p>
 * This class' Item builder sets the identity of the PROTEUS-based application
 * using loadSpecContents().
 * </p>
 * 
 * @author Jay Jay Billings, Andrew Bennett
 */
@XmlRootElement(name = "PROTEUSModel")
public class PROTEUSModel extends Item {

	/**
	 * <p>
	 * The process tag for writing the PROTEUS output file.
	 * </p>
	 */
	@XmlTransient
	protected static final String proteusProcessActionString = "Write PROTEUS Input File";

	/**
	 * The id of the neutronics data component in the Form
	 */
	private int neutronicsComponentId = 1;
	
	/**
	 * A list of the things that the PROTEUS Model can do
	 */
	private ArrayList<String> actionItems;
	
	/**
	 * <p>
	 * The constructor.
	 * </p>
	 */
	public PROTEUSModel() {
		// begin-user-code
		this(null);
		// end-user-code
	}

	/**
	 * <p>
	 * The constructor with a project space in which files should be
	 * manipulated.
	 * </p>
	 * 
	 * @param projectSpace
	 *            The Eclipse project where files should be stored and from
	 *            which they should be retrieved.
	 */
	public PROTEUSModel(IProject projectSpace) {
		// begin-user-code

		// Call super
		super(projectSpace);

		// end-user-code
	}

	/**
	 * <p>
	 * This operation sets up the Form for the PROTEUSModel. The Form contains a
	 * DataComponent with id=1 that contains Entries for the names of the
	 * PROTEUS application for which input should be modeled and for the output
	 * file and a TreeComposite with id = 2 that contains the PROTEUS input
	 * tree.
	 * </p>
	 * <p>
	 * The Entries in the DataComponent are named "PROTEUS-Based Application"
	 * with id = 1 and "Output File Name" with id=2. The TreeComposite is empty.
	 * </p>
	 */
	protected void setupForm() {
		// Create the Form
		form = new Form();
	
		if (project != null) {
			loadInput(null);
		}
	}

	/**
	 * <p>
	 * This operation is used to setup the name and description of the model.
	 * </p>
	 */
	protected void setupItemInfo() {
		// Local Declarations
		String desc = "Generate input files for the PROTEUS-SN neutron transport";
	
		// Describe the Item
		setName("PROTEUS Model");
		setDescription(desc);
		itemType = ItemType.Model;
	
		// Setup the action list. Remove key-value pair support.
		allowedActions.remove(taggedExportActionString);
		// Add PROTEUS GetPot export action
		allowedActions.add(0, proteusProcessActionString);
		actionItems = getAvailableActions();
		return;
	}

	/**
	 * <p>
	 * Overrides the reviewEntries operation. This will still call
	 * super.reviewEntries, but will handle the dependencies after all other dep
	 * handing is finished.
	 * </p>
	 * 
	 * @return the status of the form
	 */
	protected FormStatus reviewEntries(Form preparedForm) {
		FormStatus retStatus = FormStatus.ReadyToProcess;
		ArrayList<Component> components = preparedForm.getComponents();

		// Make sure the form has the right amount of data
		if (components.size() < 1) {
			System.out.println("ProteusModel Message: Could not find enough data to write a complete input format.");
			retStatus = FormStatus.InfoError;
		}
		return retStatus;
	}	
	
	/**
	 * <p>
	 * This operation creates the PROTEUS input file.
	 * </p>
	 * 
	 * @param actionName
	 *            The name of action that should be performed using the
	 *            processed Form data.
	 * @return 
	 *         The status of the Item after processing the Form and executing
	 *         the action. It returns FormStatus.InfoError if it is unable to
	 *         run for any reason, including being asked to run actions that are
	 *         not in the list of available actions.
	 */
	public FormStatus process(String actionName) {
		FormStatus retStatus;
		
		if (this.proteusProcessActionString.equals(actionName)) {

			// Get the file from the project space to create the output
			String filename = getName().replaceAll("\\s+", "_") + "_" + getId()
					+ ".inp";
			// Get the file path and build the URI that will be used to write
			IFile outputFile = project.getFile(filename);

			// Get the data from the form
			ArrayList<Component> components = form.getComponents();

			// A valid VibeModel needs 4 components
			if (components.size() > 0) {

				// create a new IPSWriter with the output file
				INIWriter writer = new INIWriter();
				try {
					// Write the output file
					writer.write(form, outputFile);
					// Refresh the project space
					project.refreshLocal(IResource.DEPTH_ONE, null);
				} catch (CoreException e) {
					// Complain
					System.err.println("ProteusModel Message: "
							+ "Failed to refresh the project space.");
					e.printStackTrace();
				}
				// return a success
				retStatus = FormStatus.Processed;
			} else {
				// return an error
				System.err.println("Not enough components to write new file!");
				retStatus = FormStatus.InfoError;
			}			
		} else {
			retStatus = super.process(actionName);
		}
		
		return retStatus;
	}

	
	/**
	 * This operation loads the given example into the Form.
	 * 
	 * @param name
	 *            The path name of the example file name to load.
	 */
	public void loadInput(String name) {
		// If nothing is specified, load case 6 from inside the plugin
		IFile inputFile = null;
		
		// Get the file specified, or some default one
		if (name == null) {
			inputFile = project.getFile("PROTEUS_Model_Builder" + System.getProperty("file.separator") + "proteus_model.inp");
		} else {
			inputFile = project.getFile(name);
		}
		
		// Load the components from the file and setup the form
		System.out.println("ProteusModel Message: Loading " + inputFile.getLocation().toOSString());

		INIReader reader = new INIReader();
		form = reader.read(inputFile);
		if (form != null) {
			form.setName(getName());
			form.setDescription(getDescription());
			form.setId(getId());
			form.setItemID(getId());
			form.setActionList(actionItems);
		} else {
			System.out.println("FORM IS NULL");
		}
	}
}