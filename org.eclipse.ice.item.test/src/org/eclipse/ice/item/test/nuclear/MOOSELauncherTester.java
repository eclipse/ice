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
package org.eclipse.ice.item.test.nuclear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URI;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.io.serializable.IIOService;
import org.eclipse.ice.item.nuclear.MOOSE;
import org.eclipse.ice.item.nuclear.MOOSELauncher;
import org.eclipse.ice.item.test.FakeIOService;
import org.eclipse.ice.item.utilities.moose.MOOSEFileHandler;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * This class tests the MOOSELauncher Item to make sure that it can correctly
 * create its Form and process a modified Form.
 * 
 * @author Jay Jay Billings
 */
public class MOOSELauncherTester {

	/**
	 * The project space used to create the workspace for the tests.
	 */
	private static IProject projectSpace;
	
	/**
	 * The IO Service used to read/write via MOOSEFileHandler.
	 */
	private static IIOService service;

	/**
	 * A MOOSE Launcher used for testing.
	 */
	private static MOOSELauncher launcher;

	/**
	 * This operation sets up the workspace. It copies the necessary MOOSE data
	 * files into ${workspace}/MOOSE.
	 */
	@BeforeClass
	public static void beforeTests() {

		// Local Declarations
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		URI defaultProjectLocation = null;
		IProject project = null;
		String projectName = "MOOSEModelTesterWorkspace";
		String separator = System.getProperty("file.separator");
		String userDir = System.getProperty("user.home") + separator
				+ "ICETests" + separator + "itemData";
		String filePath = userDir + separator + "input_coarse10.i";
		String filePath2 = userDir + separator + "input_coarse10_filetest.i";
		// Debug information
		System.out.println("MOOSE Test Data File: " + filePath);

		// Setup the project
		try {
			// Get the project handle
			project = workspaceRoot.getProject(projectName);
			// If the project does not exist, create it
			if (!project.exists()) {
				// Set the location as
				// ${workspace_loc}/MOOSEModelTesterWorkspace
				defaultProjectLocation = (new File(userDir + separator
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

			// Create the File handle and input stream for the Bison input
			// file
			IPath moosePath = new Path(filePath);
			File mooseFile = moosePath.toFile();
			FileInputStream mooseStream = new FileInputStream(mooseFile);
			// Create the file in the workspace for the Bison input file
			IFile bisonInputFile = project.getFile("input_coarse10.i");
			if (!bisonInputFile.exists()) {
				bisonInputFile.create(mooseStream, true, null);
			}

			IPath moosePath2 = new Path(filePath2);
			File mooseFile2 = moosePath2.toFile();
			FileInputStream mooseStream2 = new FileInputStream(mooseFile2);
			// Create the file in the workspace for the Bison input file
			IFile bisonInputFile2 = project
					.getFile("input_coarse10_filetest.i");
			if (!bisonInputFile2.exists()) {
				bisonInputFile2.create(mooseStream2, true, null);
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

		// Set up an IO service and add a reader
		launcher = new MOOSELauncher(projectSpace);
		service = new FakeIOService();
		service.addReader(new MOOSEFileHandler());
		launcher.setIOService(service);

		return;
	}

	/**
	 * 
	 */
	@Test
	public void checkDynamicFileGeneration() {
		
		// Local Declarations
		DataComponent fileDataComp = 
				(DataComponent) launcher.getForm().getComponent(1);
		assertTrue(fileDataComp
				.retrieveEntry("Input File").setValue("input_coarse10.i"));
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		assertEquals(4, fileDataComp.retrieveAllEntries().size());

		// Now change the file name
		assertTrue(fileDataComp
				.retrieveEntry("Input File").setValue("input_coarse10_filetest.i"));

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertEquals(3, fileDataComp.retrieveAllEntries().size());
	}

	/**
	 * <p>
	 * This operation checks the MOOSE Item to ensure that its equals()
	 * operation works.
	 * </p>
	 * 
	 */
	@Test
	public void checkEquality() {

		// Create JobLauncherItems to test
		MOOSELauncher equalItem = new MOOSELauncher(projectSpace);
		MOOSELauncher unEqualItem = new MOOSELauncher();
		MOOSELauncher transitiveItem = new MOOSELauncher(projectSpace);

		// Create a MOOSE Item
		MOOSELauncher item = new MOOSELauncher(projectSpace);

		// Set the IO service on the item so we can read/load data in
		item.setIOService(service);
		equalItem.setIOService(service);
		transitiveItem.setIOService(service);
		
		// Load the input
		item.loadInput("input_coarse10.i");
		equalItem.loadInput("input_coarse10.i");
		transitiveItem.loadInput("input_coarse10.i");
		
		// Set ICEObject data
		equalItem.setId(item.getId());
		transitiveItem.setId(item.getId());
		unEqualItem.setId(2);

		// Set names
		equalItem.setName(item.getName());
		transitiveItem.setName(item.getName());
		unEqualItem.setName("DC UnEqual");

		// Assert two equal Items return true
		assertTrue(item.equals(equalItem));

		// Assert two unequal Items return false
		assertFalse(item.equals(unEqualItem));

		// Assert equals() is reflexive
		assertTrue(item.equals(item));

		// Assert the equals() is Symmetric
		assertTrue(item.equals(equalItem) && equalItem.equals(item));

		// Assert equals() is transitive
		if (item.equals(equalItem) && equalItem.equals(transitiveItem)) {
			assertTrue(item.equals(transitiveItem));
		} else {
			fail();
		}

		// Assert equals is consistent
		assertTrue(item.equals(equalItem) && item.equals(equalItem)
				&& item.equals(equalItem));
		assertTrue(!item.equals(unEqualItem) && !item.equals(unEqualItem)
				&& !item.equals(unEqualItem));

		// Assert checking equality with null is false
		assertFalse(item == null);

		// Assert that two equal objects return same hashcode
		assertTrue(item.equals(equalItem)
				&& item.hashCode() == equalItem.hashCode());

		// Assert that hashcode is consistent
		assertTrue(item.hashCode() == item.hashCode());

		// Assert that hashcodes from unequal objects are different
		assertTrue(item.hashCode() != unEqualItem.hashCode());

	}

	/**
	 * <p>
	 * This operation checks the MOOSE to ensure that its copy() and clone()
	 * operations work as specified.
	 * </p>
	 * 
	 */
	@Test
	public void checkCopying() {

		// Local Declarations
		MOOSE cloneItem = new MOOSE(null), copyItem = new MOOSE(null);
		MOOSE mooseItem = new MOOSE(projectSpace);

		mooseItem.setIOService(service);
		mooseItem.loadInput("input_coarse10.i");
		
		mooseItem.setDescription("I am a job!");
		mooseItem.setProject(null);

		// run clone operations
		cloneItem = (MOOSE) mooseItem.clone();

		// check contents
		assertEquals(mooseItem.getAvailableActions(),
				cloneItem.getAvailableActions());
		assertEquals(mooseItem.getDescription(), cloneItem.getDescription());
		assertTrue(mooseItem.getForm().equals(cloneItem.getForm()));
		assertEquals(mooseItem.getId(), cloneItem.getId());
		assertEquals(mooseItem.getItemType(), cloneItem.getItemType());
		assertEquals(mooseItem.getName(), cloneItem.getName());
		assertEquals(mooseItem.getStatus(), cloneItem.getStatus());

		// run copy operation
		copyItem.copy(mooseItem);

		// check contents
		assertEquals(mooseItem.getAvailableActions(),
				copyItem.getAvailableActions());
		assertEquals(mooseItem.getDescription(), copyItem.getDescription());
		assertTrue(mooseItem.getForm().equals(copyItem.getForm()));
		assertEquals(mooseItem.getId(), copyItem.getId());
		assertEquals(mooseItem.getItemType(), copyItem.getItemType());
		assertEquals(mooseItem.getName(), copyItem.getName());
		assertEquals(mooseItem.getStatus(), copyItem.getStatus());

		// run copy operation by passing null
		copyItem.copy(null);

		// check contents - nothing has changed
		assertEquals(mooseItem.getAvailableActions(),
				copyItem.getAvailableActions());
		assertEquals(mooseItem.getDescription(), copyItem.getDescription());
		assertTrue(mooseItem.getForm().equals(copyItem.getForm()));
		assertEquals(mooseItem.getId(), copyItem.getId());
		assertEquals(mooseItem.getItemType(), copyItem.getItemType());
		assertEquals(mooseItem.getName(), copyItem.getName());
		assertEquals(mooseItem.getStatus(), copyItem.getStatus());

		return;
	}

	/**
	 * Closes the MOOSE tester workspace created in the BeforeClass method.
	 */
	@AfterClass
	public static void afterTests() {

		try {
			// Close and delete the fake project space created
			projectSpace.close(null);
			projectSpace.delete(true, true, null);
			
			// Nullify the IO service
			service = null;
			
		} catch (CoreException e) {
			e.printStackTrace();
		}

		return;
	}

}