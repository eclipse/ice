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
package org.eclipse.ice.client.common.internal;

import org.eclipse.ice.iclient.IClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class accepts a reference to the Client from the OSGi Framework and
 * stores it for later retrieval by members of the plugin.
 * 
 * @author Jay Jay Billings
 * 
 */
public class ClientHolder {

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory.getLogger(ClientHolder.class);
	
	/**
	 * The reference to the Client provided by the OSGi.
	 */
	private static IClient client = null;

	/**
	 * Retrieve the reference to the client
	 */
	public static IClient getClient() {
		return client;
	}

	/**
	 * Retrieve the reference to the client
	 */
	public static void setClient(IClient iceClient) {

		// Set the reference and do some reporting
		if (iceClient != null) {
			client = iceClient;
			logger.info("ICE Eclipse Common Widgets Message: "
					+ "Client set successfully!");
		} else {
			logger.info("ICE Eclipse Common Widgets Message:"
					+ "Framework attempted to set client, but the "
					+ "reference was null.");
		}
		return;
	}

	/**
	 * "Unset" the reference to the client. That is, notify the Common bundle
	 * that the client has shutdown.
	 */
	public static void unsetClient(IClient iceClient) {
		client = null;
	}

}
