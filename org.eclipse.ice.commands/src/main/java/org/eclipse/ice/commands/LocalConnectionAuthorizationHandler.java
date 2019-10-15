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

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * This class allows for a simple connection authorization handler to be
 * accessed, where one wants access to, for example, just the hostname. This is
 * particularly useful for local commands, where only the hostname is relevant.
 * 
 * @author Joe Osborn
 *
 */
public class LocalConnectionAuthorizationHandler extends ConnectionAuthorizationHandler {

	/**
	 * Default constructor
	 */
	public LocalConnectionAuthorizationHandler() {
		// By default local authorization has local host as the hostname, so set it
		hostname = getLocalHostname();
	}

	/**
	 * Local connections do not require passwords, so this implementation returns
	 * null
	 * {@link org.eclipse.ice.commands.ConnectionAuthorizationHandler#getPassword()}
	 */
	@Override
	protected char[] getPassword() {
		return null;
	}

	/**
	 * This function returns the local hostname of your local computer.
	 * 
	 * @return - String - local hostname
	 */
	protected static String getLocalHostname() {
		// Get the hostname for your local computer
		InetAddress addr = null;
		try {
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		String hostname = addr.getHostName();

		return hostname;
	}

}
