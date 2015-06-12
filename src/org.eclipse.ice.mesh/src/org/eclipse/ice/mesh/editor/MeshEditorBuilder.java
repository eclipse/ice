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
package org.eclipse.ice.mesh.editor;

import org.eclipse.core.resources.IProject;
import org.eclipse.ice.item.AbstractItemBuilder;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemBuilder;
import org.eclipse.ice.item.ItemType;

/**
 * This class implements the ItemBuilder interface to create a 2D Mesh Editor.
 * 
 * @author Jay Jay Billings
 * 
 */
public class MeshEditorBuilder extends AbstractItemBuilder {

	/**
	 * The name of this builder.
	 */
	public static final String name = "Mesh Editor";

	/**
	 * The Item type of this builder.
	 */
	public static final ItemType type = ItemType.Mesh;

	/**
	 * The Constructor
	 */
	public MeshEditorBuilder() {
		setName(name);
		setType(type);
	}
	
	/*
	 * Implements a method from ItemBuilder.
	 */
	@Override
	public Item getInstance(IProject projectSpace) {

		// Build the editor. The Mesh Editor sets its own name and ItemBuilder
		// name.
		Item meshEditor = new MeshEditor(projectSpace);

		return meshEditor;
	}

}
