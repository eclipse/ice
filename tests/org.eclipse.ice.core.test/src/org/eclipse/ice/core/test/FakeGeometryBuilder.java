/*******************************************************************************
 * Copyright (c) 2011, 2014 UT-Battelle, LLC.
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

import org.eclipse.core.resources.IProject;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemBuilder;
import org.eclipse.ice.item.ItemType;

/**
 * <p>
 * This is a fake implementation of the ItemBuilder interface that is used to
 * test the ItemManager and the Core. It fakes the creation of a Geometry Item.
 * </p>
 * 
 * @author Jay Jay Billings
 */
public class FakeGeometryBuilder implements ItemBuilder {
	/**
	 * <p>
	 * The last FakeItem created by the builder.
	 * </p>
	 * 
	 */
	private FakeItem lastFakeItem;

	/**
	 * <p>
	 * Retrieve the last FakeItem created by the builder.
	 * </p>
	 * 
	 * @return <p>
	 *         The last FakeItem created by the builder.
	 *         </p>
	 */
	public FakeItem getLastFakeItem() {
		return lastFakeItem;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ItemBuilder#getItemName()
	 */
	@Override
	public String getItemName() {
		return new String("Inigo Montoya");
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ItemBuilder#getItemType()
	 */
	@Override
	public ItemType getItemType() {
		return ItemType.Geometry;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ItemBuilder#build(IProject projectSpace)
	 */
	@Override
	public Item build(IProject projectSpace) {
		lastFakeItem = new FakeItem(projectSpace);
		return lastFakeItem;
	}
}
