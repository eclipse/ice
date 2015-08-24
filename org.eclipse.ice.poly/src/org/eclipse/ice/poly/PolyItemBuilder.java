/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Rajeev Kumar
 *******************************************************************************/
package org.eclipse.ice.poly;

import org.eclipse.core.resources.IProject;
import org.eclipse.ice.item.AbstractItemBuilder;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemType;

/**
 * @author Jay Jay Billings, Rajeev Kumar
 *
 */
public class PolyItemBuilder extends AbstractItemBuilder {

	/**
	 * The name that should be used for the Item and this builder
	 */
	public static final String name = "Poly Launcher";

	/**
	 * The type of the Item
	 */
	public static final ItemType type = ItemType.Simulation;

	/**
	 * The Constructor
	 */
	public PolyItemBuilder() {
		setName(name);
		setType(type);
	}
	
	/**
	 * Implements a method from ItemBuilder.
	 */
	@Override
	public Item getInstance(IProject project) {
		return new PolyJobLauncher(project);
	}

}
