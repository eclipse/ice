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

import java.io.File;
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

	private String separator = System.getProperty("file.separator");
	private String userDir = System.getProperty("user.home" + separator + "ICETests");
	
	/**
	 * Check the setup of a New ICE Item Project
	 */
	@Test
	public void testProjectSetup() {
		
		// Set the location to create the projects
		String projectName = "   ";
		URI loc = new File(userDir + separator + projectName).toURI();
		
		// Try creating the project with a null name
		Assert.assertNull(NewICEItemProjectSupport.createProject(null, null));

		// Try creating the project with a blank name
		Assert.assertNull(NewICEItemProjectSupport.createProject(projectName, loc));

		// Try creating the project with a valid name
		projectName = "ICEItemProjectTesterWorkspace";
		loc = new File(userDir + separator + projectName).toURI();
		projectSpace = NewICEItemProjectSupport.createProject(projectName, loc);
		Assert.assertNotNull(projectSpace);

		// Try creating the project with the same name as an already existing
		// one

		IProject project = NewICEItemProjectSupport.createProject(projectName, loc);
		Assert.assertEquals(projectSpace, project);

		// Check that all dotfiles are created correctly

		// Check that custom project nature is set up
		boolean hasICENature = false;
		try {
			hasICENature = projectSpace.hasNature(ICEItemNature.NATURE_ID);
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
