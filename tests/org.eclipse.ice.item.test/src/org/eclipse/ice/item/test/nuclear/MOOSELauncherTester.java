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
import static org.junit.Assert.assertNotNull;
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
import org.eclipse.ice.datastructures.form.AllowedValueType;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.io.serializable.IOService;
import org.eclipse.ice.item.nuclear.MOOSELauncher;
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
	private static IOService service;

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
		service = new IOService();
		service.addReader(new MOOSEFileHandler());
		launcher.setIOService(service);

		return;
	}

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
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertEquals(3, fileDataComp.retrieveAllEntries().size());
	}
	
	@Test
	public void checkCustomAppEntry() {
		
		// Local declarations
		DataComponent execDataComp = 
				(DataComponent) launcher.getForm().getComponent(5);
		final String customExecName = "Custom executable name";
		Entry standardExecEntry = execDataComp.retrieveEntry("Executable");
		Entry customExecEntry = execDataComp.retrieveEntry(customExecName);
		
		// Verify the DataComponent that holds the executable Entries
		assertEquals(execDataComp.retrieveAllEntries().size(), 2);
		assertNotNull(standardExecEntry);
		assertNotNull(customExecEntry);
		
		// Verify the standard Entry contains an option for a custom app
		assertTrue(standardExecEntry.getAllowedValues().contains(customExecName));
		
		// Verify the custom app Entry's type
		assertEquals(customExecEntry.getValueType(), AllowedValueType.Undefined);
		
		return;
	}

	/**
	 * Closes the MOOSE tester workspace created in the BeforeClass method.
	 */
	@AfterClass
	public static void afterTests() {

		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		try {
			// Close and delete the fake workspace created
			projectSpace.close(null);
			workspaceRoot.delete(true, true, null);
			
			// Nullify the IO service
			service = null;
			
		} catch (CoreException e) {
			e.printStackTrace();
		}

		return;
	}

}