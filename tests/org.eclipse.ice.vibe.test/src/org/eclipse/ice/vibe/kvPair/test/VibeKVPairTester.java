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
package org.eclipse.ice.vibe.kvPair.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.vibe.kvPair.VibeKVPair;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * A class that tests the VibeKVPair.
 * 
 * @author Andrew Bennett
 */
public class VibeKVPairTester {
	/**
	 * <p>
	 * A reference to the IProject workspace. This parameter is setup in the
	 * setupIProject operation.
	 * </p>
	 */
	private static IProject projectSpace;

	/**
	 * <p>
	 * This operation sets up the workspace. 
	 * </p>
	 */
	@BeforeClass
	public static void beforeTests() {

		// Local Declarations
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = null;
		String separator = System.getProperty("file.separator");
		String userDir = System.getProperty("user.home") + separator
				+ "ICETests" + separator + "caebatTesterWorkspace";
		// Enable Debugging
		System.setProperty("DebugICE", "");

		// Setup the project
		try {
			// Get the project handle
			IPath projectPath = new Path(userDir + separator + ".project");
			// Create the project description
			IProjectDescription desc = ResourcesPlugin.getWorkspace()
			                    .loadProjectDescription(projectPath);
			// Get the project handle and create it
			project = workspaceRoot.getProject(desc.getName());
			// Create the project if it doesn't exist
			if (!project.exists()) {
				project.create(desc, new NullProgressMonitor());
			}
			// Open the project if it is not already open
			if (project.exists() && !project.isOpen()) {
			   project.open(new NullProgressMonitor());
			}
			// Refresh the workspace
			project.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (CoreException e) {
			// Catch exception for creating the project
			e.printStackTrace();
			fail();
		}

		// Set the global project reference.
		projectSpace = project;

		return;
	}
	
	
	
	/**
	 * Test the constructor and make sure that all of the information is 
	 * entered in correctly.
	 */
	@Test
	public void checkConstruction() {
		VibeKVPair vibeKVPair;
		// Try with project as null
		vibeKVPair = new VibeKVPair(null);

		// check to see if the form exists, and the item is setup correctly.
		assertEquals("VIBE Key-Value Pair", vibeKVPair.getName());
		assertEquals("Generate input files for VIBE.",vibeKVPair.getDescription());

		// Try with project not as null
		IProject project = projectSpace;
		vibeKVPair = new VibeKVPair(project);

		// check to see if the form exists, and the item is setup correctly.
		assertEquals("VIBE Key-Value Pair", vibeKVPair.getName());
		assertEquals("Generate input files for VIBE.", vibeKVPair.getDescription());		
	}

	/**
	 * Make sure that the form gets set up okay.
	 */
	@Test
	public void checkSetupForm() {
		VibeKVPair vibeKVPair;
		IProject project = projectSpace;

		// Create a VibeLauncher
		vibeKVPair = new VibeKVPair(project);

		// Check values stored on the form
		// Check to see if executable exists
		assertEquals("VIBE Key-Value Pair", vibeKVPair.getName());
		assertEquals("Generate input files for VIBE.", vibeKVPair.getDescription());	
	}
	
	/**
	 * Try processing the form with both valid and invalid 
	 */
	@Test
	public void checkProcessing() {
		VibeKVPair vibeKVPair;
		Form form = new Form();

		// The projectspace has to be set for this to work.
		vibeKVPair = new VibeKVPair(projectSpace);

		// Check default contents
		form = vibeKVPair.getForm();
		assertNotNull(form.getComponents());
		assertEquals(1, form.getComponents().size());

		// Since there are entries and DataComponents, lets run the test!
		// Now try to pass it the wrong information into the process
		// operation on VibeKVPair
		assertEquals(FormStatus.InfoError,
				vibeKVPair.process("I AM A VAGUE, INAPPROPRIATE PROCESS!"));

		assertEquals(FormStatus.Processed,
				vibeKVPair.process("Export to key-value pair output"));
		form = vibeKVPair.getForm();
		assertNotNull(form.getComponents());
		assertEquals(1, form.getComponents().size());
		assertTrue(projectSpace.getFile(
				"VIBE_Key-Value_Pair_" + vibeKVPair.getId() + ".dat").exists());
		try {
			projectSpace.getFile("VIBE_Key-Value_Pair_" + vibeKVPair.getId() + ".dat").delete(true, true, null);
		} catch (CoreException e) {
			e.printStackTrace();
			fail("Could not delete test output file.");
		}
	}

	/**
	 * Clean up after ourselves
	 */
	@AfterClass
	public static void cleanup() {
		try {
			projectSpace.close(null);
			projectSpace.delete(true, null);
		} catch (CoreException e) {
			e.printStackTrace();
			fail("Could not clean up project space after VibeKVPairTest");
		}
	}
}
