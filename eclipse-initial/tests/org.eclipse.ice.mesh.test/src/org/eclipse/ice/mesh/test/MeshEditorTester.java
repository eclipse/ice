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
package org.eclipse.ice.mesh.test;

import static org.junit.Assert.*;

import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.mesh.MeshComponent;
import org.eclipse.ice.mesh.editor.MeshEditor;
import org.eclipse.ice.mesh.editor.MeshEditorBuilder;
import org.junit.Test;

/**
 * This class is responsible for testing the MeshEditor to make sure that it is
 * correctly constructed.
 * 
 * @author bkj
 * 
 */
public class MeshEditorTester {

	@Test
	public void checkConstruction() {

		MeshEditor editor = new MeshEditor(null);

		// Check the name
		assertEquals(MeshEditorBuilder.name,editor.getName());
		// Check the type
		assertEquals(MeshEditorBuilder.type,editor.getItemType());
		// Check the description. I don't care what it say as long as it isn't
		// empty.
		assertTrue(!editor.getDescription().isEmpty());
		
		// Get the form and check it
		Form form = editor.getForm();
		assertEquals(1,form.getComponents().size());
		assertTrue(form.getComponent(1) instanceof MeshComponent);

		return;
	}
}
