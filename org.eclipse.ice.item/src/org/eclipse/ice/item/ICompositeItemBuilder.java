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
 *   Claire Saunders, Matthew Wang, Anna Wojtowicz, Nick Stanish
 *******************************************************************************/
package org.eclipse.ice.item;

import java.util.ArrayList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * This class builds Items that depend on other Items. These "composite Items"
 * are themselves subclasses of Item, but they require functionality implemented
 * by other Items to function properly. ICompositeItemBuilder.build() must
 * return null if the list of other ItemBuilders has not be set by calling
 * ICompositeItemBuilder.addBuilders().
 * </p>
 * 
 * @author Jay Jay Billings, Nick Stanish
 */
public interface ICompositeItemBuilder extends ItemBuilder {
	
	public static final String EXTENSION_ID = "org.eclipse.ice.item.compositeItemBuilder";
	
	

	/**
	 * <p>
	 * This operation sets the list of ItemBuilders that may be used by the
	 * composite Item to construct its children. This operation should be called
	 * before ICompositeItemBuilder.build().
	 * </p>
	 * 
	 * @param itemBuilders
	 *            <p>
	 *            The list of ItemBuilders. This list should contain one or more
	 *            ItemBuilders. If it does not, then building a composite Item
	 *            with this builder must fail.
	 *            </p>
	 */
	public void addBuilders(ArrayList<ItemBuilder> itemBuilders);
	
	
	/**
	 * This operation retrieves all of the CompositeItemBuilders from the
	 * ExtensionRegistry.
	 *
	 * @return The array of CompositeItemBuilders that were found in the registry.
	 * @throws CoreException
	 *             This exception is thrown if an extension cannot be loaded.
	 */
	public static ICompositeItemBuilder[] getCompositeItemBuilders() throws CoreException {

		/**
		 * Logger for handling event messages and other information.
		 */
		Logger logger = LoggerFactory.getLogger(ICompositeItemBuilder.class);

		ICompositeItemBuilder[] builders = null;
		
		IExtensionPoint point = Platform.getExtensionRegistry()
				.getExtensionPoint(EXTENSION_ID);

		// If the point is available, create all the builders and load them into
		// the array.
		if (point != null) {
			IConfigurationElement[] elements = point.getConfigurationElements();
			builders = new ICompositeItemBuilder[elements.length];
			for (int i = 0; i < elements.length; i++) {
				builders[i] = (ICompositeItemBuilder) elements[i]
						.createExecutableExtension("class");
			}
		} else {
			logger.error("Extension Point " + EXTENSION_ID + "does not exist");
		}

		return builders;
	}
}