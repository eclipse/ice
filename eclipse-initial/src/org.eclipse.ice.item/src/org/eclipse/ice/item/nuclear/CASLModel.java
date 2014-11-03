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
package org.eclipse.ice.item.nuclear;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.persistence.Transient;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.eclipse.ice.datastructures.ICEObject.ICEJAXBManipulator;
import org.eclipse.ice.datastructures.form.AllowedValueType;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.datastructures.form.TreeComposite;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemType;
import org.eclipse.ice.item.utilities.trilinos.ParameterList;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * A CASL Item for modeling input and output.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author jaybilly
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
@XmlRootElement(name = "CASLModel")
public class CASLModel extends Item {

	/**
	 * The name of the file that is currently being used as a template for the
	 * model
	 */
	@XmlTransient
	@Transient
	private String currentTemplateFileName = null;

	/**
	 * The identification number of the data component that holds the template
	 * entry
	 */
	@XmlTransient
	@Transient
	private int selectorComponentId;

	/**
	 * The name of action for writing CASL Parameter lists
	 */
	@XmlTransient
	@Transient
	private static final String parameterListExportActionString = "Write CASL ParameterList file";

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
	public CASLModel() {
		// begin-user-code
		// Forward to the regular constructor
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
	public CASLModel(IProject projectSpace) {
		// begin-user-code
		// Pass to the super constructor
		super(projectSpace);
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation overrides Item.reviewEntries() to review the selected
	 * template for the CASLModel and load a different template, if required.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param preparedForm
	 *            <p>
	 *            The Form to review
	 *            </p>
	 * @return <p>
	 *         The status
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected FormStatus reviewEntries(Form preparedForm) {
		// begin-user-code

		// Local Declarations
		FormStatus retStatus = FormStatus.ReadyToProcess;
		DataComponent dataComp = null;
		Entry templateEntry = null;
		String templateName = null;

		// Grab the data component from the Form and only proceed if it exists
		dataComp = (DataComponent) preparedForm
				.getComponent(selectorComponentId);
		if (dataComp != null
				&& "VERA-CS Challenge Problem Templates".equals(dataComp
						.getName()) && selectorComponentId > 1) {
			// Get the entry that defines which template to use an get the name
			// of the template
			templateEntry = dataComp.retrieveAllEntries().get(0);
			templateName = templateEntry.getValue();
			// See if the template is different than the current one and re-load
			// it if needed
			if (!templateName.equals(currentTemplateFileName)) {
				// Retrieve the old tree composite
				TreeComposite tree = (TreeComposite) form.getComponent(1);
				// Copy the new one
				tree.copy(loadCASLParameterList(templateName));
				// Reset the template name
				currentTemplateFileName = templateName;
			}
		} else {
			retStatus = FormStatus.InfoError;
		}
		return retStatus;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation creates the CASL input file.
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
		IFile outputFile = null;
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		String filename = (form.getName() + "_" + form.getId()).replaceAll(
				"\\s+", "_");
		ParameterList paramList = new ParameterList();

		// Make sure the action is allowed and that the Item is enabled
		if (actionName.equals(parameterListExportActionString) && isEnabled()) {
			// Convert the TreeComposite to a ParameterList
			paramList.fromTreeComposite((TreeComposite) form.getComponent(1));
			// Write the parameter list to XML
			paramList.persistToXML(outputStream);
			// Setup the IFile handle
			outputFile = project.getFile(filename + ".xml");
			try {
				// If the output file already exists, delete it
				if (outputFile.exists()) {
					outputFile.delete(false, null);
				}
				// Create the contents of the IFile from the output stream
				outputFile.create(
						new ByteArrayInputStream(outputStream.toByteArray()),
						false, null);
				retStatus = FormStatus.Processed;
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			// Pass to this operation on Item. Maybe it can deal with the
			// request.
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
	 * This operation returns the list of XML files in the "casl" folder of the
	 * project space or null if the folder does not exist or there are no files.
	 * 
	 * @return
	 */
	private ArrayList<String> getProjectFiles() {

		// Local Declarations
		String extension = "xml";
		String projectFolderPrefix = "casl";
		ArrayList<String> files = new ArrayList<String>();

		// Add files from the project space to the Form
		if (project != null) {
			try {
				// Get the CASL folder
				IFolder caslFolder = project.getFolder(projectFolderPrefix);
				// If it exists, get any existing problem files out of it
				if (caslFolder.exists()) {
					// Get any resources that exist
					IResource[] resources = caslFolder.members();
					// Add them to the list of input files
					for (int i = 0; i < resources.length; i++) {
						// Check the resource and file type for files and the
						// proper extension respectively
						if (resources[i].getType() == IResource.FILE
								&& ((IFile) resources[i]).getFileExtension()
										.equals(extension)) {
							files.add(resources[i].getName());
						}
					}
				} else {
					// Otherwise, return a null list so that the caller knows
					// there are no resources.
					return null;
				}
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return files;
	}

	/**
	 * This operation returns the parameter list read from a CASL input file as
	 * an ICE TreeComposite.
	 * 
	 * @param filename
	 *            The name of the file in the project space that contains the
	 *            parameter list
	 * @return The tree composite that represents the parameter list
	 */
	private TreeComposite loadCASLParameterList(String filename) {

		// Local Declarations
		ICEJAXBManipulator jaxbLoader = new ICEJAXBManipulator();

		// Load the file
		IFile xmlFile = project.getFolder("casl").getFile(filename);
		try {
			// Load the parameter list from the first file
			InputStream xmlFileStream = xmlFile.getContents();
			ParameterList pList = (ParameterList) jaxbLoader.read(
					ParameterList.class, xmlFileStream);
			// Explicitly set the id of the PL TreeComposite to 1 so that we
			// know where to look for it later.
			TreeComposite pListTreeComp = pList.toTreeComposite();
			pListTreeComp.setId(1);
			// Log success
			System.out
					.println("CASLModel Message: Loaded template " + filename);
			return pListTreeComp;
		} catch (CoreException e) {
			// Complain
			e.printStackTrace();
		} catch (NullPointerException e) {
			// Complain
			e.printStackTrace();
		} catch (JAXBException e) {
			// Complain
			e.printStackTrace();
		} catch (IOException e) {
			// Complain
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * This operation returns a data component that contains the list of files
	 * that may be selected by a client from the CASL directory. This component
	 * will be empty if there are no files and contain an Entry that says
	 * "No templates available."
	 * 
	 * @param files
	 *            The set of files that are available to the client
	 * @return the data component
	 */
	private DataComponent createSelectorComponent(final ArrayList<String> files) {

		// Local Declaration
		int lastIndex = 0, lastId = 1;
		DataComponent filesComp = new DataComponent();
		Entry filesEntry = null;
		final String noFilesValue = "No templates available.";
		String entryName = "Available templates";
		String entryDesc = "The problem file template that will be loaded.";
		int entryId = 1;

		// Setup the data component
		filesComp.setName("VERA-CS Challenge Problem Templates");
		filesComp
				.setDescription("The following is a list of VERA-CS challenge "
						+ "problems that may be selected as template from which more "
						+ "specific problems can be created.");
		// Get the last component id in the form
		if (!(form.getComponents().isEmpty())) {
			lastIndex = form.getComponents().size() - 1;
			lastId = form.getComponents().get(lastIndex).getId();
		}
		// Get the id based on the last component in the Form
		selectorComponentId = lastId + 1;
		filesComp.setId(selectorComponentId);

		// Configure the values of the file Entry
		if (files != null && !(files.isEmpty())) {
			// Setup the Entry with the list of files
			filesEntry = new Entry() {
				protected void setup() {
					allowedValues.addAll(files);
					allowedValueType = AllowedValueType.Discrete;
				}
			};
		} else {
			// Setup the Entry with only value to describe that there are no
			// templates.
			filesEntry = new Entry() {
				protected void setup() {
					allowedValues.add(noFilesValue);
					allowedValueType = AllowedValueType.Discrete;
				}
			};
		}

		// Setup the file Entry's descriptive information
		filesEntry.setName(entryName);
		filesEntry.setDescription(entryDesc);
		filesEntry.setId(entryId);

		// Add the Entry to the Component
		filesComp.addEntry(filesEntry);

		return filesComp;

	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation sets up the form for the CASLModel.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Override
	protected void setupForm() {
		// begin-user-code

		// Local declarations
		ArrayList<String> problemFiles = null;

		// Create the Form
		form = new Form();

		// FIX ME! Need to get this from the project!
		// Get the sample CASL file
		if (project != null) {
			// Get the list of CASL problems available in the casl directory
			// of the project space that can be used as templates
			problemFiles = getProjectFiles();
			System.out.println("CASLModel Message: Found problem files: "
					+ problemFiles);
			// If there are project files, create the list and load the first as
			// the starting problem.
			if (problemFiles != null && !(problemFiles.isEmpty())) {
				// Set the default problem
				currentTemplateFileName = problemFiles.get(0);
				form.addComponent(loadCASLParameterList(currentTemplateFileName));
			}
			// Also create the data component that selects
			// which PL to load by client choice.
			form.addComponent(createSelectorComponent(problemFiles));
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
	@Override
	protected void setupItemInfo() {
		// begin-user-code

		// Set the name, description and type
		setName("VERA-CS Neutronics Model Builder");
		setDescription("VERA-CS is the a nuclear reactor core simulator from "
				+ "CASL that is primarily focused on neutronics.");
		itemType = ItemType.Model;

		// Add the write action and remove the key-value pair right action.
		allowedActions.remove(taggedExportActionString);
		allowedActions.add(parameterListExportActionString);

		return;
		// end-user-code
	}

}