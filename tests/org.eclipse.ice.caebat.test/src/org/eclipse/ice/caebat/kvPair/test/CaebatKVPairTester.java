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
package org.eclipse.ice.caebat.kvPair.test;

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
import org.eclipse.ice.caebat.kvPair.CaebatKVPair;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * A class that tests the CaebatKVPair.
 * 
 * @author Andrew Bennett
 * 
 */
public class CaebatKVPairTester {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * A reference to the IProject workspace. This parameter is setup in the
	 * setupIProject operation.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private static IProject projectSpace;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation sets up the workspace. 
	 * </p>
	 * <!-- end-UML-doc -->
	 */
	@BeforeClass
	public static void beforeTests() {
		// begin-user-code

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
		// end-user-code
	}
	
	
	
	/**
	 * Test the constructor and make sure that all of the information is 
	 * entered in correctly.
	 */
	@Test
	public void checkConstruction() {
		CaebatKVPair caebatKVPair;
		// Try with project as null
		caebatKVPair = new CaebatKVPair(null);

		// check to see if the form exists, and the item is setup correctly.
		assertEquals("Caebat Key-Value Pair", caebatKVPair.getName());
		assertEquals(
				"An item to generate CAEBAT key-value pair files.",
				caebatKVPair.getDescription());

		// Try with project not as null
		IProject project = projectSpace;
		caebatKVPair = new CaebatKVPair(project);

		// check to see if the form exists, and the item is setup correctly.
		assertEquals("Caebat Key-Value Pair", caebatKVPair.getName());
		assertEquals(
				"An item to generate CAEBAT key-value pair files.",
				caebatKVPair.getDescription());		
	}

	/**
	 * Make sure that the form gets set up okay.
	 */
	@Test
	public void checkSetupForm() {
		CaebatKVPair caebatKVPair;
		IProject project = projectSpace;

		// Create a CaebatLauncher
		caebatKVPair = new CaebatKVPair(project);

		// Check values stored on the form
		// Check to see if executable exists
		assertEquals("Caebat Key-Value Pair", caebatKVPair.getForm().getName());
		assertEquals("An item to generate CAEBAT key-value pair files.",
				caebatKVPair.getForm().getDescription());
	}
	
	/**
	 * Try processing the form with both valid and invalid 
	 */
	@Test
	public void checkProcessing() {
		CaebatKVPair caebatKVPair;
		Form form = new Form();

		// The projectspace has to be set for this to work.
		caebatKVPair = new CaebatKVPair(projectSpace);

		// Check default contents
		form = caebatKVPair.getForm();
		assertNotNull(form.getComponents());
		assertEquals(1, form.getComponents().size());

		// Since there are entries and DataComponents, lets run the test!
		// Now try to pass it the wrong information into the process
		// operation on CaebatKVPair
		assertEquals(FormStatus.InfoError,
				caebatKVPair.process("I AM A VAGUE, INAPPROPRIATE PROCESS!"));

		assertEquals(FormStatus.Processed,
				caebatKVPair.process("Export to key-value pair output"));
		form = caebatKVPair.getForm();
		assertNotNull(form.getComponents());
		assertEquals(1, form.getComponents().size());
		assertTrue(projectSpace.getFile(
				"Caebat_Key-Value_Pair_" + caebatKVPair.getId() + ".dat").exists());
		try {
			projectSpace.getFile("Caebat_Key-Value_Pair_" + caebatKVPair.getId() + ".dat").delete(true, true, null);
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
			fail("Could not clean up project space after CaebatKVPairTest");
		}
	}
}
