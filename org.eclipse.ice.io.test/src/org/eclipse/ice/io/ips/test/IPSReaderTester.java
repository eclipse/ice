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
import static org.junit.Assert.assertTrue;
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
import org.eclipse.ice.io.ips.IPSReader;
import org.eclipse.january.form.Component;
import org.eclipse.january.form.DataComponent;
import org.eclipse.january.form.Form;
import org.eclipse.january.form.IEntry;
import org.eclipse.january.form.MasterDetailsComponent;
import org.eclipse.january.form.TableComponent;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests the methods of the IPSReader class.  
 * 
 * @author Andrew Bennett
 *
 */
public class IPSReaderTester {
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
	public void checkIPSReader() {
		
		// Set up where to look
		IProject project = projectSpace;
		String separator = System.getProperty("file.separator");
		String filePath = System.getProperty("user.home") + separator + "ICETests" 
				+ separator + "caebatTesterWorkspace" + separator 
				+ "Caebat_Model" + separator + "example_ini.conf";
		IPath fileIPath = new Path(filePath);
		IFile inputFile = project.getFile("Caebat_Model" + separator + "example_ini.conf");

		// Create an IPSReader to test
		IPSReader reader = new IPSReader();
		assertNotNull(reader);
		assertEquals(reader.getReaderType(), "IPSReader");
		
		// Try to read in invalid INI file
		IFile fakeFile = null;
		Form form = null;
		form = reader.read(fakeFile);
		assertTrue(form == null);
		
		// Load the INI file and parse the contents into Components
		form = reader.read(inputFile);
		
		// Make sure we found some components
		ArrayList<Component> components = form.getComponents();
		assertEquals(4,components.size());	
		DataComponent timeLoopData = (DataComponent) components.get(0);
		TableComponent globalConfig = (TableComponent) components.get(1);
		TableComponent portsTable = (TableComponent) components.get(2);
		MasterDetailsComponent portsMaster = (MasterDetailsComponent) components.get(3);


		/* --- Check the GLOBAL CONFIGURATION component --- */
		String configName = "Global Configuration";
		assertEquals(configName, globalConfig.getName());
		assertEquals(20, globalConfig.numberOfRows());
		for (int i = 0; i < 20; i++) {
			assertNotNull(globalConfig.getRow(i));
		}
		
		
		/* --- Check the PORTS TABLE component --- */		
		String portsName = "Ports Table";
		assertEquals(portsName, portsTable.getName());
		assertEquals(5, portsTable.numberOfRows());
		for (int i = 0; i < 5; i++) {
			assertNotNull(portsTable.getRow(i));
		}
		
		/* --- Check the Ports Master component --- */
		String masterName = "Ports Master";
		assertEquals(masterName, portsMaster.getName());
		assertEquals(5, portsMaster.numberOfMasters());
		for (int i = 0; i < 5; i++) {
			assertNotNull(portsMaster.getMasterAtIndex(i));
		}
				
		/* --- Check the TIME LOOP component --- */
		String timeLoopName = "Time Loop Data";
		assertEquals(timeLoopName, timeLoopData.getName());
		assertEquals(5, timeLoopData.retrieveAllEntries().size());
		for (int i = 0; i < 5; i++) {
			assertNotNull(timeLoopData.retrieveAllEntries().get(i));
		}		
		
		/* --- Test the findAll method --- */
		String regex = "SIM_ROOT = .*";
		String fakex = "Sassafras my mass";
		ArrayList<IEntry> matches = reader.findAll(inputFile, regex);
		ArrayList<IEntry> fakes = reader.findAll(inputFile, fakex);
		assertEquals(fakes.size(),0);
		assertEquals(matches.size(),1);
		assertEquals(matches.get(0).getValue(), "SIM_ROOT = $CAEBAT_ROOT/vibe/trunk/examples/${SIM_NAME}");
		
		// Okay good job
		return;
	}
}
