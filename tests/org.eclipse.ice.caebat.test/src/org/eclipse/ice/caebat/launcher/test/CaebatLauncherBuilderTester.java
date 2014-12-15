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
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.item.ItemType;
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
	private IProject project;

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
