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
package org.eclipse.ice.nek5000;

import org.eclipse.core.resources.IProject;
import org.eclipse.ice.item.AbstractItemBuilder;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemType;

/**
 * This builder will construct an Item that can create Nek5000 input files.
 * 
 * @author Jay Jay Billings
 * 
 */
public class NekModelBuilder extends AbstractItemBuilder {

	/**
	 * The name of the Item
	 */
	public static final String name = "Nek5000 Model Builder";

	/**
	 * The type of the Item
	 */
	public static final ItemType type = ItemType.Model;

	/**
	 * The Constructor
	 */
	public NekModelBuilder() {
		setName(name);
		setType(type);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.item.AbstractItemBuilder#getInstance(org.eclipse.core.resources.IProject)
	 */
	@Override
	public Item getInstance(IProject project) {
		return new NekModel(project);
	}

}
