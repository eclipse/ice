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
import org.junit.Test;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.geometry.GeometryComponent;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemType;
import org.eclipse.ice.item.geometry.GeometryEditor;
import org.eclipse.ice.item.geometry.GeometryEditorBuilder;

/**
 * <p>
 * This class is responsible for testing the GeometryEditor.
 * </p>
 * 
 * @author Jay Jay Billings
 */
public class GeometryEditorTester {
	/**
	 * <p>
	 * The instance of the GeometryEditor under test.
	 * </p>
	 * 
	 */
	private GeometryEditor geometryEditor;

	/**
	 * <p>
	 * This operations checks the GeometryEditor to make sure that it properly
	 * creates its Form.
	 * </p>
	 * 
	 */
	@Test
	public void checkConstruction() {

		// Local Declarations
		GeometryEditor editor = new GeometryEditor();
		Form form = null;
		GeometryComponent geomComp = null;

		// Check the Editor's name and type
		assertEquals(GeometryEditorBuilder.name, editor.getName());
		assertEquals(GeometryEditorBuilder.type, editor.getItemType());

		// Check the Form
		form = editor.getForm();
		assertNotNull(form);
		assertEquals(GeometryEditorBuilder.name, form.getName());
		assertEquals(1, form.getActionList().size());
		assertEquals("Export to ICE Native Format", form.getActionList().get(0));

		// Get the GeometryComponent and check it
		geomComp = (GeometryComponent) form.getComponent(1);
		assertNotNull(geomComp);
		assertEquals(1, geomComp.getId());
		assertEquals("Geometry Data", geomComp.getName());

		return;

	}
}