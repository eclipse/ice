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
package org.eclipse.ice.vibe.model.test;

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
import org.eclipse.ice.vibe.model.VibeModel;
import org.eclipse.january.form.Form;
import org.eclipse.january.form.FormStatus;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * <p>
 * This class tests the operations on VibeModel. This class is also
 * responsible for building the model files designed to be used in the VIBE
 * Model for production mode. It creates two separate sets of files. The first
 * set of files are responsible for the production of the form on the model,
 * which contains safety defaults. The second set are a collection of cases
 * which will be used to pre-generate the input data on the forms.
 * </p>
 * 
 * @author Andrew Bennett
 */
public class VibeModelTester {

	/**
	 * The project space used to create the workspace for the tests.
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
			fail("VIBE Model Tester: Error!  Could not set up project space");
		}

		// Set the global project reference.
		projectSpace = project;

		return;
	}

	/**
	 * <p>
	 * This operation checks VibeModel.setupForm operation. This should also
	 * test the loadDataComponents in comparison.
	 * </p>
	 */
	@Test
	public void checkSetupForm() {
		// Local declarations
		VibeModel vibeModel;
		Form form = new Form();

		// The project space can be null for this operation and it will still work.
		vibeModel = new VibeModel(null);

		// Check contents, null model should be empty
		form = vibeModel.getForm();
		assertNotNull(form.getComponents());
		assertEquals(0, form.getComponents().size());

		return;
	}

	/**
	 * <p>
	 * This operation checks VibeModel.process operation.
	 * </p>
	 */
	@Test
	public void checkProcessing() {
		// Local Declarations
		VibeModel vibeModel;
		Form form = new Form();

		// The projectspace has to be set for this to work.
		vibeModel = new VibeModel(projectSpace);

		// Check default contents
		form = vibeModel.getForm();
		assertNotNull(form.getComponents());
		assertEquals(4, form.getComponents().size());

		// Check the name and description of the item and allowedActions
		assertEquals("VIBE Model", vibeModel.getName());
		assertEquals("Specify the components and set up a battery model for VIBE.",
				vibeModel.getDescription());

		// Since there are entries and DataComponents, lets run the test!
		// Now try to pass it the wrong information into the process
		// operation on VibeModel
		assertEquals(FormStatus.InfoError,
				vibeModel.process("I AM A VAGUE, INAPPROPRIATE PROCESS!"));

		// Check default contents
		form = vibeModel.getForm();
		assertNotNull(form.getComponents());
		assertEquals(4, form.getComponents().size());

		assertEquals(FormStatus.Processed,
				vibeModel.process("Export to VIBE INI format"));
		form = vibeModel.getForm();
		assertNotNull(form.getComponents());
		assertEquals(4, form.getComponents().size());
		assertTrue(projectSpace.getFile(
				"VIBE_Model_" + vibeModel.getId() + ".conf").exists());
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
			fail("VIBE Model Tester: Error!  Could not clean up project space");
		}
	}

}
