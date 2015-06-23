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
package org.eclipse.ice.nek5000.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URI;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.nek5000.NekModel;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * <p>
 * This class tests the NekModel Item to make sure that it can correctly create
 * its Form and process a modified Form.
 * </p>
 * 
 * @author Jay Jay Billings
 */
public class NekModelTester {

	/**
	 * The project space used to create the workspace for the tests.
	 */
	private static IProject projectSpace;

	/**
	 * This operation sets up the workspace. It copies the necessary Nek data
	 * files into ${workspace}/Nek.
	 */
	@BeforeClass
	public static void beforeTests() {

		// Local Declarations
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		URI defaultProjectLocation = null;
		IProject project = null;
		String projectName = "NekModelTesterWorkspace";
		String separator = System.getProperty("file.separator");
		String userDir = System.getProperty("user.home") + separator
				+ "ICETests" + separator + "nek5000Data";
		String filePath = userDir + separator + "conj_ht.rea";

		// Enable debugging
		System.setProperty("DebugICE", "");
		// Print debug information
		System.out.println("Nek Test Data File: " + filePath);

		// Setup the project
		try {
			// Get the project handle
			project = workspaceRoot.getProject(projectName);
			// If the project does not exist, create it
			if (!project.exists()) {
				// Set the location as ${workspace_loc}/ItemTesterWorkspace
				defaultProjectLocation = (new File(
						System.getProperty("user.dir") + separator
								+ projectName)).toURI();
				// Create the project description
				IProjectDescription desc = ResourcesPlugin.getWorkspace()
						.newProjectDescription(projectName);
				// Set the location of the project
				desc.setLocationURI(defaultProjectLocation);
				// Create the project
				project.create(desc, null);
			}
			// Open the project if it is not already open
			if (project.exists() && !project.isOpen()) {
				project.open(null);
			}
			// Setup the project directory
			IFolder nekFolder = project.getFolder("Nek5000_Model_Builder");
			if (!nekFolder.exists()) {
				// Create the directory
				nekFolder.create(true, true, null);
				// Create the File handle and input stream
				IPath nekPath = new Path(filePath);
				File nekFile = nekPath.toFile();
				FileInputStream nekStream = new FileInputStream(nekFile);
				// Create the Eclipse workspace file
				IFile nekEFSFile = nekFolder.getFile("conj_ht.rea");
				nekEFSFile.create(nekStream, true, null);
			}
			// Refresh the workspace
			project.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (CoreException e) {
			// Catch exception for creating the project
			e.printStackTrace();
			fail();
		} catch (FileNotFoundException e) {
			// Catch exception for failing to load the file
			e.printStackTrace();
			fail();
		}

		// Set the global project reference.
		projectSpace = project;

		return;
	}

	/**
	 * This operation checks the NekModel and makes sure that it can properly
	 * construct its Form.
	 */
	@Test
	public void checkConstruction() {

		// Create a NekModel to test
		NekModel model = setupNekItem();

		// Check the form
		Form form = model.getForm();
		assertNotNull(form);
		assertEquals(14, form.getComponents().size());
		assertEquals("Nek5000 Model Builder", form.getName());
		assertTrue(form.getId() > 0);

		return;
	}

	/**
	 * This operation checks the NekModel to make sure that it can correctly
	 * process its Form and generate a Nek input file.
	 */
	@Test
	public void checkProcessing() {

		// Local Declarations
		String testFilename = "Nek5000_Model_Builder_1.rea";

		// Create a NekModel to test
		NekModel model = setupNekItem();

		// Check the form
		Form form = model.getForm();
		assertNotNull(form);

		// Check the action list. It should contain the read and write actions
		// as well as the write action for ICE's default format.
		assertEquals(3, form.getActionList().size());
		assertTrue(form.getActionList().contains(NekModel.nekWriteActionString));
		assertTrue(form.getActionList().contains(NekModel.nekReadActionString));

		// Resubmit the form
		assertEquals(FormStatus.ReadyToProcess, model.submitForm(form));

		// Direct the model to write the output file
		assertEquals(FormStatus.Processed,
				model.process(NekModel.nekWriteActionString));

		// Check that the file exists
		assertTrue(projectSpace.getFile(testFilename).exists());
		// Delete the file if the check passed
		try {
			projectSpace.getFile(testFilename).delete(true, null);
		} catch (CoreException e) {
			// Complain
			System.err.println("NekModelTester Message: "
					+ "Unable to delete test file.");
			e.printStackTrace();
			// And fail
			fail();
		}

		return;
	}

	/**
	 * <p>
	 * This operation configures a NekModel. It is used by both test operations.
	 * </p>
	 * 
	 * @return <p>
	 *         A newly instantiated NekModel.
	 *         </p>
	 */
	private NekModel setupNekItem() {

		// Local Declarations
		NekModel model = new NekModel(projectSpace);

		return model;
	}

}