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
package org.eclipse.ice.projectgeneration.test;

import static org.junit.Assert.fail;

import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import org.eclipse.ice.projectgeneration.ICEItemNature;
import org.eclipse.ice.projectgeneration.NewICEItemProjectSupport;

/**
 * This class tests the creation and configuration of new ICE Item projects.
 *
 * @author arbennett
 */
public class NewICEItemProjectTester {

	private static IProject projectSpace;

	/**
	 * Set up the project for the remainder of the tests
	 */
	@BeforeClass
	public static void beforeTests() {
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = null;
		String separator = System.getProperty("file.separator");
		String userDir = System
				.getProperty("user.home" + separator + "ICETests" + separator + "ICEItemProjectTesterWorkspace");
		System.setProperty("DebugICE", "");

		// Set up the project
		try {
			// Get the project handle
			IPath projectPath = new Path(userDir + separator + ".project");
			// Create the project description
			IProjectDescription desc = ResourcesPlugin.getWorkspace().loadProjectDescription(projectPath);
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
			Assert.assertNotNull(project);
		} catch (CoreException e) {
			// Catch exception for creating the project
			e.printStackTrace();
			fail("ICE Item Project Tester: Error!  Could not set up project space");
		}
		projectSpace = project;
	}

	/**
	 * Check the setup of a New ICE Item Project
	 */
	@Test
	public void testProjectSetup() {

		// Try creating the project with a null name
		Assert.assertNull(NewICEItemProjectSupport.createProject(null, null));

		// Try creating the project with a blank name
		Assert.assertNull(NewICEItemProjectSupport.createProject("  ", null));

		// Try creating the project with a valid name
		IProject project = NewICEItemProjectSupport.createProject("Tester", null);
		Assert.assertNotNull(project);

		// Try creating the project with the same name as an already existing
		// one
		String separator = System.getProperty("file.separator");
		String userDir = System.getProperty("user.home" + separator + "ICETests");
		URI loc = null;
		try {
			loc = new URI("");
		} catch (URISyntaxException e) {
			fail("Could not create a new URI at " + userDir);
		}
		project = NewICEItemProjectSupport.createProject("ICEItemProjectTesterWorkspace", loc);
		Assert.assertEquals(project, projectSpace);

		// Check that all dotfiles are created correctly

		// Check that custom project nature is set up
		boolean hasICENature = false;
		try {
			hasICENature = project.hasNature(ICEItemNature.NATURE_ID);
		} catch (CoreException e) {
			fail("Error while trying to check project nature!");
		}
		Assert.assertTrue(hasICENature);

		// Check that MANIFEST.MF and plugin.xml are set up
		// TODO
	}
	

	/**
	 * Clean up everything we may have messed up
	 */
	@AfterClass
	public static void cleanup() {
		try {
			projectSpace.close(null);
			projectSpace.delete(true, null);
		} catch (CoreException e) {
			e.printStackTrace();
			fail("ICE Item Project Tester: Error!  Could not clean up project space");
		}
	}
}
