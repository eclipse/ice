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
package org.eclipse.ice.caebat.model.test;

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
import org.eclipse.ice.caebat.model.CaebatModel;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This class tests the operations on CaebatModel. This class is also
 * responsible for building the model files designed to be used in the Caebat
 * Model for production mode. It creates two separate SETS OF FILES. The first
 * set of files are responsible for the production of the form on the model,
 * which contains safety defaults. The second set are a collection of cases
 * which will be used to pre-generate the input data on the forms.
 * </p>
 * <!-- end-UML-doc -->
 */
public class CaebatModelTester {

	/**
	 * The project space used to create the workspace for the tests.
	 */
	private static IProject projectSpace;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation sets up the workspace. It copies the necessary CAEBAT data
	 * files into ${workspace}/CAEBAT.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
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
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks CaebatModel.setupForm operation. This should also
	 * test the loadDataComponents in comparison.
	 * </p>
	 * <!-- end-UML-doc -->
	 */
	@Test
	public void checkSetupForm() {

		// begin-user-code

		// Local declarations
		CaebatModel caebatModel;
		Form form = new Form();

		/*
		 * This operation will test the CaebatModel plugin for proper loads with
		 * the setupForm operation. This should work as long as the files exists
		 * inside the plugin directory. Although not unit tested, it does check
		 * for specific file types, if the files exist, and if the Components
		 * exist.
		 */

		// The project space can be null for this operation and it will still
		// work.
		caebatModel = new CaebatModel(null);

		// Check contents, null model should be empty
		form = caebatModel.getForm();
		assertNotNull(form.getComponents());
		assertEquals(0, form.getComponents().size());

		return;
		// end-user-code

	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks CaebatModel.process operation.
	 * </p>
	 * <!-- end-UML-doc -->
	 */
	@Test
	public void checkProcessing() {

		// begin-user-code

		// Local Declarations
		CaebatModel caebat;
		Form form = new Form();

		// The projectspace has to be set for this to work.
		caebat = new CaebatModel(projectSpace);

		// Check default contents
		form = caebat.getForm();
		assertNotNull(form.getComponents());
		assertEquals(4, form.getComponents().size());

		// Check the name and description of the item and allowedActions
		assertEquals("Caebat Model", caebat.getName());
		assertEquals("This model creates input for CAEBAT.",
				caebat.getDescription());

		// Since there are entries and DataComponents, lets run the test!
		// Now try to pass it the wrong information into the process
		// operation on CaebatModel
		assertEquals(FormStatus.InfoError,
				caebat.process("I AM A VAGUE, INAPPROPRIATE PROCESS!"));

		// Check default contents
		form = caebat.getForm();
		assertNotNull(form.getComponents());
		assertEquals(4, form.getComponents().size());

		assertEquals(FormStatus.Processed,
				caebat.process("Export to Caebat INI format"));
		form = caebat.getForm();
		assertNotNull(form.getComponents());
		assertEquals(4, form.getComponents().size());
		assertTrue(projectSpace.getFile(
				"Caebat_Model_" + caebat.getId() + ".conf").exists());

		// end-user-code

	}

}
