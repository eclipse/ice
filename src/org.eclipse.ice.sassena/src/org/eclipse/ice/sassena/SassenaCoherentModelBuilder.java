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
package org.eclipse.ice.sassena;

import org.eclipse.core.resources.IProject;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemBuilder;
import org.eclipse.ice.item.ItemType;

/**
 * This class builds the SassenaCoherentModel. It inherits its operations from
 * ItemBuilder.
 * 
 * @author Scott Forest Hull II
 */
public class SassenaCoherentModelBuilder implements ItemBuilder {

	/*
	 * Implements a method from ItemBuilder.
	 */
	@Override
	public String getItemName() {
		return "Sassena Coherent Model";
	}

	/*
	 * Implements a method from ItemBuilder.
	 */
	@Override
	public ItemType getItemType() {
		return ItemType.Model;
	}

	/*
	 * Implements a method from ItemBuilder.
	 */
	@Override
	public Item build(IProject projectSpace) {
		SassenaCoherentModel sassenaCoh = new SassenaCoherentModel(projectSpace);
		sassenaCoh.setItemBuilderName(getItemName());
		return sassenaCoh;
	}

}
