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
package org.eclipse.ice.caebat.batml.test;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.junit.BeforeClass;
import org.junit.Test;
import org.eclipse.ice.caebat.batml.BatMLModel;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.datastructures.form.emf.EMFComponent;

/**
 * This class tests the operations on BatMLModel. This class is also responsible
 * for building the model files designed to be used in the BatML Model for
 * production mode.
 */
public class BatMLModelTester {

	/**
	 * The project space used to create the workspace for the tests.
	 */
	private static IProject projectSpace;

	/**
	 * This operation sets up the workspace.
	 */
	@BeforeClass
	public static void beforeTests() {
		// begin-user-code

		// Local Declarations
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		URI defaultProjectLocation = null;
		IProject project = null;
		String projectName = "batMLTesterWorkspace";
		String separator = System.getProperty("file.separator");

		// Setup the project
		try {
			// Get the project handle
			project = workspaceRoot.getProject(projectName);
			// If the project does not exist, create it
			if (!project.exists()) {
				// Set the location as
				// ${workspace_loc}/CAEBATModelTesterWorkspace
				defaultProjectLocation = (new File(
						System.getProperty("user.home") + separator
								+ "ICETests" + separator + projectName))
						.toURI();
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
	 * This operation checks BatMLModel.setupForm operation.
	 */
	@Test
	public void checkSetupForm() {

		// begin-user-code

		// Local declarations
		BatMLModel batMLModel;
		Form form = new Form();

		// The project space can be null for this operation and it will still
		// work.
		batMLModel = new BatMLModel(projectSpace);

		// Check contents - see that there are 5 components
		form = batMLModel.getForm();
		assertNotNull(form.getComponents());
		assertEquals(1, form.getComponents().size());

		return;
		// end-user-code

	}

	/**
	 * This operation checks BatMLModel.process operation.
	 */
	@Test
	public void checkProcess() {

		// begin-user-code

		// Local Declarations
		String separator = System.getProperty("file.separator");
		String userDir = System.getProperty("user.home") + separator + "ICETests";
		String filePath = userDir + separator + "batMLTesterWorkspace"
				+ separator + "electrical.xml";

		BatMLModel batML;
		Form form = new Form();

		// The projectspace has to be set for this to work.
		batML = new BatMLModel(projectSpace);

		// Check default contents
		form = batML.getForm();
		assertNotNull(form.getComponents());
		assertEquals(1, form.getComponents().size());

		// Check the name and description of the item and allowedActions
		assertEquals("BatML Model Builder", batML.getName());
		assertEquals("This item builds models based on a BatteryML schema.",
				batML.getDescription());

		// Since there are entries and DataComponents, lets run the test!
		// Now try to pass it the wrong information into the process
		// operation on BatMLModel
		assertEquals(FormStatus.InfoError,
				batML.process("I AM A VAGUE, INAPPROPRIATE PROCESS!"));

		// This portion will try to get the operation to write the information
		// to xml file
		assertEquals(FormStatus.Processed, batML.process("Write to XML"));

		// Check to see if the file is created and exists. It will create a file
		// in relation to the Id of the item.
		assertTrue(Files.exists(Paths.get(filePath)));

		// end-user-code

	}

	@Test
	public void checkLoadInput() {
		BatMLModel batml = new BatMLModel(projectSpace);
		batml.loadInput("electrical.xml");
		EMFComponent component = (EMFComponent) batml.getForm().getComponent(1);
		assertNotNull(component);
		assertNotNull(component.getEMFTreeComposite());
		assertTrue("DocumentRoot".equals(component.getEMFTreeComposite().getName()));
	}
}
