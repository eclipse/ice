/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.item.jobprofile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import javax.xml.bind.annotation.XmlRootElement;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.datastructures.form.TableComponent;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemType;
import org.eclipse.ice.item.jobLauncher.JobLauncher;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * The JobProfile is responsible for creating JobLaunchers programmatically
 * based on information contained within the JobProfileForm. Submitting a
 * complete Form and calling process() will create a JobLauncher in memory and
 * persist it the project space
 * (ICEFiles/default/jobProfiles/Item.getName().xml) as an XML file that will be
 * loaded when ICE is restarted (or the serialized items are otherwise forced to
 * reload).
 * </p>
 * <p>
 * The JobProfile is meant to be used in tandem with the SerializedItemBuilder
 * and it creates XML files that it expects to be loaded by instances of that
 * class. The JobProfile sets the builder name of the new JobLauncher to the
 * Item name so that the SerializedItemBuilder used to load it can be found even
 * after the Item is persisted to the database.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Jay Jay Billings
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
/*
 * @Entity
 * 
 * @Table(name = "JobProfile")
 */
@XmlRootElement(name = "JobProfile")
public class JobProfile extends Item {
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
	public JobProfile() {
		// begin-user-code

		// Call the other constructor
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
	public JobProfile(IProject projectSpace) {
		// begin-user-code

		// Call the super constructor
		super(projectSpace);

		// Construct the Allowed Actions
		allowedActions = new ArrayList<String>();
		allowedActions.add("Create A JobLauncher");

		// Set the Item type
		itemType = ItemType.Model;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation creates a new JobLauncher and writes it to disk if the
	 * action name is equal to "Create new Job Launcher" and it forwards the
	 * call to the Item base class if it is equal to something else.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param actionName
	 *            <p>
	 *            The name of the action to perform.
	 *            </p>
	 * @return <p>
	 *         The status.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public FormStatus process(String actionName) {
		// begin-user-code

		// Local Declarations
		FormStatus status = FormStatus.InfoError;

		// Check the action
		if ("Create a Job Launcher".equals(actionName)) {

			// Local declarations within this scope:
			DataComponent exeInfo = null;
			DataComponent threadOps = null;
			TableComponent hostnames = null;
			Entry entry = null;

			// Create a new JobLauncher
			JobLauncher launcher = new JobLauncher(project);

			// Return status if form is not setup.
			if (form == null) {
				return status;
			}

			// Get the components
			exeInfo = (DataComponent) form.getComponents().get(0);
			threadOps = (DataComponent) form.getComponents().get(1);
			hostnames = (TableComponent) form.getComponents().get(2);

			// Set the launcher information - fills out the form Name,
			// description, and execution name + parameters
			ArrayList<Entry> execEntries = exeInfo.retrieveAllEntries();
			String execName = execEntries.get(0).getValue();
			String execDesc = "This operation will execute "
					+ execEntries.get(0).getValue();
			String executable = exeInfo.retrieveAllEntries().get(1).getValue();
			// Set the executable on the launcher
			launcher.setExecutable(execName, execDesc, executable);

			// Setup the launcher -> it will setup the form
			launcher.setName(exeInfo.retrieveAllEntries().get(0).getValue());
			launcher.setDescription("This operation will execute "
					+ exeInfo.retrieveAllEntries().get(0).getValue());

			System.out.println("Creating " + launcher.getForm().getName()
					+ " profile.");

			// Setup hostnames
			for (int i = 0; i < hostnames.getRowIds().size(); i++) {
				launcher.addHost(hostnames.getRow(i).get(0).getValue(),
						hostnames.getRow(i).get(1).getValue(), hostnames
								.getRow(i).get(2).getValue());
			}
			// setup mpi and open mp

			// Enable or disable OpenMP
			// The first entry represents the T/F statement for enabling or
			// disabling
			// value. The next entry are the values and limitations.

			if ("Yes".equals(threadOps.retrieveAllEntries().get(0).getValue())) {
				entry = threadOps.retrieveAllEntries().get(1);
				launcher.enableOpenMP(
						Integer.parseInt(entry.getAllowedValues().get(0)),
						Integer.parseInt(entry.getAllowedValues().get(1)),
						Integer.parseInt(entry.getValue()));
			} else {
				launcher.disableOpenMP();
			}

			// Enable or disable MPI
			// The first entry represents the T/F statement for enabling or
			// disabling
			// value. The next entry are the values and limitations.
			if ("Yes".equals(threadOps.retrieveAllEntries().get(2).getValue())) {
				entry = threadOps.retrieveAllEntries().get(3);
				launcher.enableMPI(
						Integer.parseInt(entry.getAllowedValues().get(0)),
						Integer.parseInt(entry.getAllowedValues().get(1)),
						Integer.parseInt(entry.getValue()));
			} else {
				launcher.disableMPI();
			}

			// Enable or disable TBB
			// The first entry represents the T/F statement for enabling or
			// disabling
			// value. The next entry are the values and limitations.
			if ("Yes".equals(threadOps.retrieveAllEntries().get(4).getValue())) {
				entry = threadOps.retrieveAllEntries().get(5);
				launcher.enableTBB(
						Integer.parseInt(entry.getAllowedValues().get(0)),
						Integer.parseInt(entry.getAllowedValues().get(1)),
						Integer.parseInt(entry.getValue()));
			} else {
				launcher.disableTBB();
			}

			// Set the Launcher's builder name so that it can be loaded
			// correctly.
			launcher.setItemBuilderName(launcher.getName());

			// Check for the jobProfiles folder and create it if it doesn't
			// exist
			IFolder jobProfileFolder = project.getFolder("jobProfiles");
			if (!jobProfileFolder.exists()) {
				// Most likely the folder already exists, but we have to create
				// it if it does not
				try {
					jobProfileFolder.create(true, true, null);
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}

			// Persist to XML JobLauncher.
			IFile file = jobProfileFolder.getFile(launcher.getName()
					.replaceAll("\\s+", "_") + ".xml");

			// Create an input stream for the file by first writing it to an
			// output stream
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			launcher.persistToXML(outputStream);
			// Write the file to the input stream - Forest, is there a better
			// way to do this? ~JJB 20120919 17:40
			InputStream fileInputStream = new ByteArrayInputStream(outputStream
					.toString().getBytes());

			// Persist the XML file
			try {
				// Delete it if it is already there.
				if (file.exists()) {
					file.delete(true, null);
				}
				// Write the file
				file.create(fileInputStream, false, null);
			} catch (CoreException e) {
				// Complain
				e.printStackTrace();
			}
			// Update the status
			status = FormStatus.Processed;
		} else {
			// If any other action is requested, let the super class deal with
			// it.
			return super.process(actionName);
		}

		System.out
				.println("JobProfile Message: Successfully serialized the JobLauncher!");

		return status;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation sets up the JobProfileForm.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected void setupForm() {
		// begin-user-code

		// Copy only the forms contents. This is required for JPA to work.
		this.form = new Form();
		this.form.copy((Form) new JobProfileForm());

		// Create a set of Actions
		ArrayList<String> actions = new ArrayList<String>();
		actions.add("Create a Job Launcher");

		// Append actions to actions list.
		this.form.setActionList(actions);

		// Setup Name and Description
		this.setName("Job Profile Editor");
		this.setDescription("Create or edit a Job Profile that will be used "
				+ "by ICE to launch jobs");

		// end-user-code
	}
}