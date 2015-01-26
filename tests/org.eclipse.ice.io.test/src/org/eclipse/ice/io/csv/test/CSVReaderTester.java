/*******************************************************************************
 * Copyright (c) 2013, 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings
 *******************************************************************************/
package org.eclipse.ice.io.csv.test;

import static org.junit.Assert.*;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.ice.io.csv.CSVReader;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

/**
 * Test class for {@link org.eclipse.ice.io.csv.CSVReader}.
 * 
 * @author Jay Jay Billings
 *
 */
public class CSVReaderTester {

	/**
	 * The CSV file that will be tested
	 */
	private static IFile testFile;
	/**
	 * The reader that will be tested.
	 */
	private static CSVReader reader;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// Get the file separator used on this system, which is different across
		// OSes.
		String separator = System.getProperty("file.separator");
		// Create the path for the reflectivity file in the ICE tests directory
		String userHome = System.getProperty("user.home");
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = null;
		String projectName = "CSVLoaderTesterWorkspace";
		String filename = "getSpecRefSqrdMod_q841.csv";
		IPath projectPath = new Path(userHome + separator + "ICETests"
				+ separator + projectName + separator + ".project");

		// Setup the project
		try {
			// Create the project description
			IProjectDescription desc = ResourcesPlugin.getWorkspace()
					.loadProjectDescription(projectPath);
			// Get the project handle and create it
			project = workspaceRoot.getProject(desc.getName());
			project.create(desc, new NullProgressMonitor());
			// Open the project if it is not already open
			if (project.exists() && !project.isOpen()) {
				project.open(new NullProgressMonitor());
			}
			// Refresh the workspace
			project.refreshLocal(IResource.DEPTH_INFINITE,
					new NullProgressMonitor());
			// Create the IFile handle for the csv file
			testFile = project.getFile(filename);
		} catch (CoreException e) {
			// Catch exception for creating the project
			e.printStackTrace();
			fail();
		}

		// Create the reader
		reader = new CSVReader();
		
		return;
	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.io.csv.CSVReader#read(org.eclipse.core.resources.IFile)}
	 * .
	 * 
	 * @throws CoreException
	 * @throws IOException
	 */
	@Test
	public void testRead() throws CoreException, IOException {

		// Load the file
		
		// Check the Form
		
		// Check the first element of the list
		
		// Check the last element of the list
		
		
		
		
	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.io.csv.CSVReader#findAll(org.eclipse.core.resources.IFile, java.lang.String)}
	 * .
	 */
	@Test
	public void testFindAll() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.eclipse.ice.io.csv.CSVReader#getReaderType()}.
	 */
	@Test
	public void testGetReaderType() {
		fail("Not yet implemented");
	}

}
