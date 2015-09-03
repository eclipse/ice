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
package org.eclipse.ice.core.iCore;

import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.core.resources.IResource;
import org.eclipse.ice.item.Item;

/**
 * <p>
 * An interface designed for item persistence within ICE.
 * </p>
 * 
 * @author Scott Forest Hull II
 */
public interface IPersistenceProvider {
	/**
	 * <p>
	 * Persists an item. Returns true if the operation was successful. False
	 * otherwise.
	 * </p>
	 * 
	 * @param item
	 *            <p>
	 *            The item to be persisted.
	 *            </p>
	 * @return
	 * 		<p>
	 *         Returns true if the operation was successful. False otherwise.
	 *         </p>
	 */
	public boolean persistItem(Item item);

	/**
	 * <p>
	 * Loads the specified item keyed on the ID. Returns the item, or null if an
	 * error was encountered.
	 * </p>
	 * 
	 * @param itemID
	 *            <p>
	 *            The item's ID.
	 *            </p>
	 * @return
	 * 		<p>
	 *         The returned item.
	 *         </p>
	 */
	public Item loadItem(int itemID);

	/**
	 * <p>
	 * Deletes an item from the persistence provider. Returns true if the
	 * operation was successful. False otherwise.
	 * </p>
	 * 
	 * @param item
	 *            <p>
	 *            The item to be deleted.
	 *            </p>
	 * @return
	 * 		<p>
	 *         True if successful. False otherwise.
	 *         </p>
	 */
	public boolean deleteItem(Item item);

	/**
	 * <p>
	 * Updates an item in the persistence unit.
	 * </p>
	 * 
	 * @param item
	 *            <p>
	 *            The item to be updated.
	 *            </p>
	 * @return
	 * 		<p>
	 *         Returns true if successful. False otherwise.
	 *         </p>
	 */
	public boolean updateItem(Item item);

	/**
	 * <p>
	 * Returns all the Items in the persistence piece.
	 * </p>
	 * 
	 * @return
	 * 		<p>
	 *         The list of items.
	 *         </p>
	 */
	public ArrayList<Item> loadItems();

	/**
	 * <p>
	 * Attempts to load the IResource as an Item. Returns the item, or null if
	 * an error was encountered.
	 * </p>
	 * 
	 * @param IResource the IResource that contains an Item.
	 * @return The returned item or null if the IResource was not an IFile (for now).
	 */
	public Item loadItem(IResource itemResource) throws IOException;
}