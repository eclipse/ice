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
package org.eclipse.ice.reflectivity;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.core.resources.IProject;
import org.eclipse.ice.datastructures.ICEObject.ListComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.datastructures.form.Material;
import org.eclipse.ice.datastructures.form.TableComponent;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemType;

/**
 * This classes calculates the reflectivity profile of a set of materials
 * layered on top of each other. It... <add more after you figure out the
 * calculations>
 * 
 * @author Jay Jay Billings, Alex McCaskey
 */
@XmlRootElement(name = "ReflectivityModel")
public class ReflectivityModel extends Item {

	/**
	 * The constructor.
	 */
	public ReflectivityModel() {
		// begin-user-code
		this(null);
		// end-user-code
	}

	/**
	 * The constructor with a project space in which files should be handled.
	 * 
	 * @param projectSpace
	 *            The Eclipse project where files should be stored and from
	 *            which they should be retrieved.
	 */
	public ReflectivityModel(IProject projectSpace) {
		// Call super
		super(projectSpace);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.item.Item#process(java.lang.String)
	 */
	@Override
	public FormStatus process(String actionName) {
		// begin-user-code
		return super.process(actionName);
		// end-user-code
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.item.Item#setupForm()
	 */
	@Override
	protected void setupForm() {
		// begin-user-code

		// Create the Form
		form = new Form();
		TableComponent table = new TableComponent();
		table.setId(1);
		table.setName("Reflectivity Input Data");
		table.setDescription("");

		ListComponent<Material> matList = new ListComponent<Material>();
		matList.setId(1);
		matList.setName("Reflectivity Input Data");
		matList.setDescription("Reflectivity Input Data");
		matList.add(new Material());
		form.addComponent(matList);

		// end-user-code
	}

	/**
	 * This operation is used to setup the name and description of the model as
	 * well as register its builder.
	 */
	protected void setupItemInfo() {
		// begin-user-code

		// Local Declarations
		String desc = "This item builds models for " + "Reflectivity.";

		// Describe the Item
		setName(ReflectivityModelBuilder.name);
		setItemBuilderName(ReflectivityModelBuilder.name);
		setDescription(desc);
		itemType = ReflectivityModelBuilder.type;

		return;
		// end-user-code
	}

}
