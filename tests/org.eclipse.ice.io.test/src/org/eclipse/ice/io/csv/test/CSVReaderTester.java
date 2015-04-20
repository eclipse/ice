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
import org.eclipse.ice.datastructures.ICEObject.ListComponent;
import org.eclipse.ice.datastructures.form.Form;
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
		Form form = reader.read(testFile);

		// Check the Form
		assertTrue(form.getComponents().get(0) instanceof ListComponent);
		ListComponent<String[]> lines = (ListComponent<String[]>) form
				.getComponents().get(0);
		assertTrue(lines.get(0) instanceof String[]);
		assertEquals(lines.size(), 175);

		// Check the first element of the list. The first line of the file is a
		// comment, so it should be skipped and this should be data.
		String [] line = lines.get(0);
		assertEquals(3,line.length);
		assertEquals("0.00740898494001999",line[0]);
		assertEquals("4.25",line[1]);
		assertEquals("0.999852520984978",line[2]);

		// Check the last element of the list
		line = lines.get(lines.size()-1);
		assertEquals(4,line.length);
		assertEquals("2.0700000000E-06",line[0]);
		assertEquals("4.7498147887E-11",line[1]);
		assertEquals("1.9976998807E-12",line[2]);
		assertEquals("1.0000000000E+02",line[3]);
		
		return;
	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.io.csv.CSVReader#findAll(org.eclipse.core.resources.IFile, java.lang.String)}
	 * .
	 */
//	@Test
	public void testFindAll() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.eclipse.ice.io.csv.CSVReader#getReaderType()}.
	 */
	@Test
	public void testGetReaderType() {
		assertEquals("csv", reader.getReaderType());
	}

}
