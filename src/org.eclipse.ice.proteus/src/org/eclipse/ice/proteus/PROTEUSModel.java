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

import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemType;

import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

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
 * @author bkj
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
@XmlRootElement(name = "PROTEUSModel")
public class PROTEUSModel extends Item {

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The process tag for writing the PROTEUS output file.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlTransient
	@Transient
	protected static final String proteusProcessActionString = "Write PROTEUS File";

	/**
	 * The id of the neutronics data component in the Form
	 */
	private int neutronicsComponentId = 1;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The constructor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public PROTEUSModel() {
		// begin-user-code
		this(null);
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The constructor with a project space in which files should be
	 * manipulated.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param projectSpace
	 *            <p>
	 *            The Eclipse project where files should be stored and from
	 *            which they should be retrieved.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public PROTEUSModel(IProject projectSpace) {
		// begin-user-code

		// Call super
		super(projectSpace);

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation creates the PROTEUS input file.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param actionName
	 *            <p>
	 *            The name of action that should be performed using the
	 *            processed Form data.
	 *            </p>
	 * @return <p>
	 *         The status of the Item after processing the Form and executing
	 *         the action. It returns FormStatus.InfoError if it is unable to
	 *         run for any reason, including being asked to run actions that are
	 *         not in the list of available actions.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public FormStatus process(String actionName) {
		// begin-user-code

		// Local Declarations
		FormStatus retStatus = FormStatus.InfoError;
		String outputFilename = "proteus_neutronics_" + getId() + ".inp";
		IFile outputFile;
		FileWriter writer = null;
		DataComponent neutronicsComponent = null;
		ArrayList<Entry> neutronicsEntries = null;
		Entry tmpEntry = null;
		String headerString = "! PROTEUS Input File. Created by ICE.";
		InputStream headerStream = new ByteArrayInputStream(
				headerString.getBytes());

		// Check that the process is something that we will do and that the Item
		// is enabled
		if (proteusProcessActionString.equals(actionName) && isEnabled()) {
			// Get the file
			outputFile = project.getFile(outputFilename);
			// Write the file and update the project space
			try {
				// Create the file if necessary
				if (!outputFile.exists()) {
					outputFile.create(headerStream, true, null);
				}
				// Create the writer. Overwrite, never append.
				writer = new FileWriter(outputFile.getLocation().toFile(),
						false);

				// Loop through each DataComponent contained in the Form
				for (int i = 1; i <= form.getNumberOfComponents(); i++) {

					// Grab the Entries from the Form
					neutronicsComponent = (DataComponent) form.getComponent(i);
					neutronicsEntries = neutronicsComponent
							.retrieveAllEntries();

					// Write the Entries to the output file
					for (int j = 0; j < neutronicsEntries.size(); j++) {
						tmpEntry = neutronicsEntries.get(j);
						writer.write(tmpEntry.getName() + "\t"
								+ tmpEntry.getValue() + "\t!"
								+ tmpEntry.getDescription() + "\n");
					}
				}
				// Close the writer
				writer.close();
				// Refresh the project
				project.refreshLocal(IResource.DEPTH_INFINITE, null);
			} catch (CoreException e) {
				// Complain
				System.out.println("PROTEUSModel Exception: "
						+ "Unable to refresh "
						+ "project space after creating output file!");
				e.printStackTrace();
			} catch (IOException e) {
				// Complain
				System.out.println("PROTEUSModel Exception: "
						+ "Unable to save neutronics file!");
				e.printStackTrace();
			}
			// Update the status
			retStatus = FormStatus.Processed;
		} else {
			// Otherwise, punt it up to the parent to see if it can handle it.
			super.process(actionName);
		}

		// Reset the status and return. It should only be updated if the Item is
		// enabled.
		if (isEnabled()) {
			status = retStatus;
			return retStatus;
		} else {
			return FormStatus.Unacceptable;
		}

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
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
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected void setupForm() {
		// begin-user-code

		// Create the Form
		form = new Form();

		// Get the neutronics spec file from the project
		if (project != null && project.isAccessible()) {
			// Get the PROTEUS folder
			IFolder proteusFolder = this.getPreferencesDirectory();
			// Get the files from it if it exists
			if (proteusFolder.exists()) {
				try {
					IResource[] resources = proteusFolder.members();
					// Check the resources and retrieve the spec file.
					for (IResource resource : resources) {
						if (resource.getType() == IResource.FILE
								&& resource.getProjectRelativePath()
										.lastSegment()
										.contains("ICEProteusInput")) {
							// Get the File
							InputStream stream = ((IFile) resource)
									.getContents();
							// Load the Form from the stream
							form.loadFromXML(stream);
						}
					}
				} catch (CoreException e) {
					// Complain
					e.printStackTrace();
				}
			}
		}

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation is used to setup the name and description of the model.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected void setupItemInfo() {
		// begin-user-code

		// Local Declarations
		String desc = "This item builds models for "
				+ "PROTEUS-based applications for simulating "
				+ "sodium-cooled fast reactors.";

		// Describe the Item
		setName("PROTEUS Model Builder");
		setDescription(desc);
		itemType = ItemType.Model;

		// Setup the action list. Remove key-value pair support.
		allowedActions.remove(taggedExportActionString);
		// Add PROTEUS GetPot export action
		allowedActions.add(proteusProcessActionString);

		return;
		// end-user-code
	}

}