/*******************************************************************************
 * Copyright (c) 2013, 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings, John Ankner
 *******************************************************************************/
package org.eclipse.ice.reflectivity.test;

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

/**
 * This class tests {@link org.eclipse.ice.reflectivity.ReflectivityCalculator}.
 * 
 * @author Jay Jay Billings, John Ankner
 *
 */
public class ReflectivityCalculatorTester {

	/**
	 * The CSV file that will be tested
	 */
	private static IFile testFile;
	/**
	 * The reader that will be tested.
	 */
	private static CSVReader reader;

	/**
	 * The project used by the test. It currently points to the
	 * CSVLoaderTesterWorkspace because the reflectivity CSV files are also used
	 * in the CSV tests.
	 */
	private static IProject project;

	/**
	 * This class loads the files for the test.
	 * 
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
	 * This class tests {@link ReflectivityCalculator#getSpectRefSqrdMod}.
	 */
	@Test
	public void testGetSpecRefSqrdMod() {
		fail("Not yet implemented");
	}

}
