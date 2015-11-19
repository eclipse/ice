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
package org.eclipse.ice.client.internal;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IExecutableExtensionFactory;
import org.eclipse.ice.iclient.IClient;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for obtaining as part of the Extension Registry and
 * as a singleton. It just sneaks into the OSGi backend using the PlatformUI to
 * grab the service.
 *
 * @author Jay Jay Billings
 *
 */
public class ClientExtensionFactory
		implements IExecutableExtensionFactory {

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(ClientExtensionFactory.class);

	/**
	 * The Client
	 */
	private static IClient client;

	/**
	 * The constructor
	 */
	public ClientExtensionFactory() {
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
		if (client == null) {
			client = PlatformUI.getWorkbench().getService(IClient.class);
			logger.debug("Reacquiring client service throw extension factory.");
		}

		return client;
	}
}
