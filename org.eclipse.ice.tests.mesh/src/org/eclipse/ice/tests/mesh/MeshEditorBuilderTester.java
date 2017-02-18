/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.tests.mesh;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemType;
import org.eclipse.ice.mesh.editor.MeshEditor;
import org.eclipse.ice.mesh.editor.MeshEditorBuilder;
import org.junit.Test;

/**
 * 
 */

/**
 * This class is responsible for testing the MeshEditorBuilder. It is a very
 * simple test.
 * 
 * @author Jay Jay Billings
 * 
 */
public class MeshEditorBuilderTester {

	/**
	 * This operation makes sure all the information on the builder is correct.
	 */
	@Test
	public void checkMeshEditorBuilder() {

		MeshEditorBuilder builder = new MeshEditorBuilder();

		// Check the name
		assertEquals("Mesh Editor", builder.getItemName());
		assertEquals("Mesh Editor", MeshEditorBuilder.name);
		// Check the type
		assertEquals(ItemType.Mesh, builder.getItemType());
		assertEquals(ItemType.Mesh, MeshEditorBuilder.type);
		// Check what it builds
		Item model = builder.build(null);
		assertTrue(model instanceof MeshEditor);

		return;
	}

}
