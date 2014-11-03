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
package org.eclipse.ice.item.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.item.jobLauncher.JobLauncher;
import org.eclipse.ice.item.jobLauncher.JobLauncherForm;
import org.eclipse.ice.item.jobprofile.JobProfile;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.junit.Test;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This class is responsible for testing the JobProfileBuilder. It is primarily
 * concerned with checking the ability of the JobProfileBuilder to write the new
 * profile to disk correctly after it is created.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author bkj
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */

public class JobProfileTester {
	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private JobProfile jobProfile;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the JobProfile by processing it and reviewing
	 * comparing the Item it creates to the original. It also checks the
	 * quantities in the XML to make sure that they are consistent with the
	 * specification and useful for launching jobs.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkProfileWriting() {
		// begin-user-code

		// setup a link to the projectspace
		// Local Declarations
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		URI defaultProjectLocation = null;
		IProject project = null;
		String separator = System.getProperty("file.separator");

		// Setup the project
		try {
			// Get the project handle
			project = workspaceRoot.getProject("itemTesterWorkspace");
			// If the project does not exist, create it
			if (!project.exists()) {
				// Set the location as ${workspace_loc}/ItemTesterWorkspace. Use
				// file() to get around the URI problems with spaces.
				defaultProjectLocation = (new File(
						System.getProperty("user.dir") + separator
								+ "itemTesterWorkspace")).toURI();
				// Create the project description
				IProjectDescription desc = ResourcesPlugin.getWorkspace()
						.newProjectDescription("itemTesterWorkspace");
				// Set the location of the project
				desc.setLocationURI(defaultProjectLocation);
				// Create the project
				project.create(desc, null);
			}
			// Open the project if it is not already open
			if (project.exists() && !project.isOpen()) {
				project.open(null);
			}
		} catch (CoreException e) {
			// Catch for creating the project
			e.printStackTrace();
			fail();
		}

		// Now we have the project space, pass project into jobProfile

		// Create a job profile
		jobProfile = new JobProfile(project);
		// Should persist file to XML
		jobProfile.process("Create a Job Launcher");

		// To find the file, one must know the projectspace
		IFile file = project.getFolder("jobProfiles").getFile(
				"JobProfile" + ".xml");
		assertTrue(file.exists());

		// load file into inputstream
		ByteArrayInputStream inputStream = null;
		try {

			// Convert to a string and then convert that string to an
			// inputstream
			BufferedReader br = new BufferedReader(new InputStreamReader(
					file.getContents()));

			StringBuilder sb = new StringBuilder();
			String line;
			try {
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(sb.toString());
			inputStream = new ByteArrayInputStream(sb.toString().getBytes());
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// load file from inputstream to object
		JobLauncher launcher = new JobLauncher();

		launcher.loadFromXML(inputStream);

		// check contents of JobLauncher (item portion). Should be default
		// values besides form:
		JobLauncher comparisonLauncher = new JobLauncher();
		comparisonLauncher.setName("JobProfile");
		comparisonLauncher
				.setDescription("This operation will execute JobProfile");
		comparisonLauncher.setItemBuilderName("JobProfile");
		assertEquals(comparisonLauncher.getName(), launcher.getName());
		assertEquals(comparisonLauncher.getDescription(),
				launcher.getDescription());
		assertEquals(comparisonLauncher.getItemType(), launcher.getItemType());
		assertEquals(comparisonLauncher.getStatus(), launcher.getStatus());
		assertEquals(comparisonLauncher.getItemBuilderName(),
				launcher.getItemBuilderName());
		// This next check is to make sure that the JobProfile sets the builder
		// name according to the specification on the class.
		assertEquals(launcher.getName(), launcher.getItemBuilderName());

		// check contents of form
		Form form = launcher.getForm();
		assertEquals("JobProfile Launcher", form.getName());
		assertEquals("This operation will execute JobProfile",
				form.getDescription());
		// This will fail because no hosts have been added! - FIXME
		// assertEquals(1, form.getHosts().size());
		// assertEquals("localhost", form.getHosts().get(0));

		// Check Components on form for specific information
		assertEquals(3, form.getComponents().size());

		// Check mpi and openmp
		// Mpi and open MP will not exists since the default form does not have
		// them enabled.

		// A test for enabling openMP and mpi
		// Enable them on the launcher
		launcher.enableMPI(1, 512000, 1);
		launcher.enableOpenMP(1, 16, 1);

		DataComponent component = (DataComponent) form.getComponents().get(3);
		Entry mpiEntry = (Entry) component.retrieveAllEntries().get(0);
		Entry openMPEntry = (Entry) component.retrieveAllEntries().get(1);
		assertEquals("1", mpiEntry.getValue());
		assertEquals("1", openMPEntry.getValue());

		// Delete the files from the workspace
		try {
			// Remove any old test files
			for (IResource resource : project.members()) {
				if (resource.getType() == IResource.FILE
						&& !(".project").equals(resource.getName())) {

					resource.delete(true, null);
				}
			}
			// Refresh the project space
			project.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (CoreException e1) {
			// Complain
			e1.printStackTrace();
			fail();
		}

		return;
		// end-user-code
	}
}
