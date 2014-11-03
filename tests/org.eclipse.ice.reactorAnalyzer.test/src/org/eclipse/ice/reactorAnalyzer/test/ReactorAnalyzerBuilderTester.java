/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.reactorAnalyzer.test;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URI;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemType;
import org.eclipse.ice.reactorAnalyzer.ReactorAnalyzer;
import org.eclipse.ice.reactorAnalyzer.ReactorAnalyzerBuilder;
import org.junit.Test;

/**
 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
 * 
 * @author bkj
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class ReactorAnalyzerBuilderTester {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The instance of the ReactorAnalyzerBuilder that will be tested.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ReactorAnalyzerBuilder reactorAnalyzerBuilder;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The fake implementation of IAnalysisTool that is used to trick the
	 * ReactorAnalyzerBuilder into building without loading heavyweight
	 * components.
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
	 * This operation checks that the ReactorAnalyzerBuilder will create and
	 * return a fully initialized ReactorAnalyzer if and only if an analysis
	 * tools has been set. It also checks the name and type of the builder.
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
					.getProject("reactorAnalyzerBuilderTesterWorkspace");
			// If the project does not exist, create it
			if (!project.exists()) {
				// Set the location as ${workspace_loc}/ItemTesterWorkspace
				defaultProjectLocation = (new File(
						System.getProperty("user.dir") + separator
								+ "reactorAnalyzerBuilderTesterWorkspace"))
						.toURI();
				// Create the project description
				IProjectDescription desc = ResourcesPlugin.getWorkspace()
						.newProjectDescription(
								"reactorAnalyzerBuilderTesterWorkspace");
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
		reactorAnalyzerBuilder = new ReactorAnalyzerBuilder();

		// Check the name - the static value, the getter and the comparison of
		// the two
		assertEquals("Reactor Analyzer", reactorAnalyzerBuilder.name);
		assertEquals("Reactor Analyzer", reactorAnalyzerBuilder.getItemName());
		assertEquals(reactorAnalyzerBuilder.name,
				reactorAnalyzerBuilder.getItemName());

		// Check the type - the static value, the getter and the comparison of
		// the two
		assertEquals(ItemType.AnalysisSession, reactorAnalyzerBuilder.type);
		assertEquals(ItemType.AnalysisSession,
				reactorAnalyzerBuilder.getItemType());
		assertEquals(reactorAnalyzerBuilder.type,
				reactorAnalyzerBuilder.getItemType());

		// Set a FakeAnalysisTool as an analysis tool and make sure construction
		// works properly
		reactorAnalyzerBuilder.addAnalysisTool(fakeAnalysisTool);
		Item analyzer = reactorAnalyzerBuilder.build(project);
		assertNotNull(analyzer);
		assertTrue(analyzer instanceof ReactorAnalyzer);
		assertEquals("Reactor Analyzer", analyzer.getName());
		assertEquals(ItemType.AnalysisSession, analyzer.getItemType());
		assertEquals(reactorAnalyzerBuilder.getItemName(),
				analyzer.getItemBuilderName());

		return;

		// end-user-code
	}
}