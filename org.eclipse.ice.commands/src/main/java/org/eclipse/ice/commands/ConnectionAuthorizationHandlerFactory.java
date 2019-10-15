/*******************************************************************************
 * Copyright (c) 2019- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings, Joe Osborn
 *******************************************************************************/
package org.eclipse.ice.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a factory class which returns the desired
 * ConnectionAuthorizationHandler, which is relevant for authorizing and
 * establishing remote connections for either commands or file transfers.
 * 
 * @author Joe Osborn
 *
 */
public class ConnectionAuthorizationHandlerFactory {

	/**
	 * Logger for handling event messages and other information. All password
	 * authentication classes can use a single logger
	 */
	public final Logger logger = LoggerFactory.getLogger(ConnectionAuthorizationHandlerFactory.class);

	/**
	 * Default constructor
	 */
	public ConnectionAuthorizationHandlerFactory() {
	}

	/**
	 * Helper function which calls the function below, where the path isn't relevant
	 * and thus can be left as null. Useful when a console or local connection
	 * authorization handler is desired
	 * 
	 * @param type
	 * @return
	 */
	public ConnectionAuthorizationHandler getConnectionAuthorizationHandler(String type) {
		return getConnectionAuthorizationHandler(type, "");
	}

	/**
	 * Factory method to get a particular connection authorization handler. Returns
	 * a handler to authorize remote connections.
	 * 
	 * @param type - type of authorization handler desired
	 * @return - authorization handler
	 */
	public ConnectionAuthorizationHandler getConnectionAuthorizationHandler(String type, String path) {
		ConnectionAuthorizationHandler auth = null;

		// Get an authenticator where you can type your password in at the console.
		// Requires that the username/hostname are manually set
		if (type.equals("console"))
			auth = new ConsoleConnectionAuthorizationHandler();
		// A text file containing all credentials. Authorization will automatically grab
		// credentials
		else if (type.equals("text"))
			auth = new TxtFileConnectionAuthorizationHandler(path);
		// An authorization for local commands, which just requires the (local) hostname
		else if (type.equals("local"))
			auth = new LocalConnectionAuthorizationHandler();
		else
			logger.error("Unknown authorization type! Will return null.");

		return auth;
	}

}
