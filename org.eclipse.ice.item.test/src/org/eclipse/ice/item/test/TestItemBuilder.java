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
package org.eclipse.ice.item.test;

import org.eclipse.core.resources.IProject;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemBuilder;
import org.eclipse.ice.item.ItemType;

/**
 * A simple builder for the TestItem so that the XML Persistence Provider can be
 * configured.
 * 
 * @author Jay Jay Billings
 * 
 */
public class TestItemBuilder implements ItemBuilder {

	@Override
	public String getItemName() {
		return "Test Item";
	}

	@Override
	public ItemType getItemType() {
		return ItemType.Simulation;
	}

	@Override
	public Item build(IProject projectSpace) {
		return new TestItem(projectSpace);
	}

}
