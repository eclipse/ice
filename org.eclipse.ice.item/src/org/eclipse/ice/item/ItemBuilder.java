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
package org.eclipse.ice.item;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * The ItemBuilder interface is used to register an Item with the ItemManager
 * and to build Items of a specific type. The getItemName() and getItemType()
 * operations provide information that can be used to organize Items and the
 * build() operation encapsulates the logical required to instantiate subclasses
 * of Item.
 * </p>
 * <p>
 * In ICE, this interface is used as a pluggable service in the OSGi framework
 * to provide Items to the Core. ItemBuilders are also registered for serialized
 * Items that are persistent on the disk.
 * </p>
 *
 * @author Jay Jay Billings
 */
public interface ItemBuilder {

	/**
	 * <p>
	 * This operation returns the short name of the Item that can be constructed
	 * by this ItemBuilder.
	 * </p>
	 *
	 * @return
	 * 		<p>
	 *         The name
	 *         </p>
	 */
	public String getItemName();

	/**
	 * <p>
	 * This operation returns the type of Item that can be built by the
	 * ItemBuilder.
	 * </p>
	 *
	 * @return
	 * 		<p>
	 *         The type
	 *         </p>
	 */
	public ItemType getItemType();

	/**
	 * This operation returns true if the Item is publishable to the list of
	 * available Items, and false otherwise. By default, this should return
	 * true, and only false when an Item developer explicitly does not want this
	 * Item provided as available to the user.
	 *
	 * @return True for publishable, false otherwise
	 */
	public boolean isPublishable();

	/**
	 * <p>
	 * This operation builds and returns an instance of the Item that can be
	 * constructed by the ItemBuilder.
	 * </p>
	 *
	 * @param projectSpace
	 *            <p>
	 *            The Eclipse project that the Item should use for storage.
	 *            </p>
	 * @return
	 * 		<p>
	 *         The newly created Item.
	 *         </p>
	 */
	public Item build(IProject projectSpace);

	/**
	 * This operation retrieves all of the ItemBuilders from the
	 * ExtensionRegistry.
	 *
	 * @return The array of ItemBuilders that were found in the registry.
	 * @throws CoreException
	 *             This exception is thrown if an extension cannot be loaded.
	 */
	public static ItemBuilder[] getItemBuilders() throws CoreException {

		/**
		 * Logger for handling event messages and other information.
		 */
		Logger logger = LoggerFactory.getLogger(ItemBuilder.class);

		ItemBuilder[] builders = null;
		String id = "org.eclipse.ice.item.itemBuilder";
		IExtensionPoint point = Platform.getExtensionRegistry()
				.getExtensionPoint(id);

		// If the point is available, create all the builders and load them into
		// the array.
		if (point != null) {
			IConfigurationElement[] elements = point.getConfigurationElements();
			builders = new ItemBuilder[elements.length];
			for (int i = 0; i < elements.length; i++) {
				builders[i] = (ItemBuilder) elements[i]
						.createExecutableExtension("class");
			}
		} else {
			logger.error("Extension Point " + id + "does not exist");
		}

		return builders;
	}
}