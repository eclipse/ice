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
package org.eclipse.ice.core.test;

import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.ice.item.ICompositeItemBuilder;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemBuilder;
import org.eclipse.ice.item.ItemType;

/**
 * <p>
 * This class implements the ICompositeItemBuilder interface and is registered
 * with the ItemManager to make sure that it sets the list of other ItemBuilders
 * that is required by a composite Item.
 * </p>
 * 
 * @author Jay Jay Billings
 */
public class FakeCompositeItemBuilder implements ICompositeItemBuilder {
	/**
	 * <p>
	 * True if the builders were set, false otherwise.
	 * </p>
	 * 
	 */
	private boolean buildersSet = false;

	/**
	 * <p>
	 * This operation returns true if the builders were registered, false
	 * otherwise.
	 * </p>
	 * 
	 * @return <p>
	 *         True if the builders were registered, false otherwise.
	 *         </p>
	 */
	public boolean itemBuildersRegistered() {
		return buildersSet;
	}

	/**
	 * <p>
	 * This operation resets the builder's registration flags for the test.
	 * </p>
	 * 
	 */
	public void reset() {

		// Reset the flag
		buildersSet = false;

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ItemBuilder#getItemName()
	 */
	@Override
	public String getItemName() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ItemBuilder#getItemType()
	 */
	@Override
	public ItemType getItemType() {
		// TODO Auto-generated method stub
		return ItemType.Simulation;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ItemBuilder#build(IProject projectSpace)
	 */
	@Override
	public Item build(IProject projectSpace) {
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ICompositeItemBuilder#addBuilders(ArrayList<ItemBuilder>
	 *      itemBuilders)
	 */
	@Override
	public void addBuilders(ArrayList<ItemBuilder> itemBuilders) {

		buildersSet = true;

	}
}