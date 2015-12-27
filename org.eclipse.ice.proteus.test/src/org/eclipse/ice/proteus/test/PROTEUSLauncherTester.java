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
package org.eclipse.ice.proteus.test;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.item.jobLauncher.JobLauncher;
import org.eclipse.ice.proteus.PROTEUSLauncher;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Class that tests PROTEUSLaucher methods.
 * @author Anna Wojtowicz
 *
 */
public class PROTEUSLauncherTester {

	/**
	 *  A fake project and workspace root for tester classes to use when a null 
	 *  IProject will not work. Setup using the setupFakeProject() method.
	 */
	private IProject project;
	private IWorkspaceRoot workspaceRoot;
	
	/**
	 * Tests the PROTEUSLauncher constructors. Only tests that objects inherit
	 * correctly from the parent class (JobLauncher). All other relevant 
	 * construction tests are in the checkItemInfoSetup() method, as the method 
	 * it tests (setupItemInfo()) is always called in the chain of superclass
	 * constructors.
	 * @author Anna Wojtowicz
	 * 
	 */	
	@Test
	public void checkConstruction() {
		
		// Call nullary constructor, check it inherits correctly
		PROTEUSLauncher launcher = new PROTEUSLauncher();
		assertTrue(launcher instanceof JobLauncher);
	
		// Setup a fake project
		setupFakeProject();
		
		// Call parameterized constructor, check it inherits correctly
		launcher = new PROTEUSLauncher(project);
		assertTrue(launcher instanceof JobLauncher);
		
		// Close and delete the tester project workspace
		try {
			project.close(null);
			workspaceRoot.delete(true,true,null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Tests setupItemInfo() method of the PROTEUSLauncher class. Is somewhat 
	 * like an extension of the construction tester. Only tests PROTEUS-specific
	 * attributes, as all other values will have been set and tested by the 
	 * parent class's tester (JobLauncherTester).
	 * @author Anna Wojtowicz
	 * 
	 */
	@Test
	public void checkItemInfoSetup() {
		
		// Define some default values to check against
		String defaultName = "PROTEUS Launcher";
		String defaultDesc = "PROTEUS is a nuclear reactor core simulator from "
				+ "Argonne National Laboratory that is primarily focused "
				+ "on sodium-cooled fast reactors.";
				
		// Call nullary constructor
		PROTEUSLauncher launcher = new PROTEUSLauncher();
		
		// Check the default values
		assertEquals(defaultName, launcher.getName());
		assertEquals(defaultDesc, launcher.getDescription());
		
		// Setup a fake project
		setupFakeProject();
		
		// Call parameterized constructor
		launcher = new PROTEUSLauncher(project);
		
		// Check the default values
		assertEquals(defaultName, launcher.getName());
		assertEquals(defaultDesc, launcher.getDescription());
		
		// Close and delete the tester project workspace
		try {
			project.close(null);
			workspaceRoot.delete(true,true,null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Tests the creation and setting of the PROTEUSLauncher Form and 
	 * corresponding Hosts.
	 * @author Anna Wojtowicz
	 * 
	 */
	@Test
	public void checkFormSetup() {
		
		// Define local declarations
		String localInstallDir = "sharp/trunk/modules/sharp/src/tests";
		String ergInstallDir = "/home/sharp/sharp/trunk/modules/sharp"
				+ "/src/tests";
		
		// Create hosts to test against
		ArrayList<String> hostOne = new ArrayList<String>();
		hostOne.add("localhost.localdomain");
		hostOne.add("linux x86_64");
		hostOne.add(localInstallDir);
		ArrayList<String> hostTwo = new ArrayList<String>();
		hostTwo.add("ergaster.ornl.gov");
		hostTwo.add("linux x86_64");
		hostTwo.add(ergInstallDir);
		
		// Create a launcher to test (superconstructor calls setupForm() method)
		PROTEUSLauncher launcher = new PROTEUSLauncher(null);
		
		// Check that the Form was set up correctly
		assertNotNull(launcher.getForm());
		assertNotNull(launcher.getAllHosts());
		assertEquals(2, launcher.getAllHosts().size());
		
		// Check the hosts are correct
		assertEquals(hostOne.get(0), launcher.getAllHosts().get(0));
		assertEquals(hostTwo.get(0), launcher.getAllHosts().get(1));
	}
	
	@Ignore
	@Test
	public void checkProcess() {
		// Do nothing. PROTEUSLauncher.process() calls the superconstructor
		// process method, which is tested by the superconstructor tests
	}
	
	/**
	 * Creates a fake workspace in the bundle for all PROTEUSLauncher unit tests 
	 * to utilize throughout their execution. Once the workspace resource is no
	 * longer needed, it is left to each individual test to close and delete
	 * the project workspace.
	 * @author Anna Wojtowicz
	 * 
	 */
	public void setupFakeProject() {
		
		// Setup a dummy project workspace
		URI projectLocation = null;
		String separator = System.getProperty("file.separator");

		try {
			// Get the project handle
			workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
			project = workspaceRoot.getProject("PROTEUSTesterWorkspace");

			// If the project does not exist, create it
			if (!project.exists()) {

				// Define the location as ${workspace_loc}/PROTEUSTesterWorkspace
				projectLocation = (new File(
						System.getProperty("user.dir") 
						+ separator + "PROTEUSTesterWorkspace")).toURI();

				// Create the project description
				IProjectDescription desc = ResourcesPlugin.getWorkspace()
						.newProjectDescription("PROTEUSTesterWorkspace");

				// Set the location of the project
				desc.setLocationURI(projectLocation);

				// Create the project
				project.create(desc, null);
			}

			// Open the project if it is not already open
			if (project.exists() && !project.isOpen()) {
				project.open(null);
			}

		} catch (CoreException e) {
			e.printStackTrace();
			fail("Project workspace could not be created.");
		}
	}
}
