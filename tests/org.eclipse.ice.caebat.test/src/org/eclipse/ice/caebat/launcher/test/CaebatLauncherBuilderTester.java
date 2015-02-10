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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.eclipse.ice.caebat.launcher.CaebatLauncher;
import org.eclipse.ice.caebat.launcher.CaebatLauncherBuilder;

import java.io.File;
import java.net.URI;

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
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * A class that tests the CaebatLauncherBuilder.
 * 
 * @author s4h
 * 
 */
public class CaebatLauncherBuilderTester {
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
	 * This operation sets up the workspace. It copies the necessary CAEBAT data
	 * files into ${workspace}/CAEBAT.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
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
	 * This operation checks the getItemType().
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkGetItemType() {
		// begin-user-code

		// Local declarations
		CaebatLauncherBuilder caebatMPLauncherBuilder;

		// Create the builder
		caebatMPLauncherBuilder = new CaebatLauncherBuilder();

		// Check the ItemType
		assertEquals(ItemType.Simulation, caebatMPLauncherBuilder.getItemType());

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the build(). It will go through and make sure that
	 * all the data on the launcher is also configured correctly (hostnames,
	 * name, description, etc).
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkBuild() {
		// begin-user-code

		// Local declarations
		IProject project = projectSpace;
		CaebatLauncherBuilder caebatMPLauncherBuilder;
		CaebatLauncher caebatMPLauncher;
		// Create the builder
		caebatMPLauncherBuilder = new CaebatLauncherBuilder();

		// call the build operation with project as null
		caebatMPLauncher = (CaebatLauncher) caebatMPLauncherBuilder.build(null);

		// Check information
		assertNotNull(caebatMPLauncher);
		// Check the item builders name
		assertEquals(caebatMPLauncherBuilder.getItemName(),
				caebatMPLauncher.getItemBuilderName());

		// Check values stored on the form
		// Check to see if executable exists
		assertEquals("Caebat Launcher", caebatMPLauncher.getForm().getName());
		assertEquals(
				"Caebat is a coupled battery and physics simulation from ORNL.",
				caebatMPLauncher.getForm().getDescription());

		// Check to see if the hosts exist
		assertNotNull(caebatMPLauncher.getAllHosts());
		assertEquals(1, caebatMPLauncher.getAllHosts().size());

		// check the contents of the hosts
		assertEquals("localhost", caebatMPLauncher.getAllHosts()
				.get(0));

		// call the build operation with project is not null
		this.setupIProject();
		caebatMPLauncher = (CaebatLauncher) caebatMPLauncherBuilder
				.build(project);

		// Check information
		assertNotNull(caebatMPLauncher);
		// Check values stored on the form
		// Check to see if executable exists
		assertEquals("Caebat Launcher", caebatMPLauncher.getForm().getName());
		assertEquals(
				"Caebat is a coupled battery and physics simulation from ORNL.",
				caebatMPLauncher.getForm().getDescription());

		// Check to see if the hosts exist
		assertNotNull(caebatMPLauncher.getAllHosts());
		assertEquals(1, caebatMPLauncher.getAllHosts().size());

		// Check hosts
		assertEquals("localhost", caebatMPLauncher.getAllHosts()
				.get(0));

		// end-user-code
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
		// begin-user-code

		// Local declarations
		CaebatLauncherBuilder caebatMPLauncherBuilder;

		// Create the builder
		caebatMPLauncherBuilder = new CaebatLauncherBuilder();

		// Check the ItemName
		assertEquals("Caebat Launcher", caebatMPLauncherBuilder.getItemName());

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * A utility operation that sets up the IProject space for the tests. It
	 * creates a CaebatTesterWorkspace for the launcher to be built in.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private void setupIProject() {
		URI defaultProjectLocation = null;
		String separator = System.getProperty("file.separator");

		try {
			// Get the project handle
			IProject project = projectSpace;
			// If the project does not exist, create it
			if (!project.exists()) {
				// Set the location as ${workspace_loc}/ItemTesterWorkspace
				defaultProjectLocation = (new File(
						System.getProperty("user.home") + separator
								+ "ICETests" + separator
								+ "caebatTesterWorkspace")).toURI();
				// Create the project description
				IProjectDescription desc = ResourcesPlugin.getWorkspace()
						.newProjectDescription("caebatTesterWorkspace");
				// Set the location of the project
				desc.setLocationURI(defaultProjectLocation);
				// Create the project
				project.create(desc, null);
			}
			// Open the project if it is not already open
			if (project.exists() && !project.isOpen()) {
				project.open(null);
			}
		} catch (CoreException e) {
			// Catch for creating the project
			e.printStackTrace();
			fail();
		}

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
		}
	}
	
}
