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

import javax.xml.bind.JAXBException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtensionFactory;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ice.datastructures.jaxbclassprovider.IJAXBClassProvider;
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
public class XMLPersistenceExtensionFactory
		implements IExecutableExtensionFactory {

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(XMLPersistenceExtensionFactory.class);

	/**
	 * The XMLPersistenceProvider.
	 */
	private static XMLPersistenceProvider provider;

	/**
	 * The constructor
	 */
	public XMLPersistenceExtensionFactory() {
		// Nothing to do
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.core.runtime.IExecutableExtensionFactory#create()
	 */
	@Override
	public Object create() throws CoreException {

		// Create the provider if it doesn't exist already
		if (provider == null) {
			provider = new XMLPersistenceProvider();
			// Load all the Item Builders
			for (ItemBuilder builder : ItemBuilder.getItemBuilders()) {
				provider.addBuilder(builder);
			}
			// Load all the JAXB providers - FIXME! Fix element ID and uncomment
			// when IJAXBProviders have been added to the registry.
			// for (IJAXBClassProvider jaxbProvider : getJAXBProviders()) {
			// provider.registerClassProvider(jaxbProvider);
			// }
			try {
				provider.start();
				// Start the service
			} catch (JAXBException e) {
				// Complain
				logger.error("Unable to start XMLPersistenceProvider", e);
			}
		}
		
		return provider;
	}

	/**
	 * This operation pulls the list of JAXB class providers from the registry
	 * for classes that need custom handling.
	 *
	 * @return The list of class providers.
	 * @throws CoreException
	 */
	private IJAXBClassProvider[] getJAXBProviders() throws CoreException {

		IJAXBClassProvider[] jaxbProviders = null;
		String id = "org.eclipse.ice.item.itemBuilder";
		IExtensionPoint point = Platform.getExtensionRegistry()
				.getExtensionPoint(id);

		// If the point is available, create all the builders and load them into
		// the array.
		if (point != null) {
			IConfigurationElement[] elements = point.getConfigurationElements();
			jaxbProviders = new IJAXBClassProvider[elements.length];
			for (int i = 0; i < elements.length; i++) {
				jaxbProviders[i] = (IJAXBClassProvider) elements[i]
						.createExecutableExtension("class");
			}
		} else {
			logger.error("Extension Point " + id + "does not exist");
		}

		return jaxbProviders;
	}

}
