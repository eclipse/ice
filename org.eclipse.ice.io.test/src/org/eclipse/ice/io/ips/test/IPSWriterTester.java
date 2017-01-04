/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.io.ips.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Scanner;

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
import org.eclipse.ice.io.ips.IPSReader;
import org.eclipse.ice.io.ips.IPSWriter;
import org.eclipse.january.form.Component;
import org.eclipse.january.form.Form;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests the methods of the IPSWriter class.
 * 
 * @author Andrew R. Bennett
 *
 */
public class IPSWriterTester {
	/**
	 * The project space used to create the workspace for the tests.
	 */
	private static IProject projectSpace;

	/**
	 * 
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
			fail();
		}

		// Set the global project reference.
		projectSpace = project;

		return;
	}
	
	/**
	 * Tests the IPSWriter
	 * @throws IOException 
	 * @throws MalformedURLException 
	 */
	@Test
	public void checkIPSWriter() throws MalformedURLException, IOException {

		// Local declarations
		IProject project = projectSpace;
		IPSWriter writer = null;
		IPSReader reader = null;

		String separator = System.getProperty("file.separator");
		String userDir = System.getProperty("user.home") + separator
				+ "ICETests" + separator + "caebatTesterWorkspace" + separator
				+ "Caebat_Model";
		String outputFilePath = userDir + separator + "ips_WriterTest.conf";
		String tempFilePath = userDir + separator + "tempFile.conf";
		String exampleFilePath = userDir + separator + "example_ini.conf";
		
		IFile outIFile = project.getFile("Caebat_Model" + separator + "ips_WriterTest.conf");
		IFile inIFile = project.getFile("Caebat_Model" + separator + "example_ini.conf");
		
		if (outIFile.exists()) {
			try {
				outIFile.delete(true, new NullProgressMonitor());
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		File exampleFile = new File(exampleFilePath);
		// Create a buffered reader to access the contents of the output file
		BufferedReader inReader = null;
		BufferedReader outReader = null;
		try {
			inReader = new BufferedReader(new InputStreamReader(inIFile.getContents()));
		} catch (CoreException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		// Test that the output file can't be read
		assertNull(outReader);

		/* --- Testing CONSTRUCTION --- */
		writer = new IPSWriter();
		assertNotNull(writer);

		/* --- Testing WRITING --- */
		// Try to write with invalid parameters
		Form fakeForm = null;
		IFile fakeFile = null;

		writer.write(fakeForm, fakeFile);

		// Generate valid Components to test with
		Form inputForm = null;
		reader = new IPSReader();
		inputForm = reader.read(inIFile);
		assertNotNull(inputForm);
		assertEquals(inputForm.getComponents().size(),4);

		// Try to write with valid parameters
		writer.write(inputForm, outIFile);
		if (!outIFile.exists()) {
			fail(" Failed to create " + outIFile.getName());
		}

		// Load up a buffered reader so we can check what came out
		try {
			outReader = new BufferedReader(new InputStreamReader(outIFile.getContents()));
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Check that the file is not empty and has the correct number of lines
		assertNotNull(outReader);
		int numLines = 0;
		try {
			while (outReader.readLine() != null) {
				numLines++;
			}
		} catch (IOException e) {
			e.printStackTrace();
			fail("Failed to read BufferedReader");
		}

		// Make sure we got the length right
		assertEquals(113, numLines);

	
		// Now try reading from the test file and creating another set of
		// Components to compare to the ones read from the example file
		Form outputForm = reader.read(outIFile);

		// Compare the two sets
		ArrayList<Component> components = outputForm.getComponents();
		ArrayList<Component> testComponents = inputForm.getComponents();
		assertEquals(components.size(), testComponents.size());
		for (int i = 0; i < components.size(); i++) {
			assertTrue(components.get(i).getName()
					.equals(testComponents.get(i).getName()));
		}

		// Delete the test output file
		// Close the buffered readers.
		try {
			inReader.close();
			outReader.close();
		} catch (IOException e) {
			e.printStackTrace();
			fail("Failed to close BufferedReader");
		}
		
		// Remove the output file
		if (outIFile.exists()) {
			try {
				outIFile.delete(true, new NullProgressMonitor());
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// Test the replace method
		File tempFile = new File(tempFilePath);
		if (tempFile.exists()) tempFile.delete();
		Files.copy(new File(exampleFilePath).toPath(), new File(tempFilePath).toPath(), StandardCopyOption.REPLACE_EXISTING);
		tempFile.deleteOnExit();
		assertTrue(tempFile.exists());
		
		// Call the replace method
		String replace = "AHAHAHAHAHA";
		writer.replace(inIFile, "SIM_ROOT = .*", replace);

		// Check if the file contains the replacement
		boolean foundReplacement = false;
		try {
			Scanner scanner = new Scanner(exampleFile);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.contains(replace)) {
					foundReplacement = true;
				}
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		assertTrue(foundReplacement);
		
		// Copy back the original file and delete the backup=
		if (exampleFile.getAbsoluteFile().exists()) {
			exampleFile.delete();
		}
		Files.copy(tempFile.toPath(), exampleFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		tempFile.delete();
		
		return;
	}
}
