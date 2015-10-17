/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings
 *******************************************************************************/
package org.eclipse.ice.core.iCore;

import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An interface designed for item persistence within ICE.
 * 
 * Implementations of this interface should be provided with default projects to
 * use for storage, if they choose. Clients should always make sure that
 * getDefaultProject() does not return null when using implementations of this
 * interface.
 * 
 * @author Jay Jay Billings, Scott Forest Hull II
 */
public interface IPersistenceProvider {

	/**
	 * This operation sets the default project that the provider should use for
	 * any operations related to the workspace if such a default project is
	 * required. This could include retrieving or writing resources, among other
	 * things.
	 * 
	 * @param project
	 *            The default project that should be used.
	 */
	public void setDefaultProject(IProject project);

	/**
	 * This operation returns the default project that is used by the provider.
	 * 
	 * @return The default project.
	 */
	public IProject getDefaultProject();

	/**
	 * Persists an item. Returns true if the operation was successful. False
	 * otherwise. The Item is persisted to the project retrieved by calling
	 * Item.getProject(), which may not be the same as the default project.
	 * 
	 * @param item
	 *            The item to be persisted.
	 * @return Returns true if the operation was successful. False otherwise.
	 */
	public boolean persistItem(Item item);

	/**
	 * Loads the specified item keyed on the ID. Returns the item, or null if an
	 * error was encountered.
	 * 
	 * @param itemID
	 *            The item's ID.
	 * @return The returned item.
	 */
	public Item loadItem(int itemID);

	/**
	 * Deletes an item from the persistence provider. Returns true if the
	 * operation was successful. False otherwise.
	 * 
	 * @param item
	 *            The item to be deleted.
	 * @return True if successful. False otherwise.
	 */
	public boolean deleteItem(Item item);

	/**
	 * Updates an item in the persistence unit.
	 * 
	 * @param item
	 *            The item to be updated.
	 * @return Returns true if successful. False otherwise.
	 */
	public boolean updateItem(Item item);

	/**
	 * Returns all the Items in the persistence piece.
	 * 
	 * @return The list of items.
	 */
	public ArrayList<Item> loadItems();

	/**
	 * Attempts to load the IResource as an Item. Returns the item, or null if
	 * an error was encountered.
	 * 
	 * @param IResource
	 *            the IResource that contains an Item.
	 * @return The returned item or null if the IResource was not an IFile (for
	 *         now).
	 */
	public Item loadItem(IResource itemResource) throws IOException;

	/**
	 * This operation retrieves the persistence from the ExtensionRegistry.
	 * 
	 * @return The provider 
	 * @throws CoreException
	 *             This exception is thrown if an extension cannot be loaded.
	 */
	public static IPersistenceProvider getProvider() throws CoreException {

		// Logger for handling event messages and other information.
		Logger logger = LoggerFactory.getLogger(IPersistenceProvider.class);

		// The string identifying the persistence provider extension point.
		String providerID = "org.eclipse.ice.core.persistenceProvider";

		// The provider
		IPersistenceProvider provider = null;
		// Get the persistence provider from the extension registry.
		IExtensionPoint point = Platform.getExtensionRegistry()
				.getExtensionPoint(providerID);
		// Retrieve the provider from the registry and set it if one has not
		// already been set.
		if (point != null) {
			// We only need one persistence provider, so just pull the
			// configuration element for the first one available.
			IConfigurationElement[] elements = point.getConfigurationElements();
			if (elements.length > 0) {
				IConfigurationElement element = elements[0];
				provider = (IPersistenceProvider) element
						.createExecutableExtension("class");
			} else {
				logger.info("No extensions found for IPersistenceProvider.");
			}
		} else {
			logger.error("Extension Point " + providerID + "does not exist");
		}

		return provider;
	}

}