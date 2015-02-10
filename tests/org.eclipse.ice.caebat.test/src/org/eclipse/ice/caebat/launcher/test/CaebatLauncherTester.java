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
package org.eclipse.ice.caebat.launcher.test;

import static org.junit.Assert.*;

import org.eclipse.ice.caebat.launcher.CaebatLauncher;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.core.resources.IFolder;
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
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * A class that tests the CaebatLauncher.
 * 
 * @author s4h, Andrew Bennett
 * 
 */
public class CaebatLauncherTester {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * A reference to the IProject workspace. This parameter is setup in the
	 * setupIProject operation.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private static IProject projectSpace;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation sets up the workspace. 
	 * </p>
	 * <!-- end-UML-doc -->
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
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation tests the CaebatLauncher(). Checks the name, description,
	 * and how it handles if a project space is given or not.
	 * </p>
	 * <p>
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkConstruction() {
		// begin-user-code

		// Local declarations
		CaebatLauncher caebatLauncher;
		String separator = System.getProperty("file.separator");

		// Try with project as null
		caebatLauncher = new CaebatLauncher(null);

		// check to see if the form exists, and the item is setup correctly.
		assertEquals("Caebat Launcher", caebatLauncher.getName());
		assertEquals(
				"Caebat is a coupled battery and physics simulation from ORNL.",
				caebatLauncher.getDescription());

		// Try with project not as null
		IProject project = projectSpace;
		caebatLauncher = new CaebatLauncher(project);

		// check to see if the form exists, and the item is setup correctly.
		assertEquals("Caebat Launcher", caebatLauncher.getName());
		assertEquals(
				"Caebat is a coupled battery and physics simulation from ORNL.",
				caebatLauncher.getDescription());

		IFolder newLimbo = project.getFolder("newDirectory");
		if (!newLimbo.exists()) {
			try {
				newLimbo.create(true, true, null);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}

		String srcPath = project.getLocation().toOSString() + separator + project.getFolder("Directory").getName();
		String destPath = project.getLocation().toOSString() + separator + project.getFolder("newDirectory").getName();
		caebatLauncher.copyInputDirectory(srcPath, destPath);
	
		// Make sure all of the files were copied, then delete them all
		ArrayList<File> copiedFiles = new ArrayList<File>();
		copiedFiles.add(new File(destPath + separator + "DeepDirectory" + separator + "DeeperThanDeep" + separator + "file"));
		copiedFiles.add(new File(destPath + separator + "DeepDirectory" + separator + "DeeperThanDeep"));
		copiedFiles.add(new File(destPath + separator + "DeepDirectory" + separator + "deepFile"));
		copiedFiles.add(new File(destPath + separator + "DeepDirectory"));		
		copiedFiles.add(new File(destPath + separator + "shallowFile"));
		copiedFiles.add(new File(destPath));

		for (File f : copiedFiles) {
			assertTrue(f.exists());
			f.delete();
		}
		
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the CaebatLauncher operation(). Checks specifically
	 * the form configuration.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkSetupForm() {
		// begin-user-code

		// Local declarations
		CaebatLauncher CaebatLauncher;

		// Setup the IProject
		IProject project = projectSpace;

		// Create a CaebatLauncher
		CaebatLauncher = new CaebatLauncher(project);

		// Check values stored on the form
		// Check to see if executable exists
		assertEquals("Caebat Launcher", CaebatLauncher.getForm().getName());
		assertEquals(
				"Caebat is a coupled battery and physics simulation from ORNL.",
				CaebatLauncher.getForm().getDescription());

		// Check to see if the hosts exist
		assertNotNull(CaebatLauncher.getAllHosts());
		assertEquals(1, CaebatLauncher.getAllHosts().size());

		// check the contents of the hosts
		assertEquals("localhost", CaebatLauncher.getAllHosts().get(0));

		// end-user-code
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
			fail("Caebat Launcher Tester: Error!  Could not clean up project space.");
		}
	}
}
