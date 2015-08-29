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

import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.core.resources.IProject;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.MeshComponent;
import org.eclipse.ice.item.Item;

/**
 * This class provides a basic 2D mesh editor, minus the UI elements. All
 * available properties and operations can be used with this default editor.
 * 
 * @author Jay Jay Billings
 * 
 */
@XmlRootElement(name = "MeshEditor")
public class MeshEditor extends Item {

	/**
	 * The default constructor.
	 * 
	 * @param projectSpace
	 *            The Eclipse Project Space where data should be stored and
	 *            retrieved.
	 */
	public MeshEditor(IProject projectSpace) {
		super(projectSpace);
	}

	/**
	 * The nullary constructor that does not much of anything and should only be
	 * used for testing.
	 */
	public MeshEditor() {
		this(null);
	}

	/**
	 * This operation configures all of the identifying information about the
	 * Item.
	 */
	@Override
	protected void setupItemInfo() {
		// Set the particulars
		setName(MeshEditorBuilder.name);
		setDescription("This editor can construct "
				+ "one or two dimensional meshes.");
		setItemBuilderName(MeshEditorBuilder.name);
		itemType = MeshEditorBuilder.type;

		return;
	}

	/**
	 * This operation creates a basic form with only a mesh component.
	 */
	@Override
	protected void setupForm() {

		// Create a basic form
		form = new Form();
		
		// Create a mesh component and add it to the Form
		MeshComponent mesh = new MeshComponent();
		mesh.setId(1);
		mesh.setDescription("A mesh of elements composed "
				+ "of vertices and edges.");
		mesh.setName("Mesh");
		form.addComponent(mesh);

		return;
	}

}