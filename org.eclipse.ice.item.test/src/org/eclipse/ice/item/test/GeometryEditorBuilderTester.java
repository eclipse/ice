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
package org.eclipse.ice.item.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.net.URI;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemType;
import org.eclipse.ice.item.geometry.GeometryEditor;
import org.eclipse.ice.item.geometry.GeometryEditorBuilder;
import org.junit.Test;

/**
 * <p>
 * This class is responsible for testing the GeometryEditorBuilder.
 * </p>
 * 
 * @author Jay Jay Billings
 */
public class GeometryEditorBuilderTester {
	/**
	 * <p>
	 * The instance of the GeometryEditorBuilder under test.
	 * </p>
	 * 
	 */
	private GeometryEditorBuilder geometryEditorBuilder;

	/**
	 * <p>
	 * This operations checks the GeometryEditorBuilder to make sure that it
	 * properly creates GeometryEditors. It also checks the name and the type of
	 * the GeometryEditorBuilder and the GeometryEditor.
	 * </p>
	 * 
	 */
	@Test
	public void checkConstruction() {

		// Local Declarations
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		URI defaultProjectLocation = null;
		IProject project = null;
		String separator = System.getProperty("file.separator");
		String filename = null;

		// Setup the project workspace
		try {
			// Get the project handle
			project = workspaceRoot.getProject("itemTesterWorkspace");
			// If the project does not exist, create it
			if (!project.exists()) {
				// Set the location as ${workspace_loc}/ItemTesterWorkspace
				defaultProjectLocation = (new File(
						System.getProperty("user.dir") + separator
								+ "itemTesterWorkspace")).toURI();
				// Create the project description
				IProjectDescription desc = ResourcesPlugin.getWorkspace()
						.newProjectDescription("itemTesterWorkspace");
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
		geometryEditorBuilder = new GeometryEditorBuilder();

		// Check the name - the static value, the getter and the comparison of
		// the two
		assertEquals("Geometry Editor", geometryEditorBuilder.name);
		assertEquals("Geometry Editor", geometryEditorBuilder.getItemName());
		assertEquals(geometryEditorBuilder.name,
				geometryEditorBuilder.getItemName());

		// Check the type - the static value, the getter and the comparison of
		// the two
		assertEquals(ItemType.Geometry, geometryEditorBuilder.type);
		assertEquals(ItemType.Geometry, geometryEditorBuilder.getItemType());
		assertEquals(geometryEditorBuilder.type,
				geometryEditorBuilder.getItemType());

		// Make sure construction works properly
		Item editor = geometryEditorBuilder.build(project);
		assertNotNull(editor);
		assertTrue(editor instanceof GeometryEditor);
		assertEquals("Geometry Editor", editor.getName());
		assertEquals(ItemType.Geometry, editor.getItemType());

		return;

	}
}