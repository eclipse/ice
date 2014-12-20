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

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URI;
import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.junit.BeforeClass;
import org.junit.Test;
import org.eclipse.ice.caebat.model.CaebatModel;
import org.eclipse.ice.datastructures.ICEObject.ICEObject;
import org.eclipse.ice.datastructures.form.AllowedValueType;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.datastructures.form.MasterDetailsComponent;
import org.eclipse.ice.datastructures.form.MatrixComponent;
import org.eclipse.ice.datastructures.form.TableComponent;
import org.eclipse.ice.datastructures.form.TimeDataComponent;
import org.eclipse.ice.datastructures.updateableComposite.Component;

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
		URI defaultProjectLocation = null;
		IProject project = null;
		String projectName = "caebatTesterWorkspace";
		String separator = System.getProperty("file.separator");
		String userDir = System.getProperty("user.home") + separator
				+ "ICETests" + separator + "caebatTesterWorkspace";
		String filePath = userDir + separator + "example_ini.conf";

		// Enable Debugging
		System.setProperty("DebugICE", "");

		// Setup the project
		try {
			// Get the project handle
			project = workspaceRoot.getProject(projectName);
			// If the project does not exist, create it
			if (!project.exists()) {
				// Set the location as
				// ${workspace_loc}/CAEBATModelTesterWorkspace
				defaultProjectLocation = (new File(userDir)).toURI();
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
		assertEquals(4, form.getComponents().size());

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
