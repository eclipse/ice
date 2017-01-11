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

import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.datastructures.form.TableComponent;
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
	 * Test the constructor and make sure that all of the information is entered
	 * in correctly.
	 */
	@Test
	public void checkConstruction() {
		VibeKVPair vibeKVPair;
		// Try with project as null
		vibeKVPair = new VibeKVPair(null);

		// check to see if the form exists, and the item is setup correctly.
		assertEquals("VIBE Key-Value Pair", vibeKVPair.getName());
		assertEquals("Generate input files for VIBE.",
				vibeKVPair.getDescription());

		// Try with project not as null
		IProject project = projectSpace;
		vibeKVPair = new VibeKVPair(project);

		// check to see if the form exists, and the item is setup correctly.
		assertEquals("VIBE Key-Value Pair", vibeKVPair.getName());
		assertEquals("Generate input files for VIBE.",
				vibeKVPair.getDescription());
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
		assertEquals("Generate input files for VIBE.",
				vibeKVPair.getDescription());
	}

	/**
	 * Test that changing the template will set the table up appropriately.
	 */
	@Test
	public void checkTemplates() {
		VibeKVPair vibeKVPair;
		IProject project = projectSpace;

		// Create a VibeLauncher
		vibeKVPair = new VibeKVPair(project);

		// The components from the item's form.
		ArrayList<Component> components = vibeKVPair.getForm().getComponents();

		// The component containing the table Key-Value pairs
		TableComponent table = null;

		// The component containing the problem templates
		DataComponent template = null;

		// Search the list of components, saving them to the approrpiate
		// variables
		for (Component component : components) {

			// The template component will be the only DataComponent
			if (component instanceof DataComponent) {
				template = (DataComponent) component;
			}

			// The table will be the only TableComponent
			else if (component instanceof TableComponent) {
				table = (TableComponent) component;
			}
		}

		//The table should be empty initially
		assertEquals(0, table.numberOfRows());
		
		//Load a template
		template.retrieveAllEntries().get(0).setValue("NTG");

		// Give the form time to load the new data
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail();
		}		
		
		// Whether or not the CUTOFF key was found in the table
		boolean foundCutoff = false;

		// Search for the CUTOFF key in each of the tables rows
		for (int id : table.getRowIds()) {
			if ("CUTOFF".equals(table.getRow(id).get(0).getValue())) {
				foundCutoff = true;
				break;
			}
		}

		// If the CUTOFF key was absent, then the table was not set up correctly
		if (!foundCutoff) {
			fail("Did not find CUTOFF row in default table.");
		}

		// Set the item to the DualFoil template
		template.retrieveAllEntries().get(0).setValue("DualFoil");

		// Give the form time to load the new data
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail();
		}

		// Whether the CUTOFFL key was found in the table
		boolean foundL = false;

		// Whether the CUTOFFH key was found in the table
		boolean foundH = false;

		// Search each row of the table for the DualFoil keys
		for (int id : table.getRowIds()) {

			// Get the value of the current key-value pair's row
			String value = table.getRow(id).get(0).getValue();

			if ("CUTOFFL".equals(value)) {
				foundL = true;
			} else if ("CUTOFFH".equals(value)) {
				foundH = true;
			}
		}

		// If the expected keys were not found, then the DualFoil template was
		// not properly applied to the table
		if (!foundL || !foundH) {
			fail("Table was not changed to DualFoil template.");
		}

		// Set the template back to NTG
		template.retrieveAllEntries().get(0).setValue("NTG");

		// Give the form time to load the new data
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail();
		}

		// Search for the CUTOFF key in each of the tables rows
		foundCutoff = false;
		for (int id : table.getRowIds()) {
			if ("CUTOFF".equals(table.getRow(id).get(0).getValue())) {
				foundCutoff = true;
				break;
			}
		}

		// If the CUTOFF key was absent, then the table was not set up correctly
		if (!foundCutoff) {
			fail("Table was not changed to NTG template.");
		}
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
		assertEquals(2, form.getComponents().size());

		// Since there are entries and DataComponents, lets run the test!
		// Now try to pass it the wrong information into the process
		// operation on VibeKVPair
		assertEquals(FormStatus.InfoError,
				vibeKVPair.process("I AM A VAGUE, INAPPROPRIATE PROCESS!"));

		assertEquals(FormStatus.Processed,
				vibeKVPair.process("Export to key-value pair output"));
		form = vibeKVPair.getForm();
		assertNotNull(form.getComponents());
		assertEquals(2, form.getComponents().size());
		assertTrue(projectSpace
				.getFile("VIBE_Key-Value_Pair_" + vibeKVPair.getId() + ".dat")
				.exists());
		try {
			projectSpace.getFile(
					"VIBE_Key-Value_Pair_" + vibeKVPair.getId() + ".dat")
					.delete(true, true, null);
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
