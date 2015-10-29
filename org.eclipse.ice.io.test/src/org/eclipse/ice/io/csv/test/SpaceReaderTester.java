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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

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
import org.eclipse.ice.io.csv.SpaceDelimitedReader;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test class for {@link org.eclipse.ice.io.csv.SpaceDelimitedReader}.
 * 
 * @author Jay Jay Billings
 *
 */
public class SpaceReaderTester {

	/**
	 * The space-delimited file that will be tested
	 */
	private static IFile testFile;

	/**
	 * The reader that will be tested.
	 */
	private static SpaceDelimitedReader reader;

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
		String filename = "waveVector_space.csv";
		IPath projectPath = new Path(userHome + separator + "ICETests"
				+ separator + projectName + separator + ".project");

		// Setup the project
		try {
			// Create the project description
			IProjectDescription desc = ResourcesPlugin.getWorkspace()
					.loadProjectDescription(projectPath);
			// Get the project handle and create it
			project = workspaceRoot.getProject(desc.getName());
			if (!project.exists()) {
				project.create(desc, new NullProgressMonitor());
			}
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
		reader = new SpaceDelimitedReader();

		return;
	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.io.csv.SpaceDelimitedReader#read(org.eclipse.core.resources.IFile)}
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
		assertEquals(402, lines.size());

		// Check the first element of the list. The first line of the file is a
		// comment, so it should be skipped and this should be data.
		String[] line = lines.get(0);
		assertEquals(4, line.length);
		assertEquals("8.14174169E-03", line[0]);
		assertEquals("0.78066729", line[1]);
		assertEquals("0.08261301", line[2]);
		assertEquals("0.000387", line[3]);

		// Check the last element of the list
		line = lines.get(lines.size() - 1);
		assertEquals(4, line.length);
		assertEquals("4.40137332E-01", line[0]);
		assertEquals("0.00000013", line[1]);
		assertEquals("0.00000007", line[2]);
		assertEquals("0.010323", line[3]);

		return;
	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.io.csv.SpaceDelimitedReader#findAll(org.eclipse.core.resources.IFile, java.lang.String)}
	 * .
	 */
	// @Test
	public void testFindAll() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.io.csv.SpaceDelimitedReader#getReaderType()}.
	 */
	@Test
	public void testGetReaderType() {
		assertEquals("space-delimited", reader.getReaderType());
	}

}
