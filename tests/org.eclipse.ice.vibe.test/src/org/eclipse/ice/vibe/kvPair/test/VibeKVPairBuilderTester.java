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
import org.eclipse.ice.vibe.kvPair.VibeKVPair;
import org.eclipse.ice.vibe.kvPair.VibeKVPairBuilder;
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
	 * <p>
	 * This operation checks the getItemType().
	 * </p>
	 */
	@Test
	public void checkGetItemType() {
		VibeKVPairBuilder vibeKVBuilder = new VibeKVPairBuilder();
		assertEquals(ItemType.Model, vibeKVBuilder.getItemType());
	}

	/**
	 * <p>
	 * This operation checks the build method.  Checks to make sure that the item name
	 * and description match.
	 * </p>
	 */
	@Test
	public void checkBuild() {
		VibeKVPairBuilder vibeKVBuilder = new VibeKVPairBuilder();

		// Call the build operation with project as null
		VibeKVPair vibeKV = (VibeKVPair) vibeKVBuilder.build(null);
		assertNotNull(vibeKV);
		
		// Check the item name and description
		assertEquals(vibeKVBuilder.getItemName(),
				vibeKV.getItemBuilderName());
		assertEquals("VIBE Key-Value Pair", vibeKV.getForm().getName());
		assertEquals("Generate input files for VIBE.",vibeKV.getForm().getDescription());
	}

	/**
	 * <p>
	 * This operation checks the getItemName().
	 * </p>
	 */
	@Test
	public void checkGetItemName() {
		VibeKVPairBuilder vibeKVBuilder = new VibeKVPairBuilder();
		assertEquals("VIBE Key-Value Pair", vibeKVBuilder.getItemName());
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
			e.printStackTrace();
			fail("Could not clean up project space");
		}
	}
}
