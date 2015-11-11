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

import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.persistence.IPersistenceProvider;

/**
 * This is a fake implementation of the persistence interface and it is used for
 * testing the core.
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
	 * This operation returns true if an Item was loaded, false otherwise.
	 *
	 * @return True if an individual Item was persisted, false otherwise.
	 */
	public boolean itemPersisted() {
		return persisted;
	}

	/**
	 * This operation returns true if an Item was updated, false otherwise.
	 *
	 * @return True if an individual Item was updated, false otherwise.
	 */
	public boolean itemUpdated() {
		return updated;
	}

	/**
	 * This operation returns true if an Item was loaded, false otherwise.
	 *
	 * @return True if an individual Item was deleted, false otherwise.
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

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.item.persistence.IPersistenceProvider#loadItem(org.eclipse.core.resources.IResource)
	 */
	@Override
	public Item loadItem(IResource itemResource) throws IOException {
		loaded = true;
		FakeItem item = new FakeItem(null);
		item.getForm().setName("The Doctor");
		return item;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.item.persistence.IPersistenceProvider#setDefaultProject(org.eclipse.core.resources.IProject)
	 */
	@Override
	public void setDefaultProject(IProject project) {
		// TODO Auto-generated method stub
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.item.persistence.IPersistenceProvider#getDefaultProject()
	 */
	@Override
	public IProject getDefaultProject() {
		// TODO Auto-generated method stub
		return null;
	}
}