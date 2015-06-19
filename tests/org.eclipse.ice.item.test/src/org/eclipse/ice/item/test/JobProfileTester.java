/*******************************************************************************
 * Copyright (c) 2012, 2014- UT-Battelle, LLC.
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
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;

import javax.xml.bind.JAXBException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.datastructures.ICEObject.ICEJAXBHandler;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.ResourceComponent;
import org.eclipse.ice.item.jobLauncher.JobLauncher;
import org.eclipse.ice.item.jobprofile.JobProfile;
import org.junit.Test;

/**
 * This class is responsible for testing the JobProfileBuilder. It is primarily
 * concerned with checking the ability of the JobProfileBuilder to write the new
 * profile to disk correctly after it is created.
 * 
 * @author Jay Jay Billings, Anna Wojtowicz
 */
public class JobProfileTester {
	
	/**
	 * A JobProfile used for testing.
	 */
	private JobProfile jobProfile;

	/**
	 * This operation checks the JobProfile by processing it and reviewing
	 * comparing the Item it creates to the original. It also checks the
	 * quantities in the XML to make sure that they are consistent with the
	 * specification and useful for launching jobs.
	 * 
	 * @throws IOException 
	 * @throws JAXBException 
	 * @throws NullPointerException 
	 */
	@Test
	public void checkProfileWriting() throws NullPointerException, JAXBException, IOException {
	
		// Begin by setting up a project space we can work with
		
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

		// Get the persisted file and make sure it's valid
		IFile file = project.getFolder("jobProfiles").getFile("JobProfile.xml");
		assertTrue(file.exists());

		// Load file into inputStream
		ByteArrayInputStream inputStream = null;
		try {

			// Convert to a string and then convert that string to an
			// InputStream
			BufferedReader br = new BufferedReader(new InputStreamReader(
					file.getContents()));

			StringBuilder sb = new StringBuilder();
			String line;
			try {
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println(sb.toString());
			inputStream = new ByteArrayInputStream(sb.toString().getBytes());
		} catch (CoreException e) {
			e.printStackTrace();
		}

		// Load file from inputSteam to object
		JobLauncher launcher = new JobLauncher();

		ICEJAXBHandler xmlHandler = new ICEJAXBHandler();
		ArrayList<Class> classList = new ArrayList<Class>();
		classList.add(JobLauncher.class);
		classList.add(ResourceComponent.class);
		launcher = (JobLauncher) xmlHandler.read(classList, inputStream);
		
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

		// Check contents of form
		Form form = launcher.getForm();
		assertEquals("JobProfile Launcher", form.getName());
		assertEquals("This operation will execute JobProfile",
				form.getDescription());
		assertEquals(3, form.getComponents().size());

		// Enable OpenMP and MPI on the launcher
		launcher.enableMPI(1, 512000, 1);
		launcher.enableOpenMP(1, 16, 1);

		// Verify they were set correctly
		DataComponent component = (DataComponent) form.getComponents().get(3);
		Entry mpiEntry = (Entry) component.retrieveAllEntries().get(1);
		Entry openMPEntry = (Entry) component.retrieveAllEntries().get(2);
		assertEquals("1", mpiEntry.getValue());
		assertEquals("1", openMPEntry.getValue());

		// Delete the files from the workspace
		try {
			// Remove any old test files
			for (IResource resource : project.members()) {
				if (resource.getType() == IResource.FILE
						&& !(".project").equals(resource.getName())) {
					// Convert the resource to a File resource. It's necessary
					// to do this for the Windows build so we can use 
					// File.deleteOnExit() here. When Windows attempts to delete 
					// a resource, it does not automatically release any open 
					// handles on files the same way *nix systems do. Failing 
					// to delete these resources causes subsequent Item tests 
					// to fail on a Windows build.
					File fileResource = 
							new File(resource.getLocation().toOSString());
					fileResource.deleteOnExit();
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
	}
}
