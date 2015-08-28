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

import org.eclipse.core.resources.IProject;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemType;
import org.eclipse.ice.item.model.AbstractModelBuilder;

/**
 * The ItemBuilder for creating reflectivity models.
 * @author Jay Jay Billings
 */
public class ReflectivityModelBuilder extends AbstractModelBuilder {

	/**
	 * The name of the builder and the default name of the Item.
	 */
	public static final String name = "Reflectivity Model";
	
	/**
	 * The type of the Item.
	 */
	public static final ItemType type = ItemType.Model;
	
	/**
	 * The constructor
	 */
	public ReflectivityModelBuilder() {
		setName("Reflectivity Model");
		setType(ItemType.Model);
	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.item.AbstractItemBuilder#getInstance(org.eclipse.core.resources.IProject)
	 */
	@Override
	public Item getInstance(IProject projectSpace) {
		return new ReflectivityModel(projectSpace);
	}

}
