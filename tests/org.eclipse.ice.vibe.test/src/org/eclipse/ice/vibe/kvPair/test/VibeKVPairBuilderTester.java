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
package org.eclipse.ice.vibe.kvPair.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.ice.item.ItemType;
import org.eclipse.ice.vibe.launcher.VibeLauncher;
import org.eclipse.ice.vibe.launcher.VibeLauncherBuilder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * A class that tests the VibeLauncherBuilder.
 * 
 * @author s4h, Andrew Bennett
 * 
 */
public class VibeKVPairBuilderTester {
	/**
	 * The project space used to create the workspace for the tests.
	 */
	private static IProject projectSpace;

	/**
	 * <p>
	 * This operation sets up the workspace. 
	 * </p>
	 */
	@BeforeClass
	public static void beforeTests() {
		// begin-user-code

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
		// end-user-code
	}

	/**
	 * <p>
	 * This operation checks the getItemType().
	 * </p>
	 */
	@Test
	public void checkGetItemType() {
		// Load it up
		VibeLauncherBuilder vibeMPLauncherBuilder = new VibeLauncherBuilder();

		// Check the ItemType
		assertEquals(ItemType.Simulation, vibeMPLauncherBuilder.getItemType());
	}

	/**
	 * <p>
	 * This operation checks the build(). It will go through and make sure that
	 * all the data on the launcher is also configured correctly (hostnames,
	 * name, description, etc).
	 * </p>
	 */
	@Test
	public void checkBuild() {
		// begin-user-code

		// Local declarations
		VibeLauncherBuilder vibeMPLauncherBuilder;
		VibeLauncher vibeMPLauncher;
		// Create the builder
		vibeMPLauncherBuilder = new VibeLauncherBuilder();

		// call the build operation with project as null
		vibeMPLauncher = (VibeLauncher) vibeMPLauncherBuilder.build(null);

		// Check information
		assertNotNull(vibeMPLauncher);
		// Check the item builders name
		assertEquals(vibeMPLauncherBuilder.getItemName(),
				vibeMPLauncher.getItemBuilderName());

		// Check values stored on the form
		// Check to see if executable exists
		assertEquals("VIBE Launcher", vibeMPLauncher.getForm().getName());
		assertEquals("Run a VIBE simulation.",vibeMPLauncher.getForm().getDescription());

		// Check to see if the hosts exist
		assertNotNull(vibeMPLauncher.getAllHosts());
		assertEquals(1, vibeMPLauncher.getAllHosts().size());

		// check the contents of the hosts
		assertEquals("localhost", vibeMPLauncher.getAllHosts()
				.get(0));

		// call the build operation with project is not null
		IProject project = projectSpace;
		vibeMPLauncher = (VibeLauncher) vibeMPLauncherBuilder
				.build(project);

		// Check information
		assertNotNull(vibeMPLauncher);
		// Check values stored on the form
		// Check to see if executable exists
		assertEquals("VIBE Launcher", vibeMPLauncher.getForm().getName());
		assertEquals("Run a VIBE simulation.",vibeMPLauncher.getForm().getDescription());

		// Check to see if the hosts exist
		assertNotNull(vibeMPLauncher.getAllHosts());
		assertEquals(1, vibeMPLauncher.getAllHosts().size());

		// Check hosts
		assertEquals("localhost", vibeMPLauncher.getAllHosts()
				.get(0));
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the getItemName().
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void checkGetItemName() {
		// Create it
		VibeLauncherBuilder vibeMPLauncherBuilder = new VibeLauncherBuilder();

		// Check the ItemName
		assertEquals("VIBE Launcher", vibeMPLauncherBuilder.getItemName());
	}
	
	/**
	 * Clean up after ourselves
	 */
	@AfterClass
	public static void cleanup() {
		try {
			projectSpace.close(null);
			projectSpace.delete(true, null);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Could not clean up project space");
		}
	}
}
