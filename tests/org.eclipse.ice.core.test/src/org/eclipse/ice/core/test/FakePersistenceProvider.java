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

import org.eclipse.ice.core.iCore.IPersistenceProvider;
import org.eclipse.ice.item.Item;

/**
 * 
 * @author Jay Jay Billings
 */
public class FakePersistenceProvider implements IPersistenceProvider {
	/**
	 * <p>
	 * True if the Items were loaded, false otherwise.
	 * </p>
	 * 
	 */
	private volatile boolean loaded = false;
	/**
	 * <p>
	 * True if an individual Item was persisted, false otherwise.
	 * </p>
	 * 
	 */
	private volatile boolean persisted = false;
	/**
	 * <p>
	 * True if an individual Item was updated, false otherwise.
	 * </p>
	 * 
	 */
	private volatile boolean updated = false;

	/**
	 * <p>
	 * True if an individual Item was deleted, false otherwise.
	 * </p>
	 * 
	 */
	private volatile boolean deleted = false;

	/**
	 * <p>
	 * This operation returns true if the Items were loaded, false otherwise.
	 * </p>
	 * 
	 * @return <p>
	 *         True if the Items were loaded, false otherwise.
	 *         </p>
	 */
	public boolean allLoaded() {
		return loaded;
	}

	/**
	 * <p>
	 * This operation resets the provider's flags for the test.
	 * </p>
	 * 
	 */
	public void reset() {

		// Reset all the flags to false
		loaded = false;
		persisted = false;
		updated = false;
		deleted = false;

	}

	/**
	 * <p>
	 * This operation returns true if an Item was loaded, false otherwise.
	 * </p>
	 * 
	 * @return <p>
	 *         True if an individual Item was persisted, false otherwise.
	 *         </p>
	 */
	public boolean itemPersisted() {
		return persisted;
	}

	/**
	 * <p>
	 * This operation returns true if an Item was updated, false otherwise.
	 * </p>
	 * 
	 * @return <p>
	 *         True if an individual Item was updated, false otherwise.
	 *         </p>
	 */
	public boolean itemUpdated() {
		return updated;
	}

	/**
	 * <p>
	 * This operation returns true if an Item was loaded, false otherwise.
	 * </p>
	 * 
	 * @return <p>
	 *         True if an individual Item was deleted, false otherwise.
	 *         </p>
	 */
	public boolean itemDeleted() {
		return deleted;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IPersistenceProvider#persistItem(Item item)
	 */
	@Override
	public boolean persistItem(Item item) {

		persisted = true;

		return persisted;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IPersistenceProvider#loadItem(int itemID)
	 */
	@Override
	public Item loadItem(int itemID) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IPersistenceProvider#deleteItem(Item item)
	 */
	@Override
	public boolean deleteItem(Item item) {

		deleted = true;

		return deleted;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IPersistenceProvider#updateItem(Item item)
	 */
	@Override
	public boolean updateItem(Item item) {

		updated = true;

		return updated;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IPersistenceProvider#loadItems()
	 */
	@Override
	public ArrayList<Item> loadItems() {

		// Local Declarations
		ArrayList<Item> items = new ArrayList<Item>();
		FakeItem fake1 = new FakeItem(null), fake2 = new FakeItem(null);

		// Set the ids
		fake1.setId(1);
		fake2.setId(3);

		// Add the Items to the list
		items.add(fake1);
		items.add(fake2);

		// Set the flag
		loaded = true;

		return items;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IPersistenceProvider#getClassForItemId(int itemID)
	 */
	public Class getClassForItemId(int itemID) {
		// TODO Auto-generated method stub
		return null;
	}
}