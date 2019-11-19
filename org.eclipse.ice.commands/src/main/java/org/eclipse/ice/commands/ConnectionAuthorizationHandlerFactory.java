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

import java.util.HashMap;
import java.util.Map;

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

	private HashMap<String, ConnectionAuthorizationHandler> handlerList = new HashMap<String, ConnectionAuthorizationHandler>();

	/**
	 * Default constructor
	 */
	public ConnectionAuthorizationHandlerFactory() {
		// Add the default authorization handler classes to the hash map
		// An authenticator where you can type your password in at the console.
		// Requires that the username/hostname are manually set
		handlerList.put("console", new ConsoleConnectionAuthorizationHandler());
		// An authorization for local commands, which just requires the (local) hostname
		handlerList.put("local", new LocalConnectionAuthorizationHandler());
		// A text file containing all credentials. Authorization will automatically grab
		// credentials
		handlerList.put("text", new TxtFileConnectionAuthorizationHandler());
		// Use a path to a key pair that was already generated to establish the
		// connection
		handlerList.put("keypath", new KeyPathConnectionAuthorizationHandler());
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

		// Iterate over the default authorization types
		for (Map.Entry<String, ConnectionAuthorizationHandler> entry : handlerList.entrySet()) {
			// Check if the type matches, and set the auth variable
			if (type.equals(entry.getKey())) {
				auth = entry.getValue();
			}
		}

		// Have a separate set for a text file (or key path) authorization since it
		// needs to take the path as a constructor argument
		// TODO - think about if there is a better way to do this - don't want to have
		// to add an additional if statement for each non-default constructor
		if (type.equals("text"))
			auth = new TxtFileConnectionAuthorizationHandler(path);
		if (type.equals("keypath"))
			auth = new KeyPathConnectionAuthorizationHandler(path);

		if (auth == null)
			logger.error("Unknown authorization type! Will return null.");

		return auth;
	}

	/**
	 * This function allows a client to add another type of authorization handler,
	 * should they have developed their own type. The factory will then add this to
	 * the list and then check for it when determining what type of
	 * ConnectionAuthorizationHandler to return.
	 * 
	 * @param type
	 * @param auth
	 */
	public void addConnectionAuthorizationHandlerType(String type, ConnectionAuthorizationHandler auth) {
		handlerList.put(type, auth);
	}

}
