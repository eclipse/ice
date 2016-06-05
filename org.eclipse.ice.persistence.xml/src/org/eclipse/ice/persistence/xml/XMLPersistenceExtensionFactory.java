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
import org.eclipse.core.runtime.IExecutableExtensionFactory;
import org.eclipse.ice.item.ItemBuilder;
import org.eclipse.january.form.IJAXBClassProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for creating the XMLPersistenceProvider as part of
 * the Extension Registry and as a singleton. It handles all of the necessary
 * start up steps as well as locating the ItemBuilders and JAXBClassProvider
 * interfaces.
 *
 * @author Jay Jay Billings, Nick Stanish
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
			// Load all the JAXB providers if and only if they are available.
			IJAXBClassProvider[] jaxbProviders = IJAXBClassProvider
					.getJAXBProviders();
			if (jaxbProviders != null && jaxbProviders.length > 0) {
				for (IJAXBClassProvider jaxbProvider : jaxbProviders) {
					provider.registerClassProvider(jaxbProvider);
				}
			}
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

}
