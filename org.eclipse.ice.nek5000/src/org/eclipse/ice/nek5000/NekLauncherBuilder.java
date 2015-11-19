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
package org.eclipse.ice.nek5000;

import org.eclipse.core.resources.IProject;
import org.eclipse.ice.item.AbstractItemBuilder;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemType;

/**
 * This class constructs a Nek5000 Job Launcher.
 * 
 * @author Jay Jay Billings
 *
 */
public class NekLauncherBuilder extends AbstractItemBuilder {

	/**
	 * The name that should be used for the Item and this builder
	 */
	public static final String name = "Nek5000 Launcher";

	/**
	 * The type of the Item
	 */
	public static final ItemType type = ItemType.Simulation;

	/**
	 * The Constructor
	 */
	public NekLauncherBuilder() {
		setName(name);
		setType(type);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.item.AbstractItemBuilder#getInstance(org.eclipse.core.resources.IProject)
	 */
	@Override
	public Item getInstance(IProject project) {
		return new NekLauncher(project);
	}

}
