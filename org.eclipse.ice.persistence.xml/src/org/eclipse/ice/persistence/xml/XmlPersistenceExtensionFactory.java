/*******************************************************************************
 * Copyright (c) 2013, 2014- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation -
 *   Jay Jay Billings
 *******************************************************************************/
package org.eclipse.ice.persistence.xml;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IExecutableExtensionFactory;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ice.item.ItemBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for creating the XMLPersistenceProvider as part of
 * the Extension Registry and as a singleton. It handles all of the necessary
 * start up steps as well as locating the ItemBuilders and JAXBClassProvider
 * interfaces.
 *
 * @author Jay Jay Billings
 *
 */
public class XmlPersistenceExtensionFactory
		implements IExecutableExtensionFactory {

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(XmlPersistenceExtensionFactory.class);

	/**
	 * The XMLPersistenceProvider.
	 */
	private static XMLPersistenceProvider provider;

	/**
	 * The constructor
	 */
	public XmlPersistenceExtensionFactory() {
		// Nothing to do
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.core.runtime.IExecutableExtensionFactory#create()
	 */
	@Override
	public Object create() throws CoreException {

		// Create the provider
		if (provider == null) {
			provider = new XMLPersistenceProvider();
			for (ItemBuilder builder : getItemBuilders()) {

			}
		}

		return provider;
	}

	/**
	 * This operation retrieves all of the ItemBuilders from the
	 * ExtensionRegistry.
	 *
	 * @return The array of ItemBuilders that were found in the registry.
	 */
	private ItemBuilder[] getItemBuilders() {

		String id = "org.eclipse.ice.item.itemBuilder";
		IExtensionPoint point = Platform.getExtensionRegistry()
				.getExtensionPoint(id);
		System.out.println("##### XMLPP - Extensions for: " + id + " #####");
		if (point != null) {
			IExtension[] extensions = point.getExtensions();
			for (IExtension extension : extensions) {
				System.out.println("--" + extension.getSimpleIdentifier());
			}
		} else {
			logger.error("Extension Point " + id + "does not exist");
		}

		return null;
	}

}
