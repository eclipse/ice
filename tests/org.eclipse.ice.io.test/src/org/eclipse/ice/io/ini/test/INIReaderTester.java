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
package org.eclipse.ice.io.ini.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;

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
import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.io.ini.INIReader;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests the methods of the INIReader class.  
 * 
 * @author Andrew Bennett
 *
 */
public class INIReaderTester {
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
				+ "ICETests" + separator + "ioTesterWorkspace";
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
	 * Tests the IPSReader
	 */
	@Test
	public void checkINIReader() {
		
		// Set up where to look
		IProject project = projectSpace;
		IFile inputFile = project.getFile("example.ini");
		IFile templateFile = project.getFile("example_template.ini");
		
		// Create an IPSReader to test
		INIReader reader = new INIReader("!");
		assertNotNull(reader);
		assertEquals(reader.getReaderType(), "INIReader");
		
		// Try to read in invalid INI file
		IFile fakeFile = null;
		Form form = null;
		form = reader.read(fakeFile);
		assertNull(form);
		
		// Load the INI file and parse the contents into Components
		form = reader.read(inputFile);
		assertNotNull(form);
		assertEquals(form.getComponents().size(), 3);

		ArrayList<Component> sections = form.getComponents();
		assertEquals(((DataComponent)sections.get(0)).retrieveAllEntries().size(), 4);
		assertEquals(((DataComponent)sections.get(1)).retrieveAllEntries().size(), 1);
		assertEquals(((DataComponent)sections.get(2)).retrieveAllEntries().size(), 2);
		
		// Test setting the template
		reader.addTemplateType("template", templateFile);
		reader.setTemplateType("template");
		
		// Test reading into the form using the template
		form = reader.read(inputFile);
		assertNotNull(form);
		assertEquals(form.getComponents().size(), 4);
		
		// Make sure the components have the right entries
		sections = form.getComponents();
		assertEquals(((DataComponent)sections.get(0)).retrieveAllEntries().size(), 5);
		assertEquals(((DataComponent)sections.get(1)).retrieveAllEntries().size(), 4);
		assertEquals(((DataComponent)sections.get(2)).retrieveAllEntries().size(), 1);		
		assertEquals(((DataComponent)sections.get(3)).retrieveAllEntries().size(), 1);					
		
		// Okay good job
		return;
	}
}
