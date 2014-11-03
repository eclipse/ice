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
package org.eclipse.ice.visit.viewer.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.eclipse.ice.visit.viewer.*;

import java.io.File;
import java.net.URI;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemType;
import org.junit.Test;

/**
 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
 * 
 * @author bkj
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class VisItViewerBuilderTester {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The instance of the VisItViewerBuilder that will be tested.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private VisItViewerBuilder VisItViewerBuilder;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The fake implementation of IAnalysisTool that is used to trick the
	 * VisItViewerBuilder into building without loading heavyweight components.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private FakeAnalysisTool fakeAnalysisTool;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks that the VisItViewerBuilder will create and return
	 * a fully initialized VisItViewer if and only if an analysis tools has been
	 * set. It also checks the name and type of the builder.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkBuilding() {
		// begin-user-code

		// Local Declarations
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		URI defaultProjectLocation = null;
		IProject project = null;
		String separator = System.getProperty("file.separator");
		String filename = null;
		fakeAnalysisTool = new FakeAnalysisTool();

		// Setup the project workspace
		try {
			// Get the project handle
			project = workspaceRoot
					.getProject("VisItViewerBuilderTesterWorkspace");
			// If the project does not exist, create it
			if (!project.exists()) {
				// Set the location as ${workspace_loc}/ItemTesterWorkspace
				defaultProjectLocation = (new File(
						System.getProperty("user.dir") + separator
								+ "VisItViewerBuilderTesterWorkspace")).toURI();
				// Create the project description
				IProjectDescription desc = ResourcesPlugin.getWorkspace()
						.newProjectDescription(
								"VisItViewerBuilderTesterWorkspace");
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

		// Instantiate the builder
		VisItViewerBuilder = new VisItViewerBuilder();

		// Check the name - the static value, the getter and the comparison of
		// the two
		assertEquals("VisIt Viewer", VisItViewerBuilder.name);
		assertEquals("VisIt Viewer", VisItViewerBuilder.getItemName());
		assertEquals(VisItViewerBuilder.name, VisItViewerBuilder.getItemName());

		// Check the type - the static value, the getter and the comparison of
		// the two
		assertEquals(ItemType.AnalysisSession, VisItViewerBuilder.type);
		assertEquals(ItemType.AnalysisSession, VisItViewerBuilder.getItemType());
		assertEquals(VisItViewerBuilder.type, VisItViewerBuilder.getItemType());

		// Make sure building with a null project doesn't work
		assertNull(VisItViewerBuilder.build(null));

		// Make sure building with no analysis tools specified fails
		assertNull(VisItViewerBuilder.build(project));

		// Set a FakeAnalysisTool as an analysis tool and make sure construction
		// works properly
		VisItViewerBuilder.addAnalysisTool(fakeAnalysisTool);
		Item analyzer = VisItViewerBuilder.build(project);
		assertNotNull(analyzer);
		assertTrue(analyzer instanceof VisItViewer);
		assertEquals("VisIt Viewer", analyzer.getName());
		assertEquals(ItemType.AnalysisSession, analyzer.getItemType());
		assertEquals(VisItViewerBuilder.getItemName(),
				analyzer.getItemBuilderName());

		return;

		// end-user-code
	}
}