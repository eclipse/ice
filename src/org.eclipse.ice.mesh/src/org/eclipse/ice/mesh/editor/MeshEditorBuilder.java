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
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemBuilder;
import org.eclipse.ice.item.ItemType;

/**
 * This class implements the ItemBuilder interface to create a 2D Mesh Editor.
 * 
 * @author Jay Jay Billings
 * 
 */
public class MeshEditorBuilder implements ItemBuilder {

	/**
	 * The name of this builder.
	 */
	public static final String name = "Mesh Editor";

	/**
	 * The Item type of this builder.
	 */
	public static final ItemType type = ItemType.Mesh;

	/**
	 * @see org.eclipse.ice.item.ItemBuilder#getItemName()
	 */
	public String getItemName() {
		return name;
	}

	/**
	 * @see org.eclipse.ice.item.ItemBuilder#getItemType()
	 */
	public ItemType getItemType() {
		return type;
	}

	/**
	 * @see org.eclipse.ice.item.ItemBuilder#build()
	 */
	public Item build(IProject projectSpace) {

		// Build the editor. The Mesh Editor sets its own name and ItemBuilder
		// name.
		Item meshEditor = new MeshEditor(projectSpace);

		return meshEditor;
	}

}
