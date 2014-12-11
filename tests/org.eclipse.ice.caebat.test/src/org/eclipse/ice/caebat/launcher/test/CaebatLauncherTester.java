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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.eclipse.ice.caebat.launcher.CaebatLauncher;

import java.io.File;
import java.net.URI;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.junit.Test;

/**
 * A class that tests the CaebatLauncher.
 * 
 * @author s4h
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
	private IProject project;

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
		CaebatLauncher CaebatLauncher;

		// Try with project as null
		CaebatLauncher = new CaebatLauncher(null);

		// check to see if the form exists, and the item is setup correctly.
		assertEquals("Caebat Launcher", CaebatLauncher.getName());
		assertEquals(
				"Caebat is a coupled battery and physics simulation from ORNL.",
				CaebatLauncher.getDescription());

		// Try with project not as null
		this.setupIProject();
		CaebatLauncher = new CaebatLauncher(project);

		// check to see if the form exists, and the item is setup correctly.
		assertEquals("Caebat Launcher", CaebatLauncher.getName());
		assertEquals(
				"Caebat is a coupled battery and physics simulation from ORNL.",
				CaebatLauncher.getDescription());

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
		this.setupIProject();

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
		assertEquals("localhost", CaebatLauncher.getAllHosts()
				.get(0));

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * A utility operation that sets up the IProject space for the tests. It
	 * tests the launcher with a caebatTesterWorkspace.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private void setupIProject() {
		// begin-user-code
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		URI defaultProjectLocation = null;
		String separator = System.getProperty("file.separator");

		try {
			// Get the project handle
			project = workspaceRoot.getProject("caebatTesterWorkspace");
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
}
